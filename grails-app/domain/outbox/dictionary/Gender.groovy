package outbox.dictionary

/**
 * Genders dictionary.
 *
 * @author Ruslan Khmelyuk
 */
class Gender {

    Integer id
    String name

    static constraints = {
        name blank: false, nullable: false
    }

    static mapping = {
        table 'Gender'
        id column: 'GenderId'
        columns {
            name column: 'GenderName'
        }
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }
}
