<div id="subscriptions">
    <div id="proposedSubscriptionLists">
        <g:if test="${proposedSubscriptions}">
            <g:form name="addSubscriptionListForm" action="addSubscriptionList" onsubmit="return false">
                <g:hiddenField name="campaignId" value="${campaign.id}"/>
                Add Subscription List:
                <g:select name="subscriptionList"
                        from="${proposedSubscriptions}"
                        optionKey="id" optionValue="name"/>
                <a href="javascript:void(0)" id="addSubscriptionList"><g:message code="add"/></a>
            </g:form>
        </g:if>
    </div>

    <g:if test="${subscriptions}">
        Total Unique Subscribers: ${totalSubscribers}
        <table id="subscriptionLists">
            <thead>
            <tr>
                <th><g:message code="name"/></th>
                <th><g:message code="create.date"/></th>
                <th>Subscribers</th>
                <th><g:message code="remove"/></th>
            </tr>
            </thead>
            <g:each var="subscription" in="${subscriptions}">
                <tr>
                    <td><g:fieldValue bean="${subscription}" field="subscriptionList.name"/></td>
                    <td><g:formatDate date="${subscription.subscriptionList.dateCreated}" format="d MMMMM yyyy"/></td>
                    <td>${subscription.subscriptionList.subscribersNumber ? subscription.subscriptionList.subscribersNumber : 0}</td>
                    <td><a href="javascript:void(0);" class="removeSubscriptionList" rel="${subscription.id}"><g:message code="remove"/></a></td>
                </tr>
            </g:each>
        </table>
    </g:if>

</div>

<g:form name="removeSubscriptionListForm" action="removeSubscriptionList">
    <g:hiddenField name="campaignSubscriptionId"/>
</g:form>

