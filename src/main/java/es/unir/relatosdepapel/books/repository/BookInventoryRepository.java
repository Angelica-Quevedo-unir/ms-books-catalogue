package es.unir.relatosdepapel.books.repository;

import es.unir.relatosdepapel.books.model.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookInventoryRepository extends JpaRepository<BookInventory, Long> {

    @Query("SELECT bi.stock FROM BookInventory bi WHERE bi.bookId = :bookId")
    int findStockByBookId(@Param("bookId") Long bookId);

    @Transactional
    @Modifying
    @Query("UPDATE BookInventory bi SET bi.stock = :newStock WHERE bi.bookId = :bookId")
    Integer updateStock(@Param("newStock") int newStock, @Param("bookId") Long bookId);

}
