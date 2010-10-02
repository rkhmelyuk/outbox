package outbox.mail

import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * @author Ruslan Khmelyuk
 * @since  2010-09-26
 */
class EmailService {

    void sendEmail(Email email) {
        try {
            Session session = createSession()
            MimeMessage message = convertEmail(email, session)
            Transport.send(message)
        }
        catch (Exception e) {
            throw new EmailException('Error to send email', e)
        }
    }

    void sendEmails(List<Email> emails) {
        if (!emails) {
            return
        }

        Transport bus = null
        try {
            Session session = createSession()
            bus = session.getTransport('smtp')

            bus.connect()
            emails.each { email ->
                if (email) {
                    MimeMessage message = convertEmail(email, session)
                    bus.sendMessage(message, InternetAddress.parse(email.to))
                }
            }
        }
        catch (Exception e) {
            throw new EmailException('Error to send emails', e)
        }
        finally {
            if (bus) {
                bus.close()
            }
        }
    }

    Session createSession() {
        def properties = new Properties()
        properties.put("mail.smtp.host", 'localhost');
        properties.put("mail.debug", 'true');

        Session.getInstance properties
    }
    
    private MimeMessage convertEmail(Email email, Session session) throws Exception {
        final MimeMessage msg = new MimeMessage(session)

        msg.sentDate = new Date()
        if (email.from) {
            msg.setFrom(InternetAddress.parse(email.from)[0])
        }
        if (email.getReplyTo() != null) {
            msg.setReplyTo(InternetAddress.parse(email.replyTo))
        }
        
        msg.setRecipients(Message.RecipientType.TO, email.to)

        if (email.getCc() != null) {
            msg.setRecipients(Message.RecipientType.CC, email.cc)
        }
        if (email.getBcc() != null) {
            msg.setRecipients(Message.RecipientType.BCC, email.bcc)
        }

        msg.setSubject(email.getSubject())

        final Multipart multipart = new MimeMultipart()
        msg.setContent(multipart)

        // Prepare message
        //
        final MimeBodyPart message = new MimeBodyPart()
        multipart.addBodyPart(message)
        message.setText(email.body, email.charset ?: 'UTF-8')
        message.setHeader("Content-Length", Integer.toString(email.body.length()))
        message.setHeader("Content-Type", email.contentType)

        return msg
    }
}
