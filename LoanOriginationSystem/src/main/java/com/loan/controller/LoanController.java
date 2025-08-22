package com.loan.controller;



import com.loan.dto.LoanRequestDto;
import com.loan.model.request.Agent;
import com.loan.model.request.Loan;
import com.loan.model.request.LoanStatus;
import com.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
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

    @GetMapping
    public Page<Loan> getLoansByStatus(@RequestParam("status") LoanStatus status,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size) {
        return loanService.getLoansByStatus(status, page, size);
    }
}
