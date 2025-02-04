package es.unir.relatosdepapel.books.repository;

import es.unir.relatosdepapel.books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:isbn IS NULL OR b.isbn = :isbn) " +
            "AND (:category IS NULL OR LOWER(b.category) = LOWER(:category))")
    Page<Book> findBooksByParameters(
            @Param("title") String title,
            @Param("author") String author,
            @Param("isbn") String isbn,
            @Param("category") String category,
            Pageable pageable
    );

    @Query("SELECT b FROM Book b WHERE b.isbn LIKE %:isbn%")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    @Query("SELECT b.id FROM Book b WHERE b.isbn LIKE %:isbn% AND b.visibility = TRUE")
    Optional<Long> findBookIdByIsbnAndVisibilityTrue(@Param("isbn") String isbn);

}
