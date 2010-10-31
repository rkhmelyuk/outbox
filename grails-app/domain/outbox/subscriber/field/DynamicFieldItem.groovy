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
    int sequence

    static mapping = {
        table 'DynamicFieldItem'

        id column: 'DynamicFieldItemId'
        field column: 'DynamicFieldId'
        name column: 'Name'
        sequence column: 'Sequence'

        version false
        cache true
        sort 'sequence'
    }

    static constraints = {
        field nullable: false
        name nullable: false, blank: false, maxSize: 500
    }

    int compareTo(DynamicFieldItem other) {
        if (!other) return 1
        sequence <=> other.sequence
    }


}
