package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the books available in store",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check the request header!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed. Please check if you have access!",
                    content = @Content)})
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Add a new book with its information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request. Please check the request body!",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check the request header!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed. Please check if you have access!",
                    content = @Content)})
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) {
        Book savedBook = bookService.saveBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request. Please check the request body!",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check the request header!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed. Please check if you have access!",
                    content = @Content)})
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@RequestBody @Valid Book book) {
        Book updatedBookDetails = bookService.updateBook(book);
        return new ResponseEntity<>(updatedBookDetails, HttpStatus.OK);
    }

    @Operation(summary = "Get book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the book with the given ISBN",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request. Please check the request body!",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check the request header!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed. Please check if you have access!",
                    content = @Content)})
    @GetMapping("/{isbn}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Book> getByIsbn(@PathVariable @Valid String isbn) {
        return new ResponseEntity<>(bookService.getByIsbn(isbn), HttpStatus.OK);
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted a book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request. Please check the request body!",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check the request header!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Authorization failed. Please check if you have access!",
                    content = @Content)})
    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Book> deleteByIsbn(@PathVariable @Valid String isbn) {
        bookService.deleteByIsbn(isbn);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}