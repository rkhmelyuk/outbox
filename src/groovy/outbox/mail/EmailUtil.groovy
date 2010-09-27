package outbox.mail

/**
 * Utils to work with emails.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class EmailUtil {

    /**
     * Prepares email in RFC 822 standard format, e.g. {@code "John Smith" <john.smith@mail.com>}
     * If short name is empty or {@code null} only email is returned.
     *
     * @param recipientName the recipient name,
     * @param recipientEmail the recipient email address.
     * @return the correct email to address as string.
     */
    public static String emailAddress(String recipientName, String recipientEmail) {
        if (!recipientEmail) {
            return null
        }
        
        def result = new StringBuilder()
        if (!recipientName) {
            recipientName = recipientEmail
        }

        result.append("\"").append(recipientName).append("\"")
        result.append(" <").append(recipientEmail).append(">")
        
        return result.toString()
    }

}
