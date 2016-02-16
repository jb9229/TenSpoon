package com.hoh.bowls;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jeong on 2016-02-15.
 */
public interface BowlRepository extends JpaRepository<Bowl, Long> {
}
