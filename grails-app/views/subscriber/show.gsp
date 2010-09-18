<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="show.subscriber.title"/></title>
        <meta name='layout' content='main'/>
    </head>
    <body>
        <g:if test="${subscriber}">
            <table>
                <tr>
                    <td><g:message code="firstName"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="firstName"/></td>
                </tr>
                <tr>
                    <td><g:message code="lastName"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="lastName"/></td>
                </tr>
                <tr>
                    <td><g:message code="email"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="email"/></td>
                </tr>
                <g:if test="${subscriber.subscriberType}">
                <tr>
                    <td><g:message code="subscriberType"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="subscriberType.name"/></td>
                </tr>
                </g:if>
                <tr>
                    <td><g:message code="gender"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="gender.name"/></td>
                </tr>
                <tr>
                    <td><g:message code="language"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="language.name"/></td>
                </tr>
                <tr>
                    <td><g:message code="timezone"/></td>
                    <td><g:fieldValue bean="${subscriber}" field="timezone.name"/></td>
                </tr>
                <tr>
                    <td><g:message code="enabled"/></td>
                    <td>${subscriber.enabled ? 'true' : 'false'}</td>
                </tr>
            </table>
            <g:link controller="subscriber" action="edit" id="${subscriber.id}"><g:message code="edit.details"/></g:link>  
        </g:if>
        <g:else>
            Subscriber is not found.
        </g:else>
    </body>
</html>
