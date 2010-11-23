<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="search.subscribers"/></title>
        <meta name='layout' content='main'/>
        <script type="text/javascript">
            $(document).ready(function() { App.subscriberSearch() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:hiddenField name="url" value="${createLink(controller: 'searchConditions', action:'renderRow')}"/>

        <div id="conditions"> </div>

        <a href="javascript:void(0)" id="addCondition"><g:message code="add"/></a>

    </body>
</html>
