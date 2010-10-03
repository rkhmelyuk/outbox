package outbox.tracking;


import org.apache.log4j.Logger
import outbox.tracking.browser.Browser
import outbox.tracking.browser.BrowserDetector

/**
 * Useful tool to get browser and operating system information
 * from string that contains User-Agent header of HTTP request.
 *
 * NOTE:
 *          Tool is useful but it will require update as soon
 *          as new operating system or new version of existed
 *          one will be released!
 *
 * @see outbox.tracking.browser.Browser
 * @see outbox.tracking.browser.BrowserDetector
 *
 * @author Ruslan Khmelyuk
 */
class UserAgentInfo {

    static final Logger log = Logger.getLogger(UserAgentInfo.class)

    static final Map<String, String> OPERATING_SYSTEMS

    static {
        // Sequence is important, as some user agent headers may contain
        // combined tokens, for example, Mac OS X and iPhone. That's the reason why
        // we add iPhone before Mac OS X entry.

        // TODO - move to the configurable storage, maybe database

        OPERATING_SYSTEMS = [:] as LinkedHashMap
        OPERATING_SYSTEMS['Win3.11'] = 'Windows 3.11'
        OPERATING_SYSTEMS['WinNT3.51'] = 'Windows NT 3.11'
        OPERATING_SYSTEMS['WinNT4.0'] = 'Windows NT 4.0'
        OPERATING_SYSTEMS['Win95'] = 'Windows 95'
        OPERATING_SYSTEMS['Win98'] = 'Windows 98'
        OPERATING_SYSTEMS['Win 9x 4.90'] = 'Windows Me'
        OPERATING_SYSTEMS['Windows NT 5.0'] = 'Windows 2000'
        OPERATING_SYSTEMS['Windows NT 5.1'] = 'Windows XP'
        OPERATING_SYSTEMS['Windows NT 5.2'] = 'Windows Server 2003/Windows XP x64 Edition'
        OPERATING_SYSTEMS['Windows NT 6.0'] = 'Windows Vista'
        OPERATING_SYSTEMS['Windows NT 6.1'] = 'Windows 7'
        OPERATING_SYSTEMS['Win'] = 'Windows (version unknown)'

        OPERATING_SYSTEMS['WindowsCE'] = 'Windows CE'

        OPERATING_SYSTEMS['Ubuntu'] = 'Ubuntu Linux'
        OPERATING_SYSTEMS['Linux'] = 'Linux'
        OPERATING_SYSTEMS['FreeBSD'] = 'FreeBSD'
        OPERATING_SYSTEMS['OpenBSD'] = 'OpenBSD'
        OPERATING_SYSTEMS['NetBSD'] = 'NetBSD'
        OPERATING_SYSTEMS['SunOS'] = 'Solaris'
        OPERATING_SYSTEMS['Unix'] = 'Unix'
        OPERATING_SYSTEMS['BeOS'] = 'BeOS '

        OPERATING_SYSTEMS['iPhone OS'] = 'iPhone'
        OPERATING_SYSTEMS['SymbianOS'] = 'SymbianOS'
        OPERATING_SYSTEMS['Nokia'] = 'Nokia'
        OPERATING_SYSTEMS['BlackBerry'] = 'BlackBerry'
        OPERATING_SYSTEMS['Nintendo Wii'] = 'Nintendo Wii'

        OPERATING_SYSTEMS['Mac OS X'] = 'MacOS X'
        OPERATING_SYSTEMS['Macintosh'] = 'MacOS'
    }

    final String userAgentHeader

    String operatingSystem
    Browser browser

    UserAgentInfo(String userAgentHeader) {
        this.userAgentHeader = userAgentHeader
    }

    void parse() {

        //
        // detecting operating system
        for (Map.Entry<String, String> entry: OPERATING_SYSTEMS.entrySet()) {
            if (userAgentHeader.indexOf(entry.key) != -1) {
                operatingSystem = entry.value
                break
            }
        }

        //
        // detecting browser and its version
        try {
            browser = BrowserDetector.detect(userAgentHeader)
        }
        catch (Exception e) {
            log.error('Error while detecting browser information, cause:', e)
        }
    }

}
