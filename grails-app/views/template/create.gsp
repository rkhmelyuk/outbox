<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="create.template.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.templateCreate() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="templateForm" controller="template" action="add" onsubmit="return false;">
            <g:hiddenField name="campaign" value="${params.campaign}"/>
            <table>
                <tr>
                    <td><g:message code="name"/></td>
                    <td><g:textField name="name" maxlength="200" value="${template.name}"/></td>
                </tr>
                <tr>
                    <td><g:message code="description"/></td>
                    <td><g:textArea name="description" value="${template.description}" style="width:600px; height: 100px"/></td>
                </tr>
                <tr>
                    <td><g:message code="template.body"/></td>
                    <td><g:textArea name="templateBody" value="${template.templateBody}" style="width:600px; height: 300px"/></td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="addTemplate"><g:message code="add.template"/></a>
            &nbsp;&nbsp;
            <g:if test="${params.campaign}">
                <g:link controller="campaign" action="show" id="${params.campaign}" params="[page: 'template']"><g:message code="cancel"/></g:link>
            </g:if>
            <g:else>
                <g:link controller="template" action="list"><g:message code="cancel"/></g:link>
            </g:else>
        </g:form>
    </body>
</html>
