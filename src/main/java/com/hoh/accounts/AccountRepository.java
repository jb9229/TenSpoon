package com.hoh.accounts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByEmail(String email);

    Page<Account> findAll(Specification<Account> spec, Pageable pageable);


    //    @Query( value="select a from com.hoh.accounts.Account a left join com.hoh.spoons.Spoon s on a.id = s.accountId where s.bowl.id = :bowlId")
    //    Page<Account> findByBowl(@Param("bowlId") Long bowlId, Pageable pageable);
}
