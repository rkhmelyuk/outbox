package outbox.subscriber.search.runner

import outbox.member.Member
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService
import outbox.subscriber.search.condition.Conditions
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.criteria.CriteriaVisitor
import outbox.subscriber.search.query.QueriesBuilder

/**
 * @author Ruslan Khmelyuk
 */
class SingleQueryRunnerTests extends GroovyTestCase {

    SingleQueryRunner singleQueryRunner
    SubscriberService subscriberService

    def member

    protected void setUp() {
        super.setUp();

        member = new Member(
                firstName: 'Test',
                lastName: 'Member',
                email: 'test+member@mailsight.com',
                username: 'username',
                password: 'password')

        member.save()
    }

    protected void tearDown() {
        member.delete()

        super.tearDown();
    }

    void testRun() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def conditions = new Conditions()
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.equal('John')))
        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 2, subscribers.total
    }

    void testRun_WithPagination() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def conditions = new Conditions()
        conditions.perPage = 1
        conditions.page = 2
        conditions.and(new SubscriberFieldCondition('FirstName', ValueCondition.equal('John')))
        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
    }

    Subscriber createTestSubscriber(id) {
        def subscriber = new Subscriber()
        subscriber.firstName = 'John'
        subscriber.lastName = 'Doe'
        subscriber.email = "john.doe_$id@nowhere.com"
        subscriber.gender = null
        subscriber.timezone = null
        subscriber.language = null
        subscriber.namePrefix = null
        subscriber.enabled = true
        subscriber.member = member
        return subscriber
    }
}
