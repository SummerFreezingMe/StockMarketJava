package com.example.stockmarketjava.controllers;

import com.example.stockmarketjava.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdministratorController {
    private final StockMarketService sms;

    @Autowired
    public AdministratorController(StockMarketService sms) {
        this.sms = sms;
    }

    @RequestMapping(value = "/change_exchange_rate", method = RequestMethod.POST, produces = {"application/json", "application/xml"})
    public Map<String, String> changeExchangeRate(@RequestBody Map<String, String> payload) {
        return sms.redactExchangeRate(payload) ;
    }

    @RequestMapping(value = "/display_sum_of_currency", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> displaySumOfCurrency(@RequestBody Map<String, String> payload) {
        return sms.displaySumOfCurrency(payload);
    }

    @RequestMapping(value = "/display_transaction_count", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Object displayTransactionCount(@RequestBody Map<String, String> payload) {
        return "";
    }
}
