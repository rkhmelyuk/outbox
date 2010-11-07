package outbox.ui.render.basic

import outbox.ui.element.UIElement

/**
 * @author Ruslan Khmelyuk
 */
class UILabelRender {

    String render(UIElement element) {
        def model = [:]
        if (element.id) {
            model.id = element.id?.encodeAsHTML()
        }
        if (element.forId) {
            model.'for' = element.forId?.encodeAsHTML()
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
        "<label $attrs>$value</label>"
    }

}
