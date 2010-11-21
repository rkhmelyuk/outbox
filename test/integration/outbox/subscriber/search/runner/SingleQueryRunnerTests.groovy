package outbox.subscriber.search.runner

import outbox.member.Member
import outbox.subscriber.Subscriber
import outbox.subscriber.SubscriberService
import outbox.subscriber.search.Conditions
import outbox.subscriber.search.CriteriaVisitor
import outbox.subscriber.search.Sort
import outbox.subscriber.search.condition.SubscriberFieldCondition
import outbox.subscriber.search.condition.SubscriptionCondition
import outbox.subscriber.search.condition.ValueCondition
import outbox.subscriber.search.query.QueriesBuilder
import outbox.subscription.Subscription
import outbox.subscription.SubscriptionList
import outbox.subscription.SubscriptionListService
import outbox.subscription.SubscriptionStatus

/**
 * @author Ruslan Khmelyuk
 */
class SingleQueryRunnerTests extends GroovyTestCase {

    SingleQueryRunner singleQueryRunner
    SubscriberService subscriberService
    SubscriptionListService subscriptionListService

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
        conditions.orderBy('SubscriberId', Sort.Desc)
        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 2, subscribers.total
        assertEquals 1, subscribers.list.size()
        assertTrue subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscription() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscriptionStatus = new SubscriptionStatus(id: 1, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = {
            return subscriptionStatus
        }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscriptionStatus)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: subscriptionStatus)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscriptionStatus)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.subscribed([sl1.id]))
        conditions.and(SubscriptionCondition.notSubscribed([sl2.id]))

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscription_SubscribedOnly() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscriptionStatus = new SubscriptionStatus(id: 1, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = {
            return subscriptionStatus
        }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscriptionStatus)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: subscriptionStatus)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscriptionStatus)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.subscribed([sl1.id, sl2.id]))

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber1)
    }

    void testSearchBySubscription_UnSubscribed() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscribed = new SubscriptionStatus(id: 1, name: 'test').save()
        def unsubscribed = new SubscriptionStatus(id: 2, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = { subscribed }
        SubscriptionStatus.metaClass.static.unsubscribed = { unsubscribed }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscribed)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: unsubscribed)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscribed)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.subscribed([sl1.id]))

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber1)
        assertFalse subscribers.list.contains(subscriber2)
    }

    void testSearchBySubscription_UnSubscribedFound() {

        def subscriber1 = createTestSubscriber(1)
        def subscriber2 = createTestSubscriber(2)

        assertTrue subscriberService.saveSubscriber(subscriber1)
        assertTrue subscriberService.saveSubscriber(subscriber2)

        def subscribed = new SubscriptionStatus(id: 1, name: 'test').save()
        def unsubscribed = new SubscriptionStatus(id: 2, name: 'test').save()

        SubscriptionStatus.metaClass.static.subscribed = { subscribed }
        SubscriptionStatus.metaClass.static.unsubscribed = { unsubscribed }

        def sl1 = createTestSubscriptionList(1)
        def sl2 = createTestSubscriptionList(2)
        assertTrue subscriptionListService.saveSubscriptionList(sl1)
        assertTrue subscriptionListService.saveSubscriptionList(sl2)

        def subscription11 = new Subscription(
                subscriber: subscriber1,
                subscriptionList: sl1,
                status: subscribed)

        def subscription21 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl1,
                status: unsubscribed)
        def subscription22 = new Subscription(
                subscriber: subscriber2,
                subscriptionList: sl2,
                status: subscribed)

        assertTrue subscriptionListService.addSubscription(subscription11)
        assertTrue subscriptionListService.addSubscription(subscription21)
        assertTrue subscriptionListService.addSubscription(subscription22)

        def conditions = new Conditions()
        conditions.and(SubscriptionCondition.notSubscribed([sl1.id]))

        def visitor = new CriteriaVisitor()
        conditions.visit(visitor)

        def queriesBuilder = new QueriesBuilder()
        def queries = queriesBuilder.build(conditions, visitor)

        def subscribers = singleQueryRunner.run(queries)

        assertNotNull subscribers
        assertEquals 1, subscribers.total
        assertTrue subscribers.list.contains(subscriber2)
        assertFalse subscribers.list.contains(subscriber1)
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

    SubscriptionList createTestSubscriptionList(id) {
        SubscriptionList subscriptionList = new SubscriptionList()
        subscriptionList.name = 'Subscribers list' + id
        subscriptionList.description = 'Subscribers list description'
        subscriptionList.owner = member
        subscriptionList.subscribersNumber = 100
        return subscriptionList
    }
}
