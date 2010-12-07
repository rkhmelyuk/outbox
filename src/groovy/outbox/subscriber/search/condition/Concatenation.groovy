package outbox.subscriber.search.condition

/**
 * The concatenations between conditions.
 *
 * @author Ruslan Khmelyuk
 */
public enum Concatenation {

    And('concatenation.and'),

    Or('concatenation.or'),

    AndNot('concatenation.and.not'),

    OrNot('concatenation.or.not')

    final String messageCode

    Concatenation(String messageCode) {
        this.messageCode = messageCode
    }
}