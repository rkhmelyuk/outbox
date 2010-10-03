package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
static class iPhoneDetector implements Detector { 

    Browser detect(String userAgentHeader) {
        Browser result = null
        if (userAgentHeader.contains('iphone')) {
            result = new Browser()
            result.name = 'Safari'
            int versionIndex = userAgentHeader.indexOf('version/') + 8
            if (versionIndex != -1 && versionIndex < userAgentHeader.length()) {
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
