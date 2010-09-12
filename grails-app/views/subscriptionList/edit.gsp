<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="edit.subscriptionList.title"/></title>
        <meta name='layout' content='main'/>
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />

        <script type="text/javascript">
            $(document).ready(function() { App.subscriptionListEdit() })
        </script>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:form name="subscriptionListForm" controller="subscriptionList" action="update" onsubmit="return false;">
            <g:hiddenField name="id" value="${subscriptionList.id}"/>
            <table>
                <tr>
                    <td><g:message code="name"/></td>
                    <td><g:textField name="name" maxlength="200" value="${subscriptionList.name}"/></td>
                </tr>
                <tr>
                    <td><g:message code="description"/></td>
                    <td><g:textArea name="description" value="${subscriptionList.description}"/></td>
                </tr>
            </table>
            <a href="javascript:void(0);" id="editSubscriptionList"><g:message code="save.subscriptionList"/></a>
            &nbsp;&nbsp;
            <g:link controller="subscriptionList" action="show" id="${subscriptionList.id}"><g:message code="back"/></g:link>
        </g:form>
    </body>
</html>
