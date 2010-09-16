package outbox.template

import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class Template implements Comparable<Template> {

    Long id
    String name
    String description
    String templateBody
    Date dateCreated
    Date lastUpdated

    Member owner

    static mapping = {
        table 'Template'
        id column: 'TemplateId'
        columns {
            name column: 'Name'
            description column: 'Description'
            templateBody column: 'TemplateBody'
            dateCreated column: 'CreateDate'
            lastUpdated column: 'ModifyDate'
            owner column: 'MemberId'
        }
        version false
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 200
        description nullable: true, blank: true, maxSize: 4000
        templateBody nullable: false, blank: false, maxSize: 10 * 1024 * 1024
        dateCreated nullable: true
        lastUpdated nullable: true
        owner nullable: false
    }

    boolean ownedBy(Long memberId) {
        if (memberId) {
            return (owner != null && owner.id == memberId)
        }
        return false
    }

    int compareTo(Template other) {
        if (!other) {
            return 1
        }

        if (dateCreated && other.dateCreated) {
            return dateCreated.compareTo(other.dateCreated)
        }
        else if (dateCreated) {
            return 1
        }
        else if (other.dateCreated) {
            return -1
        }
        
        if (id && other.id) {
            return id.compareTo(other.id)
        }
        else if (!id && !other.id) {
            return 0
        }
        return id ? 1 : -1
    }


}
