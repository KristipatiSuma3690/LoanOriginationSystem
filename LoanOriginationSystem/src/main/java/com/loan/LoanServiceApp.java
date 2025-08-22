package com.loan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.loan.repository")
@EntityScan(basePackages = "com.loan")
public class LoanServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApp.class, args);

    }

}
