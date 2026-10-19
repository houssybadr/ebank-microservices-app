package dev.yosa.ebank.ebankservice.repositories;

import dev.yosa.ebank.ebankservice.entities.BankAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findByCustomerId(Long customerId);
}
