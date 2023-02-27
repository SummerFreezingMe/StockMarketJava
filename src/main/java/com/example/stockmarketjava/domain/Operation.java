package com.example.stockmarketjava.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long operationId;
    LocalDate operationDate;
    String operationType;
    String username;
    String moneyDifferential;
    String currencyCode;

    public Operation(LocalDate operationDate, String operationType, String username, String moneyDifferential, String currencyCode) {
        this.operationDate = operationDate;
        this.operationType = operationType;
        this.username = username;
        this.moneyDifferential = moneyDifferential;
        this.currencyCode = currencyCode;
    }
}
