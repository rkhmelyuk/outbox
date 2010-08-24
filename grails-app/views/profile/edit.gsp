<%@ page import="outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>My Profile</title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <script type="text/javascript">
            $(document).ready(function() {
                $('#saveProfile').click(function() {
                    $('#profileForm').ajaxSubmit({
                        dataType: 'json',
                        success: function(response, status, xhr) {
                            if (response && status == 'success') {
                                if (response.success) {
                                    $('.status').show().text('Profile saved successfully.');
                                }
                                else {
                                    var errors = '';
                                    for (var i in response.errors) {
                                        errors += response.errors[i] + '<br/>';
                                    }
                                    $('.status').show().html(errors);
                                }
                            }
                        },
                        complete: function(xhr, status) {
                            if (status == 'error') {
                                $('.status').show().text('Error happened.');
                            }
                        }
                    })
                });
            });
        </script>
    </head>
    <body>
        <div class="status" style="display: none;">
            
        </div>
        <g:form name="profileForm" controller="profile" action="saveProfile">
            <table>
                <tr>
                    <td>Username</td>
                    <td>${member.username}</td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td>******** <g:link action="newPassword">Change Password</g:link></td>
                </tr>
                <tr>
                    <td>First Name</td>
                    <td><g:textField name="firstName" maxlength="100" value="${member.firstName}"/></td>
                </tr>
                <tr>
                    <td>Last Name</td>
                    <td><g:textField name="lastName" maxlength="100" value="${member.lastName}"/></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><g:textField name="email" maxlength="512" value="${member.email}"/></td>
                </tr>
                <tr>
                    <td>Language</td>
                    <td>
                        <g:select name="language" from="${Language.list()}"
                                value="${member.language?.id}"
                                optionKey="id" optionValue="name"/> 
                    </td>
                </tr>
                <tr>
                    <td>Time Zone</td>
                    <td>
                        <g:select name="timezone" from="${Timezone.list()}" 
                                value="${member.timezone?.id}"
                                optionKey="id" optionValue="name"/>
                    </td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="saveProfile">Save Changes</a>
            &nbsp;&nbsp;
            <g:link controller="dashboard">Cancel</g:link>
        </g:form>
    </body>
</html>
