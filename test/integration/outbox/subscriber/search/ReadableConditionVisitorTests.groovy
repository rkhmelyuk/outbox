package outbox.subscriber.search

import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-01
 */
class ReadableConditionVisitorTests extends GroovyTestCase {

    def visitor = new ReadableConditionVisitor()

    @Override protected void setUp() {
        def request = new MockHttpServletRequest()
        def response = new MockHttpServletResponse()
        request.addPreferredLocale Locale.ENGLISH

        SecurityRequestHolder.set request, response
    }

    void testSubscribeFieldr_WithValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        visitor.visitSubscriberFieldCondition condition
        assertEquals "Subscriber field 'Email' equals to 'test'", visitor.subscriberDescription.toString()
    }

    void testSubscriberField_WithoutValue() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.empty())
        visitor.visitSubscriberFieldCondition condition
        assertEquals "Subscriber field 'Email' is empty", visitor.subscriberDescription.toString()
    }

    void testSubscriberField_Invisible() {
        def condition = new SubscriberFieldCondition(Names.Email, ValueCondition.equal('test'))
        condition.visible = false
        visitor.visitSubscriberFieldCondition condition
        assertEquals '', visitor.subscriberDescription.toString()
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
