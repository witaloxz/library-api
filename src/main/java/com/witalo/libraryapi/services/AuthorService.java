package com.witalo.libraryapi.services;

import com.witalo.libraryapi.dto.AuthorDTO;
import com.witalo.libraryapi.model.Author;
import com.witalo.libraryapi.model.User;
import com.witalo.libraryapi.repository.AuthorRepository;
import com.witalo.libraryapi.security.SecurityService;
import com.witalo.libraryapi.services.exceptions.DataBaseException;
import com.witalo.libraryapi.services.exceptions.ResourceAlreadyExistsException;
import com.witalo.libraryapi.services.exceptions.ResourceNotFoundException;
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

    @Autowired
    private SecurityService securityService;

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

        // ✅ VALIDAÇÃO DE PROPRIEDADE: Só o usuário que criou pode editar
        validateAuthorOwnership(author);

        validateAuthorDTO(dto, true, id);
        updateEntity(author, dto);
        author = authorRepository.save(author);
        return new AuthorDTO(author);
    }

    @Transactional
    public void delete(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        validateAuthorOwnership(author);

        if (!author.getBooks().isEmpty()) {
            throw new DataBaseException("Cannot delete author with existing books");
        }

        try {
            authorRepository.delete(author);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation: " + e.getMostSpecificCause().getMessage());
        }
    }

    // -------------------- Private Methods --------------------

    private void validateAuthorDTO(AuthorDTO dto, boolean isUpdate, UUID currentId) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new DataBaseException("Author name is required");
        }

        if (dto.getDate() == null) {
            throw new DataBaseException("Author birth date is required");
        }

        if (dto.getDate().isAfter(LocalDate.now())) {
            throw new DataBaseException("Birth date cannot be in the future");
        }

        if (dto.getNationality() == null || dto.getNationality().isBlank()) {
            throw new DataBaseException("Author nationality is required");
        }

        if (!isUpdate) {
            if (authorRepository.existsByNameAndDate(dto.getName(), dto.getDate())) {
                throw new ResourceAlreadyExistsException(
                        "Author already exists with the same name and date of birth"
                );
            }
        } else {
            if (authorRepository.existsByNameAndDateAndIdNot(dto.getName(), dto.getDate(), currentId)) {
                throw new ResourceAlreadyExistsException(
                        "Another author already exists with the same name and date of birth"
                );
            }
        }
    }


    private void validateAuthorOwnership(Author author) {
        User currentUser = securityService.getByLoginUser();
        if (!author.getUser().getId().equals(currentUser.getId())) {
            throw new DataBaseException("You can only modify authors created by you");
        }
    }

    private Author toEntity(AuthorDTO dto) {
        Author entity = new Author();
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setNationality(dto.getNationality());
        entity.setUser(securityService.getByLoginUser());
        return entity;
    }

    private void updateEntity(Author author, AuthorDTO dto) {
        author.setName(dto.getName());
        author.setDate(dto.getDate());
        author.setNationality(dto.getNationality());
    }
}