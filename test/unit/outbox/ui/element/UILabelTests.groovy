package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UILabelTests extends GroovyTestCase {

    void testFields() {
        def label = new UILabel()
        label.text = 'test text'
        label.forId = 'test for'

        assertEquals 'test text', label.text
        assertEquals 'test for', label.forId
    }
}
