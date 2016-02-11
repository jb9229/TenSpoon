package com.hoh.spoons;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by test on 2016-01-31.
 */
public class SpoonSpecs {

    public static Specification<Spoon> accountIdEqual(final Long accountId){
        return new Specification<Spoon>() {
            @Override
            public Predicate toPredicate(Root<Spoon> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Spoon_.accountId), accountId);
            }
        };
    }
}
