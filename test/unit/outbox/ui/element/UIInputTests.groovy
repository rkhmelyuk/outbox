package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTests extends GroovyTestCase {

    void testFields() {
        def label = new UILabel()
        def input = new UIInput()
        input.name = 'test name'
        input.label = label
        input.mandatory = true

        assertEquals 'test name', input.name
        assertEquals label, input.label
        assertTrue input.mandatory
    }
}
