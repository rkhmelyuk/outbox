package outbox.subscriber.field

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldItemTests extends GrailsUnitTestCase {

    void testFields() {
        def item = new DynamicFieldItem()
        item.id = 1
        item.field = new DynamicField(id: 2)
        item.name = 'Test Item Name'

        assertEquals 1, item.id
        assertEquals 2, item.field.id
        assertEquals 'Test Item Name', item.name
    }

    void testSorting() {

        def left = new DynamicFieldItem(id: 1, name: 'b')
        def right = new DynamicFieldItem(id: 1, name: 'b')

        assertEquals(1, left.compareTo(null))
        assertEquals(0, left.compareTo(right))

        left.id = 2
        assertEquals(1, left.compareTo(right))

        left.name = 'a'
        assertEquals(-1, left.compareTo(right))
        assertEquals(1, right.compareTo(left))
    }
}
