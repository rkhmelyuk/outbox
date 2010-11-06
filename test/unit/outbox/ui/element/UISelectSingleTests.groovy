package outbox.ui.element

/**
 * @author Ruslan Khmelyuk
 */
class UISelectSingleTests extends GroovyTestCase {

    void testFields() {
        def select = new UISelectSingle()
        select.value = 'test value'
        select.options = true
        def selectItems = []
        select.selectItems = selectItems

        assertEquals 'test value', select.value
        assertEquals selectItems, select.selectItems
        assertTrue select.options
    }
}
