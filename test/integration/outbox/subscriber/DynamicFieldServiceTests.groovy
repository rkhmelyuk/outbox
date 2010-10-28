package outbox.subscriber

import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType

/**
 * {@link outbox.subscriber.DynamicFieldService} tests.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldServiceTests extends GrailsUnitTestCase {

    DynamicFieldService dynamicFieldService

    Member member

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()
    }

    protected void tearDown() {
        member.delete()

        super.tearDown();
    }

    void testAddDynamicField() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.addDynamicField(field)

        def found = dynamicFieldService.getDynamicField(field.id)
        assertEquals field, found
    }

    void testAddDynamicField_Sequence() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)
        field1.sequence = 0
        field2.sequence = 0
        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)

        def found1 = dynamicFieldService.getDynamicField(field1.id)
        assertEquals field1, found1
        assertEquals 1, found1.sequence

        def found2 = dynamicFieldService.getDynamicField(field2.id)
        assertEquals field2, found2
        assertEquals 2, found2.sequence
    }

    void testGetDynamicFields() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)
        def field3 = createDynamicField(3)

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)
        assertTrue dynamicFieldService.addDynamicField(field3)

        def fields = dynamicFieldService.getDynamicFields(member)
        assertEquals 3, fields.size()
        assertTrue fields.contains(field1)
        assertTrue fields.contains(field2)
        assertTrue fields.contains(field3)
    }

    void assertEquals(DynamicField left, DynamicField right) {
        assertNotNull right
        assertEquals left.id, right.id
        assertEquals left.name, right.name
        assertEquals left.sequence, right.sequence
        assertEquals left.label, right.label
        assertEquals left.mandatory, right.mandatory
        assertEquals left.owner.id, right.owner.id
        assertEquals left.max, right.max
        assertEquals left.min, right.min
        assertEquals left.maxlength, right.maxlength
    }

    DynamicField createDynamicField(def id) {
        def dynamicField = new DynamicField()

        dynamicField.name = 'dynamicField' + id
        dynamicField.type = DynamicFieldType.String
        dynamicField.sequence = 1
        dynamicField.label = 'Dynaic Field Label'
        dynamicField.mandatory = true
        dynamicField.owner = member
        dynamicField.max = 10
        dynamicField.min = -5
        dynamicField.maxlength = 120

        return dynamicField
    }
}
