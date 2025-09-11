package com.witalo.librayapi.model;

import com.witalo.librayapi.model.enums.BookGenres;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String isbn;
    private String title;
    private LocalDate datePublication;

    @Enumerated(EnumType.STRING)
    private BookGenres bookGenres;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private Author author;
}