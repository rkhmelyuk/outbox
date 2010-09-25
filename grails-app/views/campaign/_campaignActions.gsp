<%@ page import="outbox.campaign.CampaignState" %>
<g:if test="${campaign.state == CampaignState.Ready}">
    <g:link controller="campaign" action="send"><g:message code="send"/></g:link>
</g:if>
