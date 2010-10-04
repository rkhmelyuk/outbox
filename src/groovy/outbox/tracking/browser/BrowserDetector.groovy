package outbox.tracking.browser

/**
 * Useful tool to detect browser name and version specified in the string
 * that contains User-Agent HTTP request header.
 * <p>
 * Contains list of most useful browsers.
 *
 * @author Ruslan Khmelyuk
 */
class BrowserDetector {

    private static final List<Detector> DETECTORS

    static {
        // Sequence is important, UnknownDetector should be the last,
        // as it detects and tries to parse user agent for unknown browser.
        DETECTORS = new ArrayList<Detector>()
        DETECTORS.add(new CustomDetector('IE', 'msie '))
        DETECTORS.add(new SafariDetector())
        DETECTORS.add(new iPhoneDetector())
        DETECTORS.add(new CustomDetector('Android', 'android '))
        DETECTORS.add(new CustomDetector('Opera', 'opera/'))
        DETECTORS.add(new CustomDetector('Konqueror', 'konqueror/'))
        DETECTORS.add(new CustomDetector('Lunascape', 'lunascape/'))
        DETECTORS.add(new CustomDetector('Iron', 'iron/'))
        DETECTORS.add(new CustomDetector('Shiretoko', 'shiretoko/'))
        DETECTORS.add(new CustomDetector('Camino', 'camino/'))
        DETECTORS.add(new CustomDetector('Chimera', 'chimera/'))
        DETECTORS.add(new CustomDetector('Firebird', 'firebird/'))
        DETECTORS.add(new CustomDetector('Phoenix', 'phoenix/'))
        DETECTORS.add(new CustomDetector('Galeon', 'galeon/'))
        DETECTORS.add(new CustomDetector('Firefox', 'firefox/'))
        DETECTORS.add(new CustomDetector('Chrome', 'chrome/'))

        DETECTORS.add(new UnknownDetector())
    }

    static Browser detect(final String userAgentHeader) {
        def userAgentHeaderLowerCase = userAgentHeader.toLowerCase()
        for (Detector each: DETECTORS) {
            Browser result = each.detect(userAgentHeaderLowerCase)
            if (result != null) {
                return result
            }
        }
        return null
    }

}
