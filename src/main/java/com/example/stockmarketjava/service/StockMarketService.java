package com.example.stockmarketjava.service;

import com.example.stockmarketjava.domain.User;
import com.example.stockmarketjava.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class StockMarketService {
    private final UsersRepository userRepository;


    @Autowired
    public StockMarketService(UsersRepository userRepository) {
        this.userRepository = userRepository;
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

    public boolean isUserUnique(Map<String, String> userdata) {
        return !userRepository.existsUserByUsername(userdata.get("username")) &&
                !userRepository.existsUserByEmail(userdata.get("email"));
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
                if (Float.parseFloat(userdata.get("value")) < currentUser.getRUB_value()) {
                    currentUser.setRUB_value(currentUser.getRUB_value() - Float.parseFloat(userdata.get("value")));
                }
            }
            case "BTC" -> {
                if (Float.parseFloat(userdata.get("value")) < currentUser.getBTC_value()) {
                    currentUser.setBTC_value(currentUser.getBTC_value() - Float.parseFloat(userdata.get("value")));
                }
            }
            case "TON" -> {
                if (Float.parseFloat(userdata.get("value")) < currentUser.getTON_value()) {
                    currentUser.setTON_value(currentUser.getTON_value() - Float.parseFloat(userdata.get("value")));
                }
            }

        }
        userRepository.save(currentUser);
        return model;
    }

    public Map<String, String> displayExchangeRate(Map<String, String> userdata) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(userdata.get("secret_key"));
        switch (userdata.get("currency")) {
            case "RUB" -> {
                if (Float.parseFloat(userdata.get("value")) < currentUser.getRUB_value()) {
                    //todo: value update via query
                }
            }
            case "BTC" -> {
            }
            case "TON" -> {
            }
        }
        return model;
    }

    public Map<String, String> exchangeCurrency(Map<String, String> userdata) {
        return null;
    }
}
