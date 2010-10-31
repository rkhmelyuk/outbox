<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="dynamicFields.title"/></title>
        <meta name='layout' content='main'/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.fancybox.css')}" />

        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        <g:javascript library="jquery.fancybox" />
        <g:javascript library="jquery.tmpl" />

        <script type="text/javascript">
            $(document).ready(function() { App.dynamicFields() })
        </script>
    </head>
    <body class="dynamicFields">
        <g:link elementId="create" controller="dynamicField" action="create">
            <g:message code="add.dynamicField"/></g:link>

        <div id="dynamicFields">
            <g:render template="dynamicFields"/>
        </div>
    </body>
</html>
