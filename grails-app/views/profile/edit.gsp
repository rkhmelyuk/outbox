<%@ page import="outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>My Profile</title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.editProfile() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="profileForm" controller="profile" action="saveProfile">
            <table>
                <tr>
                    <td><g:message code="username"/></td>
                    <td>${member.username}</td>
                </tr>
                <tr>
                    <td><g:message code="password"/></td>
                    <td>******** <g:link action="newPassword"><g:message code="change.password"/></g:link></td>
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
            </table>
            <a href="javascript:void(0);" id="saveProfile"><g:message code="save.profile"/></a>
            &nbsp;&nbsp;
            <g:link controller="dashboard"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
