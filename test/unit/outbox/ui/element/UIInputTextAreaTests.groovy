package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTextAreaTests extends GroovyTestCase {

    void testFields() {
        def text = new UIInputTextArea()
        text.maxlength = 10
        text.rows = 20
        text.cols = 30
        text.value = 'test value'

        assertEquals 10, text.maxlength
        assertEquals 20, text.rows
        assertEquals 30, text.cols
        assertEquals 'test value', text.value
    }
}
