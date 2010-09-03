package outbox.subscriber

import outbox.dictionary.Gender
import outbox.dictionary.Language
import outbox.dictionary.Timezone

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

    Gender gender
    Language language
    Timezone timezone

    // TODO - move to the SubscriberDetails entity
    Date createDate

    static mapping = {
        table 'Subscriber'
        id column: 'SubscriberId'
        columns {
            firstName column: 'FirstName'
            lastName column: 'LastName'
            email column: 'Email'
            gender column: 'GenderId'
            language column: 'LanguageId'
            timezone column: 'TimezoneId'
            createDate column: 'CreateDate'
        }
        version false
        cache true
    }

    static constraints = {
        firstName nullable: true, blank: true, maxSize: 100
        lastName nullable: true, blank: true, maxSize: 100
        email nullable: false, blank: false, maxSize: 512, email: true
        gender nullabe: true
        timezone nullable: true
        language nullable: true
        createDate nullable: true
    }
}
