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

    void testNonDuplicateName() {
        SubscribersList list = new SubscribersList(id: 1, name: 'name', owner: new Member(id: 1))
        mockDomain(SubscribersList, [list])

        assertFalse SubscribersList.duplicateName(list, list.name)
    }

    void testDuplicateEmail() {
        Member member = new Member(id: 1)
        SubscribersList list1 = new SubscribersList(id: 1, name: 'name', owner: member)
        SubscribersList list2 = new SubscribersList(id: 2, name: 'name', owner: member)

        mockDomain(SubscribersList, [list1, list2])

        assertTrue SubscribersList.duplicateName(list2, list2.name)
    }
}
