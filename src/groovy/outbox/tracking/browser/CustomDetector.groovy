package outbox.tracking.browser

/**
 * @author Ruslan Khmelyuk
 */
static class CustomDetector implements Detector {

    final String browserName

    // Browser token should be lowercase always!
    final String browserToken

    CustomDetector(String browserName, String browserToken) {
        this.browserName = browserName
        this.browserToken = browserToken
    }

    Browser detect(String userAgentHeader) {
        Browser result = null
        if (userAgentHeader) {
            def tokenIndex = userAgentHeader.indexOf(browserToken)
            if (tokenIndex != -1) {
                result = new Browser()
                result.name = browserName
                int versionIndex = tokenIndex + browserToken.length()
                if (versionIndex < userAgentHeader.length()) {
                    int nextSpaceIndex = userAgentHeader.indexOf(' ', versionIndex)
                    int nextPropertyIndex = userAgentHeader.indexOf(';', versionIndex)
                    int nextQuoteIndex = userAgentHeader.indexOf(')', versionIndex)

                    if (nextSpaceIndex != -1 || nextPropertyIndex != -1 || nextQuoteIndex != -1) {
                        int index = correctIndex(nextSpaceIndex, nextPropertyIndex, nextQuoteIndex)
                        result.version = userAgentHeader.substring(versionIndex, index)
                    }
                    else {
                        result.version = userAgentHeader.substring(versionIndex)
                    }
                }
                else {
                    result.version = UNKNOWN
                }
            }
        }
        return result
    }

    int correctIndex(int index1, int index2, int index3) {
        int result = index1
        if (result == -1 || (index2 != -1 && index2 < result)) {
            result = index2
        }
        if (result == -1 || (index3 != -1 && index3 < result)) {
            result = index3
        }
        return result
    }
}
