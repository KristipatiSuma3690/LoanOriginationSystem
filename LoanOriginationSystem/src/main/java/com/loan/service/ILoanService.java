package com.loan.service;

import com.loan.dto.LoanRequestDto;
import com.loan.model.Agent;
import com.loan.model.Loan;
import com.loan.model.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ILoanService {
    Loan submitLoan(LoanRequestDto dto);
    Agent createAgent(Agent agent);
    Loan getLoanById(Long id);
    Agent getAgentById(Long id);
    List<Loan> getAllLoans();
    List<Agent> getAllAgents();
    Page<Loan> getLoansByStatus(LoanStatus status, int page, int size);
    Map<LoanStatus, Long> getStatusCounts();
    List<Map<String, Object>> getTopCustomers();
    Page<Loan> getLoansByStatusByCreatedAt(LoanStatus status, Pageable pageable);
    Page<Loan> getAllLoansByCreatedAt(Pageable pageable);
    Page<Loan> getLoansByStatusWithPagination(String status, int page, int size);
    void agentDecision(Long agentId, Long loanId, String decision);

}
