<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="dynamicFields.title"/></title>
        <meta name='layout' content='main'/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.fancybox.css')}" />

        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        <g:javascript library="jquery.fancybox" />

        <script type="text/javascript">
            $(document).ready(function() { App.dynamicFields() })
        </script>
    </head>
    <body class="dynamicFields">
        <div class="status" style="display: none;"> </div>

        <g:link elementId="create" controller="dynamicField" action="create">
            <g:message code="add.dynamicField"/></g:link>

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
