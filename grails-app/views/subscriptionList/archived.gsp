<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="archived.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
        <script type="text/javascript">
            $(document).ready(function() { App.archivedSubscriptionLists() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>

        <g:link controller="subscriptionList"><< <g:message code="active.subscriptionList"/></g:link>

        <div class="c"></div>

        <g:form name="filterForm" action="archived" method="GET">
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
            <g:formPagination name="page"
                    page="${conditions.page}" maxsteps="10"
                    total="${total}" max="${conditions.itemsPerPage}"
                    callbackClass="page_callback" currentClass="current"/>
        </g:form>
    </body>
</html>
