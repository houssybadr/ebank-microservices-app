package dev.yosa.ebank.customerservice;

import dev.yosa.ebank.customerservice.entities.Customer;
import dev.yosa.ebank.customerservice.services.CustomerService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerService customerService) {
        return args -> {
            List<String> names = List.of("Mohammed", "Iman", "Yassine");
            names.forEach(name -> customerService.saveCustomer(
                    Customer.builder().name(name).email(name + "@gmail.com").build()));
        };
    }
}
