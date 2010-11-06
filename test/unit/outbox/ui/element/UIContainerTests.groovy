package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UIContainerTests extends GroovyTestCase {

    void testFields() {
        def container = new UIContainer()
        def elements = []
        container.elements = elements

        assertEquals elements, container.elements
    }
}
