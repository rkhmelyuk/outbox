package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
static class SafariDetector implements Detector {
    
    Browser detect(String userAgentHeader) {
        Browser result = null
        if (userAgentHeader.contains('safari')
                && !userAgentHeader.contains('chrome')
                && !userAgentHeader.contains('android')
                && !userAgentHeader.contains('iron')) {
            result = new Browser()
            result.name = 'Safari'
            int tokenIndex = userAgentHeader.indexOf('version/')
            int versionIndex = tokenIndex + 8
            if (tokenIndex != -1 && versionIndex < userAgentHeader.length()) {
                int nextSpaceIndex = userAgentHeader.indexOf(' ', versionIndex)
                if (nextSpaceIndex != -1) {
                    result.version = userAgentHeader.substring(versionIndex, nextSpaceIndex)
                }
                else {
                    result.version = userAgentHeader.substring(versionIndex)
                }
            }
            else {
                result.version = UNKNOWN
            }
        }
        return result
    }
}
