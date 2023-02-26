package com.example.stockmarketjava.repos;


import com.example.stockmarketjava.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String name);

    boolean existsUserBySecretKey(String secretKey);

    User findBySecretKey(String secret_key);

    List<User> findUserByRoleIs(String role);



}