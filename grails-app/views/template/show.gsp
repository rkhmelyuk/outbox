<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:fieldValue bean="${template}" field="name"/></title>
        <meta name='layout' content='main'/>
    </head>
    <body class="template">
        <g:link controller="template"><g:message code="other.templates"/></g:link>

        <h2><g:fieldValue bean="${template}" field="name"/></h2>
        <g:if test="${template.description}">
            <div class="description">
                <g:fieldValue bean="${template}" field="description"/>
            </div>
        </g:if>
        <g:link controller="template" action="edit" id="${template.id}"><g:message code="edit.details"/></g:link>

        <div class="body">
            <g:fieldValue bean="${template}" field="templateBody"/>
        </div>
    </body>
</html>
