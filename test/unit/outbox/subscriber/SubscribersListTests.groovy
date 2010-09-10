package outbox.subscriber

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class SubscribersListTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()
        def list = new SubscribersList()
        list.id = 1
        list.name = 'SL Name'
        list.description = 'SL Description'
        list.subscribersNumber = 100
        list.owner = new Member(id: 2)
        list.dateCreated = date

        assertEquals 1, list.id
        assertEquals 'SL Name', list.name
        assertEquals 'SL Description', list.description
        assertEquals 100, list.subscribersNumber
        assertEquals 2, list.owner.id
        assertEquals date, list.dateCreated
    }
}
