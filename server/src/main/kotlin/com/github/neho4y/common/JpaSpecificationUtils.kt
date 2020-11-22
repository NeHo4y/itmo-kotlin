package com.github.neho4y.common

import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate

internal fun <T> T?.addOptionalFilter(
    path: Path<T>,
    predicates: MutableList<Predicate>,
    builder: CriteriaBuilder
) = this?.also {
    predicates.add(builder.equal(path, this))
}

internal fun Boolean?.addOptionalFilter(
    path: Path<Boolean>,
    predicates: MutableList<Predicate>,
    builder: CriteriaBuilder
) = this?.also {
    val predicate = if (this) {
        builder.isTrue(path)
    } else {
        builder.isFalse(path)
    }
    predicates.add(predicate)
}

internal fun String?.addOptionalFilter(
    path: Path<String>,
    predicates: MutableList<Predicate>,
    builder: CriteriaBuilder
) = this?.also {
    if (this.isNotBlank()) {
        val alias = "%$this%"
        predicates.add(builder.like(builder.upper(path), alias.toUpperCase(Locale.ENGLISH)))
    }
}
