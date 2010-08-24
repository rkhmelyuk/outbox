package outbox.security

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import outbox.member.Member

/**
 * Represents currently logged in user. 
 *
 * @author Ruslan Khmelyuk
 * @since 2010-08-24
 */
class OutboxUser extends GrailsUser {

    Long id
    String firstName
    String lastName
    String email

    def OutboxUser(String username, String password, boolean enabled,
                   boolean accountNonExpired, boolean credentialsNonExpired,
                   boolean accountNonLocked, List authorities, Member member) {

        super(username, password, enabled,
                accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities, member.id);

        this.id = member.id
        this.firstName = member.firstName
        this.lastName = member.lastName
        this.email = member.email
    }
}
