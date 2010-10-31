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
        item.sequence = 3
        item.name = 'Test Item Name'

        assertEquals 1, item.id
        assertEquals 2, item.field.id
        assertEquals 3, item.sequence
        assertEquals 'Test Item Name', item.name
    }

    void testSorting() {

        def left = new DynamicFieldItem(sequence: 1)
        def right = new DynamicFieldItem(sequence: 1)

        assertEquals(1, left.compareTo(null))
        assertEquals(0, left.compareTo(right))

        left.sequence = 0
        assertEquals(-1, left.compareTo(right))
        assertEquals(1, right.compareTo(left))
    }
}
