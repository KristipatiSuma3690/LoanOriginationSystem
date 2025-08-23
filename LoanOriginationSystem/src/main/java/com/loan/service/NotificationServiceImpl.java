package com.loan.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements INotificationService {

    private static final org.slf4j.Logger LOG=LoggerFactory.getLogger(NotificationServiceImpl.class);
    @Override
    public void notifyAgentAssignment(Long agentId, Long loanId) {
        LOG.info("Agent with ID {} has been assigned to loan ID {}",agentId, loanId);
    }

    @Override
    public void notifyManagerAssignment(Long managerId, Long loanId) {
        LOG.info("Manager with ID {}  has been assigned to loan ID {}",managerId, loanId);
    }

    @Override
    public void notifyCustomerApproval(String customerPhone, Long loanId) {
        LOG.info("Customer with phone {} has been approved for loan ID {}",customerPhone, loanId);
    }
}
