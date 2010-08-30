package outbox.member

import org.apache.commons.lang.builder.HashCodeBuilder

class MemberRole implements Serializable {

	Member member
	Role role

	boolean equals(other) {
		if (!(other instanceof MemberRole)) {
			return false
		}

		other.member?.id == member?.id &&
			other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (member) builder.append(member.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static MemberRole get(Member member, Role role) {
		MemberRole.findByMemberAndRole(member, role)
	}

	static MemberRole create(Member member, Role role, boolean flush = false) {
		new MemberRole(member: member, role: role).save(flush: flush, insert: true)
	}

    static MemberRole change(Member member, Role role, boolean flush = false) {
        Set<Role> roles = member.authorities
        Set<Role> toRemove = []
        for (Role each : roles) {
            if (role.id.equals(each.id)) {
                return get(member, role)
            }
            else if (!each.user) {
                toRemove << each
            }
        }
        
        MemberRole result = null;
        MemberRole.withTransaction {
            toRemove.each { remove(member, it, flush) }
            result = new MemberRole(member: member, role: role).save(flush: flush, insert: true)
        }

        return result
	}

	static boolean remove(Member member, Role role, boolean flush = false) {
		MemberRole instance = MemberRole.findByMemberAndRole(member, role)
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAll(Member member) {
		executeUpdate 'DELETE FROM MemberRole WHERE member=:member', [member: member]
	}

	static void removeAll(Role role) {
		executeUpdate 'DELETE FROM MemberRole WHERE role=:role', [role: role]
	}

	static mapping = {
        table 'MemberRole'
		id composite: ['role', 'member']
        columns {
            role column: 'RoleId'
            member column: 'MemberId'
        }
		version false
	}
}
