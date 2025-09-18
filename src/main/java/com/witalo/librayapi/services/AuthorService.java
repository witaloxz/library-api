package com.witalo.librayapi.services;

import com.witalo.librayapi.dto.AuthorDTO;
import com.witalo.librayapi.model.Author;
import com.witalo.librayapi.repository.AuthorRepository;
import com.witalo.librayapi.services.exceptions.DatabaseException;
import com.witalo.librayapi.services.exceptions.ResourceAlreadyExistsException;
import com.witalo.librayapi.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<AuthorDTO> findAllPaged(Pageable pageable) {
        Page<Author> list = authorRepository.findAll(pageable);
        return list.map(AuthorDTO::new);
    }

    @Transactional(readOnly = true)
    public AuthorDTO findById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return new AuthorDTO(author);
    }

    @Transactional
    public AuthorDTO insert(AuthorDTO dto) {
        validateAuthorDTO(dto, false, null);

        Author entity = toEntity(dto);
        entity = authorRepository.save(entity);
        return new AuthorDTO(entity);
    }

    @Transactional
    public AuthorDTO update(UUID id, AuthorDTO dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        validateAuthorDTO(dto, true, id);

        updateEntity(author, dto);
        author = authorRepository.save(author);
        return new AuthorDTO(author);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

            if (!author.getBooks().isEmpty()) {
                throw new DatabaseException("Cannot delete author with existing books");
            }

            authorRepository.delete(author);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getMostSpecificCause().getMessage());
        }
    }

    // -------------------- Private Methods --------------------

    private void validateAuthorDTO(AuthorDTO dto, boolean isUpdate, UUID currentId) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new DatabaseException("Author name is required");
        }

        if (dto.getDate() == null) {
            throw new DatabaseException("Author birth date is required");
        }

        if (dto.getDate().isAfter(LocalDate.now())) {
            throw new DatabaseException("Birth date cannot be in the future");
        }

        if (dto.getNationality() == null || dto.getNationality().isBlank()) {
            throw new DatabaseException("Author nationality is required");
        }


        if (!isUpdate) {
            if (authorRepository.existsByNameAndDateAndNationality(dto.getName(), dto.getDate(), dto.getNationality())) {
                throw new ResourceAlreadyExistsException(
                        "Author already exists with the same name, date of birth and nationality"
                );
            }
        } else {
            if (authorRepository.existsByNameAndDateAndNationalityAndIdNot(dto.getName(), dto.getDate(), dto.getNationality(), currentId)) {
                throw new ResourceAlreadyExistsException(
                        "Another author already exists with the same name, date of birth and nationality"
                );
            }
        }
    }

    private Author toEntity(AuthorDTO dto) {
        Author entity = new Author();
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setNationality(dto.getNationality());
        return entity;
    }

    private void updateEntity(Author author, AuthorDTO dto) {
        author.setName(dto.getName());
        author.setDate(dto.getDate());
        author.setNationality(dto.getNationality());
    }
}
