<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
        <script type="text/javascript">
            $(document).ready(function() { App.subscriptionLists() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>

        <g:link controller="subscriptionList" action="create"><g:message code="create.subscriptionList"/></g:link>
        |
        <g:link controller="subscriber" action="search"><g:message code="search.subscribers"/></g:link>
        |
        <g:link controller="subscriber" action="create"><g:message code="create.subscriber"/></g:link>
        |
        <g:link controller="subscriptionList" action="archived"><g:message code="archived"/></g:link>

        <g:if test="${freeSubscribersCount}">
            |
            <g:link controller="subscriptionList" action="freeSubscribers">
                <g:message code="subscribers.without.subscription" args="[freeSubscribersCount]"/>
            </g:link>
        </g:if>
        <g:form name="filterForm" action="list" method="GET">
            <g:hiddenField name="column" value="${conditions.column}"/>
            <g:hiddenField name="sort" value="${conditions.sort}"/>
            <table id="results" style="width: 800px;">
                <thead>
                    <tr>
                        <td><g:sortColumn field="name" class="page_callback"><g:message code="name"/></g:sortColumn></td>
                        <td><g:sortColumn field="subscribersNumber" class="page_callback"><g:message code="subscribers.number"/></g:sortColumn></td>
                        <td><g:sortColumn field="dateCreated" class="page_callback"><g:message code="create.date"/></g:sortColumn></td>
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
            <g:formPagination name="page"
                    page="${conditions.page}" maxsteps="10"
                    total="${total}" max="${conditions.itemsPerPage}"
                    callbackClass="page_callback" currentClass="current"/>
        </g:form>
    </body>
</html>
