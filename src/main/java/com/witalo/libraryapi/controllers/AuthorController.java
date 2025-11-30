package com.witalo.libraryapi.controllers;

import com.witalo.libraryapi.dto.AuthorDTO;
import com.witalo.libraryapi.services.AuthorService;
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
@RequestMapping("/authors")
@Tag(name = "Authors", description = "Authors management API")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve a paginated list of all authors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<Page<AuthorDTO>> findAllPaged(Pageable pageable){
        Page<AuthorDTO> list = authorService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve a specific author by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found and returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<AuthorDTO> findById(@PathVariable UUID id){
        AuthorDTO authorDTO = authorService.findById(id);
        return ResponseEntity.ok().body(authorDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new author", description = "Register a new author in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "409", description = "Author already exists"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<AuthorDTO> insert(@Valid @RequestBody AuthorDTO authorDTO){
        authorDTO = authorService.insert(authorDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(authorDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(authorDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update author", description = "Update an existing author's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Author conflict - duplicate data"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<AuthorDTO> update (@PathVariable UUID id, @Valid @RequestBody AuthorDTO dto){
        dto = authorService.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author", description = "Remove an author from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete - author has associated books")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
