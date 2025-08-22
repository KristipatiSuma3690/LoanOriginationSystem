package com.loan.repository;

import com.loan.model.request.Loan;
import com.loan.model.request.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByApplicationStatus(LoanStatus status);
    Page<Loan> findByApplicationStatus(LoanStatus status, Pageable pageable);

    @Query("SELECT l.customerName, COUNT(l) as cnt FROM Loan l WHERE l.applicationStatus IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT') GROUP BY l.customerName ORDER BY cnt DESC")
    List<Object[]> findTopCustomers(Pageable pageable);

    long countByApplicationStatus(LoanStatus status);
}
