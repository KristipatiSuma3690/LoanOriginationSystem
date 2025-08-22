//package com.loan.controller;
//
//
//import com.loan.businessDao.LoanServiceDao;
//import com.loan.exception.LoanNotFoundException;
//import com.loan.model.request.Loan;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//
//
//@RestController
//@RequestMapping("/api/v1/loans")
//public class LoanOSServiceController {
//
//    @Autowired
//    private LoanServiceDao loanServiceDAO;
//
//    @GetMapping("/")
//    public ResponseEntity<List<Loan>> getAllEvents() {
//        System.out.println("Fetching all loans");
//        List<Loan> loanList = loanServiceDAO.findAllLoans();
//        return ResponseEntity.ok(loanList);
//    }
//
//    @PostMapping("/")
//    @Transactional
//    public ResponseEntity<Loan> newLoanApplication(@RequestBody Loan loan) {
//        Loan createdLoan = loanServiceDAO.createLoan(loan);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
//    }
//    @GetMapping("/{id}")
//    public EntityModel<Optional<Loan>> getEventById(@PathVariable Long id) {
//        Optional<Loan> eventById = loanServiceDAO.findLoanById(id);
//
//        if (eventById.isEmpty()) {
//            throw new LoanNotFoundException("Event with Id " + id + " is not Found");
//        }
//        return EntityModel.of(eventById);
//    }
//
//    @GetMapping("/customers/top")
//    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
//        List<Map<String, Object>> topCustomers = loanServiceDAO.getTopCustomers();
//        return ResponseEntity.ok(topCustomers);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEvent(@Valid @PathVariable Long id) {
//        Optional<Loan> event = loanServiceDAO.findLoanById(id);
//
//        if (event.isPresent()) {
//            loanServiceDAO.deleteLoanById(event.get());
//            return ResponseEntity.noContent().build();
//        } else {
//            throw new LoanNotFoundException("Loan  With  id: " + id + " Not Found So Cannot be deleted..");
//        }
//    }
//
//
//
//
//
//
//
//
//}
