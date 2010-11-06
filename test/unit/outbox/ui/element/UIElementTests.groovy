package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIElementTests extends GroovyTestCase {

    void testFields() {
        def element = new UIElement()
        element.id = 'test id'
        element.style = 'test style'
        element.styleClass = 'test style class'

        assertEquals 'test id', element.id
        assertEquals 'test style', element.style
        assertEquals 'test style class', element.styleClass
    }
}
