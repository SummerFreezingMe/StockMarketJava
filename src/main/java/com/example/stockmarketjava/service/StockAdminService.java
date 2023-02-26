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
        if (isAdmin(userdata.get("secret_key"))) {
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
            userdata.remove("secret_key");
            userdata.remove("base_currency");
            model.putAll(userdata);
        } else model.put("error", "Ошибка! Нет прав для выполнения данной команды");

        return model;
    }

    public Map<String, String> displaySumOfCurrency(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        if (isAdmin(userdata.get("secret_key"))) {

            Iterable<User> allUsers = userRepository.findAll();
            Float chosenCurrencySum = 0.0f;
            String chosenCurrency = userdata.get("currency");
            switch (chosenCurrency) {
                case ("TON") -> {
                    for (User user :
                            allUsers) {
                        chosenCurrencySum += user.getTON_value();
                    }
                }
                case ("RUB") -> {
                    for (User user :
                            allUsers) {
                        chosenCurrencySum += user.getRUB_value();
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
        } else model.put("error", "Ошибка! Нет прав для выполнения данной команды");
        return model;
    }

    public Map<String, String> displayTransactionCount(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        if (isAdmin(userdata.get("secret_key"))) {
            LocalDate dateFrom = LocalDate.parse(userdata.get("date_from"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDate dateTo = LocalDate.parse(userdata.get("date_to"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            List<Operation> listOfOperations = operationRepository.findByOperationDateBetween(dateFrom, dateTo);
            model.put("operation_count", String.valueOf(listOfOperations.size()));
        } else model.put("error", "Ошибка! Нет прав для выполнения данной команды");
        return model;
    }

    public boolean isAdmin(String userKey) {
        return userRepository.findUserByRoleIs("ADMIN").contains(userRepository.findBySecretKey(userKey));
    }
}