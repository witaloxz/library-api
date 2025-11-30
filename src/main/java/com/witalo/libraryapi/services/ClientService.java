package com.witalo.libraryapi.services;

import com.witalo.libraryapi.dto.ClientDTO;
import com.witalo.libraryapi.model.Client;
import com.witalo.libraryapi.repository.ClientRepository;
import com.witalo.libraryapi.services.exceptions.DataBaseException;
import com.witalo.libraryapi.services.exceptions.ResourceAlreadyExistsException;
import com.witalo.libraryapi.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(Pageable pageable){
        return clientRepository.findAll(pageable).map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findByClientId(String clientId) {
        Client client = clientRepository.findByClientId(clientId);
        if (client == null) {
            throw new ResourceNotFoundException("Client not found with clientId: " + clientId);
        }
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(UUID id){
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO){
        validateClientDTO(clientDTO, false, null);
        Client client = toEntity(clientDTO);
        clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(UUID uuid, ClientDTO clientDTO){
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + uuid));

        validateClientDTO(clientDTO, true, client.getClientId());
        updateEntity(client, clientDTO);
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public void delete(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        try {
            clientRepository.delete(client);
        } catch (Exception e) {
            throw new DataBaseException("Could not delete client: " + e.getMessage());
        }
    }

    // -------------------- Private Methods --------------------

    private void validateClientDTO(ClientDTO dto, boolean isUpdate, String currentClientId) {

        if (dto.getClientId() == null || dto.getClientId().isBlank()) {
            throw new DataBaseException("Client ID is required");
        }

        if (dto.getClientSecret() == null || dto.getClientSecret().isBlank()) {
            throw new DataBaseException("Client secret is required");
        }

        if (dto.getClientSecret().length() < 8) {
            throw new DataBaseException("Client secret must have at least 8 characters");
        }

        if (dto.getRedirectURI() == null || dto.getRedirectURI().isBlank()) {
            throw new DataBaseException("Redirect URI is required");
        }

        if (dto.getScope() == null || dto.getScope().isBlank()) {
            throw new DataBaseException("Scope is required");
        }


        if (isUpdate) {
            if (!dto.getClientId().equals(currentClientId) &&
                    clientRepository.existsByClientId(dto.getClientId())) {
                throw new ResourceAlreadyExistsException("Client ID already exists: " + dto.getClientId());
            }
        } else {
            if (clientRepository.existsByClientId(dto.getClientId())) {
                throw new ResourceAlreadyExistsException("Client ID already exists: " + dto.getClientId());
            }
        }
    }

    private Client toEntity(ClientDTO clientDTO){
        Client entity = new Client();
        entity.setClientId(clientDTO.getClientId());
        entity.setClientSecret(passwordEncoder.encode(clientDTO.getClientSecret()));
        entity.setRedirectURI(clientDTO.getRedirectURI());
        entity.setScope(clientDTO.getScope());
        return entity;
    }

    private void updateEntity(Client entity, ClientDTO clientDTO){
        entity.setClientId(clientDTO.getClientId());
        entity.setClientSecret(passwordEncoder.encode(clientDTO.getClientSecret()));
        entity.setRedirectURI(clientDTO.getRedirectURI());
        entity.setScope(clientDTO.getScope());
    }
}