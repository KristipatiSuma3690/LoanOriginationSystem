//package com.loan.businessDao;
//
//import com.loan.model.request.Loan;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Repository
//public class LoanServiceDao {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public List<Loan> findAllLoans() {
//        System.out.println("Fetching all loans");
//        return entityManager.createQuery("FROM Loan ", Loan.class).getResultList();
//    }
//
//    public Optional<Loan> findLoanById(Long id) {
//        return Optional.ofNullable(entityManager.find(Loan.class, id));
//    }
//
//    public Loan createLoan(Loan loan) {
//        entityManager.persist(loan);
//        return loan;
//    }
//
//    public Loan updateLoan(Loan loan) {
//        return entityManager.merge(loan);
//    }
//
//    public void deleteLoanById(Loan loan) {
//        entityManager.remove(loan);
//    }
//
//    public List<Loan> findLoansByStatus(String status) {
//        return entityManager.createQuery("FROM Loan WHERE application_status = :status", Loan.class)
//                .setParameter("status", status)
//                .getResultList();
//    }
//
//    public List<Map> getTopCustomers() {
//        String query = "SELECT l.customerName AS customerName, COUNT(l.id) AS loanCount " +
//                "FROM Loan l " +
//                "WHERE l.application_status IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT') " +
//                "GROUP BY l.customerName " +
//                "ORDER BY loanCount DESC";
//        return entityManager.createQuery(query, Map.class)
//                .setMaxResults(3)
//                .getResultList();
//    }
//
//    @Query("SELECT l.customerName, COUNT(l) as cnt FROM Loan l WHERE l.applicationStatus IN ('APPROVED_BY_SYSTEM', 'APPROVED_BY_AGENT') GROUP BY l.customerName ORDER BY cnt DESC")
//    List<Object[]> findTopCustomers(Pageable pageable) ;
//
//
//}
