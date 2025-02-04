package es.unir.relatosdepapel.books.service;

import es.unir.relatosdepapel.books.dto.BookAvailabilityResponseDTO;
import es.unir.relatosdepapel.books.dto.BookStockReductionResponseDTO;

public interface IBookInventoryService {

    BookAvailabilityResponseDTO checkBookAvailability(String isbn, Long quantity);
    BookStockReductionResponseDTO reduceBookStock(String isbn, int quantity);
    BookStockReductionResponseDTO restoreBookStock(String isbn, int quantity);
}
