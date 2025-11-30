package com.witalo.libraryapi.services;

import com.witalo.libraryapi.dto.BookDTO;
import com.witalo.libraryapi.model.Author;
import com.witalo.libraryapi.model.Book;
import com.witalo.libraryapi.model.enums.BookGenres;
import com.witalo.libraryapi.repository.AuthorRepository;
import com.witalo.libraryapi.repository.BookRepository;
import com.witalo.libraryapi.services.exceptions.DataBaseException;
import com.witalo.libraryapi.services.exceptions.InvalidBookGenreException;
import com.witalo.libraryapi.services.exceptions.ResourceAlreadyExistsException;
import com.witalo.libraryapi.services.exceptions.ResourceNotFoundException;
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
        validateBookDTO(dto, false, null);
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
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    // -------------------- Private Methods --------------------

    private void validateBookDTO(BookDTO dto, boolean isUpdate, String currentIsbn) {

        if (isUpdate) {
            if (!dto.getIsbn().equals(currentIsbn) && bookRepository.existsByIsbn(dto.getIsbn())) {
                throw new ResourceAlreadyExistsException("Book already exists with ISBN: " + dto.getIsbn());
            }
        } else {
            if (bookRepository.existsByIsbn(dto.getIsbn())) {
                throw new ResourceAlreadyExistsException("Book already exists with ISBN: " + dto.getIsbn());
            }
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new DataBaseException("Book title is required");
        }

        if (dto.getAuthorId() == null) {
            throw new DataBaseException("Author is required");
        }

        if (dto.getDatePublication() == null) {
            throw new DataBaseException("Publication date is required");
        }
        if (dto.getDatePublication().isAfter(LocalDate.now())) {
            throw new DataBaseException("Publication date cannot be in the future");
        }

        if (dto.getDatePublication().getYear() >= 2020 && dto.getPrice() == null) {
            throw new DataBaseException("Price is required for books published from 2020");
        }

        if (dto.getBookGenres() == null) {
            throw new InvalidBookGenreException("Book genre is required");
        }

    }

    private Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDatePublication(dto.getDatePublication());
        book.setBookGenres(dto.getBookGenres());
        book.setPrice(dto.getPrice());
        book.setAuthor(getAuthorOrThrow(dto.getAuthorId()));

        return book;
    }

    private void updateEntity(Book book, BookDTO dto) {
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDatePublication(dto.getDatePublication());
        book.setBookGenres(dto.getBookGenres());
        book.setPrice(dto.getPrice());
        book.setAuthor(getAuthorOrThrow(dto.getAuthorId()));
    }

    private Author getAuthorOrThrow(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }
}