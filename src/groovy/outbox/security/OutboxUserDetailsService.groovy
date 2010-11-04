package outbox.security

import org.codehaus.groovy.grails.plugins.springsecurity.GormUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * @author Ruslan Khmelyuk
 * @since 2010-08-24
 */
class OutboxUserDetailsService extends GormUserDetailsService {

    protected loadUser(String username, session) {
        def config = SpringSecurityUtils.securityConfig
        
        String userDomainClassName = config.userLookup.userDomainClassName
        String usernamePropertyName = config.userLookup.usernamePropertyName

        List<?> users = session.createQuery(
                "from $userDomainClassName where $usernamePropertyName = :username or email = :email")
                .setString('username', username).setString('email', username).list()

        if (!users) {
            throw new UsernameNotFoundException('User is not found', username)
        }

        return users[0]
    }

    protected GrailsUser createUserDetails(Object user, Collection<GrantedAuthority> authorities) {
        def config = SpringSecurityUtils.securityConfig

        String usernamePropertyName = config.userLookup.usernamePropertyName
        String passwordPropertyName = config.userLookup.passwordPropertyName
        String enabledPropertyName = config.userLookup.enabledPropertyName
        String accountExpiredPropertyName = config.userLookup.accountExpiredPropertyName
        String accountLockedPropertyName = config.userLookup.accountLockedPropertyName
        String passwordExpiredPropertyName = config.userLookup.passwordExpiredPropertyName

        String username = user."$usernamePropertyName"
        String password = user."$passwordPropertyName"

        boolean enabled = enabledPropertyName ? user."$enabledPropertyName" : true
        boolean accountExpired = accountExpiredPropertyName ? user."$accountExpiredPropertyName" : false
        boolean accountLocked = accountLockedPropertyName ? user."$accountLockedPropertyName" : false
        boolean passwordExpired = passwordExpiredPropertyName ? user."$passwordExpiredPropertyName" : false

        return new OutboxUser(username, password, enabled,
                !accountExpired, !passwordExpired,
                !accountLocked, authorities, user)
    }

}
