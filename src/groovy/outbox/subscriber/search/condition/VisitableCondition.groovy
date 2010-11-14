package outbox.subscriber.search.condition

/**
 * Defines method used by visitors to fetch conditions.
 * 
 * @author Ruslan Khmelyuk
 */
public interface VisitableCondition {

    void visit(ConditionVisitor visitor)

}