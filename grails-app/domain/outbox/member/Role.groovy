package outbox.member

/**
 * @author Ruslan Khmelyuk
 */
class Role {

    Integer id
	String authority
    String name

	static mapping = {
        table 'Role'
        id column: 'RoleId'
        columns {
            authority column: 'Authority'
            name column: 'RoleName'
        }
        version false
		cache usage: 'read-only', include: 'non-lazy'
	}

	static constraints = {
		authority maxSize: 50, blank: false, unique: true
        name maxSize: 50, blank: false, unique: true
	}

    static transients = [ 'user' ]

    /**
     * Gets role for ROLE_USER authority.
     * This role is required for member to be a user of this application.
     * @return the found role.
     */
    static Role userRole() {
        return Role.findByAuthority('ROLE_USER')
    }

    /**
     * Gets the list of roles that can be assigned to user.
     * @return the list of assignable roles.
     */
    static List<Role> assignableRoles() {
        return Role.findAllByAuthorityNotEqual('ROLE_USER')
    }

    public boolean isUser() {
        return 'ROLE_USER'.equals(authority)
    }
}
