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

    Date dateCreated
    Date lastUpdated

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
            dateCreated column: 'CreateDate'
            lastUpdated column: 'ModifyDate'
        }
        version false
        cache true
	}

    static constraints = {
        firstName maxSize: 100, blank: false, nullable: false
        lastName maxSize: 100, blank: false, nullable: false
		username maxSize: 250, blank: false, unique: true
		password maxSize: 250, blank: false, nullable: false
        email maxSize: 512, nullable: false, blank: false, unique: true, email: true
        timezone nullable: true
        language nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
	}

	static transients = ['assignableAuthority', 'fullName']

	Set<Role> getAuthorities() {
		MemberRole.findAllByMember(this).collect { it.role } as Set
	}

    Role getAssignableAuthority() {
        (Role) authorities.find { Role role -> !role.user }
    }

    String getFullName() {
        def result
        if (firstName && lastName) {
            result = "$firstName $lastName"
        }
        else if (firstName) {
            result = firstName
        }
        else if (lastName) {
            result = lastName
        }
        else {
            result = ''
        }
        return result

    }
}
