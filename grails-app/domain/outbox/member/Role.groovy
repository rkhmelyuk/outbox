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
		cache true
	}

	static constraints = {
		authority maxSize: 50, blank: false, unique: true
        name maxSize: 50, blank: false, unique: true
	}

    /**
     * Gets role for ROLE_USER authority.
     * This role is required for member to be a user of this application.
     * @return the role.
     */
    static Role userRole() {
        return Role.findByAuthority('ROLE_USER')
    }

}
