package outbox.campaign

import outbox.member.Member
import outbox.template.Template

/**
 * @author Ruslan Khmelyuk
 */
class Campaign implements Comparable<Campaign> {

    Long id
    CampaignState state

    String name
    String description

    String subject
    Template template

    Date dateCreated
    Date lastUpdated

    Date startDate
    Date endDate

    Member owner

    static mapping = {
        table 'Campaign'
        id column: 'CampaignId'
        columns {
            state column: 'CampaignState'
            name column: 'Name'
            description column: 'Description'
            subject column: 'Subject'
            template column: 'TemplateId', lazy: true
            dateCreated column: 'CreateDate'
            lastUpdated column: 'ModifyDate'
            startDate column: 'StartDate'
            endDate column: 'EndDate'
            owner column: 'MemberId'
        }
        version false
        cache true
    }

    static constraints = {
        state nullable: false
        name nullable: false, blank: false, maxSize: 500
        description nullable: true, blank: true, maxSize: 4000
        subject nullable: false, blank: false, maxSize: 1000
        dateCreated nullable: true
        lastUpdated nullable: true
        startDate nullable: true
        endDate nullable: true
        owner nullable: false
        template nullable: true
    }

    /**
     * Checks whether this Campaign belongs to the user with specified id.
     *
     * @param memberId the member id
     * @return {@code true} if belongs, otherwise {@code false}.
     */
    boolean ownedBy(Long memberId) {
        if (memberId) {
            return (owner != null && owner.id == memberId)
        }
        return false
    }

    /**
     * Used to order campaigns by create date and then by id.
     */
    int compareTo(Campaign other) {
        def value = 0
        if (dateCreated && other.dateCreated) {
            value = dateCreated.compareTo(other.dateCreated)
        }
        else if (dateCreated) {
            return 1
        }
        else if (other.dateCreated) {
            return -1
        }
        if (value == 0) {
            if (id && other.id) {
                value = id.compareTo(other.id)
            }
            else if (id) {
                return 1
            }
            else if (other.id) {
                return -1
            }
        }
        return value
    }

}
