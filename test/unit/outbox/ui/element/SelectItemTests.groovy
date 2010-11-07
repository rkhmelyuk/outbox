package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class SelectItemTests extends GroovyTestCase {

    void testFields() {
        def item = new SelectItem()
        item.value = 'test value'
        item.label = 'test label'

        assertEquals 'test label', item.label
        assertEquals 'test value', item.value
    }
}
