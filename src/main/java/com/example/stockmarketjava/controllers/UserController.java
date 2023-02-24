package com.example.stockmarketjava.controllers;


import com.example.stockmarketjava.domain.Role;
import com.example.stockmarketjava.domain.User;
import com.example.stockmarketjava.repos.UsersRepository;
import com.example.stockmarketjava.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final StockMarketService sms;

    private final UsersRepository userRepository;


    @Autowired
    public UserController(StockMarketService sms, UsersRepository userRepository) {
        this.sms = sms;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/register_user", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Object addUser(@RequestBody Map<String, String> payload) {
        if (sms.isUserUnique(payload)) {
            Map<String, String> model = new HashMap<>();
            String secretKey = sms.generateSecretKey();
            model.put("secret_key", secretKey);
            userRepository.save(new User(payload.get("username"), payload.get("email"), secretKey, Role.USER, 0f, 0f, 0f));
            return model;
        } else
            return "Ошибка! Пользователем с таким именем или почтой уже существует!";
    }

    @RequestMapping(value = "/get_balance", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> getBalance(@RequestParam String secret_key) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(secret_key);
        model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
        model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
        model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
        return model;
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> depositMoney(@RequestBody Map<String, String> payload) {
        return sms.depositCurrency(payload);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> withdrawMoney(@RequestBody Map<String, String> payload) {
       return sms.withdrawCurrency(payload);
    }
    @RequestMapping(value = "/exchange_rate", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> browseExchangeRate(@RequestBody Map<String, String> payload) {
        return sms.displayExchangeRate(payload);
    }
    @RequestMapping(value = "/exchange_currency", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> exchangeCurrency(@RequestBody Map<String, String> payload) {
        return sms.exchangeCurrency(payload);
    }
}
