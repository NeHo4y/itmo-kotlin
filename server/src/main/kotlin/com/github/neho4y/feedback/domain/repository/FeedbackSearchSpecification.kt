package com.github.neho4y.feedback.domain.repository

import com.github.neho4u.shared.model.feedback.FeedbackFilter
import com.github.neho4y.common.addOptionalFilter
import com.github.neho4y.feedback.domain.Feedback
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class FeedbackSearchSpecification(private val feedbackFilter: FeedbackFilter) : Specification<Feedback> {
    override fun toPredicate(
        root: Root<Feedback>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        val predicates = mutableListOf<Predicate>()
        feedbackFilter.number?.addOptionalFilter(root.get("id"), predicates, criteriaBuilder)
        feedbackFilter.header?.addOptionalFilter(root.get("header"), predicates, criteriaBuilder)
        feedbackFilter.authorId?.addOptionalFilter(root.get("authorId"), predicates, criteriaBuilder)
        feedbackFilter.categoryId?.addOptionalFilter(root.get("categoryId"), predicates, criteriaBuilder)
        feedbackFilter.topicId?.addOptionalFilter(root.get("topicId"), predicates, criteriaBuilder)
        feedbackFilter.subtopicId?.addOptionalFilter(root.get("subtopicId"), predicates, criteriaBuilder)
        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}
