<%@ page import="outbox.campaign.CampaignState" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:fieldValue bean="${campaign}" field="name"/> <g:message code="campaign"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.campaignShow() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <h1>
            <span id="name"><g:fieldValue bean="${campaign}" field="name"/></span>
            <span style="font-size:10px"><g:message code="${campaign.state?.messageCode}"/></span>
        </h1>
        <span><g:message code="from.date" args="[formatDate(date: campaign.dateCreated, format: 'dd MMM yyyy')]"/></span>

        <div class="subMenu">
            <g:if test="${campaign.hasReports}">
                <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'reports']">Reports</g:link>
            </g:if>
            <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'details']">Details</g:link>
            <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'subscribers']">
                Subscribers<g:if test="${needSubscribers}">!</g:if>
            </g:link>
            <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'template']">
                Template<g:if test="${needTemplate}">!</g:if>
            </g:link>
        </div>

        <g:if test="${needTemplate || needSubscribers}">
            <div class="note need">
                <g:if test="${needSubscribers}">
                    You need to <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'subscribers']">add subscribers</g:link> to start this campaign.
                </g:if>
                <g:if test="${needTemplate}">
                    You need to <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'template']">setup template</g:link> to start this campaign.
                </g:if>
            </div>
        </g:if>
        <g:elseif test="${campaign.state == CampaignState.Ready}">
            <g:link controller="campaign" action="start">Start</g:link>
        </g:elseif>

        <g:if test="${page == 'details'}">
            <g:render template="campaignDetails" model="${params}"/>
        </g:if>
        <g:elseif test="${page == 'reports'}">
            <g:render template="campaignReports" model="${params}"/>
        </g:elseif>
        <g:elseif test="${page == 'subscribers'}">
            <g:render template="campaignSubscribers" model="${params}"/>
        </g:elseif>
        <g:elseif test="${page == 'template'}">
            <g:render template="campaignTemplate" model="${params}"/>
        </g:elseif>
    </body>
</html>
