package com.witalo.librayapi.services;

import com.witalo.librayapi.dto.BookDTO;
import com.witalo.librayapi.model.Author;
import com.witalo.librayapi.model.Book;
import com.witalo.librayapi.repository.AuthorRepository;
import com.witalo.librayapi.repository.BookRepository;
import com.witalo.librayapi.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> findAllPaged(Pageable pageable){
        Page<Book> list = bookRepository.findAll(pageable);
        return list.map(BookDTO::new);
    }

    @Transactional(readOnly = true)
    public BookDTO findById(UUID uuid){
        Book book = bookRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + uuid));
        return new BookDTO(book);
    }

    @Transactional
    public BookDTO insert(BookDTO dto){
        Book book = toEntity(dto);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    @Transactional
    public BookDTO update(UUID id, BookDTO dto){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        updateEntity(book, dto);
        book = bookRepository.save(book);
        return new BookDTO(book);
    }

    @Transactional
    public void delete(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);

    }

    private Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setDatePublication(dto.getDatePublication());
        book.setBookGenres(dto.getBookGenres());
        book.setPrice(dto.getPrice());

        if (dto.getAuthorId() != null) {
            Author author = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + dto.getAuthorId()));
            book.setAuthor(author);
        }

        return book;
    }

    private void updateEntity(Book entity, BookDTO dto) {
        entity.setIsbn(dto.getIsbn());
        entity.setTitle(dto.getTitle());
        entity.setDatePublication(dto.getDatePublication());
        entity.setBookGenres(dto.getBookGenres());
        entity.setPrice(dto.getPrice());

        if (dto.getAuthorId() != null) {
            Author author = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + dto.getAuthorId()));
            entity.setAuthor(author);
        }
    }
}