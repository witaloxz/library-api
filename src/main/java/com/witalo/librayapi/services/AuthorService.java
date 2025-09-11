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

import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<AuthorDTO> findAllPaged(Pageable pageable){
        Page<Author> list = authorRepository.findAll(pageable);
        return list.map(AuthorDTO::new);
    }

    @Transactional(readOnly = true)
    public AuthorDTO findById(UUID id){
        Author author = authorRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return  new AuthorDTO(author);
    }

    @Transactional
    public AuthorDTO insert(AuthorDTO dto) {

        if(authorRepository.existsByNameAndDateAndNationality(dto.getName(), dto.getDate(), dto.getNationality())){
            throw new ResourceAlreadyExistsException("Author already exists with the same name, date of birth and nationality");
        }
        Author entity = toEntity(dto);
        entity = authorRepository.save(entity);
        return new AuthorDTO(entity);
    }

    @Transactional
    public AuthorDTO update (UUID id, AuthorDTO dto){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        if (authorRepository.existsByNameAndDateAndNationalityAndIdNot(dto.getName(), dto.getDate(), dto.getNationality(), id)) {
            throw new ResourceAlreadyExistsException("Another author with the same name, date of birth and nationality already exists");
        }

        updateEntity(author, dto);
        author = authorRepository.save(author);
        return new AuthorDTO(author);
    }

    @Transactional
    public void delete(UUID id){
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));

            if (!author.getBooks().isEmpty()) {
                throw new DatabaseException("Cannot delete author with existing books");
            }
            authorRepository.delete(author);

        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getMessage());
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
