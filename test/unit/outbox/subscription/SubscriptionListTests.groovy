package outbox.subscription

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class SubscriptionListTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()
        def list = new SubscriptionList()
        list.id = 1
        list.name = 'SL Name'
        list.description = 'SL Description'
        list.subscribersNumber = 100
        list.owner = new Member(id: 2)
        list.dateCreated = date
        list.archived = true

        assertEquals 1, list.id
        assertEquals 'SL Name', list.name
        assertEquals 'SL Description', list.description
        assertEquals 100, list.subscribersNumber
        assertEquals 2, list.owner.id
        assertEquals date, list.dateCreated
        assertTrue list.archived
    }

    void testNonDuplicateName() {
        SubscriptionList list = new SubscriptionList(id: 1, name: 'name', owner: new Member(id: 1))
        mockDomain(SubscriptionList, [list])

        assertFalse SubscriptionList.duplicateName(list, list.name)
    }

    void testDuplicateEmail() {
        Member member = new Member(id: 1)
        SubscriptionList list1 = new SubscriptionList(id: 1, name: 'name', owner: member)
        SubscriptionList list2 = new SubscriptionList(id: 2, name: 'name', owner: member)

        mockDomain(SubscriptionList, [list1, list2])

        assertTrue SubscriptionList.duplicateName(list2, list2.name)
    }

    void testOwnedBy() {
        def list = new SubscriptionList(owner: null)

        assertFalse list.ownedBy(1)
        assertFalse list.ownedBy(null)

        list.owner = new Member(id: 2)
        assertFalse list.ownedBy(1)
        assertFalse list.ownedBy(null)
        assertTrue list.ownedBy(2)
    }

}
