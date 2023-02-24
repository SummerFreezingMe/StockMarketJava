package com.example.stockmarketjava.repos;


import com.example.stockmarketjava.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String name);
    User findBySecretKey(String secret_key);

   // @Modifying
   // @Query("update User u set u.firstname = ?1, u.lastname = ?2 where u.id = ?3")
  //  void setRUBWallet(float RUB_Value, String secret_key);
}