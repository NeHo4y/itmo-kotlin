package com.github.neho4y.category.controller

import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4y.category.domain.Category
import com.github.neho4y.category.domain.Subtopic
import com.github.neho4y.category.domain.Topic

internal fun Category.toDto() = CategoryDto(
    id = id,
    description = description
)

internal fun Topic.toDto() = TopicDto(
    id = id,
    description = description,
    categoryId = categoryId
)

internal fun Subtopic.toDto() = SubtopicDto(
    id = id,
    description = description,
    topicId = topicId
)
