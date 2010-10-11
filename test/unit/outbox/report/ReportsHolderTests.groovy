package outbox.report

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class ReportsHolderTests extends GrailsUnitTestCase {

    def holder

    @Override protected void setUp() {
        super.setUp()
        holder = new ReportsHolder()
    }

    void testRegister() {

        def report1 = new Report(name: 'test1')
        def report2 = new Report(name: 'test2')

        holder.register report1
        holder.register report2

        assertEquals report1, holder.getReport('test1')
        assertEquals report2, holder.getReport('test2')
    }

    void testInit() {
        def reportsFactoryControl = mockFor(ReportsFactory, true)
        reportsFactoryControl.demand.totalClicksReport { -> return new Report(name: 'totalClicks') }
        reportsFactoryControl.demand.totalOpensReport { -> return new Report(name: 'totalOpens') }
        reportsFactoryControl.demand.clicksByDateReport { -> return new Report(name: 'clicksByDate') }
        reportsFactoryControl.demand.opensByDateReport { -> return new Report(name: 'opensByDate') }
        reportsFactoryControl.demand.totalSubscribersReport { -> return new Report(name: 'totalSubscribers') }
        reportsFactoryControl.demand.openedReport { -> return new Report(name: 'opened') }

        holder.reportsFactory = reportsFactoryControl.createMock()

        holder.init()

        reportsFactoryControl.verify()

        assertNotNull holder.getReport('totalClicks')
        assertNotNull holder.getReport('totalOpens')
        assertNotNull holder.getReport('clicksByDate')
        assertNotNull holder.getReport('opensByDate')
        assertNotNull holder.getReport('totalSubscribers')
        assertNotNull holder.getReport('opened')
    }

}
