package outbox.subscriber.search

/**
 * The enumeration of value condition types.
 * @author Ruslan Khmelyuk
 */
public enum ValueConditionType {

    Equal,

    NotEqual,

    Greater,

    Less,

    GreaterOrEqual,

    LessOrEqual,

    Like,

    InList,

    NotInList,

    Empty,

    Filled

}