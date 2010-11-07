package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.render.UIRender
import outbox.ui.element.*

/**
 * @author Ruslan Khmelyuk
 */
class BasicUIRender implements UIRender {

    TagLibraryLookup gspTagLibraryLookup

    String renderInputText(UIInputText element) {
        new UIInputTextRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
    }

    String renderInputTextArea(UIInputTextArea element) {
        new UIInputTextAreaRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
    }

    String renderInputSelect(UISelectSingle element) {
        throw new UnsupportedOperationException()
    }

    String renderInputCheckbox(UICheckbox element) {
        new UICheckboxRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
    }

    String renderLabel(UILabel element) {
        throw new UnsupportedOperationException()
    }

    String renderOutput(UIOutput element) {
        throw new UnsupportedOperationException()
    }


}
