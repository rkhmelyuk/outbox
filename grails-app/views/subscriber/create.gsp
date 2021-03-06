<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="create.subscriber.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscriberCreate() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="subscriberForm" controller="subscriber" action="add" onsubmit="return false;">
            <g:hiddenField name="listId" value="${listId}"/>
            <table>
                <tr>
                    <td><g:message code="email"/></td>
                    <td><g:textField name="email" maxlength="512" value="${subscriber.email}"/></td>
                </tr>
                <tr>
                    <td colspan="2"><h3><g:message code="optional.fields"/></h3></td>
                </tr>
                <tr>
                    <td><g:message code="title"/></td>
                    <td>
                        <g:select name="namePrefix" from="${NamePrefix.list()}"
                            optionKey="id" optionValue="name" noSelection="['':'']"
                            value="${subscriber.namePrefix?.id}"/>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="firstName"/></td>
                    <td><g:textField name="firstName" maxlength="100" value="${subscriber.firstName}"/></td>
                </tr>
                <tr>
                    <td><g:message code="lastName"/></td>
                    <td><g:textField name="lastName" maxlength="100" value="${subscriber.lastName}"/></td>
                </tr>
                <g:if test="${subscriberTypes}">
                <tr>
                    <td><g:message code="subscriberType"/></td>
                    <td>
                        <g:select name="subscriberType" from="${subscriberTypes}"
                                value="${subscriber.subscriberType?.id}"
                                optionKey="id" optionValue="name" noSelection="['':'']"/>
                    </td>
                </tr>
                </g:if>
                <tr>
                    <td><g:message code="gender"/></td>
                    <td>
                        <g:select name="gender" from="${Gender.list()}"
                                value="${subscriber.gender?.id}"
                                optionKey="id" optionValue="name" noSelection="['':'']"/>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="language"/></td>
                    <td>
                        <g:select name="language" from="${Language.list()}"
                                value="${subscriber.language?.id}"
                                optionKey="id" optionValue="name" noSelection="['':'']"/>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="timezone"/></td>
                    <td>
                        <g:select name="timezone" from="${Timezone.list()}"
                                value="${subscriber.timezone?.id}"
                                optionKey="id" optionValue="name" noSelection="['':'']"/>
                    </td>
                </tr>
                <tr>
                    <td><label for="enabled"><g:message code="subscriber.enabled"/></label></td>
                    <td>
                        <g:checkBox name="enabled" checked="${true}" value="${subscriber.enabled}"/>
                        <g:message code="subscriber.enabled.hint"/>
                    </td>
                </tr>
                <g:each var="element" in ="${dynamicFieldsForm.elements}">
                    <tr>
                        <td><app:renderElement element="${element.label}"/></td>
                        <td><app:renderElement element="${element}"/></td>
                    </tr>
                </g:each>
            </table>
            <a href="javascript:void(0);" id="addSubscriber"><g:message code="create.subscriber"/></a>
            &nbsp;&nbsp;
            <g:if test="${listId}">
                <g:link controller="subscriptionList" action="show" id="${listId}"><g:message code="cancel"/></g:link>
            </g:if>
            <g:else>
                <g:link controller="subscriptionList" action="freeSubscribers"><g:message code="cancel"/></g:link>
            </g:else> 
        </g:form>
    </body>
</html>
