package outbox.template

import grails.test.GrailsUnitTestCase
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class TemplateServiceTests extends GrailsUnitTestCase {

    TemplateService templateService

    def member

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()
    }

    protected void tearDown() {
        member.delete()

        super.tearDown();
    }

    void testAddTemplate() {
        def template = createTestTemplate()

        assertTrue templateService.addTemplate(template)

        def found = templateService.getTemplate(template.id)
        assertEquals template, found
    }

    void testSaveTemplate() {
        def template = createTestTemplate()

        assertTrue templateService.addTemplate(template)
        def found = templateService.getTemplate(template.id)
        found.name = 'Test Test Name'
        assertTrue templateService.saveTemplate(found)
        def found2 = templateService.getTemplate(template.id)
        assertEquals found, found2
    }

    void testDeleteTemplate() {
        def template = createTestTemplate()
        assertTrue templateService.addTemplate(template)

        templateService.deleteTemplate template
        assertNull templateService.getTemplate(template.id)
    }

    void testGetMemberTemplates() {
        def template1 = createTestTemplate()
        def template2 = createTestTemplate()
        template2.name = template2.name + '2'
        assertTrue templateService.addTemplate(template1)
        assertTrue templateService.addTemplate(template2)

        def templates = templateService.getMemberTemplates(member, 1, 10)
        assertEquals 2, templates.size()
    }

    void assertEquals(Template left, Template right) {
        assertEquals left.id, right.id
        assertEquals left.name, right.name
        assertEquals left.description, right.description
        assertEquals left.templateBody, right.templateBody
        assertEquals left.owner.id, right.owner.id
    }

    Template createTestTemplate() {
        Template template = new Template()
        template.name = 'Test Name'
        template.description = 'Test Description'
        template.templateBody = 'Test Template Body'
        template.owner = member
        return template
    }
}
