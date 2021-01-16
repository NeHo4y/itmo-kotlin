package com.github.neho4y.category.controller

import com.github.neho4u.shared.model.category.*
import com.github.neho4y.category.service.CategoryService
import com.github.neho4y.category.service.SubtopicService
import com.github.neho4y.category.service.TopicService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/category")
class CategoryController(
    private val categoryService: CategoryService,
    private val topicService: TopicService,
    private val subtopicService: SubtopicService
) {

    @GetMapping("/all")
    suspend fun getAllCategories(): List<CategoryDto> {
        return categoryService.getAllCategories()
    }

    @GetMapping("/{id}")
    suspend fun getCategoryById(@PathVariable id: Long): CategoryDto? {
        return categoryService.getCategory(id)
    }

    @PostMapping
    suspend fun createCategory(@RequestBody categoryCreationDto: CategoryCreationDto): CategoryDto {
        return categoryService.createCategory(categoryCreationDto)
    }

    @PostMapping("/{id}")
    suspend fun updateCategory(@PathVariable id: Long, @RequestBody categoryDto: CategoryDto): CategoryDto {
        return categoryService.updateCategory(categoryDto)
    }

    @GetMapping("/topic/all")
    suspend fun getAllTopics(): List<TopicDto> {
        return topicService.getAllTopics()
    }

    @GetMapping("/topic/{id}")
    suspend fun getTopicById(@PathVariable id: Long): TopicDto? {
        return topicService.getTopic(id)
    }

    @GetMapping("/{categoryId}/topics")
    suspend fun getTopicsByCategoryId(@PathVariable categoryId: Long): List<TopicDto> {
        return topicService.getTopicsByCategoryId(categoryId)
    }

    @PostMapping("/topic")
    suspend fun createTopic(@RequestBody topicDto: TopicCreationDto): TopicDto {
        return topicService.createTopic(topicDto)
    }

    @PostMapping("/topic/{id}")
    suspend fun updateTopic(@PathVariable id: Long, @RequestBody topicDto: TopicDto) {
        return topicService.updateTopic(topicDto)
    }

    @GetMapping("/topic/subtopic/all")
    suspend fun getAllSubtopics(): List<SubtopicDto> {
        return subtopicService.getAllSubtopics()
    }

    @GetMapping("/topic/subtopic/{id}")
    suspend fun getSubtopicById(@PathVariable id: Long): SubtopicDto? {
        return subtopicService.getSubtopic(id)
    }

    @GetMapping("/topic/{topicId}/subtopics")
    suspend fun getSubtopicsByTopicId(@PathVariable topicId: Long): List<SubtopicDto> {
        return subtopicService.getSubtopicByTopicId(topicId)
    }

    @PostMapping("/topic/subtopic")
    suspend fun createSubtopic(@RequestBody topicDto: SubtopicCreationDto): SubtopicDto {
        return subtopicService.createSubtopic(topicDto)
    }

    @PostMapping("/topic/subtopic/{id}")
    suspend fun updateSubtopic(@PathVariable id: Long, @RequestBody topicDto: SubtopicDto) {
        return subtopicService.updateSubtopic(topicDto)
    }
}
