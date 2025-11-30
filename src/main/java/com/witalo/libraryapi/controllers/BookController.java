package com.witalo.libraryapi.controllers;

import com.witalo.libraryapi.dto.BookDTO;
import com.witalo.libraryapi.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Books management API")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a paginated list of all books with their details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<Page<BookDTO>> findAll(Pageable pageable) {
        Page<BookDTO> list = bookService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its unique ID with complete details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found and returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDTO> findById(@PathVariable UUID id){
        BookDTO bookDTO = bookService.findById(id);
        return ResponseEntity.ok().body(bookDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new book", description = "Register a new book in the library system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Book already exists"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<BookDTO> insert(@Valid @RequestBody BookDTO dto) {
        dto = bookService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    @Operation(summary = "Update book", description = "Update an existing book's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Book conflict - duplicate data"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<BookDTO> update (@PathVariable UUID id,@Valid @RequestBody BookDTO dto) {
        dto = bookService.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Remove a book from the library system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete - book has associated records")
    })
    public ResponseEntity<Void> delete (@PathVariable UUID id){
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
