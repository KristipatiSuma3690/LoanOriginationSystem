package com.loan.service;

import com.loan.constants.LoanConstants;
import com.loan.dto.LoanRequestDto;
import com.loan.exception.AgentNotFoundException;
import com.loan.exception.LoanNotFoundException;
import com.loan.model.Agent;
import com.loan.model.Loan;
import com.loan.model.LoanStatus;
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
public class LoanServiceImpl implements ILoanService{

    LoanRepository loanRepository;

    AgentRepository agentRepository;
    private final INotificationService notificationService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, AgentRepository agentRepository, INotificationService notificationService) {
        this.loanRepository= loanRepository;
        this.agentRepository = agentRepository;
        this.notificationService = notificationService;


        startBackgroundProcessing();
    }

    @Override
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
    @Override
    public Agent createAgent(Agent agent) {
        // Validate agent details if necessary
        if (agent.getManager() != null && !agent.getManager().getId().equals(agent.getId())) {
            agent.setManager(agentRepository.findById(agent.getManager().getId())
                    .orElseThrow(() -> new NoSuchElementException("Manager not found")));
        }
        return agentRepository.save(agent);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(LoanConstants.LOAN_NOT_FOUND + "for Id: "+id));
    }
    @Override
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new AgentNotFoundException(LoanConstants.AGENT_NOT_FOUND + "for Id: "+id));
    }
    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public Page<Loan> getLoansByStatus(LoanStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return loanRepository.findByApplicationStatus(status, pageable);
    }
    @Override
    public Map<LoanStatus, Long> getStatusCounts() {
        Map<LoanStatus, Long> counts = new EnumMap<>(LoanStatus.class);
        for (LoanStatus status : LoanStatus.values()) {
            counts.put(status, loanRepository.countByApplicationStatus(status));
        }
        return counts;
    }
    @Override
    public List<Map<String, Object>> getTopCustomers() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> result = loanRepository.findTopCustomers(pageable);
        List<Map<String, Object>> topCustomers = new ArrayList<>();
        for (Object[] row : result) {
            topCustomers.add(Map.of("customerName", row[0], "approvedLoanCount", row[1]));
        }
        return topCustomers;
    }
    @Override
    public Page<Loan> getLoansByStatusByCreatedAt(LoanStatus status, Pageable pageable) {
        return loanRepository.findByApplicationStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Override
    public Page<Loan> getAllLoansByCreatedAt(Pageable pageable) {
        return loanRepository.findAllOrderByCreatedAtDesc(pageable);
    }
    @Override
    public Page<Loan> getLoansByStatusWithPagination(String status, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Loan> loans;

        if (status != null && !status.trim().isEmpty()) {
            try {
                LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
                loans = getLoansByStatusByCreatedAt(loanStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid loan status: " + status +
                        ". Valid values are: " + java.util.Arrays.toString(LoanStatus.values()));
            }
        } else {
            // If no status provided, return all loans
            loans = getAllLoansByCreatedAt(pageable);
        }
        return loans;
    }


    @Transactional
    @Override
    public void agentDecision(Long agentId, Long loanId, String decision) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(LoanConstants.LOAN_NOT_FOUND + "for Id: "+loanId));
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AgentNotFoundException(LoanConstants.LOAN_NOT_FOUND + "for Id: "+agentId));


        if (loan.getApplicationStatus() != LoanStatus.UNDER_REVIEW || !agent.equals(loan.getAssignedAgent())) {
            throw new IllegalStateException("Loan not under review or not assigned to agent");
        }

        if (LoanConstants.APPROVE.equalsIgnoreCase(decision)) {
            loan.setApplicationStatus(LoanStatus.APPROVED_BY_AGENT);
            notificationService.notifyCustomerApproval(loan.getCustomerPhone(), loan.getId());
        } else if (LoanConstants.REJECT.equalsIgnoreCase(decision)) {
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

            int delay = 25; // 25 sec delay
            Thread.sleep(delay * 1000L);

            // if amount < 5000 -> system approve, else if >10,00,00,000 -> reject, else under review
            if(loan.getLoanAmount().compareTo(BigDecimal.valueOf(5000)) < 0) {
                loan.setApplicationStatus(LoanStatus.APPROVED_BY_SYSTEM);
                loanRepository.save(loan);
                notificationService.notifyCustomerApproval(loan.getCustomerPhone(), loan.getId());
            } else if (loan.getLoanAmount().compareTo(BigDecimal.valueOf(100000000)) > 0) {
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
            e.printStackTrace();
        }
    }


}
