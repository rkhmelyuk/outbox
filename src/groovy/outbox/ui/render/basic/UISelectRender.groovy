package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIElement
import outbox.ui.render.Render

/**
 * @author Ruslan Khmelyuk
 */
class UISelectRender implements Render {

    TagLibraryLookup gspTagLibraryLookup

    String render(UIElement element) {
        def model = [:]
        if (element.id) {
            model.id = element.id
        }
        if (element.name) {
            model.name = element.name
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
        model.optionKey='value'
        model.optionValue='label'
        model.value = element.value
        model.from = element.selectItems

        gspTagLibraryLookup.lookupNamespaceDispatcher("g").select(model)
    }

}
