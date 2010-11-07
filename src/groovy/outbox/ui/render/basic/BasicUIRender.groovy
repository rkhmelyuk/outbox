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
        new UISelectRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
    }

    String renderRadioGroup(UISelectSingle element) {
        throw new UnsupportedOperationException()
    }

    String renderInputCheckbox(UICheckbox element) {
        new UICheckboxRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
    }

    String renderLabel(UILabel element) {
        new UILabelRender().render(element)
    }

    String renderOutput(UIOutput element) {
        new UILabelRender().render(element)
    }


}
