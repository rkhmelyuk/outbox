package outbox.dictionary

/**
 * The prefix for person name, ie Mr, Miss, Mrs etc.
 * 
 * @author Ruslan Khmelyuk
 */
class NamePrefix {

    Integer id
    String name

    static mapping = {
        table 'NamePrefix'
        id column: 'NamePrefixId'
        columns {
            name column: 'NamePrefixName'
        }
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }

    static constraints = {
        name nullable: false, blank: false, maxSize: 20
    }
}
