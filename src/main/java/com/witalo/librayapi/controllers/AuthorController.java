package com.witalo.librayapi.controllers;

import com.witalo.librayapi.dto.AuthorDTO;
import com.witalo.librayapi.services.AuthorService;
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
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> findAllPaged(Pageable pageable){
        Page<AuthorDTO> list = authorService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable UUID id){
        AuthorDTO authorDTO = authorService.findById(id);
        return ResponseEntity.ok().body(authorDTO);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> insert(@Valid @RequestBody AuthorDTO authorDTO){
        authorDTO = authorService.insert(authorDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(authorDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(authorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update (@PathVariable UUID id, @Valid @RequestBody AuthorDTO dto){
        dto = authorService.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}