package com.witalo.librayapi.services;

import com.witalo.librayapi.dto.BookDTO;
import com.witalo.librayapi.model.Author;
import com.witalo.librayapi.model.Book;
import com.witalo.librayapi.model.enums.BookGenres;
import com.witalo.librayapi.repository.AuthorRepository;
import com.witalo.librayapi.repository.BookRepository;
import com.witalo.librayapi.services.exceptions.DatabaseException;
import com.witalo.librayapi.services.exceptions.InvalidBookGenreException;
import com.witalo.librayapi.services.exceptions.ResourceAlreadyExistsException;
import com.witalo.librayapi.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    public Page<BookDTO> findAllPaged(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookDTO::new);
    }

    public BookDTO findById(UUID uuid) {
        Book book = bookRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + uuid));
        return new BookDTO(book);
    }

    @Transactional
    public BookDTO insert(BookDTO dto) {
        validateBookDTO(dto, false);
        Book book = toEntity(dto);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    @Transactional
    public BookDTO update(UUID id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        validateBookDTO(dto, true, book.getIsbn());
        updateEntity(book, dto);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    @Transactional
    public void delete(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }


    // -------------------- Private Methods --------------------

    private void validateBookDTO(BookDTO dto, boolean isUpdate) {
        validateBookDTO(dto, isUpdate, null);
    }

    private void validateBookDTO(BookDTO dto, boolean isUpdate, String currentIsbn) {

        if (!isUpdate || !dto.getIsbn().equals(currentIsbn)) {
            if (bookRepository.existsByIsbn(dto.getIsbn())) {
                throw new ResourceAlreadyExistsException("Book already exists with ISBN: " + dto.getIsbn());
            }
        }


        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new DatabaseException("Book title is required");
        }


        if (dto.getDatePublication() == null) {
            throw new DatabaseException("Publication date is required");
        }
        if (dto.getDatePublication().isAfter(LocalDate.now())) {
            throw new DatabaseException("Publication date cannot be in the future");
        }


        if (dto.getDatePublication().getYear() >= 2020 && dto.getPrice() == null) {
            throw new DatabaseException("Price is required for books published from 2020");
        }


        if (dto.getBookGenres() == null) {
            throw new InvalidBookGenreException("Book genre is required and must be valid.");
        }
        try {
            BookGenres.valueOf(dto.getBookGenres().name());
        } catch (IllegalArgumentException e) {
            throw new InvalidBookGenreException("Invalid book genre: " + dto.getBookGenres());
        }
    }

    private Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDatePublication(dto.getDatePublication());
        book.setBookGenres(dto.getBookGenres());
        book.setPrice(dto.getPrice());

        if (dto.getAuthorId() != null) {
            book.setAuthor(getAuthorOrThrow(dto.getAuthorId()));
        }
        return book;
    }

    private void updateEntity(Book book, BookDTO dto) {
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDatePublication(dto.getDatePublication());
        book.setBookGenres(dto.getBookGenres());
        book.setPrice(dto.getPrice());

        if (dto.getAuthorId() != null) {
            book.setAuthor(getAuthorOrThrow(dto.getAuthorId()));
        }
    }

    private Author getAuthorOrThrow(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }
}
