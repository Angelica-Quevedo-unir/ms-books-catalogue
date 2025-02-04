package es.unir.relatosdepapel.books.hateoas;

import es.unir.relatosdepapel.books.controller.BookController;
import es.unir.relatosdepapel.books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<BookModel>> {

    @Override
    public EntityModel<BookModel> toModel(Book book) {
        BookModel bookModel = new BookModel(book);

        bookModel.add(linkTo(methodOn(BookController.class).getBookById(book.getId())).withSelfRel());
        bookModel.add(linkTo(methodOn(BookController.class).getAllBooks(Pageable.unpaged())).withRel("all-books"));
        bookModel.add(linkTo(methodOn(BookController.class).getBookByIsbn(book.getIsbn())).withRel("by-isbn"));
        bookModel.add(linkTo(methodOn(BookController.class).updateBook(book.getId(), null)).withRel("update"));
        bookModel.add(linkTo(methodOn(BookController.class).deleteBook(book.getId())).withRel("delete"));

        return EntityModel.of(bookModel);
    }

    public Book toEntity(BookModel bookModel) {
        Book book = new Book();
        book.setId(bookModel.getId());
        book.setTitle(bookModel.getTitle());
        book.setAuthor(bookModel.getAuthor());
        book.setPublicationDate(bookModel.getPublicationDate());
        book.setCategory(bookModel.getCategory());
        book.setIsbn(bookModel.getIsbn());
        book.setRating(bookModel.getRating());
        book.setVisibility(bookModel.getVisibility());
        return book;
    }

    public PagedModel<EntityModel<BookModel>> toPagedModel(Page<Book> bookPage) {
        List<EntityModel<BookModel>> bookModels = bookPage.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                bookPage.getSize(), bookPage.getNumber(), bookPage.getTotalElements(), bookPage.getTotalPages()
        );

        return PagedModel.of(bookModels, metadata);
    }
}