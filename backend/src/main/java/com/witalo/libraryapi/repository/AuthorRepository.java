package com.witalo.libraryapi.repository;

import com.witalo.libraryapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByNameAndDateAndNationality(String name, LocalDate date, String nationality);
    boolean existsByNameAndDateAndNationalityAndIdNot(String name, LocalDate date, String nationality, UUID id);
}
