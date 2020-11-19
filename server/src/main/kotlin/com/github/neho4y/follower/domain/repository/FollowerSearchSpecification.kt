package com.github.neho4y.follower.domain.repository

import com.github.neho4y.common.addOptionalFilter
import com.github.neho4y.follower.domain.FeedbackFollower
import com.github.neho4y.follower.model.FollowerFilterDto
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class FollowerSearchSpecification(private val searchDto: FollowerFilterDto) : Specification<FeedbackFollower> {
    override fun toPredicate(
        root: Root<FeedbackFollower>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        val predicates = mutableListOf<Predicate>()
        searchDto.feedbackId.addOptionalFilter(root.get("feedbackId"), predicates, criteriaBuilder)
        searchDto.followerType.addOptionalFilter(root.get("followerType"), predicates, criteriaBuilder)
        searchDto.userId.addOptionalFilter(root.get("userId"), predicates, criteriaBuilder)
        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}
