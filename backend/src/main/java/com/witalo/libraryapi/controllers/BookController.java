package com.witalo.libraryapi.controllers;

import com.witalo.libraryapi.dto.BookDTO;
import com.witalo.libraryapi.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(Pageable pageable) {
        Page<BookDTO> list = bookService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable UUID id){
        BookDTO bookDTO = bookService.findById(id);
        return ResponseEntity.ok().body(bookDTO);
    }

    @PostMapping
    public ResponseEntity<BookDTO> insert(@Valid @RequestBody BookDTO dto) {
        dto = bookService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<BookDTO> update (@PathVariable UUID id,@Valid @RequestBody BookDTO dto) {
        dto = bookService.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable UUID id){
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
