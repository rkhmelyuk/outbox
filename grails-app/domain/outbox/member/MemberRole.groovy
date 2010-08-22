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

	static MemberRole get(long memberId, long roleId) {
		find 'from MemberRole where member.id=:memberId and role.id=:roleId',
			[memberId: memberId, roleId: roleId]
	}

	static MemberRole create(Member member, Role role, boolean flush = false) {
		new MemberRole(member: member, role: role).save(flush: flush, insert: true)
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
