<%@ page import="outbox.campaign.CampaignState" %>
<g:if test="${campaign.state == CampaignState.Ready}">
    <g:link controller="campaign" action="send"><g:message code="send"/></g:link>
</g:if>
<g:if test="${campaign.notStarted}">
    <g:link controller="campaign" action="delete" id="${campaign.id}"><g:message code="delete"/></g:link>
</g:if>
