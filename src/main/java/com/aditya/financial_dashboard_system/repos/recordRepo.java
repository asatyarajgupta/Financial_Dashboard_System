package com.aditya.financial_dashboard_system.repos;

import com.aditya.financial_dashboard_system.entities.recordEntity;
import com.aditya.financial_dashboard_system.utils.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface recordRepo extends JpaRepository<recordEntity, UUID> {
    List<recordEntity> findByCategory(Category category);
    List<recordEntity> findByDescription(String description);

    @Query("SELECT SUM(r.amount) FROM recordEntity r WHERE r.category = :cat")
    Double sumByType(@Param("cat") Category cat);
    @Query("SELECT r.category, SUM(r.amount) FROM recordEntity r GROUP BY r.category")
    List<Object[]> sumGroupedByCategory();

    List<recordEntity> findTop10ByOrderByDateAndTimeDesc();

    @Query("SELECT MONTH(r.dateAndTime), YEAR(r.dateAndTime), SUM(r.amount) FROM recordEntity r GROUP BY YEAR(r.dateAndTime), MONTH(r.dateAndTime) ORDER BY YEAR(r.dateAndTime), MONTH(r.dateAndTime)")
    List<Object[]> getMonthlyTotals();
}
