package dev.yosa.ebank.ebankservice;

import dev.yosa.ebank.ebankservice.entities.BankAccount;
import dev.yosa.ebank.ebankservice.services.EbankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(EbankService ebankService) {
        return args -> {
            for (long i = 1; i <= 3; i++) {
                for (int j = 1; j <= 5; j++) {
                    ebankService.saveBankAccount(BankAccount.builder()
                            .type(Math.random() > 0.5 ? "current account" : "saving account")
                            .balance(1000 + Math.random() * 60000)
                            .customerId(i)
                            .build());
                }
            }
        };
    }
}
