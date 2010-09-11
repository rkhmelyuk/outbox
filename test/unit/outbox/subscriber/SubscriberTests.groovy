package outbox.subscriber

import grails.test.GrailsUnitTestCase
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.NamePrefix
import outbox.dictionary.Timezone
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class SubscriberTests extends GrailsUnitTestCase {

    void testFields() {
        Subscriber subscriber = new Subscriber()

        subscriber.firstName = 'Test'
        subscriber.lastName = 'User'
        subscriber.email = 'test.user@mailsight.com'
        subscriber.gender = new Gender(id: 1)
        subscriber.language = new Language(id: 2)
        subscriber.timezone = new Timezone(id: 3)
        subscriber.namePrefix = new NamePrefix(id: 4)
        subscriber.subscriberType = new SubscriberType(id: 5)

        assertEquals 'Test', subscriber.firstName
        assertEquals 'User', subscriber.lastName
        assertEquals 'test.user@mailsight.com', subscriber.email

        assertNotNull subscriber.gender
        assertNotNull subscriber.language
        assertNotNull subscriber.timezone
        
        assertEquals 1, subscriber.gender.id
        assertEquals 2, subscriber.language.id
        assertEquals 3, subscriber.timezone.id
        assertEquals 4, subscriber.namePrefix.id
        assertEquals 5, subscriber.subscriberType.id
    }

    void testFullName() {
        Subscriber subscriber = new Subscriber(firstName: 'Test', lastName: 'User')
        assertEquals 'Test User', subscriber.fullName

        subscriber.firstName = null
        assertEquals 'User', subscriber.fullName

        subscriber.lastName = null
        assertEquals '', subscriber.fullName

        subscriber.firstName = 'Test'
        assertEquals 'Test', subscriber.fullName
    }

    void testNonDuplicateEmail() {
        Subscriber subscriber = new Subscriber(id: '000000', email: 'test@mailsight.com', member: new Member(id: 1))
        mockDomain(Subscriber, [subscriber])

        assertFalse Subscriber.duplicateEmail(subscriber, subscriber.email)
    }

    void testDuplicateEmail() {
        Member member = new Member(id: 1)
        Subscriber subscriber1 = new Subscriber(id: '0000000', email: 'test@mailsight.com', member: member)
        Subscriber subscriber2 = new Subscriber(id: '1111111', email: 'test@mailsight.com', member: member)

        mockDomain(Subscriber, [subscriber1])

        assertTrue Subscriber.duplicateEmail(subscriber2, subscriber2.email)
    }
}
