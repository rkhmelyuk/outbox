<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>

        <g:link controller="subscriber" action="create"><g:message code="create.subscriber"/></g:link>
        <table id="results" style="width: 800px;">
            <thead>
                <tr>
                    <td><g:message code="email"/></td>
                    <td><g:message code="name"/></td>
                    <td><g:message code="create.date"/></td>
                    <td><g:message code="view"/></td>
                </tr>
            </thead>
            <tbody>
                <g:each var="subscriber" in="${subscribers}">
                    <tr>
                        <td><g:link controller="subscriber" action="show" id="${subscriber.id}">
                            <g:fieldValue bean="${subscriber}" field="email"/></g:link></td>
                        <td><g:fieldValue bean="${subscriber}" field="fullName"/></td>
                        <td><g:formatDate date="${subscriber.dateCreated}" format="d MMM yyyy, HH:mm"/></td>
                        <td><g:link controller="subscriber" action="show" id="${subscriber.id}"><g:message code="view"/></g:link></td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </body>
</html>
