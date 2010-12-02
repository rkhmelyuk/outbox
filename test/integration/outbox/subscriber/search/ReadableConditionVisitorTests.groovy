package outbox.subscriber.search

import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.condition.DynamicFieldCondition
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldType
import grails.test.GrailsUnitTestCase
import outbox.subscriber.field.DynamicFieldItem

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-01
 */
class ReadableConditionVisitorTests extends GrailsUnitTestCase {

    def visitor = new ReadableConditionVisitor()

    @Override protected void setUp() {
        def request = new MockHttpServletRequest()
        def response = new MockHttpServletResponse()
        request.addPreferredLocale Locale.ENGLISH

        SecurityRequestHolder.set request, response
    }

    void testSubscribeField_WithValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        visitor.visitSubscriberFieldCondition condition
        assertEquals "Field 'Email' equals to 'test'", visitor.subscriberDescription.toString()
    }

    void testSubscriberField_EmptyWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.empty())
        visitor.visitSubscriberFieldCondition condition
        assertEquals "Field 'Email' is empty", visitor.subscriberDescription.toString()
    }

    void testSubscriberField_FilledWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.filled())
        visitor.visitSubscriberFieldCondition condition
        assertEquals "Field 'Email' is filled", visitor.subscriberDescription.toString()
    }

    void testSubscriberField_Invisible() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        condition.visible = false
        visitor.visitSubscriberFieldCondition condition
        assertEquals '', visitor.subscriberDescription.toString()
    }

    void testDynamicField_String() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.String)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal('test'))
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' equals to 'test'", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_Number() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Number)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal(12.33))
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' equals to '12.33'", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_Boolean() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Boolean)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal(true))
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' equals to 'True'", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_SingleSelect() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Boolean)
        def condition = new DynamicFieldCondition(dynamicField,
                ValueCondition.equal(new DynamicFieldItem(name: 'Item')))
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' equals to 'Item'", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_EmptyWithoutValue() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.empty())
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' is empty", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_FilledWithoutValue() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.filled())
        visitor.visitDynamicFieldCondition condition
        assertEquals "Field 'Test' is filled", visitor.dynamicFieldDescription.toString()
    }

    void testDynamicField_Invisible() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal('test'))
        condition.visible = false
        visitor.visitDynamicFieldCondition condition
        assertEquals '', visitor.dynamicFieldDescription.toString()
    }

    void testSubscriberFieldName() {
        assertEquals 'Email', visitor.subscriberFieldName(Names.Email)
        assertEquals 'First Name', visitor.subscriberFieldName(Names.FirstName)
        assertEquals 'Last Name', visitor.subscriberFieldName(Names.LastName)
        assertEquals 'Gender', visitor.subscriberFieldName(Names.GenderId)
        assertEquals 'Language', visitor.subscriberFieldName(Names.LanguageId)
        assertEquals 'Timezone', visitor.subscriberFieldName(Names.TimezoneId)
        assertEquals 'Subscriber Type', visitor.subscriberFieldName(Names.SubscriberTypeId)
    }

    void testValueName() {
        assertEquals "'test'", visitor.valueName(ValueCondition.equal('test'))
        assertEquals "'test'", visitor.valueName(ValueCondition.like('test'))
        assertEquals "[1, 2, 3]", visitor.valueName(ValueCondition.inList([1, 2, 3]))
        assertEquals "[a, b, c]", visitor.valueName(ValueCondition.notInList(['a', 'b', 'c']))
        assertEquals "", visitor.valueName(ValueCondition.empty())
        assertEquals "", visitor.valueName(ValueCondition.filled())
    }
}
