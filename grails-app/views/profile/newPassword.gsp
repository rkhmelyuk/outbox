<%@ page import="outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="change.password.title"/></title>
        <meta name='layout' content='main'/>
        
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        <g:javascript library="jquery.password_strength" />

        <script type="text/javascript">
            $(document).ready(function() { App.newPassword() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="newPasswordForm" controller="profile" action="saveNewPassword">
            <table>
                <tr>
                    <td><g:message code="current.password"/></td>
                    <td><g:passwordField name="oldPassword" maxlength="100" value=""/></td>
                </tr>
                <tr>
                    <td><g:message code="new.password"/></td>
                    <td><g:passwordField name="newPassword" maxlength="100" value=""/>
                        <div class="password-strength"> </div>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="new.password.confirmation"/></td>
                    <td><g:passwordField name="passwordConfirmation" maxlength="100" value=""/></td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="savePassword"><g:message code="save.password"/></a>
            &nbsp;&nbsp;
            <g:link controller="profile"><g:message code="back"/></g:link>
        </g:form>
    </body>
</html>
