package com.example.stockmarketjava.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String username;
    private String email;
    @Id
    private String secretKey;
    private String role;
    private Float BTC_value;
    private Float TON_value;
    private Float RUB_value;

}
