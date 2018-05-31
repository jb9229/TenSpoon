package com.hoh.AdTS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by test on 2018-03-23.
 */
public interface  AdTSRepository extends JpaRepository<AdTS, Long> {
    AdTS findByOrderNum(int orderNum);

    @Query(value ="select max(a.orderNum) from AdTS a")
    Integer getMaxAdTSOrderNum();
}
