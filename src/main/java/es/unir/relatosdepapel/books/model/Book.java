package es.unir.relatosdepapel.books.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_books_title", columnList = "title"),
        @Index(name = "idx_books_author", columnList = "author"),
        @Index(name = "idx_books_category", columnList = "category"),
        @Index(name = "idx_books_title_author", columnList = "title, author")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    private String description;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_digital")
    private Boolean isDigital;

    @Column(nullable = false)
    private Byte rating;

    @Column(nullable = false)
    private Boolean visibility = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Book(String title, String author, LocalDate publicationDate, String category, String isbn,
                String description, BigDecimal price, String imageUrl, Boolean isDigital, Byte rating, Boolean visibility) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.category = category;
        this.isbn = isbn;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDigital = isDigital;
        this.rating = rating;
        this.visibility = visibility;
    }

    public void setRating(Byte rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", category='" + category + '\'' +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isDigital='" + isDigital + '\'' +
                ", rating=" + rating +
                ", visibility=" + visibility +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
