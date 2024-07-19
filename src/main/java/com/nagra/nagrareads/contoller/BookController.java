package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.model.Book;
import com.nagra.nagrareads.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }
}