package outbox.member

import outbox.dictionary.Language
import outbox.dictionary.Timezone

/**
 * The member out application.
 */
class Member {

    Long id

    String firstName
    String lastName
    String email

	String username
	String password

    Timezone timezone
    Language language

	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
        firstName maxSize: 100, blank: false, nullable: false
        lastName maxSize: 100, blank: false, nullable: false
		username maxSize: 250, blank: false, unique: true
		password maxSize: 250, blank: false, nullable: false
        email maxSize: 512, nullable: false, blank: false, unique: true, email: true
        timezone nullable: true
        language nullable: true
	}

	static mapping = {
        table 'Member'
        id column: 'MemberId'
        columns {
            firstName column: 'FirstName'
            lastName column: 'LastName'
            username column: 'Username'
		    password column: 'Password'
            email column: 'Email'
            timezone column: 'TimeZoneId'
            language column: 'LanguageId'
            enabled column: 'Enabled'
            accountExpired column: 'AccountExpired'
            accountLocked column: 'AccountLocked'
            passwordExpired column: 'PasswordExpired'
        }
        version false
        cache true
	}

	Set<Role> getAuthorities() {
		MemberRole.findAllByMember(this).collect { it.role } as Set
	}
}
