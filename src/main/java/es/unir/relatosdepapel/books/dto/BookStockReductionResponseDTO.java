package es.unir.relatosdepapel.books.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookStockReductionResponseDTO {
    private String status;
    private String productId;
    private int remainingStock;
}
