package com.aditya.financial_dashboard_system.services;

import com.aditya.financial_dashboard_system.entities.recordEntity;
import com.aditya.financial_dashboard_system.exceptions.noSuchEntityExists;
import com.aditya.financial_dashboard_system.utils.Category;
import com.aditya.financial_dashboard_system.repos.recordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Double.sum;

@Service
@RequiredArgsConstructor
public class recordService {
    private final recordRepo repo;

    public Page<recordEntity> getAllRecords(Pageable pageable){
        return repo.findAll(pageable);
    }

    public ResponseEntity<?> getRecordById(UUID id) {
        Optional<recordEntity> record = repo.findById(id);
        if (record.isEmpty()) {
            throw new noSuchEntityExists("No Such User Exists");
        }
        return ResponseEntity.ok(repo.findById(id));
    }

    public ResponseEntity<?> getTotalIncome() {
        Double total = repo.sumByType(Category.INCOME);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    public ResponseEntity<?> getTotalExpenses() {
        Double total = repo.sumByType(Category.EXPENSES);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    public ResponseEntity<?> getNetBalance() {
        Double income = repo.sumByType(Category.INCOME);
        Double expense = repo.sumByType(Category.EXPENSES);
        return ResponseEntity.ok((income != null ? income : 0.0) - (expense != null ? expense : 0.0));
    }

    public ResponseEntity<?> getCategoryWiseTotals() {
        return ResponseEntity.ok(repo.sumGroupedByCategory());
    }

    public ResponseEntity<?> getRecentActivity() {
        return ResponseEntity.ok(repo.findTop10ByOrderByDateAndTimeDesc());
    }

    public ResponseEntity<?> getMonthlyTrends() {
        return ResponseEntity.ok(repo.getMonthlyTotals());
    }


    public ResponseEntity<?> getByCategory(Category category) {
        return ResponseEntity.ok(repo.findByCategory(category));
    }
    public ResponseEntity<?> getByDescription(String description) {
        return ResponseEntity.ok(repo.findByDescription(description));
    }

    public ResponseEntity<?> saveRecord(recordEntity record){
        record.setDateAndTime(LocalDateTime.now());
        if (record.getAmount() == null || record.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount is Invalid");
        }
        if (record.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        repo.save(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }
    public ResponseEntity<?> updateRecord(UUID id, recordEntity record) {
        Optional<recordEntity> updatedRecord = repo.findById(id);
        if (updatedRecord.isEmpty()) {
            throw new noSuchEntityExists("No Such Record Exists");
        }
        updatedRecord.get().setAmount(record.getAmount() != null && record.getAmount() > 0  ? record.getAmount() : updatedRecord.get().getAmount());
        updatedRecord.get().setLastUpdated(LocalDateTime.now());
        updatedRecord.get().setDescription(record.getDescription() == null ? updatedRecord.get().getDescription() : record.getDescription());
        updatedRecord.get().setCategory(record.getCategory() == null ? updatedRecord.get().getCategory() : record.getCategory());
        repo.save(updatedRecord.get());
        return ResponseEntity.ok(updatedRecord.get());
    }

    public ResponseEntity<?> deleteById(UUID id) {
        Optional<recordEntity> record = repo.findById(id);
        if (record.isEmpty()) {
            throw new noSuchEntityExists("No Such Record Exists");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Record Successfully deleted");
    }
}
