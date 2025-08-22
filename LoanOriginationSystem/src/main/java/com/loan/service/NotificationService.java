package com.loan.service;

public interface NotificationService {
    void notifyAgentAssignment(Long agentId, Long loanId);
    void notifyManagerAssignment(Long managerId, Long loanId);
    void notifyCustomerApproval(String customerPhone, Long loanId);
}