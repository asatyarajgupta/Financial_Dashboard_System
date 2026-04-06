package com.aditya.financial_dashboard_system.services;

import com.aditya.financial_dashboard_system.entities.recordEntity;
import com.aditya.financial_dashboard_system.exceptions.noSuchEntityExists;
import com.aditya.financial_dashboard_system.repos.recordRepo;
import com.aditya.financial_dashboard_system.utils.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class recordServiceTest {

    @Mock
    private recordRepo repo;

    @InjectMocks
    private recordService service;

    private recordEntity sampleRecord;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleRecord = new recordEntity();
        sampleRecord.setId(sampleId);
        sampleRecord.setAmount(500.0);
        sampleRecord.setCategory(Category.INCOME);
        sampleRecord.setDescription("Salary");
        sampleRecord.setDateAndTime(LocalDateTime.now());
    }

    // --- getAllRecords ---

    @Test
    void getAllRecords_shouldReturnPageOfRecords() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<recordEntity> page = new PageImpl<>(List.of(sampleRecord));
        when(repo.findAll(pageable)).thenReturn(page);

        Page<recordEntity> result = service.getAllRecords(pageable);

        assertEquals(1, result.getTotalElements());
    }

    // --- getRecordById ---

    @Test
    void getRecordById_shouldReturnRecord_whenRecordExists() {
        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<?> response = service.getRecordById(sampleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRecordById_shouldThrow_whenRecordNotFound() {
        when(repo.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(noSuchEntityExists.class,
                () -> service.getRecordById(sampleId));
    }

    // --- getTotalIncome ---

    @Test
    void getTotalIncome_shouldReturnTotalIncome() {
        when(repo.sumByType(Category.INCOME)).thenReturn(1500.0);

        ResponseEntity<?> response = service.getTotalIncome();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1500.0, response.getBody());
    }

    @Test
    void getTotalIncome_shouldReturnZero_whenNoIncomeRecords() {
        when(repo.sumByType(Category.INCOME)).thenReturn(null);

        ResponseEntity<?> response = service.getTotalIncome();

        assertEquals(0.0, response.getBody());
    }

    // --- getTotalExpenses ---

    @Test
    void getTotalExpenses_shouldReturnTotalExpenses() {
        when(repo.sumByType(Category.EXPENSES)).thenReturn(300.0);

        ResponseEntity<?> response = service.getTotalExpenses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(300.0, response.getBody());
    }

    @Test
    void getTotalExpenses_shouldReturnZero_whenNoExpenseRecords() {
        when(repo.sumByType(Category.EXPENSES)).thenReturn(null);

        ResponseEntity<?> response = service.getTotalExpenses();

        assertEquals(0.0, response.getBody());
    }

    // --- getNetBalance ---

    @Test
    void getNetBalance_shouldReturnCorrectBalance() {
        when(repo.sumByType(Category.INCOME)).thenReturn(1000.0);
        when(repo.sumByType(Category.EXPENSES)).thenReturn(400.0);

        ResponseEntity<?> response = service.getNetBalance();

        assertEquals(600.0, response.getBody());
    }

    @Test
    void getNetBalance_shouldHandleNullValues() {
        when(repo.sumByType(Category.INCOME)).thenReturn(null);
        when(repo.sumByType(Category.EXPENSES)).thenReturn(null);

        ResponseEntity<?> response = service.getNetBalance();

        assertEquals(0.0, response.getBody());
    }

    @Test
    void getNetBalance_shouldReturnNegative_whenExpensesExceedIncome() {
        when(repo.sumByType(Category.INCOME)).thenReturn(200.0);
        when(repo.sumByType(Category.EXPENSES)).thenReturn(500.0);

        ResponseEntity<?> response = service.getNetBalance();

        assertEquals(-300.0, response.getBody());
    }

    // --- saveRecord ---

    @Test
    void saveRecord_shouldReturnCreated_whenValidRecord() {
        ResponseEntity<?> response = service.saveRecord(sampleRecord);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(repo, times(1)).save(sampleRecord);
    }

    @Test
    void saveRecord_shouldSetDateAndTime() {
        sampleRecord.setDateAndTime(null);

        service.saveRecord(sampleRecord);

        assertNotNull(sampleRecord.getDateAndTime()); // should be auto-set
    }

    @Test
    void saveRecord_shouldThrow_whenAmountIsNull() {
        sampleRecord.setAmount(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveRecord(sampleRecord));
    }

    @Test
    void saveRecord_shouldThrow_whenAmountIsZero() {
        sampleRecord.setAmount(0.0);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveRecord(sampleRecord));
    }

    @Test
    void saveRecord_shouldThrow_whenAmountIsNegative() {
        sampleRecord.setAmount(-100.0);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveRecord(sampleRecord));
    }

    @Test
    void saveRecord_shouldThrow_whenCategoryIsNull() {
        sampleRecord.setCategory(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveRecord(sampleRecord));
    }

    // --- updateRecord ---

    @Test
    void updateRecord_shouldUpdateAndReturn_whenRecordExists() {
        recordEntity updatedData = new recordEntity();
        updatedData.setAmount(999.0);
        updatedData.setCategory(Category.EXPENSES);
        updatedData.setDescription("Updated desc");

        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<?> response = service.updateRecord(sampleId, updatedData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(repo, times(1)).save(sampleRecord);
    }

    @Test
    void updateRecord_shouldThrow_whenRecordNotFound() {
        when(repo.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(noSuchEntityExists.class,
                () -> service.updateRecord(sampleId, sampleRecord));
    }

    @Test
    void updateRecord_shouldKeepOldValues_whenNewValuesAreNull() {
        recordEntity updatedData = new recordEntity();
        updatedData.setAmount(null);
        updatedData.setCategory(null);
        updatedData.setDescription(null);

        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleRecord));

        service.updateRecord(sampleId, updatedData);

        // original values should be preserved
        assertEquals(500.0, sampleRecord.getAmount());
        assertEquals(Category.INCOME, sampleRecord.getCategory());
    }

    // --- deleteById ---

    @Test
    void deleteById_shouldDelete_whenRecordExists() {
        when(repo.findById(sampleId)).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<?> response = service.deleteById(sampleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record Successfully deleted", response.getBody());
        verify(repo, times(1)).deleteById(sampleId);
    }

    @Test
    void deleteById_shouldThrow_whenRecordNotFound() {
        when(repo.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(noSuchEntityExists.class,
                () -> service.deleteById(sampleId));
    }

    // --- getByCategory ---

    @Test
    void getByCategory_shouldReturnRecords() {
        when(repo.findByCategory(Category.INCOME)).thenReturn(List.of(sampleRecord));

        ResponseEntity<?> response = service.getByCategory(Category.INCOME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // --- getByDescription ---

    @Test
    void getByDescription_shouldReturnMatchingRecords() {
        when(repo.findByDescription("Salary")).thenReturn(List.of(sampleRecord));

        ResponseEntity<?> response = service.getByDescription("Salary");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}