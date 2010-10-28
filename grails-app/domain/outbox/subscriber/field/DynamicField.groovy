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
        name  nullable: false, blank: false, maxSize: 200
        label nullable: false, blank: false, maxSize: 200
        type nullable: false
        min nullable: true
        max  nullable: true
        maxlength nullable: true, min: 0, max: 4000
    }

    /**
     * Checks whether this Campaign belongs to the user with specified id.
     *
     * @param memberId the member id
     * @return {@code true} if belongs, otherwise {@code false}.
     */
    boolean ownedBy(Long memberId) {
        if (memberId) {
            return (owner != null && owner.id == memberId)
        }
        return false
    }

    int compareTo(DynamicField other) {
        if (!other) {
            return 1
        }

        sequence <=> other.sequence
    }

}
