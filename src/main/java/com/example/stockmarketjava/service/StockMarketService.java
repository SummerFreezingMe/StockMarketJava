package com.example.stockmarketjava.service;

import com.example.stockmarketjava.domain.ExchangeRate;
import com.example.stockmarketjava.domain.Role;
import com.example.stockmarketjava.domain.User;
import com.example.stockmarketjava.repos.ExchangeRateRepository;
import com.example.stockmarketjava.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class StockMarketService {
    private final UsersRepository userRepository;
    private final ExchangeRate exchange_rate;

    @Autowired
    public StockMarketService(UsersRepository userRepository, ExchangeRateRepository exchangeRateRepository) {
        this.userRepository = userRepository;
        exchange_rate = exchangeRateRepository.findAll().iterator().next();
    }

    public String generateSecretKey() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 30;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Object userAddition(Map<String, String> userdata) {
        if (!userRepository.existsUserByUsername(userdata.get("username")) &&
                !userRepository.existsUserByEmail(userdata.get("email"))) {
            Map<String, String> model = new HashMap<>();
            String secretKey = generateSecretKey();
            model.put("secret_key", secretKey);
            userRepository.save(new User(userdata.get("username"), userdata.get("email"), secretKey, Role.USER, 0f, 0f, 0f));
            return model;
        } else
            return "Ошибка! Пользователем с таким именем или почтой уже существует!";
    }

    public Map<String, String> depositCurrency(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(userdata.get("secret_key"));
        switch (userdata.keySet().toArray()[1].toString()) {
            case ("TON_wallet") -> {
                currentUser.setTON_value(Float.valueOf(currentUser.getTON_value() + userdata.get("TON_wallet")));
                model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
            }
            case ("RUB_wallet") -> {

                currentUser.setRUB_value(currentUser.getRUB_value() + Float.parseFloat(userdata.get("RUB_wallet")));
                model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
            }
            case ("BTC_wallet") -> {

                currentUser.setBTC_value(Float.valueOf(currentUser.getBTC_value() + userdata.get("BTC_wallet")));
                model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
            }

        }
        userRepository.save(currentUser);
        return model;
    }

    public Map<String, String> withdrawCurrency(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(userdata.get("secret_key"));
        switch (userdata.get("currency")) {
            case "RUB" -> {
                if (Float.parseFloat(userdata.get("count")) < currentUser.getRUB_value()) {
                    currentUser.setRUB_value(currentUser.getRUB_value() - Float.parseFloat(userdata.get("count")));
                    model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }
            case "BTC" -> {
                if (Float.parseFloat(userdata.get("count")) < currentUser.getBTC_value()) {
                    currentUser.setBTC_value(currentUser.getBTC_value() - Float.parseFloat(userdata.get("count")));
                    model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }
            case "TON" -> {
                if (Float.parseFloat(userdata.get("count")) < currentUser.getTON_value()) {
                    currentUser.setTON_value(currentUser.getTON_value() - Float.parseFloat(userdata.get("count")));
                    model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }

        }
        userRepository.save(currentUser);
        return model;
    }

    public Map<String, String> displayExchangeRate(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        if (userRepository.existsUserBySecretKey(userdata.get("secret_key"))) {

            switch (userdata.get("currency")) {
                case "RUB" -> {
                    model.put("BTC", String.valueOf(exchange_rate.getBTC() / exchange_rate.getRUB()));
                    model.put("TON", String.valueOf(exchange_rate.getTON() / exchange_rate.getRUB()));
                }
                case "BTC" -> {
                    model.put("TON", String.valueOf(exchange_rate.getTON() / exchange_rate.getBTC()));
                    model.put("RUB", String.valueOf(exchange_rate.getRUB() / exchange_rate.getBTC()));

                }
                case "TON" -> {
                    model.put("BTC", String.valueOf(exchange_rate.getBTC() / exchange_rate.getTON()));
                    model.put("RUB", String.valueOf(exchange_rate.getRUB() / exchange_rate.getTON()));
                }
            }
        }
        return model;
    }

    public Map<String, String> exchangeCurrency(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        model.put("secret_key", userdata.get("secret_key"));

        User currentUser = userRepository.findBySecretKey(userdata.get("secret_key"));
        switch (userdata.get("currency_from")) {
            case "RUB" -> {
                if (Float.parseFloat(userdata.get("amount")) < currentUser.getRUB_value()) {
                    currentUser.setRUB_value(currentUser.getRUB_value() - Float.parseFloat((userdata.get("amount"))));
                    model.put("RUB", String.valueOf(currentUser.getRUB_value()));

                    if (userdata.get("currency_to").equals("BTC")) {
                        float fromRUBtoBTC = exchange_rate.getBTC() / exchange_rate.getRUB();

                        currentUser.setBTC_value(currentUser.getBTC_value() + Float.parseFloat((userdata.get("amount"))) * fromRUBtoBTC);
                        model.put("BTC", String.valueOf(currentUser.getBTC_value()));

                    } else if (userdata.get("currency_to").equals("TON")) {
                        float fromRUBtoTON = exchange_rate.getTON() / exchange_rate.getRUB();
                        currentUser.setTON_value(currentUser.getTON_value() + Float.parseFloat((userdata.get("amount"))) * fromRUBtoTON);
                        model.put("TON", String.valueOf(currentUser.getTON_value()));
                    }
                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }
            case "BTC" -> {
                if (Float.parseFloat(userdata.get("amount")) < currentUser.getBTC_value()) {
                    currentUser.setBTC_value(currentUser.getBTC_value() - Float.parseFloat((userdata.get("amount"))));
                    model.put("BTC", String.valueOf(currentUser.getBTC_value()));
                    if (userdata.get("currency_to").equals("RUB")) {
                        float fromBTCtoRUB = exchange_rate.getRUB() / exchange_rate.getBTC();
                        currentUser.setRUB_value(currentUser.getRUB_value() + Float.parseFloat((userdata.get("amount"))) * fromBTCtoRUB);
                        model.put("RUB", String.valueOf(currentUser.getRUB_value()));
                    } else if (userdata.get("currency_to").equals("TON")) {
                        float fromBTCtoTON = exchange_rate.getTON() / exchange_rate.getBTC();
                        currentUser.setTON_value(currentUser.getTON_value() + Float.parseFloat((userdata.get("amount"))) * fromBTCtoTON);
                        model.put("TON", String.valueOf(currentUser.getTON_value()));
                    }
                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }
            case "TON" -> {
                if (Float.parseFloat(userdata.get("amount")) < currentUser.getRUB_value()) {
                    currentUser.setTON_value(currentUser.getTON_value() - Float.parseFloat((userdata.get("amount"))));
                    model.put("TON", String.valueOf(currentUser.getTON_value()));
                    if (userdata.get("currency_to").equals("BTC")) {
                        float fromTONtoBTC = exchange_rate.getBTC() / exchange_rate.getTON();
                        currentUser.setBTC_value(currentUser.getBTC_value() + Float.parseFloat((userdata.get("amount"))) * fromTONtoBTC);
                        model.put("BTC", String.valueOf(currentUser.getBTC_value()));
                    } else if (userdata.get("currency_to").equals("RUB")) {
                        float fromTONtoRUB = exchange_rate.getRUB() / exchange_rate.getTON();
                        currentUser.setRUB_value(currentUser.getRUB_value() + Float.parseFloat((userdata.get("amount"))) * fromTONtoRUB);
                        model.put("RUB", String.valueOf(currentUser.getRUB_value()));
                    }

                } else model.put("error", "Ошибка! На счёте недостаточно ");
            }
        }
        userRepository.save(currentUser);
        return model;
    }

    public Map<String, String> getBalance(String secret_key) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(secret_key);
        model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
        model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
        model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
        return model;
    }

}
