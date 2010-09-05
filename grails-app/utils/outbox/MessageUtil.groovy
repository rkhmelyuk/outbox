package outbox

import java.text.MessageFormat
import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.validation.FieldError
import org.springframework.web.servlet.support.RequestContextUtils

/**
 * @author Ruslan Khmelyuk
 * @since  2010-08-25
 */
class MessageUtil {

    static void addErrors(HttpServletRequest req, Map model, def binding) {
        def errors = model.errors ? model.errors : [:]
        binding.allErrors.each { FieldError error ->
            String message = null;
            for (String each : error.codes) {
                try {
                    message = getMessage(each, error.arguments, req)
                    break;
                }
                catch (NoSuchMessageException e) {
                    // skip it
                }
            }
            if (message == null && error.defaultMessage) {
                message = new MessageFormat(error.defaultMessage).format(error.arguments);
            }
            errors.put(error.field, message)
        }
        model << [errors: errors]
    }

    public static String getMessage(String messageCode, Object[] params, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request)
        MessageSource messageSource = (MessageSource) ApplicationHolder.application.mainContext.getBean("messageSource");
        return messageSource.getMessage(messageCode, params, locale)
    }
}
