<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.subscribersList.title"/></title>
        <meta name='layout' content='main'/>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>

        <g:link controller="subscribersList" action="create"><g:message code="create.subscribersList"/></g:link>
        <table id="results" style="width: 800px;">
            <thead>
                <tr>
                    <td><g:message code="name"/></td>
                    <td><g:message code="subscribers.number"/></td>
                    <td><g:message code="create.date"/></td>
                    <td><g:message code="view"/></td>
                </tr>
            </thead>
            <tbody>
                <g:each var="subscribersList" in="${subscribersLists}">
                    <tr title="${subscribersList.description}">
                        <td><g:link controller="subscribersList" action="show" id="${subscribersList.id}">${subscribersList.name}</g:link></td>
                        <td>
                            <g:if test="${subscribersList.subscribersNumber}">${subscribersList.subscribersNumber}</g:if>
                            <g:else>-</g:else>
                        </td>
                        <td><g:formatDate date="${subscribersList.dateCreated}" format="d MMM yyyy, HH:mm"/></td>
                        <td></td>
                        <td><g:link controller="subscribersList" action="show" id="${subscribersList.id}">View</g:link></td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </body>
</html>
