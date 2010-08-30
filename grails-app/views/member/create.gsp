<%@ page import="outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="member.create.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        <g:javascript library="jquery.password_strength" />

        <script type="text/javascript">
            $(document).ready(function() { App.memberCreate() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="memberForm" controller="member" action="save">
            <table>
                <tr>
                    <td><g:message code="username"/></td>
                    <td><g:textField name="username" maxlength="250" value="${member.username}"/></td>
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
            </table>
            <a href="javascript:void(0);" id="createMember"><g:message code="create.member"/></a>
            &nbsp;&nbsp;
            <g:link controller="member" action="list"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
