package outbox.subscriber.search.criteria

import outbox.subscriber.search.query.Query

/**
 * @author Ruslan Khmelyuk
 */
class SubqueryCriterion implements Criterion {

    SubqueryConditionType condition
    Query subquery

}
