package es.unir.relatosdepapel.books.service.impl;

import es.unir.relatosdepapel.books.exception.ResourceNotFoundException;
import es.unir.relatosdepapel.books.model.Book;
import es.unir.relatosdepapel.books.repository.BookRepository;
import es.unir.relatosdepapel.books.service.IBookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IBookServiceImpl implements IBookService {

    private final BookRepository bookRepository;

    public IBookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        validateBookExists(id);
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public Book partialUpdateBook(Long id, Book partialUpdate) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));

        Optional.ofNullable(partialUpdate.getTitle()).ifPresent(existingBook::setTitle);
        Optional.ofNullable(partialUpdate.getAuthor()).ifPresent(existingBook::setAuthor);
        Optional.ofNullable(partialUpdate.getPublicationDate()).ifPresent(existingBook::setPublicationDate);
        Optional.ofNullable(partialUpdate.getCategory()).ifPresent(existingBook::setCategory);
        Optional.ofNullable(partialUpdate.getIsbn()).ifPresent(existingBook::setIsbn);
        Optional.ofNullable(partialUpdate.getRating()).ifPresent(existingBook::setRating);
        Optional.ofNullable(partialUpdate.getVisibility()).ifPresent(existingBook::setVisibility);

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        validateBookExists(id);
        bookRepository.deleteById(id);
    }

    @Override
    public Page<Book> searchBooks(String title, String author, String isbn, String category,
                                  int page, int size, String sortField, String sortDirection) {
        Sort sort = createSort(sortField, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findBooksByParameters(title, author, isbn, category, pageable);
    }

    private void validateBookExists(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book with ID " + id + " not found");
        }
    }

    private Sort createSort(String sortField, String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC");
            return sortField != null ? JpaSort.unsafe(direction, sortField) : Sort.by(direction, "id");
        } catch (IllegalArgumentException e) {
            return Sort.by(Sort.Direction.ASC, "id");
        }
    }
}
