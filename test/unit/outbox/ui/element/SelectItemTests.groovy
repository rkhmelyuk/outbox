package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class SelectItemTests extends GroovyTestCase {

    void testFields() {
        def item = new SelectItem()
        item.key = 'test key'
        item.value = 'test value'

        assertEquals 'test key', item.key
        assertEquals 'test value', item.value
    }
}
