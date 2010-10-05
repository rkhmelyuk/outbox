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
        
        // Key must be lowercase

        // TODO - move to the configurable storage, maybe database

        OPERATING_SYSTEMS = [:] as LinkedHashMap
        OPERATING_SYSTEMS['win3.11'] = 'Windows 3.11'
        OPERATING_SYSTEMS['winnt3.51'] = 'Windows NT 3.11'
        OPERATING_SYSTEMS['winnt4.0'] = 'Windows NT 4.0'
        OPERATING_SYSTEMS['win95'] = 'Windows 95'
        OPERATING_SYSTEMS['win98'] = 'Windows 98'
        OPERATING_SYSTEMS['win 9x 4.90'] = 'Windows Me'
        OPERATING_SYSTEMS['windows nt 5.0'] = 'Windows 2000'
        OPERATING_SYSTEMS['windows nt 5.1'] = 'Windows XP'
        OPERATING_SYSTEMS['windows nt 5.2'] = 'Windows Server 2003/Windows XP x64 Edition'
        OPERATING_SYSTEMS['windows nt 6.0'] = 'Windows Vista'
        OPERATING_SYSTEMS['windows nt 6.1'] = 'Windows 7'
        OPERATING_SYSTEMS['win'] = 'Windows (version unknown)'

        OPERATING_SYSTEMS['windowsce'] = 'Windows CE'

        OPERATING_SYSTEMS['ubuntu'] = 'Ubuntu Linux'
        OPERATING_SYSTEMS['linux'] = 'Linux'
        OPERATING_SYSTEMS['freebsd'] = 'FreeBSD'
        OPERATING_SYSTEMS['openbsd'] = 'OpenBSD'
        OPERATING_SYSTEMS['netbsd'] = 'NetBSD'
        OPERATING_SYSTEMS['sunos'] = 'Solaris'
        OPERATING_SYSTEMS['unix'] = 'Unix'
        OPERATING_SYSTEMS['beos'] = 'BeOS '

        OPERATING_SYSTEMS['iphone'] = 'iPhone'
        OPERATING_SYSTEMS['symbianos'] = 'SymbianOS'
        OPERATING_SYSTEMS['nokia'] = 'Nokia'
        OPERATING_SYSTEMS['blackberry'] = 'BlackBerry'
        OPERATING_SYSTEMS['nintendo wii'] = 'Nintendo Wii'

        OPERATING_SYSTEMS['mac os x 10.6'] = 'MacOS X 10.6'
        OPERATING_SYSTEMS['mac os x 10.5'] = 'MacOS X 10.5'
        OPERATING_SYSTEMS['mac os x'] = 'MacOS X'
        OPERATING_SYSTEMS['macintosh'] = 'MacOS'
    }

    final String userAgentHeader

    String operatingSystem
    Browser browser

    UserAgentInfo(String userAgentHeader) {
        this.userAgentHeader = userAgentHeader.toLowerCase()
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
