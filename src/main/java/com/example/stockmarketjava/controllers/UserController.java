package com.example.stockmarketjava.controllers;


import com.example.stockmarketjava.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    private final StockMarketService sms;


    @Autowired
    public UserController(StockMarketService sms) {
        this.sms = sms;
    }


    @RequestMapping(value = "/register_user", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Object addUser(@RequestBody Map<String, String> payload) {

        return sms.userAddition(payload);
    }

    @RequestMapping(value = "/get_balance", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> getBalance(@RequestParam String secret_key) {
        return sms.getBalance(secret_key);
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
