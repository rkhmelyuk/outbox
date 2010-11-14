package outbox.subscriber.search.condition

/**
 * @author Ruslan Khmelyuk
 */
class ValueConditionTests extends GroovyTestCase {

    void testFields() {
        def condition = new ValueCondition('value', ValueConditionType.Equal)

        assertEquals 'value', condition.value
        assertEquals ValueConditionType.Equal, condition.type
    }

    void testLike() {
        def condition = ValueCondition.like('test like')
        assertEquals 'test like', condition.value
        assertEquals ValueConditionType.Like, condition.type
    }

    void testEqual() {
        def condition = ValueCondition.equal('test like')
        assertEquals 'test like', condition.value
        assertEquals ValueConditionType.Equal, condition.type
    }

    void testNotEqual() {
        def condition = ValueCondition.notEqual('test like')
        assertEquals 'test like', condition.value
        assertEquals ValueConditionType.NotEqual, condition.type
    }

    void testEmpty() {
        def condition = ValueCondition.empty()
        assertNull condition.value
        assertEquals ValueConditionType.Empty, condition.type
    }

    void testFilled() {
        def condition = ValueCondition.filled()
        assertNull condition.value
        assertEquals ValueConditionType.Filled, condition.type
    }

    void testGreater() {
        def condition = ValueCondition.greater(10)
        assertEquals 10, condition.value
        assertEquals ValueConditionType.Greater, condition.type
    }

    void testGreaterEqual() {
        def condition = ValueCondition.greaterEqual(10)
        assertEquals 10, condition.value
        assertEquals ValueConditionType.GreaterOrEqual, condition.type
    }

    void testLess() {
        def condition = ValueCondition.less(10)
        assertEquals 10, condition.value
        assertEquals ValueConditionType.Less, condition.type
    }

    void testLessEqual() {
        def condition = ValueCondition.lessEqual(10)
        assertEquals 10, condition.value
        assertEquals ValueConditionType.LessOrEqual, condition.type
    }

    void testInList() {
        def list = [10, 20]
        def condition = ValueCondition.inList(list)
        assertEquals list, condition.value
        assertEquals ValueConditionType.InList, condition.type
    }

    void testNotInList() {
        def list = [10, 20]
        def condition = ValueCondition.notInList(list)
        assertEquals list, condition.value
        assertEquals ValueConditionType.NotInList, condition.type
    }
}
