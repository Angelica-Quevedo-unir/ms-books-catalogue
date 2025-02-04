package es.unir.relatosdepapel.books.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookAvailabilityResponseDTO {
    private boolean available;
    private int availableStock;
    private String message;
}
