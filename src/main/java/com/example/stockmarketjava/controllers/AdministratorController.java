package com.example.stockmarketjava.controllers;

import com.example.stockmarketjava.service.StockAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdministratorController {
    private final StockAdminService sas;

    @Autowired
    public AdministratorController(StockAdminService sas) {
        this.sas = sas;
    }

    @RequestMapping(value = "/change_exchange_rate", method = RequestMethod.POST, produces = {"application/json", "application/xml"},consumes =   {"application/json", "application/xml"})
    public Map<String, String> changeExchangeRate(@RequestBody Map<String, String> payload) {
        return sas.redactExchangeRate(payload);
    }

    @RequestMapping(value = "/display_sum_of_currency", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> displaySumOfCurrency(@RequestBody Map<String, String> payload) {
        return sas.displaySumOfCurrency(payload);
    }

    @RequestMapping(value = "/display_transaction_count", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public Map<String, String> displayTransactionCount(@RequestBody Map<String, String> payload) {
        return
                sas.displayTransactionCount(payload);
    }
}
