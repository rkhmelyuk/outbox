<%@ page import="outbox.campaign.CampaignState" %>
<g:if test="${campaign.state == CampaignState.Ready}">
    <g:link controller="campaign" action="start"><g:message code="start"/></g:link>
</g:if>
