<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="show.subscribersList.title"/></title>
        <meta name='layout' content='main'/>
        %{--
        <g:javascript library="jquery.form" />
        <g:javascript library="jquery.validate" />
        --}%
        
        <script type="text/javascript">
            $(document).ready(function() { App.subscribersListShow() })
        </script>
    </head>
    <body class="subscribersList">
        <div class="status" style="display: none;"> </div>
        <g:link controller="subscribersList"><g:message code="subscribersLists.other"/></g:link>

        <h2>${subscribersList.name}</h2>
        <g:if test="${subscribersList.description}">
            <div class="description">
                ${subscribersList.description}
            </div>
        </g:if>
        <g:link controller="subscribersList" action="edit" id="${subscribersList.id}"><g:message code="edit.details"/></g:link>
        <a href="javascript:void(0)" id="deleteSubscribersList"><g:message code="subscribersList.delete"/></a>

        <div id="removeNotion">
            <h3><g:message code="notion"/></h3>
            <g:message code="subscribersList.delete.notion.message"/>
            <br/>
            <g:link controller="subscribersList" action="delete" id="${subscribersList.id}"><g:message code="delete.confirm"/></g:link>
            <a href="javascript:void(0);" id="discardDeleteSubscribersList"><g:message code="delete.discard"/></a>
        </div>

        <div class="subscribers">
            List of subscribers goes here.
            / need to think to be fine both for dynamic and static lists and allow show more and edit list / 
        </div>
        <div class="campaigns">
            List of campaigns that used this list. 
        </div>
        %{--<g:form name="subscribersListForm" controller="subscribersList" action="update">
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
        </g:form>--}%
    </body>
</html>
