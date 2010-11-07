package outbox.ui.render

import outbox.ui.element.*

/**
 * @author Ruslan Khmelyuk
 */
interface UIRenderFactory {

    String render(UIElement element)

    String renderInputText(UIInputText element)

    String renderInputTextArea(UIInputTextArea element)

    String renderInputSelect(UISelectSingle element)

    String renderRadioGroup(UISelectSingle element)

    String renderInputCheckbox(UICheckbox element)

    String renderLabel(UILabel element)

    String renderOutput(UIOutput element)

}
