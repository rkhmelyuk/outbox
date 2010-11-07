package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIElement
import outbox.ui.render.Render

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTextRender implements Render {

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
        if (element.maxlength) {
            model.maxlength = element.maxlength
        }
        if (element.value) {
            model.value = element.value
        }
        if (element.style) {
            model.style = element.style
        }
        if (element.styleClass) {
            model.'class' = element.styleClass
        }

        model += element.args

        g.textField(model)
    }

}
