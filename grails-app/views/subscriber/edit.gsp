<%@ page import="outbox.dictionary.NamePrefix; outbox.dictionary.Gender; outbox.dictionary.Timezone; outbox.dictionary.Language" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="edit.subscriber.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscriberEdit() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="subscriberForm" controller="subscriber" action="update" onsubmit="return false;">
            <g:hiddenField name="id" value="${subscriber.id}"/>
            <table>
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
                <tr>
                    <td><g:message code="email"/></td>
                    <td><g:textField name="email" maxlength="512" value="${subscriber.email}"/></td>
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
                        <g:checkBox name="enabled" checked="${subscriber.enabled}" value="true"/>
                        <g:message code="subscriber.enabled.hint"/>
                    </td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="editSubscriber"><g:message code="save.subscriber"/></a>
            &nbsp;&nbsp;
            <g:link controller="subscriber" action="show" id="${subscriber.id}"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
