<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="edit.subscribersList.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscribersListEdit() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="subscribersListForm" controller="subscribersList" action="update">
            <g:hiddenField name="id" value="${subscribersList.id}"/>
            <table>
                <tr>
                    <td><g:message code="name"/></td>
                    <td><g:textField name="name" maxlength="200" value="${subscribersList.name}"/></td>
                </tr>
                <tr>
                    <td><g:message code="description"/></td>
                    <td><g:textArea name="description" value="${subscribersList.description}"/></td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="editSubscribersList"><g:message code="save.subscribersList"/></a>
            &nbsp;&nbsp;
            <g:link controller="subscribersList"><g:message code="cancel"/></g:link>
        </g:form>
    </body>
</html>
