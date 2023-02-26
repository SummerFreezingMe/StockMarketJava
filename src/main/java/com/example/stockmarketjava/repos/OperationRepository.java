package com.example.stockmarketjava.repos;

import com.example.stockmarketjava.domain.Operation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> findByOperationDateBetween(LocalDate d1, LocalDate d2);
}
