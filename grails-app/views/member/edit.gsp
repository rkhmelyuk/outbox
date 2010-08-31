<%@ page import="outbox.member.Role; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="member.edit.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        <g:javascript library="jquery.password_strength" />

        <script type="text/javascript">
            $(document).ready(function() { App.memberEdit() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:if test="${member}">
            <g:form name="memberForm" controller="member" action="update">
                <g:hiddenField name="id" value="${member.id}"/>
                <table>
                    <tr>
                        <td><g:message code="username"/></td>
                        <td>${member.username}</td>
                    </tr>
                    <tr>
                        <td><g:message code="firstName"/></td>
                        <td><g:textField name="firstName" maxlength="100" value="${member.firstName}"/></td>
                    </tr>
                    <tr>
                        <td><g:message code="lastName"/></td>
                        <td><g:textField name="lastName" maxlength="100" value="${member.lastName}"/></td>
                    </tr>
                    <tr>
                        <td><g:message code="email"/></td>
                        <td><g:textField name="email" maxlength="512" value="${member.email}"/></td>
                    </tr>
                    <tr>
                        <td><g:message code="language"/></td>
                        <td>
                            <g:select name="language" from="${Language.list()}"
                                    value="${member.language?.id}"
                                    optionKey="id" optionValue="name"/>
                        </td>
                    </tr>
                    <tr>
                        <td><g:message code="timezone"/></td>
                        <td>
                            <g:select name="timezone" from="${Timezone.list()}"
                                    value="${member.timezone?.id}"
                                    optionKey="id" optionValue="name"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            Enter both password and confirmation to change member password:
                        </td>
                    </tr>
                    <tr>
                        <td><g:message code="password"/></td>
                        <td>
                            <g:passwordField name="password" maxlength="100" value=""/>
                            <div class="password-strength"> </div>
                        </td>
                    </tr>
                    <tr>
                        <td><g:message code="password.confirmation"/></td>
                        <td><g:passwordField name="passwordConfirmation" maxlength="100" value=""/></td>
                    </tr>
                    <tr>
                        <td><g:message code="role"/></td>
                        <td><g:select name="role" from="${Role.assignableRoles()}"
                                value="${member.assignableAuthority?.id}" 
                                optionKey="id" optionValue="name"/></td>
                    </tr>
                </table>
                <a href="javascript:void(0);" id="saveMember"><g:message code="save.changes"/></a>
                &nbsp;&nbsp;
                <g:link controller="member" action="list"><g:message code="back"/></g:link>
            </g:form>
        </g:if>
        <g:else>
            Member is not found.
        </g:else>
    </body>
</html>
