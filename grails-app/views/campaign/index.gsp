<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.campaigns.title"/></title>
        <meta name='layout' content='main'/>

        <script type="text/javascript">
            //$(document).ready(function() { App.templatesList() })
        </script>
    </head>
    <body class="campaignsIndex">
        <div class="status" style="display: none;"> </div>
        <g:link controller="campaign" action="create"><g:message code="create.campaign"/></g:link>

        <h2><g:message code="campaigns"/></h2>
        <g:if test="${campaigns}">
            <div id="campaigns">
                <g:each var="campaign" in="${campaigns}" status="index">
                    <div class="campaign <g:if test="${index % 2}">odd</g:if>">
                        <span class="name">
                            <g:link controller="campaign" action="show" id="${campaign.id}"><g:fieldValue bean="${campaign}" field="name"/></g:link>
                        </span>
                        <span class="status"><span class="status_${campaign.state?.id}"><g:message code="${campaign.state?.messageCode}"/></span></span>
                        <span class="date"><g:formatDate date="${campaign.dateCreated}" format="dd-MMM-yyyy"/></span>
                    </div>
                    <div class="c"></div>
                </g:each>
            </div>
            <div class="c"></div>
            <g:link controller="campaign" action="list"><g:message code="see.all"/></g:link>
        </g:if>
        <g:else>
            No Campaigns Found   
        </g:else>
    </body>
</html>
