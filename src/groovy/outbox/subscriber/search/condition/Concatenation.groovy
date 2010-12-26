package outbox.subscriber.search.condition

/**
 * The concatenations between conditions.
 *
 * @author Ruslan Khmelyuk
 */
public enum Concatenation {

    And(1, 'concatenation.and'),

    Or(2, 'concatenation.or'),

    AndNot(3, 'concatenation.and.not'),

    OrNot(4, 'concatenation.or.not')

    final int id
    final String messageCode

    Concatenation(int id, String messageCode) {
        this.id = id
        this.messageCode = messageCode
    }

    static Concatenation getById(Integer id) {
        if (id != null) {
            for (each in Concatenation) {
                if (each.id == id) {
                    return each
                }
            }
        }
        return null
    }
}