package outbox.subscriber

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.AppConstant
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.NamePrefix
import outbox.dictionary.Timezone
import outbox.member.Member
import outbox.subscriber.search.Columns

/**
 * The subscriber for newsletter or used as recipient for emails.
 *
 * @author Ruslan Khmelyuk
 */
class Subscriber {

    String id

    String firstName
    String lastName
    String email

    boolean enabled

    NamePrefix namePrefix
    Gender gender
    Language language
    Timezone timezone
    SubscriberType subscriberType

    Member member

    Date dateCreated

    static mapping = {
        table 'Subscriber'
        id column: Columns.SubscriberId , generator: 'assigned'
        columns {
            firstName column: Columns.FirstName
            lastName column: Columns.LastName
            email column: Columns.Email
            gender column: Columns.GenderId, lazy: false
            language column: Columns.LanguageId, lazy: false
            timezone column: Columns.TimezoneId, lazy: false
            dateCreated column: Columns.CreateDate
            member column: Columns.MemberId, lazy: true
            enabled column: Columns.Enabled
            namePrefix column: Columns.NamePrefixId, lazy: false
            subscriberType column: Columns.SubscriberTypeId, lazy: false
        }
        version false
        cache true
    }

    static constraints = {
        id maxSize: 40
        firstName nullable: true, blank: true, maxSize: 100
        lastName nullable: true, blank: true, maxSize: 100
        email nullable: false, blank: false, maxSize: 512, email: true, validator : { val, obj ->
            if (Subscriber.duplicateEmail(obj, val)) {
                return 'subscriber.email.unique'
            }
        }
        member nullable: false
        gender nullable: true
        timezone nullable: true
        language nullable: true
        dateCreated nullable: true
        namePrefix nullable: true
        subscriberType nullable: true
    }

    static transients = ['fullName']

    def beforeInsert() {
        id = generateId()
    }

    /**
     * Generated id for this object.
     * Before generating id, both email and member must be set.
     * 
     * @return the generated id.
     */
    String generateId() {
        String string = email + '-' + member?.id + '-' + AppConstant.SUBSCRIBER_ID_SALT
        SHA1Codec.encode(string.bytes)
    }

    /**
     * Gets subscriber full name.
     * @return the subscriber full name.
     */
    String getFullName() {
        if (firstName && lastName) {
            return "$firstName $lastName"
        }
        else if (firstName) {
            return firstName
        }
        else if (lastName) {
            return lastName
        }
        return ''
    }

    /**
     * Check whether email is duplicate. We use member information from subscriber parameter
     * and check email specified as second parameter.
     *
     * @param subscriber the subscriber, that should have this email.
     * @param email the new email for subscriber.
     * @return {@code true} if email is duplicate, otherwise false.
     */
    static boolean duplicateEmail(Subscriber subscriber, String email) {
        def found = Subscriber.findByMemberAndEmail(subscriber.member, email)
        return (found && !found.id.equals(subscriber.id))
    }
}
