package outbox

import org.codehaus.groovy.grails.plugins.springsecurity.SecurityRequestHolder as SRH

import java.text.MessageFormat
import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.servlet.support.RequestContextUtils

/**
 * @author Ruslan Khmelyuk
 * @since  2010-08-25
 */
class MessageUtil {

    static void addErrors(HttpServletRequest req, Map model, def binding) {
        def errors = model.errors ? model.errors : [:]
        binding.allErrors.eachWithIndex { ObjectError error, int index ->
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
            if (error instanceof FieldError) {
                errors.put(error.field, message)
            }
            else {
                errors.put(index, message)
            }
        }
        model << [errors: errors]
    }

    public static String getMessage(String messageCode, Object[] params, HttpServletRequest request) {
        request = request ?: SRH.request
        def locale = RequestContextUtils.getLocale(request)
        def messageSource = (MessageSource) ApplicationHolder.application.mainContext.getBean("messageSource");
        messageSource.getMessage(messageCode, params, locale)
    }
}
