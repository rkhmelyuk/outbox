package outbox.member

import outbox.dictionary.Language

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

    TimeZone timezone
    Language language

	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
        firstName maxSize: 100, blank: true, nullable: true
        lastName maxSize: 100, blank: true, nullable: true
		username maxSize: 250, blank: false, unique: true
		password maxSize: 250, blank: false
        email maxSize: 512, nullable: false, blank: false, unique: true
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
