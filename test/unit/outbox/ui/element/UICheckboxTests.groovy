package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UICheckboxTests extends GroovyTestCase {

    void testFields() {
        def checkbox = new UICheckbox()
        checkbox.value = true

        assertTrue checkbox.value
    }

}
