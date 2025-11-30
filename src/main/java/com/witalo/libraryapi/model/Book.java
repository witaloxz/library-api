package com.witalo.libraryapi.model;

import com.witalo.libraryapi.model.enums.BookGenres;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
