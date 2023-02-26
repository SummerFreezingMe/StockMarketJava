package com.example.stockmarketjava.service;

import com.example.stockmarketjava.domain.ExchangeRate;
import com.example.stockmarketjava.domain.Operation;
import com.example.stockmarketjava.domain.User;
import com.example.stockmarketjava.repos.ExchangeRateRepository;
import com.example.stockmarketjava.repos.OperationRepository;
import com.example.stockmarketjava.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockAdminService {

    private final UsersRepository userRepository;
    private final ExchangeRate exchange_rate;
    private final OperationRepository operationRepository;

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public StockAdminService(UsersRepository userRepository, ExchangeRateRepository exchangeRateRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        exchange_rate = exchangeRateRepository.findAll().iterator().next();
        this.exchangeRateRepository = exchangeRateRepository;
        this.operationRepository = operationRepository;
    }
    public Map<String, String> redactExchangeRate(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        switch (userdata.get("base_currency")) {
            case ("TON") -> {
                exchange_rate.setTON(1f);
                exchange_rate.setBTC(Float.valueOf(userdata.get("BTC")));
                exchange_rate.setRUB(Float.valueOf(userdata.get("RUB")));
            }
            case ("RUB") -> {
                exchange_rate.setRUB(1f);
                exchange_rate.setBTC(Float.valueOf(userdata.get("BTC")));
                exchange_rate.setTON(Float.valueOf(userdata.get("TON")));
            }
            case ("BTC") -> {
                exchange_rate.setBTC(1f);
                exchange_rate.setTON(Float.valueOf(userdata.get("TON")));
                exchange_rate.setRUB(Float.valueOf(userdata.get("RUB")));
            }
        }
        exchangeRateRepository.save(exchange_rate);
        return model;
    }

    public Map<String, String> displaySumOfCurrency(Map<String, String> userdata) {

        Map<String, String> model = new HashMap<>();
        Iterable<User> allUsers = userRepository.findAll();
        Float chosenCurrencySum = 0.0f;
        String chosenCurrency = userdata.get("currency");
        switch (chosenCurrency) {
            case ("TON") -> {
                for (User user :
                        allUsers) {
                    chosenCurrencySum +=user.getTON_value();
                }
            }
            case ("RUB") -> {
                for (User user :
                        allUsers) {
                    chosenCurrencySum +=user.getRUB_value();
                }
            }
            case ("BTC") -> {
                for (User user :
                        allUsers) {
                    chosenCurrencySum += user.getBTC_value();
                }
            }
        }
        model.put(chosenCurrency, String.valueOf(chosenCurrencySum));
        return model;
    }

    public Map<String, Integer> displayTransactionCount(Map<String, String> userdata) {
        Map<String, Integer> model = new HashMap<>();
        LocalDate dateFrom = LocalDate.parse(userdata.get("date_from"),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate dateTo = LocalDate.parse(userdata.get("date_to"),DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        List<Operation> listOfOperations= operationRepository.findByOperationDateBetween(dateFrom,dateTo);
        model.put("operation_count",listOfOperations.size());
        return model;
    }
}
