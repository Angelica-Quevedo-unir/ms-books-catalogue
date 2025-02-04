package es.unir.relatosdepapel.books.controller;

import es.unir.relatosdepapel.books.dto.BookAvailabilityResponseDTO;
import es.unir.relatosdepapel.books.dto.BookStockReductionResponseDTO;
import es.unir.relatosdepapel.books.dto.BookStockUpdateRequestDTO;
import es.unir.relatosdepapel.books.service.IBookInventoryService;
import es.unir.relatosdepapel.books.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Book Inventory", description = "APIs for managing the book inventory")
public class BookInventoryController {

    private final IBookService bookService;
    private final IBookInventoryService bookInventoryService;

    public BookInventoryController(IBookService bookService,
                                   IBookInventoryService bookInventoryService) {
        this.bookService = bookService;
        this.bookInventoryService = bookInventoryService;
    }

    @Operation(
            summary = "Check book availability [INTERNAL]",
            description = "Checks if there is sufficient stock available for a book given its ISBN and the requested quantity."
    )
    @ApiResponse(responseCode = "200", description = "Stock verificado exitosamente")
    @ApiResponse(responseCode = "404", description = "El libro no fue encontrado")
    @GetMapping("/{isbn}/availability")
    public ResponseEntity<BookAvailabilityResponseDTO> checkBookAvailability(
            @PathVariable String isbn, @RequestParam("quantity") Long quantity) {
        return ResponseEntity.ok(bookInventoryService.checkBookAvailability(isbn, quantity));
    }

    @Operation(
            summary = "Reduce book stock [INTERNAL]",
            description = "Reduces the available stock of a book after a confirmed purchase."
    )
    @ApiResponse(responseCode = "200", description = "Stock reducido exitosamente")
    @ApiResponse(responseCode = "404", description = "El libro no fue encontrado")
    @ApiResponse(responseCode = "400", description = "Cantidad solicitada no válida")
    @PatchMapping("/{isbn}/stock/reduce")
    public ResponseEntity<BookStockReductionResponseDTO> reduceBookStock(
            @PathVariable String isbn, @RequestBody BookStockUpdateRequestDTO request) {
        return ResponseEntity.ok(bookInventoryService.reduceBookStock(isbn, request.getQuantity()));
    }

    @Operation(
            summary = "Restore book stock [INTERNAL]",
            description = "Restores the stock of a book after a purchase cancellation."
    )
    @ApiResponse(responseCode = "200", description = "Stock restaurado exitosamente")
    @ApiResponse(responseCode = "404", description = "El libro no fue encontrado")
    @ApiResponse(responseCode = "400", description = "Cantidad solicitada no válida")
    @PatchMapping("/{isbn}/stock/restore")
    public ResponseEntity<BookStockReductionResponseDTO> restoreBookStock(
            @PathVariable String isbn, @RequestBody BookStockUpdateRequestDTO request) {
        return ResponseEntity.ok(bookInventoryService.restoreBookStock(isbn, request.getQuantity()));
    }
}