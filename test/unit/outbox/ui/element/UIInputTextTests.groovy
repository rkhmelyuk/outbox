package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTextTests extends GroovyTestCase {

    void testFields() {
        def text = new UIInputText()
        text.maxlength = 10
        text.size = 20
        text.value = 'test value'

        assertEquals 10, text.maxlength
        assertEquals 20, text.size
        assertEquals 'test value', text.value
    }
}
