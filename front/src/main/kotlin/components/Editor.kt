package components

import Client
import com.github.neho4u.shared.model.category.CategoryDto
import com.github.neho4u.shared.model.category.SubtopicDto
import com.github.neho4u.shared.model.category.TopicDto
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.feedback.FeedbackCreationDto
import com.github.neho4u.shared.model.user.UserData
import io.ktor.utils.io.core.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.connected.push
import reducers.State
import redux.RAction
import redux.WrapperAction
import utils.onChangeSetState
import kotlin.reflect.KProperty0

interface EditorProps : RProps {
    var currentUser: UserData?
    var onSubmit: (redirectTo: String?) -> Unit
}

private interface EditorStateProps : RProps {
    var currentUser: UserData?
}

private interface EditorDispatchProps : RProps {
    var onSubmit: (redirectTo: String?) -> Unit
}

data class Choices(
    val category: Long? = null,
    val topic: Long? = null,
    val subtopic: Long? = null
)

val Editor = functionalComponent<EditorProps> { props ->
    if (props.currentUser == null) return@functionalComponent

    val (header, setHeader) = useState("")
    val (body, setBody) = useState("")
    val (categories, setCategories) = useState(listOf<CategoryDto>())
    val (topics, setTopics) = useState(listOf<TopicDto>())
    val (subtopics, setSubtopics) = useState(listOf<SubtopicDto>())
    val (choices, setChoices) = useState(Choices())
    val (inProgress, setInProgress) = useState(false)

    useEffectWithCleanup(dependencies = emptyList()) {
        val job = MainScope().launch {
            setInProgress(true)

            Client().use {
                setCategories(it.category().getCategories())
                setTopics(it.category().getTopics())
                setSubtopics(it.category().getSubtopics())
            }

            setInProgress(false)
        }
        return@useEffectWithCleanup {
            job.cancel()
        }
    }

    fun submit() {
        val (category, topic, subtopic) = choices
        if (category != null || topic != null || subtopic != null) {
            MainScope().launch {
                setInProgress(true)
                var createdId: Long? = null
                try {
                    val dto = FeedbackCreationDto(header, category!!, topic!!, subtopic!!, body)
                    createdId = Client().use {
                        val created = it.feedback().create(dto)
                        it.comment().add(CommentCreationDto(created.id, "body", body))
                        return@use created.id
                    }
                } catch (e: Exception) {
                    console.error(e.message)
                }
                setInProgress(false)
                createdId?.let {
                    props.onSubmit("/feedback/$createdId")
                }
            }
        }
    }

    fun <T> RDOMBuilder<DIV>.selector(
        list: List<T>,
        setHandler: (id: Long?) -> Choices,
        filter: (it: T) -> Boolean,
        bindProp: KProperty0<Long?>,
        content: RDOMBuilder<OPTION>.(it: T) -> Unit,
    ) {
        val synthetic = "synthetic"
        select("form-control") {
            attrs.onChangeFunction = { ev ->
                val target = ev.target as? HTMLSelectElement
                setChoices(setHandler(target?.value?.toLongOrNull()))
            }
            val filtered = list.filter(filter)
            attrs.disabled = filtered.isEmpty()
            option {
                attrs.value = synthetic
                +"Select ${bindProp.name}"
            }
            filtered.forEach {
                option {
                    content(it)
                }
            }
            if (bindProp.get() == null) {
                attrs.value = synthetic
            }
        }
    }

    editorContainer {
        form {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                submit()
            }

            fieldSet {
                fieldSet("form-group") {
                    input(type = InputType.text, classes = "form-control form-control-lg") {
                        attrs {
                            value = header
                            placeholder = "Feedback header"
                            onChangeFunction = onChangeSetState(setHeader)
                        }
                    }
                }

                fieldSet("form-group") {
                    textArea(rows = "8", classes = "form-control") {
                        attrs {
                            value = body
                            placeholder = "Write your feedback (in markdown)"
                            onChangeFunction = onChangeSetState(setBody)
                        }
                    }
                }

                fieldSet("form-group") {
                    div("container") {
                        div("row") {
                            div("col-sm") {
                                selector(
                                    list = categories,
                                    setHandler = { Choices(category = it) },
                                    filter = { true },
                                    bindProp = choices::category
                                ) {
                                    attrs.value = it.id.toString()
                                    +it.description
                                }
                            }
                            div("col-sm") {
                                selector(
                                    list = topics,
                                    setHandler = { choices.copy(topic = it, subtopic = null) },
                                    filter = { it.categoryId == choices.category },
                                    bindProp = choices::topic
                                ) {
                                    attrs.value = it.id.toString()
                                    +it.description
                                }
                            }

                            div("col-sm") {
                                selector(
                                    list = subtopics,
                                    setHandler = { choices.copy(subtopic = it) },
                                    filter = { it.topicId == choices.topic },
                                    bindProp = choices::subtopic
                                ) {
                                    attrs.value = it.id.toString()
                                    +it.description
                                }
                            }
                        }
                    }
                }

                button(classes = "btn btn-lg pull-xs-right btn-primary") {
                    attrs {
                        type = ButtonType.submit
                        val (category, topic, subtopic) = choices
                        disabled = inProgress || category == null || topic == null || subtopic == null
                    }
                    +"Send Feedback"
                }
            }
        }
    }
}

private fun RBuilder.editorContainer(block: RDOMBuilder<DIV>.() -> Unit) {
    div("editor-page") {
        div("container page") {
            div("row") {
                div("col-md-10 offset-md-1 col-xs-12") {
                    block()
                }
            }
        }
    }
}

val editorView =
    rConnect<State, RAction, WrapperAction, RProps, EditorStateProps, EditorDispatchProps, EditorProps>(
        { state, _ ->
            currentUser = state.auth.currentUser
        },
        { dispatch, _ ->
            onSubmit = { path -> path?.let { dispatch(push(path)) } }
        }
    )(Editor.unsafeCast<RClass<EditorProps>>())
