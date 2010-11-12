package outbox.subscriber.search

/**
 * Defines method used by visitors to fetch conditions.
 * 
 * @author Ruslan Khmelyuk
 */
public interface VisitableCondition {

    void visit(ConditionVisitor visitor)

}