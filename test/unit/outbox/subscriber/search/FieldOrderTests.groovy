package outbox.subscriber.search

/**
 * @author Ruslan Khmelyuk
 */
class FieldOrderTests extends GroovyTestCase {

    void testFields() {
        def order = new FieldOrder('column', Sort.Desc)
        assertEquals 'column', order.column
        assertEquals Sort.Desc, order.sort
    }
}
