package com.loan.service;

import org.springframework.stereotype.Service;

@Service
public class LoggerNotificationService implements NotificationService {

    @Override
    public void notifyAgentAssignment(Long agentId, Long loanId) {
        System.out.println("Agent with ID " + agentId + " has been assigned to loan ID " + loanId);
    }

    @Override
    public void notifyManagerAssignment(Long managerId, Long loanId) {
        System.out.println("Manager with ID " + managerId + " has been assigned to loan ID " + loanId);
    }

    @Override
    public void notifyCustomerApproval(String customerPhone, Long loanId) {
        System.out.println("Customer with phone " + customerPhone + " has been approved for loan ID " + loanId);
    }
}
