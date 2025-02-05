package es.unir.relatosdepapel.books.service;

import es.unir.relatosdepapel.books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IBookService {

    Book createBook(Book book);
    Page<Book> getAllBooks(Pageable pageable);
    void validateSortField(String sortField);
    Optional<Book> getBookById(Long id);
    Optional<Book> getBookByIsbn(String isbn);
    Book updateBook(Long id, Book book);
    Book partialUpdateBook(Long id, Book partialUpdate);
    void deleteBook(Long id);
    Page<Book> searchBooks(String title, String author, String isbn, String category,
                           int page, int size, String sortField, String sortDirection);

}
