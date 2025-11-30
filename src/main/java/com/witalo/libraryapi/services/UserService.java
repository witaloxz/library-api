package com.witalo.libraryapi.services;

import com.witalo.libraryapi.dto.UserDTO;
import com.witalo.libraryapi.model.User;
import com.witalo.libraryapi.repository.UserRepository;
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
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(null);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserDTO(user);

    }

    @Transactional
    public UserDTO insert(UserDTO dto){
        if(userRepository.existsByLogin(dto.getLogin())){
            throw new ResourceAlreadyExistsException("User already exists with login: " + dto.getLogin());
        }

        User entity = toEntity(dto);
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(UUID id, UserDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(userRepository.existsByLoginAndIdNot(dto.getLogin(),id)){
            throw new ResourceAlreadyExistsException("Another user already exists with login: " + dto.getLogin());
        }

        updateEntity(user, dto);
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public void delete(UUID id ){


        assert id != null;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        try {
            userRepository.delete(user);
        }
        catch (Exception e){
            throw new DataBaseException("Could not delete user: " + e.getMessage());
        }

    }

    // -------------------- Private Methods --------------------

    private User toEntity(UserDTO dto){
        User entity = new User();
        entity.setLogin(dto.getLogin());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(dto.getRoles());
        return entity;
    }

    private void updateEntity(User entity, UserDTO dto){
        entity.setLogin(dto.getLogin());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(dto.getRoles());
    }
}