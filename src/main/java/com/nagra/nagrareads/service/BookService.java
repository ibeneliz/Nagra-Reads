package com.nagra.nagrareads.service;

import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.model.Review;
import com.nagra.nagrareads.repository.BookRepository;
import com.nagra.nagrareads.repository.NagraUserRepository;
import com.nagra.nagrareads.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private NagraUserRepository nagraUserRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        bookRepository.findByIsbn(book.getIsbn()).ifPresent(existingBook -> {
            throw new IllegalArgumentException("Book with same ISBN already exists");
        });
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        Book existingBook = bookRepository.findByIsbn(book.getIsbn())
                .orElseThrow(() -> new IllegalArgumentException("Book with the given ISBN is not found!"));
        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setGenre(book.getGenre());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setPublicationDate(book.getPublicationDate());
        return bookRepository.save(existingBook);
    }

    public Book getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with the given ISBN is not found!"));
    }

    public void deleteByIsbn(String isbn) {
        Book existingBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with the given ISBN is not found!"));
        bookRepository.deleteById(existingBook.getId());
    }

    public Review addReviewsByIsbnAndUsername(String isbn, String username, Review review) {
        NagraUser nagraUser = nagraUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with the given username is not found!"));
        return reviewRepository.findByBookIsbnAndNagraUserUsername(isbn, username)
                .map(updatedReview -> {
                    updatedReview.setRating(review.getRating());
                    updatedReview.setComment(review.getComment());
                    updatedReview.setReviewDate(LocalDateTime.now());
                    return reviewRepository.save(updatedReview);
                })
                .orElseGet(() -> bookRepository.findByIsbn(isbn)
                        .map(book -> {
                            review.setBook(book);
                            review.setNagraUser(nagraUser);
                            review.setReviewDate(LocalDateTime.now());
                            return reviewRepository.save(review);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Book with the given ISBN is not found for you!")));
    }

    public Review getReviewsByIsbnAndUsername(String isbn, String username) {
        return reviewRepository.findByBookIsbnAndNagraUserUsername(isbn, username)
                .orElseThrow(() -> new IllegalArgumentException("Book with the given ISBN is not found for you!"));
    }

    public List<Review> getReviewsByIsbn(String isbn, String username) {
        return reviewRepository.findAllByBookIsbn(isbn);
    }
}
