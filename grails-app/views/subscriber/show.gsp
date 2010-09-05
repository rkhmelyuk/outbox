<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="show.subscriber.title"/></title>
        <meta name='layout' content='main'/>
        
        %{--<script type="text/javascript">
            $(document).ready(function() { App.memberEdit() })
        </script>--}%
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:if test="${subscriber}">
            ${subscriber.firstName}<br/>
            ${subscriber.lastName}<br/>
            ${subscriber.email}<br/>
            
        </g:if>
        <g:else>
            Subscriber is not found.
        </g:else>
    </body>
</html>
