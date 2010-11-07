package outbox.ui.render.basic

import outbox.ui.element.UIElement
import outbox.ui.render.Render

/**
 * @author Ruslan Khmelyuk
 */
class UIOutputRender implements Render {

    String render(UIElement element) {
        def model = [:]
        if (element.id) {
            model.id = element.id?.encodeAsHTML()
        }
        if (element.style) {
            model.style = element.style?.encodeAsHTML()
        }
        if (element.styleClass) {
            model.'class' = element.styleClass?.encodeAsHTML()
        }

        model += element.args

        def attrs = ''
        model.each { attrs += "$it.key=\"$it.value\" " }

        def value = element.text ?: ''
        "<span $attrs>$value</span>"
    }
}
