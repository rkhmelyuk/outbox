package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.render.UIRenderFactory
import outbox.ui.element.*

/**
 * @author Ruslan Khmelyuk
 */
class BasicUIRender implements UIRenderFactory {

    TagLibraryLookup gspTagLibraryLookup

    String render(UIElement element) {
        if (element instanceof UIInputText) {
            return renderInputText(element)
        }
        else if (element instanceof UIInputTextArea) {
            return renderInputTextArea(element)
        }
        else if (element instanceof UISelectSingle) {
            if (element.options) {
                return renderRadioGroup(element)
            }
            return renderInputSelect(element)
        }
        else if (element instanceof UICheckbox) {
            return renderInputCheckbox(element)
        }
        else if (element instanceof UILabel) {
            return renderLabel(element)
        }
        else if (element instanceof UIOutput) {
            return renderOutput(element)
        }

        return null
    }

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
        new UIRadioGroupRender(gspTagLibraryLookup: gspTagLibraryLookup).render(element)
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
