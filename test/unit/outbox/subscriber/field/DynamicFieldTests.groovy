package outbox.subscriber.field

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldTests extends GrailsUnitTestCase {

    void testFields() {
        def field = new DynamicField()

        field.id = 1
        field.owner = new Member(id: 2)
        field.name = 'testName'
        field.label = 'Test Label'
        field.type = DynamicFieldType.Integer
        field.sequence = 3
        field.mandatory = true
        field.min = 0
        field.max = 100
        field.maxlength = 50

        assertEquals 1, field.id
        assertEquals 2, field.owner.id
        assertEquals 3, field.sequence
        assertEquals 0, field.min
        assertEquals 100, field.max
        assertEquals 50, field.maxlength
        assertEquals 'testName', field.name
        assertEquals 'Test Label', field.label
        assertEquals DynamicFieldType.Integer, field.type
        assertTrue field.mandatory
    }
}
