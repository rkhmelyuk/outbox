package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIElement
import outbox.ui.render.Render

/**
 * @author Ruslan Khmelyuk
 */
class UICheckboxRender implements Render {

    TagLibraryLookup gspTagLibraryLookup

    String render(UIElement element) {
        def g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
        def model = [:]
        if (element.id) {
            model.id = element.id
        }
        if (element.name) {
            model.name = element.name
        }
        model.value = 'true'
        model.checked = element.value
        if (element.style) {
            model.style = element.style
        }
        if (element.styleClass) {
            model.'class' = element.styleClass
        }

        model += element.args

        g.checkBox(model)
    }

}
