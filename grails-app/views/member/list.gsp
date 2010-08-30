<%@ page import="outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="members.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <script type="text/javascript">
            $(document).ready(function() { App.membersList() })
        </script>
    </head>
    <body>
        <g:form name="filterForm" controller="member" action="list" method="GET">
            <table>
                <tr>
                    <td>
                        <g:message code="username"/><br/>
                        <g:textField name="username" value="${condition.username}"/>
                    </td>
                    <td>
                        <g:message code="name"/><br/>
                        <g:textField name="fullName" value="${condition.fullName}"/>
                    </td>
                    <td>
                        <input type="submit" value="Search"/>
                    </td>
                </tr>
            </table>
            <g:select name="itemsPerPage" from="[5, 10, 25, 50]" value="${condition.itemsPerPage}"/>

            <g:link controller="member" action="create"><g:message code="create.member"/></g:link>
        
            <table id="results">
                <thead>
                    <tr>
                        <td><g:message code="username"/></td>
                        <td><g:message code="full.name"/></td>
                        <td><g:message code="email"/></td>
                        <td>Status</td>
                        <td><g:message code="edit"/></td>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="member" in="${members}">
                        <tr>
                            <td>${member.username}</td>
                            <td>${member.firstName} ${member.lastName}</td>
                            <td>${member.email}</td>
                            <td></td>
                            <td><g:link controller="member" action="edit" id="${member.id}">Edit</g:link></td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <g:formPagination name="page" total="${total}" max="${condition.itemsPerPage}"
                    page="${condition.page}" maxsteps="10"
                    callbackClass="page_callback" currentClass="current"/>
        </g:form>
    </body>
</html>
