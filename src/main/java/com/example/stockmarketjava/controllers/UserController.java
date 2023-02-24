package com.example.stockmarketjava.controllers;


import com.example.stockmarketjava.domain.Role;
import com.example.stockmarketjava.domain.User;
import com.example.stockmarketjava.repos.UsersRepository;
import com.example.stockmarketjava.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, String> addUser(@RequestBody Map<String, String> payload) {
        Map<String, String> model = new HashMap<>();
        String secretKey = sms.generateSecretKey();
        model.put("secret_key",secretKey );
        userRepository.save(new User(payload.get("username"), payload.get("email"),secretKey, Role.USER,0f,0f,0f));
        return model;
    }

    @RequestMapping(value = "/get_balance", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> getBalance(@RequestBody Map<String, String> payload) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(payload.get("secret_key"));
        model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
        model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
        model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
        return model;
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> depositMoney(@RequestBody Map<String, String> payload) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(payload.get("secret_key"));


        switch (payload.keySet().toArray()[1].toString()) {
            case ("TON_wallet") -> {
                model.put("TON_wallet", String.valueOf(currentUser.getTON_value()));
              currentUser.setTON_value(Float.valueOf(currentUser.getTON_value() + model.get("TON_wallet")));
            }
            case ("RUB_wallet") -> {
                model.put("RUB_wallet", String.valueOf(currentUser.getRUB_value()));
                currentUser.setRUB_value(Float.valueOf(currentUser.getRUB_value() + model.get("RUB_wallet")));
            }
            case ("BTC_wallet") -> {
                model.put("BTC_wallet", String.valueOf(currentUser.getBTC_value()));
                currentUser.setBTC_value(Float.valueOf(currentUser.getBTC_value() + model.get("BTC_wallet")));
            }
        }
        return model;
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> withdrawMoney(@RequestBody Map<String, String> payload) {
        Map<String, String> model = new HashMap<>();
        User currentUser = userRepository.findBySecretKey(payload.get("secret_key"));
        switch (payload.get("currency")) {
        case "RUB"->{
            if (Float.parseFloat(payload.get("value"))<currentUser.getRUB_value()){
                //todo: value update via query
            }
        }
            case "BTC"->{}
            case "TON"->{}
        }
        return model;
    }
}