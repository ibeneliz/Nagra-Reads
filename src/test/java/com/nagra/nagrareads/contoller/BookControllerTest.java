package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.configuration.jwt.NagraUserDetailService;
import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private NagraUserDetailService nagraUserDetailService;

    @Test
    @WithMockUser(roles = {"USER"})
    void when_getAllBooks_should_returnAllBooks() throws Exception {
        Book book1 = new Book();
        book1.setTitle("Book One");
        Book book2 = new Book();
        book2.setTitle("Book Two");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Book One")))
                .andExpect(jsonPath("$[1].title", is("Book Two")));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void when_addBook_should_addBookToDB() throws Exception {
        Book book = new Book();
        book.setTitle("Example Book Title");
        book.setAuthor("Author Name");
        book.setGenre("Fiction");
        book.setDescription("A detailed description of the book.");
        book.setPublicationDate(LocalDate.now());
        book.setIsbn("9783161484101");
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Example Book Title 11",
                                  "author": "Author Name",
                                  "description": "A detailed description of the book.",
                                  "publicationDate": "2023-01-25",
                                  "genre": "Fiction",
                                  "isbn": "9783161484101"
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Example Book Title")));
        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void when_addBook_should_failToAddWithoutTitle() throws Exception {
        Book book = new Book();
        book.setAuthor("Author Name");
        book.setGenre("Fiction");
        book.setDescription("A detailed description of the book.");
        book.setPublicationDate(LocalDate.now());
        book.setIsbn("9783161484101");
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": "Author Name",
                                  "description": "A detailed description of the book.",
                                  "publicationDate": "2023-01-25",
                                  "genre": "Fiction",
                                  "isbn": "9783161484101"
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("must not be blank")));
        verify(bookService, times(0)).saveBook(any(Book.class));
    }
}