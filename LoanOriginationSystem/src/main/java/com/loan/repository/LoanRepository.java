package com.loan.repository;

import com.loan.model.Loan;
import com.loan.model.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByApplicationStatus(LoanStatus status);

    Page<Loan> findByApplicationStatus(LoanStatus status, Pageable pageable);

    @Query("SELECT l FROM Loan l ORDER BY l.createdAt DESC")
    Page<Loan> findAllOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.applicationStatus = :status ORDER BY l.createdAt DESC")
    Page<Loan> findByApplicationStatusOrderByCreatedAtDesc(@Param("status") LoanStatus status, Pageable pageable);

    @Query("SELECT l.customerName, COUNT(l) as cnt FROM Loan l WHERE l.applicationStatus IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT') GROUP BY l.customerName ORDER BY cnt DESC")
    List<Object[]> findTopCustomers(Pageable pageable);

    long countByApplicationStatus(LoanStatus status);
}
