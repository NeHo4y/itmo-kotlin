package components

import markdown.it.MarkdownIt
import markdown.it.mkMarkdown
import react.RBuilder
import react.RProps
import react.child
import react.dom.InnerHTML
import react.dom.div
import react.functionalComponent
import sanitize.html.IOptions
import sanitize.html.sanitize
import utils.jsApply

private val markdownRenderer = mkMarkdown(
    jsApply<MarkdownIt.Options> {
        xhtmlOut = true
        linkify = true
    }
)

private val sanitizeOptions = jsApply<IOptions> {
    allowedTags = arrayOf(
        "table", "tr", "th", "td", "caption", "thead", "tbody", "tfoot", "col", "colgroup",
        "b", "i", "em", "strong",
        "a",
        "h1", "h2", "h3", "h4", "h5", "h6",
        "hr",
        "p", "blockquote", "pre", "code",
        "ol", "ul", "li",
        "img",
        "span"
    )
    allowedAttributes = jsApply {
        span = arrayOf("class")
        code = arrayOf("class")
        img = arrayOf("src", "alt")
        a = arrayOf("href", "title")
        pre = arrayOf("class")
    }
    allowedSchemesByTag = jsApply {
        img = arrayOf("data", "http", "https")
    }
}

interface MarkdownWithCodeProps : RProps {
    var rawText: String
}

val MarkdownText = functionalComponent<MarkdownWithCodeProps> { props ->
    val render = markdownRenderer.render(props.rawText)
    div {
        attrs["dangerouslySetInnerHTML"] = InnerHTML(
            sanitize(render, sanitizeOptions)
        )
    }
}

fun RBuilder.markdownText(code: String) = child(MarkdownText) { attrs.rawText = code }
