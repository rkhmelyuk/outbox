package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIElement
import outbox.ui.element.UILabel
import outbox.ui.render.Render

/**
 * @author Ruslan Khmelyuk
 */
class UIRadioGroupRender implements Render {

    TagLibraryLookup gspTagLibraryLookup

    String render(UIElement element) {
        def model = [:]
        if (element.name) {
            model.name = element.name
        }
        if (element.style) {
            model.style = element.style
        }
        if (element.styleClass) {
            model.'class' = element.styleClass
        }

        def out = ''
        def g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
        def id = element.id ?: model.name
        def labelRender = new UILabelRender()
        element.selectItems.eachWithIndex { it, index ->
            def itemId = id + '_' + index
            model.id = itemId
            model.value = it.value
            model.checked = it.value == element.value

            out += g.radio(model)

            def label = new UILabel(forId: itemId, text: it.label)
            out += labelRender.render(label)
        }

        return out
    }

}
