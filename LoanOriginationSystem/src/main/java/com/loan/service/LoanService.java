package com.loan.service;

import com.loan.dto.LoanRequestDto;
import com.loan.model.request.Agent;
import com.loan.model.request.Loan;
import com.loan.model.request.LoanStatus;
import com.loan.repository.AgentRepository;
import com.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoanService {

    LoanRepository loanRepository;

    AgentRepository agentRepository;
    private final NotificationService notificationService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public LoanService(LoanRepository loanRepository, AgentRepository agentRepository, NotificationService notificationService) {
        this.loanRepository= loanRepository;
        this.agentRepository = agentRepository;
        this.notificationService = notificationService;

        // Start background processing loop
        startBackgroundProcessing();
    }

    public Loan submitLoan(LoanRequestDto dto) {
        Loan loan = Loan.builder()
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .loanAmount(dto.getLoanAmount())
                .loanType(dto.getLoanType())
                .applicationStatus(LoanStatus.APPLIED)
                .createdAt(LocalDateTime.now())
                .build();
        return loanRepository.save(loan);
    }

    public Agent createAgent(Agent agent) {
        // Validate agent details if necessary
        if (agent.getManager() != null && !agent.getManager().getId().equals(agent.getId())) {
            agent.setManager(agentRepository.findById(agent.getManager().getId())
                    .orElseThrow(() -> new NoSuchElementException("Manager not found")));
        }
        return agentRepository.save(agent);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));
    }
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agent not found"));
    }
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Page<Loan> getLoansByStatus(LoanStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return loanRepository.findByApplicationStatus(status, pageable);
    }

    public Map<LoanStatus, Long> getStatusCounts() {
        Map<LoanStatus, Long> counts = new EnumMap<>(LoanStatus.class);
        for (LoanStatus status : LoanStatus.values()) {
            counts.put(status, loanRepository.countByApplicationStatus(status));
        }
        return counts;
    }

    public List<Map<String, Object>> getTopCustomers() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> result = loanRepository.findTopCustomers(pageable);
        List<Map<String, Object>> topCustomers = new ArrayList<>();
        for (Object[] row : result) {
            topCustomers.add(Map.of("customerName", row[0], "approvedLoanCount", row[1]));
        }
        return topCustomers;
    }

    @Transactional
    public void agentDecision(Long agentId, Long loanId, String decision) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NoSuchElementException("Agent not found"));

        System.out.println("Loan "+loan.toString());
        System.out.println("Agent "+agent.toString());
        if (loan.getApplicationStatus() != LoanStatus.UNDER_REVIEW || !agent.equals(loan.getAssignedAgent())) {
            throw new IllegalStateException("Loan not under review or not assigned to agent");
        }

        if ("APPROVE".equalsIgnoreCase(decision)) {
            loan.setApplicationStatus(LoanStatus.APPROVED_BY_AGENT);
            notificationService.notifyCustomerApproval(loan.getCustomerPhone(), loan.getId());
        } else if ("REJECT".equalsIgnoreCase(decision)) {
            loan.setApplicationStatus(LoanStatus.REJECTED_BY_AGENT);
        } else {
            throw new IllegalArgumentException("Invalid decision");
        }
        loanRepository.save(loan);
    }

    // --- Background Processing Logic ---

    private void startBackgroundProcessing() {
        Runnable backgroundTask = () -> {
            while (true) {
                List<Loan> appliedLoans = loanRepository.findByApplicationStatus(LoanStatus.APPLIED);
                for (Loan loan : appliedLoans) {
                    executorService.submit(() -> processLoan(loan));
                }
                try {
                    Thread.sleep(5000); // Check every 5 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread backgroundThread = new Thread(backgroundTask, "LoanProcessorBackground");
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void processLoan(Loan loan) {
        try {
            // Simulate system check delay
            int delay = 20 + new Random().nextInt(10); // 20-30 seconds
            Thread.sleep(delay * 1000L);

            // Simple rules: if amount < 5000 -> system approve, else if >100000 -> reject, else under review
            if(loan.getLoanAmount().compareTo(BigDecimal.valueOf(5000)) < 0) {
                loan.setApplicationStatus(LoanStatus.APPROVED_BY_SYSTEM);
                loanRepository.save(loan);
                notificationService.notifyCustomerApproval(loan.getCustomerPhone(), loan.getId());
            } else if (loan.getLoanAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
                loan.setApplicationStatus(LoanStatus.REJECTED_BY_SYSTEM);
                loanRepository.save(loan);
            } else {
                loan.setApplicationStatus(LoanStatus.UNDER_REVIEW);
                // Assign to random agent
                List<Agent> agents = agentRepository.findAll();
                if (!agents.isEmpty()) {
                    Agent assigned = agents.get(new Random().nextInt(agents.size()));
                    loan.setAssignedAgent(assigned);
                    loanRepository.save(loan);
                    notificationService.notifyAgentAssignment(assigned.getId(), loan.getId());
                    if (assigned.getManager() != null) {
                        notificationService.notifyManagerAssignment(assigned.getManager().getId(), loan.getId());
                    }
                }
            }
        } catch (Exception e) {
            // Log and continue
            e.printStackTrace();
        }
    }
}
