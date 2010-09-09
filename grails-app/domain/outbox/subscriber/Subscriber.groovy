package outbox.subscriber

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.AppConstant
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.NamePrefix
import outbox.dictionary.Timezone
import outbox.member.Member

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

    // TODO - move to the SubscriberDetails entity
    Date dateCreated

    static mapping = {
        table 'Subscriber'
        id column: 'SubscriberId', generator: 'assigned'
        columns {
            firstName column: 'FirstName'
            lastName column: 'LastName'
            email column: 'Email'
            gender column: 'GenderId'
            language column: 'LanguageId'
            timezone column: 'TimezoneId'
            dateCreated column: 'CreateDate'
            member column: 'MemberId', lazy: true
            enabled column: 'Enabled'
            namePrefix column: 'NamePrefixId'
            subscriberType column: 'SubscriberTypeId'
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
        if (!id) {
            generateId()
        }
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
    
    def generateId() {
        String string = email + '-' + member?.id + '-' + AppConstant.SUBSCRIBER_ID_SALT
        id = SHA1Codec.encode(string.bytes)
    }

    /**
     * Check whether email is duplicate. We use member information from subscriber parameter
     * and check email specified by second parameter.
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
