package outbox.subscriber.field

/**
 * @author Ruslan Khmelyuk
 */
class DynamicFieldValuesTests extends GroovyTestCase {

    void testGetFields() {
        def values = createTestFieldValues()
        assertEquals 3, values.fields.size()
    }

    void testGetValues() {
        def values = createTestFieldValues()
        assertEquals 2, values.values.size()
    }

    void testGetNames() {
        def values = createTestFieldValues()
        assertEquals(['test1', 'test2', 'test3'], values.names)
    }

    void testGetLabels() {
        def values = createTestFieldValues()
        assertEquals(['Test1', 'Test2', 'Test3'], values.labels)
    }

    void testAddValue() {
        def values = createTestFieldValues()

        assertFalse values.addValue(null)
        assertFalse values.addValue(new DynamicFieldValue())
        assertFalse values.addValue(new DynamicFieldValue(dynamicField: new DynamicField(id: 2)))
        assertTrue values.addValue(new DynamicFieldValue(dynamicField: new DynamicField(id: 3)))
        assertTrue values.hasValue(new DynamicField(id: 3))
    }

    void testGet() {
        def values = createTestFieldValues()

        assertEquals 1, values.get('test1').id
        assertEquals 2, values.get('test2').id
        assertNull values.get('test3')

        assertEquals 1, values.get(new DynamicField(id: 1)).id
        assertEquals 2, values.get(new DynamicField(id: 2)).id
        assertNull values.get(new DynamicField(id: 3))
    }

    void testValue() {
        def values = createTestFieldValues()

        assertEquals 'value1', values.value('test1')
        assertEquals 'value2', values.value('test2')
        assertNull values.get('test3')

        assertEquals 'value1', values.value(new DynamicField(id: 1))
        assertEquals 'value2', values.value(new DynamicField(id: 2))
        assertNull values.value(new DynamicField(id: 3))
    }

    void testHasValue() {
        def values = createTestFieldValues()

        assertTrue values.hasValue('test1')
        assertTrue values.hasValue('test2')
        assertFalse values.hasValue('test3')

        assertTrue values.hasValue(new DynamicField(id: 1))
        assertTrue values.hasValue(new DynamicField(id: 2))
        assertFalse values.hasValue(new DynamicField(id: 3))
    }

    DynamicFieldValues createTestFieldValues() {
        def field1 = new DynamicField(id: 1, name: 'test1', label: 'Test1')
        def field2 = new DynamicField(id: 2, name: 'test2', label: 'Test2')
        def field3 = new DynamicField(id: 3, name: 'test3', label: 'Test3')
        def fields = [field1, field2, field3]

        def value1 = new DynamicFieldValue(id: 1, dynamicField: field1, value: 'value1')
        def value2 = new DynamicFieldValue(id: 2, dynamicField: field2, value: 'value2')
        def values = [value1, value2]

        new DynamicFieldValues(fields, values)
    }
}
