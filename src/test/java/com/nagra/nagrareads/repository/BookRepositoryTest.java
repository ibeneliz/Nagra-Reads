package com.nagra.nagrareads.repository;

import com.nagra.nagrareads.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
}, showSql = true)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book newBook;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        newBook = new Book();
        newBook.setTitle("Example Book Title 11");
        newBook.setDescription("A detailed description of the book.");
        newBook.setAuthor("Author Name");
        newBook.setGenre("Fiction");
        newBook.setIsbn("1234567891234");
        newBook.setPublicationDate(LocalDate.now());
        bookRepository.save(newBook);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        bookRepository.delete(newBook);
    }

    @Test
    void addedBook_whenSaved_thenCanBeFoundById() {
        Book savedBook = bookRepository.findById(newBook.getId()).orElse(null);
        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(savedBook.getTitle(), newBook.getTitle());
        Assertions.assertEquals(savedBook.getDescription(), newBook.getDescription());
        Assertions.assertEquals(savedBook.getAuthor(), newBook.getAuthor());
        Assertions.assertEquals(savedBook.getGenre(), newBook.getGenre());
        Assertions.assertEquals(savedBook.getIsbn(), newBook.getIsbn());
        Assertions.assertEquals(savedBook.getPublicationDate(), newBook.getPublicationDate());
    }

    @Test
    void book_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        newBook.setDescription("A detailed description");
        bookRepository.save(newBook);

        Book updatedBook = bookRepository.findById(newBook.getId()).orElse(null);

        Assertions.assertNotNull(updatedBook);
        Assertions.assertEquals("A detailed description", updatedBook.getDescription());
    }
}