<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="create.campaign.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.campaignCreate() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="campaignForm" controller="campaign" action="add" onsubmit="return false;">
            <table>
                <tr>
                    <td><g:message code="name"/></td>
                    <td><g:textField name="name" maxlength="200" value="${campaign.name}"/></td>
                </tr>
                <tr>
                    <td><g:message code="description"/></td>
                    <td><g:textArea name="description" value="${campaign.description}" style="width:600px; height: 100px"/></td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="addCampaign"><g:message code="add.campaign"/></a>
            &nbsp;&nbsp;
            <g:link controller="campaign"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
