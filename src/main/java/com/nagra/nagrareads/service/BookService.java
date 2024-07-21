package com.nagra.nagrareads.service;

import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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

}
