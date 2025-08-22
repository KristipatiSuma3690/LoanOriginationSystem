//package com.loan.service;
//
//import com.loan.businessDao.LoanServiceDao;
//import com.loan.model.request.Loan;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class LoanProcessingService {
//    @Autowired
//    private LoanServiceDao loanServiceDao;
//
//    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // Thread pool with 5 threads
//    private final Random random = new Random();
//
//    public void processLoans() {
//        List<Loan> appliedLoans = loanServiceDao.findLoansByStatus("APPLIED");
//
//        for (Loan loan : appliedLoans) {
//            executorService.submit(() -> processLoan(loan));
//        }
//    }
//
//    private void processLoan(Loan loan) {
//        try {
//            // Simulate system checks with a random delay
//            int delay = 10 + random.nextInt(15); // Random delay between 10-25 seconds
//            TimeUnit.SECONDS.sleep(delay);
//
//            // Apply simple rules to decide loan status
//            if (loan.getLoan_amount().compareTo(new java.math.BigDecimal("100000")) < 0) {
//                loan.setApplication_status("APPROVED_BY_SYSTEM");
//            } else if (loan.getLoan_amount().compareTo(new java.math.BigDecimal("500000")) > 0) {
//                loan.setApplication_status("REJECTED_BY_SYSTEM");
//            } else {
//                loan.setApplication_status("UNDER_REVIEW");
//            }
//
//            // Update loan status in the database
//            loanServiceDao.updateLoan(loan);
//            System.out.println("Processed loan ID: " + loan.getLoan_id() + " with status: " + loan.getApplication_status());
//
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.err.println("Loan processing interrupted for loan ID: " + loan.getLoan_id());
//        }
//    }
//
//    public void shutdown() {
//        executorService.shutdown();
//        try {
//            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//        }
//    }
//}
