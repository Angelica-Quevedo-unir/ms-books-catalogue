package es.unir.relatosdepapel.books.hateoas;

import es.unir.relatosdepapel.books.model.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookModel extends RepresentationModel<BookModel> {

    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String category;
    private String isbn;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isDigital;
    private Byte rating;
    private Boolean visibility;

    public BookModel(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publicationDate = book.getPublicationDate();
        this.category = book.getCategory();
        this.isbn = book.getIsbn();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.imageUrl = book.getImageUrl();
        this.isDigital = book.getIsDigital();
        this.rating = book.getRating();
    }
}
