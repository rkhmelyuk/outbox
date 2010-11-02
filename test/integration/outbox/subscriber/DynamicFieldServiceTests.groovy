package outbox.subscriber

import org.hibernate.Session
import outbox.member.Member
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldStatus
import outbox.subscriber.field.DynamicFieldType

/**
 * {@link outbox.subscriber.DynamicFieldService} tests.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldServiceTests extends GroovyTestCase {

    Session session
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

    void testSaveDynamicFieldItem() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item = createDynamicFieldItem(field)
        assertTrue dynamicFieldService.saveDynamicFieldItem(item)

        def found = dynamicFieldService.getDynamicFieldItem(item.id)
        assertEquals(item, found)
    }

    void testAddDynamicFieldItems_Sequence() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item1 = createDynamicFieldItem(field)
        def item2 = createDynamicFieldItem(field)
        def item3 = createDynamicFieldItem(field)
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])

        def found1 = dynamicFieldService.getDynamicFieldItem(item1.id)
        def found2 = dynamicFieldService.getDynamicFieldItem(item2.id)
        def found3 = dynamicFieldService.getDynamicFieldItem(item3.id)

        assertEquals(item1, found1)
        assertEquals(item2, found2)
        assertEquals(item3, found3)
    }

    void testAddDynamicFieldItems_NotSequence() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.String
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item1 = createDynamicFieldItem(field)
        def item2 = createDynamicFieldItem(field)
        def item3 = createDynamicFieldItem(field)
        assertFalse dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])

        assertNull dynamicFieldService.getDynamicFieldItem(item1.id)
        assertNull dynamicFieldService.getDynamicFieldItem(item2.id)
        assertNull dynamicFieldService.getDynamicFieldItem(item3.id)
    }

    void testGetDynamicFieldItems_Sequence() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item1 = createDynamicFieldItem(field)
        def item2 = createDynamicFieldItem(field)
        def item3 = createDynamicFieldItem(field)
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])

        def found = dynamicFieldService.getDynamicFieldItems(field)

        assertEquals 3, found.size()
        assertTrue found.contains(item1)
        assertTrue found.contains(item2)
        assertTrue found.contains(item3)
    }

    void testGetDynamicFieldItems_NotSequence() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.String
        assertTrue dynamicFieldService.saveDynamicField(field)

        def found = dynamicFieldService.getDynamicFieldItems(field)
        assertEquals 0, found.size()
    }

    void testUpdateDynamicFieldItems_Update() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item1 = createDynamicFieldItem(field)
        def item2 = createDynamicFieldItem(field)
        def item3 = createDynamicFieldItem(field)
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])
        dynamicFieldService.getDynamicFieldItems(field)

        item1.name = 'Changed Name'
        def item4 = createDynamicFieldItem(field)
        def item5 = createDynamicFieldItem(field)

        assertTrue dynamicFieldService.updateDynamicFieldItems(field, [item1, item4, item5])

        def found = dynamicFieldService.getDynamicFieldItems(field)

        assertEquals 3, found.size()
        assertTrue found.contains(item1)
        assertTrue found.contains(item4)
        assertTrue found.contains(item5)

        assertEquals item1.name, dynamicFieldService.getDynamicFieldItem(item1.id).name
    }

    void testUpdateDynamicFieldItems_Cleanup() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item1 = createDynamicFieldItem(field)
        def item2 = createDynamicFieldItem(field)
        def item3 = createDynamicFieldItem(field)

        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])
        assertEquals 3, dynamicFieldService.getDynamicFieldItems(field).size()

        assertTrue dynamicFieldService.updateDynamicFieldItems(field, [])
        assertEquals 0, dynamicFieldService.getDynamicFieldItems(field).size()
    }

    void testDeleteDynamicField() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.saveDynamicField(field)
        assertTrue dynamicFieldService.deleteDynamicField(field)
        assertNull dynamicFieldService.getDynamicField(field.id)
    }

    void testHideDynamicField() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.saveDynamicField(field)
        assertTrue dynamicFieldService.hideDynamicField(field)

        def found = dynamicFieldService.getDynamicField(field.id)
        assertEquals DynamicFieldStatus.Hidden, found.status
    }

    void assertEquals(DynamicField left, DynamicField right) {
        assertNotNull right
        assertEquals left.id, right.id
        assertEquals left.name, right.name
        assertEquals left.sequence, right.sequence
        assertEquals left.type, right.type
        assertEquals left.status, right.status
        assertEquals left.label, right.label
        assertEquals left.mandatory, right.mandatory
        assertEquals left.owner.id, right.owner.id
        assertEquals left.max, right.max
        assertEquals left.min, right.min
        assertEquals left.maxlength, right.maxlength
    }

    void assertEquals(DynamicFieldItem left, DynamicFieldItem right) {
        assertNotNull right
        assertEquals left.id, right.id
        assertEquals left.name, right.name
        assertEquals left.field.id, right.field.id
    }

    DynamicField createDynamicField(def id) {
        def dynamicField = new DynamicField()

        dynamicField.name = 'dynamicField' + id
        dynamicField.type = DynamicFieldType.String
        dynamicField.status = DynamicFieldStatus.Active
        dynamicField.sequence = 0
        dynamicField.label = 'Dynaic Field Label'
        dynamicField.mandatory = true
        dynamicField.owner = member
        dynamicField.max = 10
        dynamicField.min = -5
        dynamicField.maxlength = 120

        return dynamicField
    }

    DynamicFieldItem createDynamicFieldItem(DynamicField dynamicField) {
        return new DynamicFieldItem(field: dynamicField, name: 'Item')
    }
}
