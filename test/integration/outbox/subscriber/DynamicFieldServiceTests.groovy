package outbox.subscriber

import grails.test.GrailsUnitTestCase
import org.hibernate.Session
import outbox.member.Member
import outbox.task.TaskService
import outbox.subscriber.field.*

/**
 * {@link outbox.subscriber.DynamicFieldService} tests.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldServiceTests extends GrailsUnitTestCase {

    Session session
    DynamicFieldService dynamicFieldService
    SubscriberService subscriberService

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
        field3.status = DynamicFieldStatus.Removed

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)
        assertTrue dynamicFieldService.addDynamicField(field3)

        def fields = dynamicFieldService.getDynamicFields(member)
        assertEquals 2, fields.size()
        assertTrue fields.contains(field1)
        assertTrue fields.contains(field2)
        assertFalse fields.contains(field3)
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
        item3.removed = true
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])

        def found = dynamicFieldService.getDynamicFieldItems(field)

        assertEquals 2, found.size()
        assertTrue found.contains(item1)
        assertTrue found.contains(item2)
        assertFalse found.contains(item3)
    }

    void testGetDynamicFieldItems_NotSequence() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.String
        assertTrue dynamicFieldService.saveDynamicField(field)

        def found = dynamicFieldService.getDynamicFieldItems(field)
        assertEquals 0, found.size()
    }

    void testUpdateDynamicFieldItems_Update() {
        def originalTaskService = dynamicFieldService.taskService
        try {
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

            def taskServiceControl = mockFor(TaskService)
            taskServiceControl.demand.enqueueTask(2..2) { task ->
                assertEquals 'RemoveDynamicFieldItem', task.name
                return true
            }
            dynamicFieldService.taskService = taskServiceControl.createMock()
            assertTrue dynamicFieldService.updateDynamicFieldItems(field, [item1, item4, item5])
            taskServiceControl.verify()

            def found = dynamicFieldService.getDynamicFieldItems(field)

            assertEquals 3, found.size()
            assertTrue found.contains(item1)
            assertTrue found.contains(item4)
            assertTrue found.contains(item5)

            assertEquals item1.name, dynamicFieldService.getDynamicFieldItem(item1.id).name
        }
        finally {
            dynamicFieldService.taskService = originalTaskService
        }
    }

    void testUpdateDynamicFieldItems_Cleanup() {
        def originalTaskService = dynamicFieldService.taskService
        try {
            def field = createDynamicField(1)
            field.type = DynamicFieldType.SingleSelect
            assertTrue dynamicFieldService.saveDynamicField(field)

            def item1 = createDynamicFieldItem(field)
            def item2 = createDynamicFieldItem(field)
            def item3 = createDynamicFieldItem(field)

            assertTrue dynamicFieldService.addDynamicFieldItems(field, [item1, item2, item3])
            assertEquals 3, dynamicFieldService.getDynamicFieldItems(field).size()

            def taskServiceControl = mockFor(TaskService)
            taskServiceControl.demand.enqueueTask(3..3) { task ->
                assertEquals 'RemoveDynamicFieldItem', task.name
                return true
            }
            dynamicFieldService.taskService = taskServiceControl.createMock()
            assertTrue dynamicFieldService.updateDynamicFieldItems(field, [])
            taskServiceControl.verify()

            assertEquals 0, dynamicFieldService.getDynamicFieldItems(field).size()
        }
        finally {
            dynamicFieldService.taskService = originalTaskService
        }
    }

    void testDeleteDynamicFieldItem() {
        def originalTaskService = dynamicFieldService.taskService
        try {
            def field = createDynamicField(1)
            field.type = DynamicFieldType.SingleSelect
            assertTrue dynamicFieldService.saveDynamicField(field)

            def item = createDynamicFieldItem(field)
            assertTrue dynamicFieldService.addDynamicFieldItems(field, [item])

            def taskServiceControl = mockFor(TaskService)
            taskServiceControl.demand.enqueueTask { task ->
                assertEquals 'RemoveDynamicFieldItem', task.name
                assertEquals item.id, task.params.dynamicFieldItemId
                return true
            }
            dynamicFieldService.taskService = taskServiceControl.createMock()
            assertTrue dynamicFieldService.deleteDynamicFieldItem(item)
            taskServiceControl.verify()

            def found = dynamicFieldService.getDynamicFieldItem(item.id)
            assertNotNull found
            assertTrue found.removed
        }
        finally {
            dynamicFieldService.taskService = originalTaskService
        }
    }

    void testDeleteRemovedDynamicFieldItem() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item = createDynamicFieldItem(field)
        item.removed = true
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item])
        assertTrue dynamicFieldService.deleteRemovedDynamicFieldItem(item)
    }

    void testDeleteRemovedDynamicFieldItem_NotRemoved() {
        def field = createDynamicField(1)
        field.type = DynamicFieldType.SingleSelect
        assertTrue dynamicFieldService.saveDynamicField(field)

        def item = createDynamicFieldItem(field)
        item.removed = false
        assertTrue dynamicFieldService.addDynamicFieldItems(field, [item])
        assertFalse dynamicFieldService.deleteRemovedDynamicFieldItem(item)
    }

    void testDeleteRemovedDynamicFieldItem_Null() {
        assertFalse dynamicFieldService.deleteRemovedDynamicFieldItem(null)
    }

    void testDeleteDynamicField() {
        def originalTaskService = dynamicFieldService.taskService
        try {
            def field = createDynamicField(1)
            assertTrue dynamicFieldService.saveDynamicField(field)

            def taskServiceControl = mockFor(TaskService)
            taskServiceControl.demand.enqueueTask { task ->
                assertEquals 'RemoveDynamicField', task.name
                assertEquals field.id, task.params.dynamicFieldId
                return true
            }
            dynamicFieldService.taskService = taskServiceControl.createMock()
            assertTrue dynamicFieldService.deleteDynamicField(field)
            taskServiceControl.verify()

            def found = dynamicFieldService.getDynamicField(field.id)
            assertNotNull found
            assertEquals DynamicFieldStatus.Removed, found.status
        }
        finally {
            dynamicFieldService.taskService = originalTaskService
        }
    }

    void testDeleteRemovedDynamicField() {
        def field = createDynamicField(1)
        field.status = DynamicFieldStatus.Removed
        assertTrue dynamicFieldService.saveDynamicField(field)
        assertTrue dynamicFieldService.deleteRemovedDynamicField(field)
        assertNull dynamicFieldService.getDynamicField(field.id)
    }

    void testDeleteRemovedDynamicField_NotRemoved() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.saveDynamicField(field)
        assertFalse dynamicFieldService.deleteRemovedDynamicField(field)
        assertNotNull dynamicFieldService.getDynamicField(field.id)
    }

    void testDeleteRemovedDynamicField_Null() {
        assertFalse dynamicFieldService.deleteRemovedDynamicField(null)
    }

    void testHideDynamicField() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.saveDynamicField(field)
        assertTrue dynamicFieldService.hideDynamicField(field)

        def found = dynamicFieldService.getDynamicField(field.id)
        assertEquals DynamicFieldStatus.Hidden, found.status
    }

    void testAddDynamicFieldValue() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.addDynamicField(field)

        def subscriber = createTestSubscriber()
        assertTrue subscriberService.saveSubscriber(subscriber)

        def value = new DynamicFieldValue()
        value.subscriber = subscriber
        value.dynamicField = field
        value.value = '123'

        assertTrue dynamicFieldService.saveDynamicFieldValue(value)
        def found = dynamicFieldService.getDynamicFieldValue(value.id)

        assertNotNull found
        assertEquals value.subscriber.id, found.subscriber.id
        assertEquals value.dynamicField.id, found.dynamicField.id
        assertEquals value.value, found.value
    }

    void testGetDynamicFieldValues() {
        def field1 = createDynamicField(1)
        def field2 = createDynamicField(2)
        def field3 = createDynamicField(3)
        def field4 = createDynamicField(4)

        assertTrue dynamicFieldService.addDynamicField(field1)
        assertTrue dynamicFieldService.addDynamicField(field2)
        assertTrue dynamicFieldService.addDynamicField(field3)
        assertTrue dynamicFieldService.addDynamicField(field4)

        def subscriber = createTestSubscriber()
        assertTrue subscriberService.saveSubscriber(subscriber)

        def value1 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field1, value: '123')
        def value2 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field2, value: '123')
        def value3 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field3, value: '123')
        def value4 = new DynamicFieldValue(subscriber: subscriber, dynamicField: field4, value: '123')

        assertTrue dynamicFieldService.saveDynamicFieldValue(value1)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value2)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value3)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value4)

        def found = dynamicFieldService.getDynamicFieldValues(subscriber)

        assertEquals 4, found.size()
        assertTrue found.contains(value1)
        assertTrue found.contains(value2)
        assertTrue found.contains(value3)
        assertTrue found.contains(value4)

        found = dynamicFieldService.getDynamicFieldValues(null)
        assertEquals 0, found.size()

        found = dynamicFieldService.getDynamicFieldValues(new Subscriber())
        assertEquals 0, found.size()
    }

    void testRemoveDynamicFieldValues() {
        def field = createDynamicField(1)
        assertTrue dynamicFieldService.addDynamicField(field)

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)
        def subscriber3 = createTestSubscriber(3)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)
        assertTrue subscriberService.saveSubscriber(subscriber3)

        def value1 = new DynamicFieldValue(subscriber: subscriber1, dynamicField: field, value: '123')
        def value2 = new DynamicFieldValue(subscriber: subscriber2, dynamicField: field, value: '123')
        def value3 = new DynamicFieldValue(subscriber: subscriber3, dynamicField: field, value: '123')

        assertTrue dynamicFieldService.saveDynamicFieldValue(value1)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value2)
        assertTrue dynamicFieldService.saveDynamicFieldValue(value3)

        dynamicFieldService.deleteDynamicFieldValues(field)

        // assertNull dynamicFieldService.getDynamicFieldValue(value1.id)
        // assertNull dynamicFieldService.getDynamicFieldValue(value2.id)
        // assertNull dynamicFieldService.getDynamicFieldValue(value3.id)
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

    Subscriber createTestSubscriber(i) {
        Subscriber subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = "john.doe$i@nowhere.com"
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }
}
