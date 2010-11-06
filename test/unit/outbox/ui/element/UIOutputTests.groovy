package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIOutputTests extends GroovyTestCase {

    void testFields() {
        def label = new UILabel()
        def output = new UIOutput()
        output.text = 'test text'
        output.label = label

        assertEquals 'test text', output.text
        assertEquals label, output.label
    }
}
