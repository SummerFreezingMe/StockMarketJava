package com.example.stockmarketjava.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange_rate")
@Component
public class ExchangeRate {
    @Id
    private Float RUB;

    private Float TON;

    private Float BTC;


}

