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
        name nullable: false, blank: false, maxSize: 200, validator: { val, obj ->
            if (duplicateName(obj, val)) {
                return 'template.name.unique'
            }
        }
        description nullable: true, blank: true, maxSize: 4000
        templateBody nullable: false, blank: false, maxSize: 10 * 1024 * 1024
        dateCreated nullable: true
        lastUpdated nullable: true
        owner nullable: false
    }

    /**
     * Checks whether this Template belongs to the user with specified id.
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
     * Check whether name is duplicate. We use member information from template parameter
     * and check name specified as second parameter.
     *
     * @param template the template, that should have this name.
     * @param name the new name for the template.
     * @return {@code true} if name is duplicate for this member, otherwise returns {@code false}.
     */
    static boolean duplicateName(Template template, String name) {
        def found = Template.findByOwnerAndName(template.owner, name)
        return (found && !found.id.equals(template.id))
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
