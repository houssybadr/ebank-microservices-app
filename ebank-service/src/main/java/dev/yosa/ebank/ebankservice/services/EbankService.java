package dev.yosa.ebank.ebankservice.services;

import dev.yosa.ebank.ebankservice.entities.BankAccount;
import dev.yosa.ebank.ebankservice.model.Customer;
import dev.yosa.ebank.ebankservice.repositories.BankAccountRepository;
import dev.yosa.ebank.ebankservice.rest.CustomerRestClient;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class EbankService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;

    public EbankService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    @Tool(description = "Get the list of all bank accounts")
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Tool(description = "Find a bank account by its id")
    public BankAccount getBankAccountById(@ToolParam(description = "the bank account id") String id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Customer customer = customerRestClient.getCustomerById(account.getCustomerId());
        account.setCustomer(customer);
        return account;
    }

    @Tool(description = "Save a new bank account (balance, type and customerId)")
    public BankAccount saveBankAccount(@ToolParam(description = "the bank account to save") BankAccount bankAccount) {
        customerRestClient.getCustomerById(bankAccount.getCustomerId());
        if (bankAccount.getId() == null || bankAccount.getId().isEmpty()) {
            bankAccount.setId(UUID.randomUUID().toString());
        }
        if (bankAccount.getCreatedAt() == null) {
            bankAccount.setCreatedAt(new Date());
        }
        return bankAccountRepository.save(bankAccount);
    }
}
