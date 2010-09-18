<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>${template?.name?.encodeAsHTML()}</title>
        <meta name='layout' content='main'/>
        %{--
        <g:javascript library="jquery.form"/>
        <g:javascript library="jquery.validate"/>
        --}%

        <script type="text/javascript">
            //$(document).ready(function() { App.templateEdit() })
        </script>
    </head>
    <body class="template">
        <g:link controller="template"><g:message code="other.templates"/></g:link>

        <h2>${template.name?.encodeAsHTML()}</h2>
        <g:if test="${template.description}">
            <div class="description">
                ${template.description?.encodeAsHTML()}
            </div>
        </g:if>
        <g:link controller="template" action="edit" id="${template.id}"><g:message code="edit.details"/></g:link>

        <div class="body">
            ${template.templateBody}
        </div>
    </body>
</html>
