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

        <div id="conditions">

        </div>

        <a href="javascript:void(0)" id="addCondition" rel="${createLink(controller: 'searchConditions', action:'addRow')}"><g:message code="add"/></a>

    </body>
</html>
