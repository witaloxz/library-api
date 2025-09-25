package com.witalo.libraryapi.dto;

import com.witalo.libraryapi.model.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @NotBlank(message = "Author name is required")
    private String name;
    @NotNull(message = "Birth date is required")
    private LocalDate date;
    @NotBlank(message = "Nationality is required")
    private String nationality;

    public AuthorDTO(Author author){
        this.id = author.getId();
        this.name = author.getName();
        this.date = author.getDate();
        this.nationality = author.getNationality();
    }
}
