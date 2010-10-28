<%@ page import="outbox.subscriber.field.DynamicFieldType" %>
<script type="text/javascript">
    $(document).ready(function() { App.createDynamicField() })
</script>
<g:form name="createForm" action="add" onsubmit="return false;">
    <table>
        <tr>
            <td><label for="label"><g:message code="label"/></label></td>
            <td><g:textField name="label" value="${dynamicField?.name}"/></td>
        </tr>
        <tr>
            <td><label for="type"><g:message code="type"/></label></td>
            <td>
                <g:select name="type" from="${DynamicFieldType.values()}"
                        optionKey="id" optionValue="message"
                        value="${dynamicField?.type?.id}"/>
            </td>
        </tr>
        <tr>
            <td><label for="mandatory"><g:message code="mandatory"/></label></td>
            <td><g:checkBox name="mandatory" value="true" checked="${dynamicField?.mandatory}"/>
            </td>
        </tr>
    </table>
    <a href="javascript:void(0);" id="add"><g:message code="add"/></a>
    <a href="javascript:void(0);" id="cancel"><g:message code="cancel"/></a>
</g:form>