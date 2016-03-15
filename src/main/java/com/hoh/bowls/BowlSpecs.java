package com.hoh.bowls;


import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by jeong on 2016-03-08.
 */
public class BowlSpecs {

    public static Specification<Bowl> bowlTypeEqual(final BowlType theme) {
        return new Specification<Bowl>() {
            @Override
            public Predicate toPredicate(Root<Bowl> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Bowl_.theme), theme);
            }
        };
    }

}
