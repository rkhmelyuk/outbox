package outbox.campaign

import grails.test.GrailsUnitTestCase
import outbox.member.Member
import outbox.template.Template

/**
 * @author Ruslan Khmelyuk
 */
class CampaignTests extends GrailsUnitTestCase {

    void testFields() {
        def date = new Date()
        def campaign = new Campaign()
        campaign.id = 1
        campaign.owner = new Member(id: 2)
        campaign.template = new Template(id: 3)
        campaign.state = CampaignState.Ready
        campaign.name = 'Test Name'
        campaign.description = 'Test Description'
        campaign.subject = 'Test Subject'
        campaign.dateCreated = date - 10
        campaign.lastUpdated = date - 5
        campaign.startDate = date - 6
        campaign.endDate = date + 10

        assertEquals 1, campaign.id
        assertEquals 2, campaign.owner.id
        assertEquals 3, campaign.template.id
        assertEquals CampaignState.Ready, campaign.state
        assertEquals 'Test Name', campaign.name
        assertEquals 'Test Description', campaign.description
        assertEquals 'Test Subject', campaign.subject
        assertEquals date - 10, campaign.dateCreated
        assertEquals date - 5, campaign.lastUpdated
        assertEquals date - 6, campaign.startDate
        assertEquals date + 10, campaign.endDate
    }

    void testSorting() {
        def date = new Date()
        def campaign1 = new Campaign()
        def campaign2 = new Campaign()
        
        assertEquals(0, campaign1.compareTo(campaign2))
        assertEquals(0, campaign2.compareTo(campaign1))

        campaign1.id = 10
        campaign2.id = null
        assertEquals(1, campaign1.compareTo(campaign2))
        assertEquals(-1, campaign2.compareTo(campaign1))

        campaign1.id = 10
        campaign2.id = 20
        assertEquals(-1, campaign1.compareTo(campaign2))
        assertEquals(1, campaign2.compareTo(campaign1))

        campaign1.dateCreated = date
        campaign2.dateCreated = null
        assertEquals(1, campaign1.compareTo(campaign2))
        assertEquals(-1, campaign2.compareTo(campaign1))

        campaign1.dateCreated = date
        campaign2.dateCreated = date + 1
        assertEquals(-1, campaign1.compareTo(campaign2))
        assertEquals(1, campaign2.compareTo(campaign1))

        campaign1.dateCreated = date
        campaign2.dateCreated = date
        campaign1.id = null
        campaign2.id = null
        assertEquals(0, campaign1.compareTo(campaign2))
        assertEquals(0, campaign2.compareTo(campaign1))
    }
}
