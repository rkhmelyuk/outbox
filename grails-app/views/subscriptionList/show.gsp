<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="show.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
        
        <script type="text/javascript">
            $(document).ready(function() { App.subscriptionListShow() })
        </script>
    </head>
    <body class="subscriptionList">
        <div class="status" style="display: none;"> </div>
        <g:link controller="subscriptionList"><g:message code="subscriptionLists.other"/></g:link>

        <h2>${subscriptionList.name}</h2>
        <g:if test="${subscriptionList.description}">
            <div class="description">
                ${subscriptionList.description}
            </div>
        </g:if>
        <g:link controller="subscriptionList" action="edit" id="${subscriptionList.id}"><g:message code="edit.details"/></g:link>
        <a href="javascript:void(0)" id="deleteSubscriptionList"><g:message code="subscriptionList.delete"/></a>

        <div id="removeNotion">
            <h3><g:message code="notion"/></h3>
            <g:message code="subscriptionList.delete.notion.message"/>
            <br/>
            <g:link controller="subscriptionList" action="delete" id="${subscriptionList.id}"><g:message code="delete.confirm"/></g:link>
            <a href="javascript:void(0);" id="discardDeleteSubscriptionList"><g:message code="delete.discard"/></a>
        </div>

        <div class="subscribers">
            <h3><g:message code="subscribers"/></h3>
            <g:link controller="subscriber" action="create" params="[list:subscriptionList.id]"><g:message code="create.subscriber"/></g:link>
            <g:if test="${subscriptions}">
                <table>
                    <tr>
                        <td><g:message code="email"/></td>
                        <td><g:message code="name"/></td>
                        <td><g:message code="create.date"/></td>
                        <td><g:message code="status"/></td>
                    </tr>
                    <g:each var="subscription" in="${subscriptions}">
                    <tr>
                        <td><g:link controller="subscriber" action="show" id="${subscription.subscriber?.id}">
                            ${subscription.subscriber?.email}</g:link></td>
                        <td>${subscription.subscriber?.fullName}</td>
                        <td><g:formatDate date="${subscription.subscriber?.dateCreated}" format="d MMMMM yyyy"/></td>
                        <td>${subscription.status?.name}</td>
                    </tr>
                    </g:each>
                </table>
            </g:if>
        </div>
        <div class="campaigns">
            List of campaigns that used this list. 
        </div>
    </body>
</html>
