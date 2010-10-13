<%@ page import="outbox.campaign.CampaignState" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:fieldValue bean="${campaign}" field="name"/> <g:message code="campaign"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <g:if test="${page == 'reports'}">
            <g:javascript library="highcharts.src" />
            <g:javascript library="report" />
        </g:if>

        <script type="text/javascript">
            $(document).ready(function() {
                App.campaignShow();
                <g:if test="${page == 'details'}">
                    App.campaignEdit();
                </g:if>
                <g:elseif test="${page == 'reports'}">
                    App.campaignReports();
                </g:elseif>
                <g:elseif test="${page == 'subscribers'}">
                    App.campaignSubscribers();
                </g:elseif>
                <g:elseif test="${page == 'template'}">
                    App.campaignTemplate();
                </g:elseif>
            });
        </script>
    </head>
    <body class="campaignShow">
        <div class="status" style="display: none;"> </div>
        <h1>
            <span id="name"><g:fieldValue bean="${campaign}" field="name"/></span>
            <span id="state" style="font-size:10px"><g:message code="${campaign.state?.messageCode}"/></span>
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

        <div id="notifications">
            <g:render template="campaignNotifications"/> 
        </div>
        <div id="actions">
            <g:render template="campaignActions"/>
        </div>

        <div id="showBody">
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
        </div>
    </body>
</html>
