package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
class UserAgentInfoTests extends GroovyTestCase {

    void testParse_Linux() {
        def userAgent = 'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.6) Gecko/20050614 Firefox/0.8'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'Linux', userAgentInfo.operatingSystem
        assertEquals 'Firefox', userAgentInfo.browser.name
        assertEquals '0.8', userAgentInfo.browser.version
    }

    void testParse_Ubuntu() {
        def userAgent = 'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.10) Gecko/20100915 Ubuntu/10.04 (lucid) Firefox/3.6.10'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'Ubuntu Linux', userAgentInfo.operatingSystem
    }

    void testParse_Windows() {
        def userAgent = 'Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'Windows Vista', userAgentInfo.operatingSystem
        assertEquals 'Safari', userAgentInfo.browser.name
        assertEquals '5.0.1', userAgentInfo.browser.version
    }

    void testParse_MacOS() {
        def userAgent = 'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en; rv:1.9.0.8pre) Gecko/2009022800 Camino/2.0b3pre'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'MacOS X 10.5', userAgentInfo.operatingSystem
    }

    void testParse_iPhone() {
        def userAgent = 'Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'iPhone', userAgentInfo.operatingSystem
    }

    void testParse_Wind7() {
        def userAgent = 'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.1 (KHTML, like Gecko) Iron/5.0.326.0 Chrome/5.0.326.0 Safari/533.1'
        def userAgentInfo = new UserAgentInfo(userAgent)

        userAgentInfo.parse()

        assertEquals 'Windows 7', userAgentInfo.operatingSystem
    }
}
