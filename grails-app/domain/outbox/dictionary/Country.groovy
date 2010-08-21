package outbox.dictionary

/**
 * Countries dictionary.
 *
 * @author Ruslan Khmelyuk
 */
class Country {

    Integer id

    String code
    String name

    static mapping = {
        table 'Country'
        id column: 'CountryId'
        columns {
            code column: 'CountryCode'
            name column: 'CountryName'
        }
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }

    static constraints = {
        code maxSize: 2, nullable: false
        name maxSize: 100, nullable: false
    }
}
