package outbox.subscriber.field

/**
 * Dynamic Field can represent enumeration (e.g. SingleSelect).
 * This domain is used to store enum items.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicFieldItem implements Comparable<DynamicFieldItem> {

    Long id
    DynamicField field
    String name

    static mapping = {
        table 'DynamicFieldItem'

        id column: 'DynamicFieldItemId'
        field column: 'DynamicFieldId'
        name column: 'Name'

        version false
        cache true
        sort 'name'
    }

    static constraints = {
        field nullable: false
        name nullable: false, blank: false, maxSize: 500
    }

    int compareTo(DynamicFieldItem other) {
        if (!other) return 1

        int result = name <=> other.name
        if (result == 0) {
            result = id <=> other.id
        }
        return result
    }


}
