import com.github.neho4u.shared.Calculator
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*

private const val defaultResult = " = \uD83E\uDD14"

val App = functionalComponent<RProps> { _ ->
    val (firstNumber, setFirstNumber) = useState("")
    val (secondNumber, setSecondNumber) = useState("")
    val (resultText, setResultText) = useState(defaultResult)

    useEffect(dependencies = listOf(firstNumber, secondNumber)) {
        val first = firstNumber.toIntOrNull()
        val second = secondNumber.toIntOrNull()
        if (first == null || second == null) {
            setResultText(defaultResult)
        } else {
            setResultText(" = ${Calculator.sum(first, second)}")
        }
    }

    h1 {
        +"Kotlin multiplatform calculator"
    }

    div {
        input(InputType.text) {
            attrs.onChangeFunction = onChangeSetState(setFirstNumber)
            attrs.value = firstNumber
        }
        +" + "
        input(InputType.text) {
            attrs.onChangeFunction = onChangeSetState(setSecondNumber)
            attrs.value = secondNumber
        }
        +resultText
    }
}

private fun onChangeSetState(
    setStateHandler: RSetState<String>
): (Event) -> Unit = {
    setStateHandler((it.target as HTMLInputElement).value)
}
