package com.loan.controller;



import com.loan.dto.LoanRequestDto;
import com.loan.model.Loan;
import com.loan.model.LoanStatus;
import com.loan.service.ILoanService;
import com.loan.service.LoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final ILoanService loanService;

    @Autowired
    public LoanController(ILoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public Loan submitLoan(@RequestBody LoanRequestDto dto) {
        return loanService.submitLoan(dto);
    }


    @GetMapping("/{id}")
    public Loan getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id);
    }
    @GetMapping("/")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
    @GetMapping("/status-count")
    public Map<LoanStatus, Long> getStatusCounts() {
        return loanService.getStatusCounts();
    }

    @GetMapping("/top")
    public List<Map<String, Object>> getTopCustomers() {
        return loanService.getTopCustomers();
    }

    @GetMapping("/status")
    public ResponseEntity<Page<Loan>> getLoansByStatus(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Loan> loans=loanService.getLoansByStatusWithPagination(status, page,size);
        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }
}
