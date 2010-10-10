package outbox;

/**
 * Application constants
 *
 * @author Ruslan Khmelyuk
 * @since 2010-09-04
 */
public interface AppConstant {

    /**
     * The salt for generating Subscriber id.
     */
    String SUBSCRIBER_ID_SALT = "KSielA3!*sCQrPow(+A";

    /**
     * The salt for generated Tracking Reference id.
     */
    String TRACKING_REFERENCE_SALT = "Ksid<>Ajd823lASsK<lsOs;lidH82lkJNAS8-*^&";

    String TRACKING_INFO_SALT = "lAKsjd8Kj23r8AKFjhDIJ38923jD&#KDjdkdnLDMJEU";

    /**
     * The address for Openings resource
     */
    String OPEN_PING_RESOURCE = "http://mailsight.com/ping.png";
}
