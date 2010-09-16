package outbox.template

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class TemplateTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()

        Template template = new Template()
        template.id = 1
        template.name = 'Test Template Name'
        template.description = 'Test Template Description'
        template.templateBody = 'Test Template Body'
        template.owner = new Member(id: 2)
        template.dateCreated = date - 2
        template.lastUpdated = date

        assertEquals 1, template.id
        assertEquals 2, template.owner.id
        assertEquals 'Test Template Name', template.name
        assertEquals 'Test Template Description', template.description
        assertEquals 'Test Template Body', template.templateBody
        assertEquals date-2, template.dateCreated
        assertEquals date, template.lastUpdated
    }

    void testCompareTo() {
        def date = new Date()

        def template1 = new Template()
        def template2 = new Template()

        assertEquals(0, template1.compareTo(template2))
        assertEquals(0, template2.compareTo(template1))

        template1.dateCreated = date
        assertEquals(1, template1.compareTo(template2))
        assertEquals(-1, template2.compareTo(template1))

        template2.dateCreated = date
        assertEquals(0, template1.compareTo(template2))
        assertEquals(0, template2.compareTo(template1))

        template2.dateCreated = date + 2
        assertEquals(-1, template1.compareTo(template2))
        assertEquals(1, template2.compareTo(template1))
    }

    void testOwnedBy() {

        def template = new Template(owner: null)

        assertFalse template.ownedBy(1)
        assertFalse template.ownedBy(null)

        template.owner = new Member(id: 2)
        assertFalse template.ownedBy(1)
        assertFalse template.ownedBy(null)
        assertTrue template.ownedBy(2)
    }
}
