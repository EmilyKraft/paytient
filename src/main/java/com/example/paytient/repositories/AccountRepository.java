package com.example.paytient.repositories;

import com.example.paytient.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    List<Account> findAll();
    Optional<Account> findById(String id);
    Account save(Account account);

}
