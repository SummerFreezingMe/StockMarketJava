package com.example.stockmarketjava.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StockMarketService {

    public String generateSecretKey(){
        StringBuilder secretKey = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 30; i++) {
            secretKey.append((char)(rnd.nextInt(78)+49));
        }
        return secretKey.toString();
    }
}
