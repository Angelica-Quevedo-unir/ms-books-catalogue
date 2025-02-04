package es.unir.relatosdepapel.books.controller;

import es.unir.relatosdepapel.books.exception.ResourceNotFoundException;
import es.unir.relatosdepapel.books.hateoas.BookModel;
import es.unir.relatosdepapel.books.model.Book;
import es.unir.relatosdepapel.books.hateoas.BookModelAssembler;
import es.unir.relatosdepapel.books.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book Management", description = "APIs for managing books catalogue")
public class BookController {

    private final IBookService bookService;
    private final BookModelAssembler bookModelAssembler;

    public BookController(IBookService bookService, BookModelAssembler bookModelAssembler) {
        this.bookService = bookService;
        this.bookModelAssembler = bookModelAssembler;
    }

    @Operation(summary = "Create a new book", description = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @PostMapping
    public ResponseEntity<EntityModel<BookModel>> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookModelAssembler.toModel(createdBook));
    }

    @Operation(summary = "Get all books", description = "Retrieve all books with pagination")
    @ApiResponse(responseCode = "200", description = "Books list retrieved successfully")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BookModel>>> getAllBooks(@ParameterObject Pageable pageable) {
        Page<Book> bookPage = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(bookModelAssembler.toPagedModel(bookPage));
    }

    @Operation(summary = "Get a book by ID", description = "Retrieve a single book by its ID")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BookModel>> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return ResponseEntity.ok(bookModelAssembler.toModel(book));
    }

    @Operation(summary = "Update a book by ID", description = "Update an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BookModel>> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(bookModelAssembler.toModel(updatedBook));
    }

    @Operation(summary = "Delete a book by ID", description = "Delete a book by its ID")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a book by ISBN", description = "Retrieve a book by its ISBN")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<EntityModel<BookModel>> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.getBookByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
        return ResponseEntity.ok(bookModelAssembler.toModel(book));
    }
}
