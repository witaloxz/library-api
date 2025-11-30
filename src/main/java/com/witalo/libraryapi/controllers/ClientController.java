package com.witalo.libraryapi.controllers;

import com.witalo.libraryapi.dto.ClientDTO;
import com.witalo.libraryapi.services.ClientService;
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
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Clients management API")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieve a paginated list of all library clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<Page<ClientDTO>> findAll (Pageable pageable){
        Page<ClientDTO> list = clientService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieve a specific client by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found and returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientDTO> findByID (@PathVariable UUID id){
        ClientDTO clientDTO = clientService.findById(id);
        return ResponseEntity.ok().body(clientDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new client", description = "Register a new client in the library system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "409", description = "Client already exists"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<ClientDTO> insert(@Valid @RequestBody ClientDTO dto) {
        dto = clientService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update client", description = "Update an existing client's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Client conflict - duplicate data"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    public ResponseEntity<ClientDTO> update (@PathVariable UUID id, @Valid @RequestBody ClientDTO clientDTO){
        clientDTO = clientService.update(id,clientDTO);
        return ResponseEntity.ok().body(clientDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client", description = "Remove a client from the library system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete - client has associated records")
    })
    public ResponseEntity<Void> delete (@PathVariable UUID id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
