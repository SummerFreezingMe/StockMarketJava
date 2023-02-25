
package com.example.stockmarketjava.repos;

import com.example.stockmarketjava.domain.ExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

}
