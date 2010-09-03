package outbox.subscriber

import grails.test.*
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone

class SubscriberTests extends GrailsUnitTestCase {

    void testFields() {
        Subscriber subscriber = new Subscriber()

        subscriber.firstName = 'Test'
        subscriber.lastName = 'User'
        subscriber.email = 'test.user@mailsight.com'
        subscriber.gender = new Gender(id: 1)
        subscriber.language = new Language(id: 2)
        subscriber.timezone = new Timezone(id: 3)

        assertEquals 'Test', subscriber.firstName
        assertEquals 'User', subscriber.lastName
        assertEquals 'test.user@mailsight.com', subscriber.email

        assertNotNull subscriber.gender
        assertNotNull subscriber.language
        assertNotNull subscriber.timezone
        
        assertEquals 1, subscriber.gender.id
        assertEquals 2, subscriber.language.id
        assertEquals 3, subscriber.timezone.id
    }
}
