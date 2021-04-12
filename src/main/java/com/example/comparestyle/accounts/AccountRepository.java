package com.example.comparestyle.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByUsername(String username);

    Account findByUsername3(String username);

    Account findByUsername2(String username);

}
