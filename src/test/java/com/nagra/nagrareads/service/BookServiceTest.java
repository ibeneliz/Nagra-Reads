package com.nagra.nagrareads.service;

import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooksTest() {
        Book book1 = new Book(); // Assuming Book has a no-arg constructor
        Book book2 = new Book();
        List<Book> mockBooks = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(mockBooks);

        List<Book> resultBooks = bookService.getAllBooks();

        assertEquals(2, resultBooks.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void saveBookTest() {
        Book book = new Book(); // Assuming Book has a no-arg constructor
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book resultBook = bookService.saveBook(book);

        assertEquals(book, resultBook);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}