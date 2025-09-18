package com.witalo.librayapi.dto;

import com.witalo.librayapi.model.Book;
import com.witalo.librayapi.model.enums.BookGenres;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @ISBN
    @NotBlank(message = "ISBN is required")
    private String isbn;
    @NotBlank(message = "Title is required")
    private String title;
    @Past
    @NotNull(message = "Publication date is required")
    private LocalDate datePublication;
    private BookGenres bookGenres;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;
    private UUID authorId;

    public BookDTO(Book book){
        this.id = book.getId();
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.datePublication = book.getDatePublication();
        this.bookGenres = book.getBookGenres();
        this.price = book.getPrice();
        this.authorId =  book.getAuthor() != null ? book.getAuthor().getId() : null;
    }
}