package es.unir.relatosdepapel.books.service.impl;

import es.unir.relatosdepapel.books.exception.ResourceNotFoundException;
import es.unir.relatosdepapel.books.exception.StockUpdateException;
import es.unir.relatosdepapel.books.dto.BookAvailabilityResponseDTO;
import es.unir.relatosdepapel.books.dto.BookStockReductionResponseDTO;
import es.unir.relatosdepapel.books.repository.BookInventoryRepository;
import es.unir.relatosdepapel.books.repository.BookRepository;
import es.unir.relatosdepapel.books.service.IBookInventoryService;
import org.springframework.stereotype.Service;

@Service
public class IBookInventoryServiceImpl implements IBookInventoryService {

    private static final String BOOK_NOT_FOUND_MSG = "Book with ISBN %s not found";

    private final BookRepository bookRepository;
    private final BookInventoryRepository bookInventoryRepository;

    public IBookInventoryServiceImpl(BookRepository bookRepository,
                                     BookInventoryRepository bookInventoryRepository) {
        this.bookRepository = bookRepository;
        this.bookInventoryRepository = bookInventoryRepository;
    }

    @Override
    public BookAvailabilityResponseDTO checkBookAvailability(String isbn, Long quantity) {
        Long bookId = getBookIdOrThrow(isbn);
        int currentStock = bookInventoryRepository.findStockByBookId(bookId);

        boolean isAvailable = currentStock >= quantity;
        String message = isAvailable
                ? String.format("The book with ISBN %s is available", isbn)
                : String.format("Not enough stock for book with ISBN %s", isbn);

        return BookAvailabilityResponseDTO.builder()
                .available(isAvailable)
                .availableStock(currentStock)
                .message(message)
                .build();
    }

    @Override
    public BookStockReductionResponseDTO reduceBookStock(String isbn, int quantity) {
        Long bookId = getBookIdOrThrow(isbn);
        int currentStock = bookInventoryRepository.findStockByBookId(bookId);

        validateStockQuantity(currentStock, quantity);
        updateStockOrThrow(bookId, currentStock - quantity);

        return buildStockResponse(isbn, currentStock - quantity);
    }

    @Override
    public BookStockReductionResponseDTO restoreBookStock(String isbn, int quantity) {
        Long bookId = getBookIdOrThrow(isbn);
        int currentStock = bookInventoryRepository.findStockByBookId(bookId);

        updateStockOrThrow(bookId, currentStock + quantity);

        return buildStockResponse(isbn, currentStock + quantity);
    }

    private Long getBookIdOrThrow(String isbn) {
        return bookRepository.findBookIdByIsbnAndVisibilityTrue(isbn)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND_MSG, isbn)));
    }

    private void validateStockQuantity(int currentStock, int requestedQuantity) {
        if (currentStock < requestedQuantity) {
            throw new StockUpdateException("The quantity to reduce exceeds the current stock");
        }
    }

    private void updateStockOrThrow(Long bookId, int newStock) {
        if (bookInventoryRepository.updateStock(newStock, bookId) != 1) {
            throw new StockUpdateException("Error updating stock");
        }
    }

    private BookStockReductionResponseDTO buildStockResponse(String isbn, int remainingStock) {
        return BookStockReductionResponseDTO.builder()
                .status("OK")
                .productId(isbn)
                .remainingStock(remainingStock)
                .build();
    }
}
