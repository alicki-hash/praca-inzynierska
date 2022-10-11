package com.example.application.data.generator;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
                                      WorkScopeRepository workScopeRepository, ExpenseRepository expenseRepository,
                                      InvestmentRepository investmentRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");
            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class,
                    LocalDateTime.now());
            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
            List<Company> companies = companyRepository.saveAll(companyGenerator.create(5, seed));

            List<WorkScope> workScopes = workScopeRepository
                    .saveAll(Stream.of("Hydraulika", "Wykończenia", "Projekt", "Elektryka", "Inne", "Posadzki", "Tynki",
                                    "Dach", "Elewacja", "Fundamenty")
                            .map(WorkScope::new).collect(Collectors.toList()));

            logger.info("... generating 50 Contact entities...");
            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
                    LocalDateTime.now());
            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
            contactGenerator.setData(Contact::setPhone, DataType.PHONE_NUMBER);

            Random r = new Random(seed);
            List<Contact> contacts = contactGenerator.create(50, seed).stream().peek(contact -> {
                contact.setCompany(companies.get(r.nextInt(companies.size())));
                contact.setWorkScope(workScopes.get(r.nextInt(workScopes.size())));
            }).collect(Collectors.toList());

            contactRepository.saveAll(contacts);


            logger.info("... generating 50 Expense entities...");

            ExampleDataGenerator<Expense> expenseGenerator = new ExampleDataGenerator<>(Expense.class,
                    LocalDateTime.now());
            expenseGenerator.setData(Expense::setTitle, DataType.BOOK_TITLE);
            expenseGenerator.setData(Expense::setDescriptionOfExpense, DataType.SENTENCE);
            expenseGenerator.setData(Expense::setDateOfExpense, DataType.DATE_LAST_1_YEAR);
            expenseGenerator.setData(Expense::setAmount, DataType.PRICE);

            List<Expense> expenses = expenseGenerator.create(50, seed).stream().peek(expense -> {
                expense.setWorkScope(workScopes.get(r.nextInt(workScopes.size())));
                expense.setStatus(Status.getRandom());
            }).collect(Collectors.toList());

            expenseRepository.saveAll(expenses);

            Investment myInvestment = new Investment("Dom na Sezamkowej",
            LocalDate.now().minusMonths(9),
            BigDecimal.valueOf(500000));

            investmentRepository.save(myInvestment);

            logger.info("Generated demo data");
        };
    }

}
