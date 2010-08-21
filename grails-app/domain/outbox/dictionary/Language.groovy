package outbox.dictionary

/**
 * Languages dictionary.
 *
 * @author Ruslan Khmelyuk
 */
class Language {

    Integer id
    String code
    String name

    static mapping = {
        table 'Language'
        id column: 'LanguageId'
        columns {
            code column: 'LanguageCode'
            name column: 'LanguageName'
        }
        version false
        cache usage: 'read-only', include: 'non-lazy'
    }

    static constraints = {
        code maxSize: 2, nullable: false
        name maxSize: 50, nullable: false
    }
}
