package com.witalo.libraryapi.repository;

import com.witalo.libraryapi.model.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByNameAndDateAndNationality(String name, LocalDate date, String nationality);
    boolean existsByNameAndDateAndNationalityAndIdNot(String name, LocalDate date, String nationality, UUID id);

    boolean existsByNameAndDate(String name, LocalDate date);
    boolean existsByNameAndDateAndIdNot(String name, LocalDate date, UUID id);
}