package outbox.subscriber.search

import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import outbox.subscriber.field.DynamicField
import outbox.subscriber.field.DynamicFieldItem
import outbox.subscriber.field.DynamicFieldType
import outbox.subscription.SubscriptionList
import outbox.subscriber.search.condition.*

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
        assertEquals "AND Field 'Email' equals to 'test'", visitor.subscriberDescriptions.first()
    }

    void testSubscribeField_EqualWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal(''))
        visitor.visitSubscriberFieldCondition condition
        assertEquals "AND Field 'Email' is empty", visitor.subscriberDescriptions.first()
    }

    void testSubscribeField_NotEqualWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.notEqual(''))
        visitor.visitSubscriberFieldCondition condition
        assertEquals "AND Field 'Email' is filled", visitor.subscriberDescriptions.first()
    }

    void testSubscriberField_EmptyWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.empty())
        visitor.visitSubscriberFieldCondition condition
        assertEquals "AND Field 'Email' is empty", visitor.subscriberDescriptions.first()
    }

    void testSubscriberField_FilledWithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.filled())
        visitor.visitSubscriberFieldCondition condition
        assertEquals "AND Field 'Email' is filled", visitor.subscriberDescriptions.first()
    }

    void testSubscriberField_Invisible() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        condition.visible = false
        visitor.visitSubscriberFieldCondition condition
        assertTrue visitor.subscriberDescriptions.empty
    }

    void testDynamicField_String() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.String)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal('test'))
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' equals to 'test'", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_Number() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Number)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal(12.33))
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' equals to '12.33'", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_Boolean() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Boolean)
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal(true))
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' equals to 'True'", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_SingleSelect() {
        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.Boolean)
        def condition = new DynamicFieldCondition(dynamicField,
                ValueCondition.equal(new DynamicFieldItem(name: 'Item')))
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' equals to 'Item'", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_EmptyWithoutValue() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.empty())
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' is empty", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_FilledWithoutValue() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.filled())
        visitor.visitDynamicFieldCondition condition
        assertEquals "AND Field 'Test' is filled", visitor.dynamicFieldDescriptions.first()
    }

    void testDynamicField_Invisible() {
        def dynamicField = new DynamicField(label: 'Test')
        def condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal('test'))
        condition.visible = false
        visitor.visitDynamicFieldCondition condition
        assertTrue visitor.dynamicFieldDescriptions.empty
    }

    void testSubscriptionField_SubscribedTo() {
        def condition = SubscriptionCondition.subscribed(new SubscriptionList(name: 'Test'))
        visitor.visitSubscriptionCondition condition
        assertEquals "AND Subscribed to List 'Test'", visitor.subscriptionDescriptions.first()
    }

    void testSubscriptionField_NotSubscribedTo() {
        def condition = SubscriptionCondition.notSubscribed(new SubscriptionList(name: 'Test'))
        visitor.visitSubscriptionCondition condition
        assertEquals "AND Not Subscribed to List 'Test'", visitor.subscriptionDescriptions.first()
    }

    void testSubscriptionField_Invisible() {
        def condition = SubscriptionCondition.subscribed(new SubscriptionList())
        condition.visible = false
        visitor.visitSubscriptionCondition condition
        assertTrue visitor.subscriptionDescriptions.empty
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

    void testConcatenation() {
        def condition = new SubscriberFieldCondition('test', ValueCondition.equal('test'))

        condition.concatenation = Concatenation.And
        assertEquals 'AND', visitor.concatenation(condition)

        condition.concatenation = Concatenation.Or
        assertEquals 'OR', visitor.concatenation(condition)

        condition.concatenation = Concatenation.AndNot
        assertEquals 'AND NOT', visitor.concatenation(condition)

        condition.concatenation = Concatenation.OrNot
        assertEquals 'OR NOT', visitor.concatenation(condition)
    }

    void testMultipleConditions() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        visitor.visitSubscriberFieldCondition condition

        def dynamicField = new DynamicField(label: 'Test', type: DynamicFieldType.String)
        condition = new DynamicFieldCondition(dynamicField, ValueCondition.equal('test'))
        visitor.visitDynamicFieldCondition condition

        condition = SubscriptionCondition.subscribed(new SubscriptionList(name: 'Test'))
        visitor.visitSubscriptionCondition condition

        def text = "Field 'Email' equals to 'test' AND Field 'Test' equals to 'test' AND Subscribed to List 'Test'"
        assertEquals text, visitor.readableString
    }
}
