package com.hoh.spoons;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2016-01-31.
 */
public interface SpoonRepository extends JpaRepository<Spoon, Long> {

    Page<Spoon> findAll(Specification<Spoon> spec, Pageable pageable);
}
