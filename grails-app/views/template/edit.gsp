<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="create.template.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.templateEdit() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="templateForm" controller="template" action="update" onsubmit="return false;">
            <g:hiddenField name="id" value="${template.id}"/>
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
            <a href="javascript:void(0);" id="editTemplate"><g:message code="save.template"/></a>
            &nbsp;&nbsp;
            <g:link controller="template"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
