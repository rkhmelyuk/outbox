<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="dynamicFields.title"/></title>
        <meta name='layout' content='main'/>
        %{--<g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscriberTypes() })
        </script>--}%
    </head>
    <body class="dynamicFields">
        <div class="status" style="display: none;"> </div>

        <table>
            <tr>
                <td><g:message code="label"/></td>
                <td><g:message code="name"/></td>
                <td><g:message code="type"/></td>
                <td><g:message code="delete"/></td>
            </tr>
            <g:each var="dynamicField" in="${dynamicFields}">
                <tr>
                    <td><g:fieldValue bean="${dynamicField}" field="label"/></td>
                    <td><g:fieldValue bean="${dynamicField}" field="name"/></td>
                    <td><g:message code="${dynamicField.type?.messageCode}"/></td>
                    <td></td>
                </tr>
            </g:each>
        </table>

    </body>
</html>
