package com.aditya.financial_dashboard_system.controllers;
import com.aditya.financial_dashboard_system.entities.recordEntity;
import com.aditya.financial_dashboard_system.utils.Category;
import com.aditya.financial_dashboard_system.services.recordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class recordController {
    private final recordService service;

    @GetMapping("/dashboard/income")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<?> getTotalIncome() { return service.getTotalIncome(); }

    @GetMapping("/dashboard/expenses")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<?> getTotalExpenses() { return service.getTotalExpenses(); }

    @GetMapping("/dashboard/balance")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<?> getNetBalance() { return service.getNetBalance(); }

    @GetMapping("/dashboard/recent")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<?> getRecentActivity() { return service.getRecentActivity(); }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "id") String sortBy,
                                    @RequestParam(defaultValue = "true") boolean ascending)
         {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            return ResponseEntity.ok(service.getAllRecords(pageable));
    }

    @GetMapping("/insights/category")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> getCategoryTotals() { return service.getCategoryWiseTotals(); }

    @GetMapping("/insights/trends")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> getMonthlyTrends() { return service.getMonthlyTrends(); }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return service.getRecordById(id);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<?> getByCategory(@PathVariable Category category) {
        return service.getByCategory(category);
    }

    // FEATURE : search feature that using q? for description

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> postRecords(@RequestBody recordEntity record){

        return service.saveRecord(record);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> putRecords(@RequestBody recordEntity record, @PathVariable UUID id){
        return service.updateRecord(id, record);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteRecord(@PathVariable UUID id){
        return service.deleteById(id);
    }




}
