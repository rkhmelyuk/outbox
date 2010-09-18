<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>

        <g:link controller="subscriptionList" action="create"><g:message code="create.subscriptionList"/></g:link>
        |
        <g:link controller="subscriber" action="create"><g:message code="create.subscriber"/></g:link>
        |
        <g:if test="${freeSubscribersCount}">
            <g:link controller="subscriptionList" action="freeSubscribers">
                <g:message code="subscribers.without.subscription" args="[freeSubscribersCount]"/>
            </g:link>
        </g:if>
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
                <g:each var="subscriptionList" in="${subscriptionLists}">
                    <tr title="<g:fieldValue bean="${subscriptionList}" field="description"/>">
                        <td><g:link controller="subscriptionList" action="show" id="${subscriptionList.id}">
                            <g:fieldValue bean="${subscriptionList}" field="name" /></g:link></td>
                        <td>
                            <g:if test="${subscriptionList.subscribersNumber}">${subscriptionList.subscribersNumber}</g:if>
                            <g:else>-</g:else>
                        </td>
                        <td><g:formatDate date="${subscriptionList.dateCreated}" format="d MMM yyyy, HH:mm"/></td>
                        <td></td>
                        <td><g:link controller="subscriptionList" action="show" id="${subscriptionList.id}"><g:message code="view"/></g:link></td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </body>
</html>
