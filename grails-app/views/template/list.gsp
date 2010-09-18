<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="list.templates.title"/></title>
        <meta name='layout' content='main'/>

        <script type="text/javascript">
            $(document).ready(function() { App.templatesList() })
        </script>
    </head>
    <body class="templatesList">
        <div class="status" style="display: none;"> </div>
        <h1><g:message code="templates"/></h1>
        <g:link controller="template" action="create"><g:message code="add.template"/></g:link> 
        <g:if test="templates">
            <div id="templates">
                <g:render template="templateListRecords" collection="${templates}" var="template"/>
            </div>
            <div class="c"></div>
            <g:if test="${nextPage}">
            <a href="javascript:void(0);" id="moreTemplates" rel="<g:createLink action="templatesPage" params="[page: nextPage]"/>"><g:message code="more.templates"/></a>
            </g:if>
        </g:if>
        <g:else>
            None template found.
        </g:else>
    </body>
</html>
