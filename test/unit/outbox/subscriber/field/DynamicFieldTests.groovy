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
        field.type = DynamicFieldType.Number
        field.status = DynamicFieldStatus.Hidden
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
        assertEquals DynamicFieldType.Number, field.type
        assertEquals DynamicFieldStatus.Hidden, field.status
        assertTrue field.mandatory
    }

    void testSorting() {
        assertEquals(1, new DynamicField().compareTo(null))
        assertEquals(0, new DynamicField(sequence: 1).compareTo(new DynamicField(sequence: 1)))
        assertEquals(1, new DynamicField(sequence: 1).compareTo(new DynamicField(sequence: 0)))
        assertEquals(-1, new DynamicField(sequence: 0).compareTo(new DynamicField(sequence: 1)))
    }

    void testNonDuplicateName() {
        DynamicField field = new DynamicField(id: 1, name: 'name', owner: new Member(id: 1))
        mockDomain(DynamicField, [field])

        assertFalse DynamicField.duplicateName(field, field.name)
    }

    void testDuplicateEmail() {
        Member member = new Member(id: 1)
        DynamicField field1 = new DynamicField(id: 1, name: 'name', owner: member)
        DynamicField field2 = new DynamicField(id: 2, name: 'name', owner: member)

        mockDomain(DynamicField, [field1, field2])

        assertTrue DynamicField.duplicateName(field2, field2.name)
    }

    void testOwnedBy() {
        def field = new DynamicField(owner: null)

        assertFalse field.ownedBy(1)
        assertFalse field.ownedBy(null)

        field.owner = new Member(id: 2)
        assertFalse field.ownedBy(1)
        assertFalse field.ownedBy(null)
        assertTrue field.ownedBy(2)
    }
}
