package com.hoh.accounts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2015-10-18.
 */
public interface AccountRepository extends JpaRepository<Account, Long>{
    Account findByUsername(String username);

    Account findByEmail(String email);

    Page<Account> findAll(Specification<Account> spec, Pageable pageable);
}
