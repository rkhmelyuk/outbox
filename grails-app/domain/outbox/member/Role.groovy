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
}
