package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
class BrowserDetectorTests extends GroovyTestCase {

    void testDetect() {
        detect('Safari', '5.0.1', 'Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8')
        detect('IE', '9.0', 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; Media Center PC 6.0; InfoPath.3; MS-RTC LM 8; Zune 4.7)')
        detect('IE', '8.0', 'Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 1.1.4322)')
        detect('IE', '7.0', 'Mozilla/5.0 (compatible; MSIE 7.0; Windows NT 6.0; WOW64; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; c .NET CLR 3.0.04506; .NET CLR 3.5.30707; InfoPath.1; el-GR)')
        detect('Firefox', '4.0', 'Mozilla/5.0 (Windows; U; Windows NT 6.1; ru; rv:1.9.2.3) Gecko/20100401 Firefox/4.0 (.NET CLR 3.5.30729)')
        detect('Firefox', '3.6.4', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; cs; rv:1.9.2.4) Gecko/20100611 Firefox/3.6.4')
        detect('Chrome', '7.0.531.0', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.9 (KHTML, like Gecko) Chrome/7.0.531.0 Safari/534.9')
        detect('Konqueror', '4.3', 'Mozilla/5.0 (compatible; Konqueror/4.3; Linux 2.6.31-16-generic; X11) KHTML/4.3.2 (like Gecko)')
        detect('Opera', '9.99', 'Opera/9.99 (Windows NT 5.1; U; pl) Presto/9.9.9')
        detect('Camino', '2.0b3pre', 'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en; rv:1.9.0.8pre) Gecko/2009022800 Camino/2.0b3pre')
        detect('Lunascape', '6.2.1.22445', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.1.11) Gecko/20100723 Firefox/3.5.11 Lunascape/6.2.1.22445 ( .NET CLR 3.5.30729)')
        detect('Galeon', '2.0.6', 'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Galeon/2.0.6 (Ubuntu 2.0.6-2)')
        detect('Phoenix', '0.5', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.4a) Gecko/20030411 Phoenix/0.5')
        detect('Firebird', '0.7', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.6a) Gecko/20031002 Firebird/0.7')
        detect('Shiretoko', '3.5.6', 'Mozilla/5.0 (X11; U; Linux i686; ja; rv:1.9.1.6) Gecko/20091216 Shiretoko/3.5.6')
        detect('Iron', '5.0.326.0', 'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.1 (KHTML, like Gecko) Iron/5.0.326.0 Chrome/5.0.326.0 Safari/533.1')

    }

    void detect(name, version, userAgent) {
        def browser = BrowserDetector.detect(userAgent)
        assertEquals "Wrong browser name $name", name, browser.name
        assertEquals "Wrong browser version $version", version, browser.version
    }
}
