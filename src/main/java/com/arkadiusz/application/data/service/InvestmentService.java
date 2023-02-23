package com.arkadiusz.application.data.service;

import com.arkadiusz.application.data.entity.Investment;
import com.arkadiusz.application.data.repository.InvestmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    public static final Logger LOGGER = Logger.getLogger(Investment.class.getName());

    public InvestmentService(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }


    public void saveInvestment(Investment investment) {
        if (investment == null) {
            LOGGER.log(Level.SEVERE, "Inwestycja jest pusta!");
        }
        investmentRepository.save(investment);
    }

//    public Investment getInvestment() {
//        return new Investment("Test1", LocalDate.MAX, BigDecimal.TEN);
//    }

    public List<Investment> findAllInvestments() {
        return investmentRepository.findAll();
    }
}
