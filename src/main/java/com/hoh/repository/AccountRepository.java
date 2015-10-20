package com.hoh.repository;

import com.hoh.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2015-10-18.
 */
public interface AccountRepository extends JpaRepository<Account, Long>{

}
