package outbox.subscriber.field

import outbox.member.Member

/**
 * The information about dynamic field.
 *
 * @author Ruslan Khmelyuk
 */
class DynamicField implements Comparable<DynamicField> {

    Long id
    Member owner

    // General
    String name
    String label
    DynamicFieldType type
    DynamicFieldStatus status
    int sequence

    // Constraints
    boolean mandatory
    Integer min, max
    Integer maxlength

    static mapping = {
        table 'DynamicField'

        id column: 'DynamicFieldId'
        owner column: 'MemberId'
        name column: 'Name'
        label column: 'Label'
        type column: 'Type'
        status column: 'Status'
        sequence column: 'Sequence'
        mandatory column: 'Mandatory'
        min column: 'MinValue'
        max column: 'MaxValue'
        maxlength column: 'MaxLength'

        version false
        cache true
        sort 'sequence'
    }

    static constraints = {
        owner nullable: false
        name  nullable: false, blank: false, maxSize: 200, validator: { val, obj ->
            if (DynamicField.duplicateName(obj, val)) {
                return 'dynamicField.name.unique'
            }
        }
        label nullable: false, blank: false, maxSize: 200
        status nullable: false
        type nullable: false
        max nullable: true
        min nullable: true, validator: { val, obj ->
            if (val != null && obj.max != null && obj.max <= val) {
                return 'dynamicField.min.larger.max'
            }
        }
        maxlength nullable: true, min: 0, max: 9999
    }

    /**
     * Checks whether this Campaign belongs to the user with specified id.
     *
     * @param memberId the member id
     * @return {@code true} if belongs, otherwise {@code false}.
     */
    boolean ownedBy(Long memberId) {
        memberId && owner?.id == memberId
    }

    /**
     * Check whether name is duplicate. We use member information from dynamic field parameter value
     * and check name specified as second parameter.
     *
     * @param field the dynamic field, that should have this name.
     * @param name the new name for the dynamic field.
     * @return {@code true} if name is duplicate for this member, otherwise returns {@code false}.
     */
    static boolean duplicateName(DynamicField field, String name) {
        def found = DynamicField.findByOwnerAndName(field.owner, name)
        return (found && !found.id.equals(field.id))
    }

    int compareTo(DynamicField other) {
        if (!other) {
            return 1
        }

        sequence <=> other.sequence
    }

}
