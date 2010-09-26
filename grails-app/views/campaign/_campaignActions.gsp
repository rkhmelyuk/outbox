<%@ page import="outbox.campaign.CampaignState" %>
<g:if test="${campaign.state == CampaignState.Ready}">
    <a href="javascript:void(0);" id="sendCampaign" rel="<g:createLink action="send" id="${campaign.id}" params="[page: page]"/>"><g:message code="send"/></a>
</g:if>
<g:if test="${campaign.notStarted}">
    <g:link controller="campaign" action="delete" id="${campaign.id}"><g:message code="delete"/></g:link>
</g:if>
