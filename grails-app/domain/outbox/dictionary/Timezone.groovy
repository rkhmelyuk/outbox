package outbox.dictionary

/**
 * Timezones dictionary.
 *
 * @author Ruslan Khmelyuk
 */
class Timezone {

    static int DEFAULT_ID = 15

    Integer id
    String name
    float timeOffset

    static mapping = {
        table 'TimeZone'
        id column: 'TimeZoneId'
        columns {
            name column: 'Name'
            timeOffset column: 'TimeOffset'
        }
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }

    static constraints = {
        name maxSize: 400, nullable: false
    }
}
