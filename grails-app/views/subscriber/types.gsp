<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="subscriberTypes.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscriberTypes() })
        </script>
    </head>
    <body class="subscriberTypes">
        <div class="status" style="display: none;"> </div>

        <div class="addSubscriberType">
            <g:form name="addSubscriberTypeForm" controller="subscriber"
                    action="addSubscriberType" onsubmit="return false;">
                <label for="name"><g:message code="subscriberType.name"/></label>
                <input type="text" id="name" name="name" maxlength="200"/>
                <a href="javascript:void(0);" id="addSubscriberType"><g:message code="add"/></a>
            </g:form>
        </div>
        <h3><g:message code="subscriberTypes"/></h3>
        <g:message code="subscriberType.click.to.change.name"/>
        <div id="types">
            <g:if test="${subscriberTypes}">
                <g:each var="subscriberType" in="${subscriberTypes}">
                    <g:render template="subscriberType" bean="${subscriberType}"/> 
                </g:each>
            </g:if>
        </div>
        <div id="typePrototype" style="display: none;">
            <g:render template="subscriberType" bean="[id: '{{id}}', name: '{{name}}']"/>
        </div>

        <g:form name="deleteSubscriberTypeForm" controller="subscriber" action="deleteSubscriberType">
            <g:hiddenField name="id"/>
        </g:form>
        <g:form name="updateSubscriberTypeForm" controller="subscriber" action="updateSubscriberType">
            <g:hiddenField name="id" id="updateSubscriberTypeId"/>
            <g:hiddenField name="name" id="updateSubscriberTypeName"/>
        </g:form>
    </body>
</html>
