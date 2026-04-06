package com.aditya.financial_dashboard_system.services;

import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.exceptions.noSuchEntityExists;
import com.aditya.financial_dashboard_system.repos.userRepo;
import com.aditya.financial_dashboard_system.utils.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class userServiceTest {

    @Mock
    private userRepo repo;

    @InjectMocks
    private userService service;

    private userEntity sampleUser;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleUser = new userEntity();
        sampleUser.setId(sampleId);
        sampleUser.setUsername("aditya");
        sampleUser.setEmail("aditya@example.com");
        sampleUser.setPassword("secret123");
        sampleUser.setRole(Role.VIEWER);
    }

    // --- getAllUsers ---

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        when(repo.findAll()).thenReturn(List.of(sampleUser));

        ResponseEntity<?> response = service.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
    }

    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsers() {
        when(repo.findAll()).thenReturn(List.of());

        ResponseEntity<?> response = service.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> body = (List<?>) response.getBody();
        assertTrue(body.isEmpty());
    }

    // --- getUserById ---

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = service.getUserById(sampleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserById_shouldThrow_whenUserNotFound() {
        when(repo.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(noSuchEntityExists.class, () -> service.getUserById(sampleId));
    }

    // --- saveUser ---

    @Test
    void saveUser_shouldReturnCreated_whenValidUser() {
        ResponseEntity<?> response = service.saveUser(sampleUser, Role.VIEWER);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(repo, times(1)).save(sampleUser); // confirm save was called
    }

    @Test
    void saveUser_shouldThrow_whenUsernameIsNull() {
        sampleUser.setUsername(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveUser(sampleUser, Role.VIEWER));
    }

    @Test
    void saveUser_shouldThrow_whenUsernameIsBlank() {
        sampleUser.setUsername("   ");

        assertThrows(IllegalArgumentException.class,
                () -> service.saveUser(sampleUser, Role.VIEWER));
    }

    @Test
    void saveUser_shouldThrow_whenEmailIsNull() {
        sampleUser.setEmail(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveUser(sampleUser, Role.VIEWER));
    }

    @Test
    void saveUser_shouldThrow_whenPasswordIsNull() {
        sampleUser.setPassword(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveUser(sampleUser, Role.VIEWER));
    }

    @Test
    void saveUser_shouldEncodePassword() {
        String rawPassword = "secret123";
        sampleUser.setPassword(rawPassword);

        service.saveUser(sampleUser, Role.VIEWER);

        // password should be encoded (not plain text anymore)
        assertNotEquals(rawPassword, sampleUser.getPassword());
    }

    // --- updateUser ---

    @Test
    void updateUser_shouldUpdateAndReturnUser_whenUserExists() {
        userEntity updatedData = new userEntity();
        updatedData.setUsername("newName");
        updatedData.setEmail("new@example.com");

        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = service.updateUser(sampleId, updatedData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(repo, times(1)).save(sampleUser);
    }

    @Test
    void updateUser_shouldThrow_whenUserNotFound() {
        when(repo.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(noSuchEntityExists.class,
                () -> service.updateUser(sampleId, sampleUser));
    }

    // --- deleteById ---

    @Test
    void deleteById_shouldDelete_whenUserExists() {
        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = service.deleteById(sampleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(repo, times(1)).deleteById(sampleId);
    }

    // --- getUserByUsername ---

    @Test
    void getUserByUsername_shouldReturnUser_whenUsernameExists() {
        when(repo.findByUsername("aditya")).thenReturn(sampleUser);

        ResponseEntity<?> response = service.getUserByUsername("aditya");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserByUsername_shouldThrow_whenUsernameNotFound() {
        when(repo.findByUsername("ghost")).thenReturn(null);

        assertThrows(noSuchEntityExists.class,
                () -> service.getUserByUsername("ghost"));
    }
}