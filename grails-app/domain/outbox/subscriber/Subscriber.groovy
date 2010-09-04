package outbox.subscriber

import org.codehaus.groovy.grails.plugins.codecs.SHA1Codec
import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone
import outbox.member.Member
import outbox.AppConstant

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

    Gender gender
    Language language
    Timezone timezone

    Member member

    // TODO - move to the SubscriberDetails entity
    Date createDate

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
            createDate column: 'CreateDate'
            member column: 'MemberId', lazy: true
            enabled column: 'Enabled'
        }
        version false
        cache true
    }

    static constraints = {
        id maxSize: 40
        firstName nullable: true, blank: true, maxSize: 100
        lastName nullable: true, blank: true, maxSize: 100
        email nullable: false, blank: false, maxSize: 512, email: true
        member nullable: false
        gender nullable: true
        timezone nullable: true
        language nullable: true
        createDate nullable: true
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
}
