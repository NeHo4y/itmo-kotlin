package com.github.neho4u.model

import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4u.shared.model.common.IdName

sealed class IdWithName(val id: Long, val name: String) {
    override fun toString() = name

    open fun isMyParent(idWithName: IdWithName): Boolean = false
}

class SimpleIdWithName(id: Long, name: String) : IdWithName(id, name)

class HierarchyIdWithName(id: Long, name: String, val parent: Long) : IdWithName(id, name) {
    override fun isMyParent(idWithName: IdWithName) = parent == idWithName.id
}

private const val PLACEHOLDER_ID = -1L
class Placeholder(name: String) : IdWithName(PLACEHOLDER_ID, name)

internal fun CategoryDto.toIdWithName(): IdWithName = SimpleIdWithName(id, description)
internal fun TopicDto.toIdWithName(): IdWithName = HierarchyIdWithName(id, description, categoryId)
internal fun SubtopicDto.toIdWithName(): IdWithName = HierarchyIdWithName(id, description, topicId)
internal fun IdName.toIdWithName(): IdWithName = SimpleIdWithName(id, name ?: "ЕГГОГ")
