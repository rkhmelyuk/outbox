<%@ page import="outbox.subscriber.field.DynamicFieldType" %>
<script type="text/javascript">
    $(document).ready(function() { App.createDynamicField() })
</script>
<div style="width: 650px; height: 400px;">
    <div class="status" style="display: none;"> </div>
    <g:form name="createForm" action="add" onsubmit="return false;">
        <table>
            <tr>
                <td><label for="label"><g:message code="label"/></label></td>
                <td><g:textField name="label" value="${dynamicField?.label}" maxlength="200"/></td>
            </tr>
            <tr>
                <td><label for="name"><g:message code="name"/></label></td>
                <td><g:textField name="name" value="${dynamicField?.name}" maxlength="200"/></td>
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
                <td><g:checkBox name="mandatory" value="true" checked="${dynamicField?.mandatory}"/></td>
            </tr>
            <tr class="constraint constraint_2">
                <td><label for="min"><g:message code="minimal.value"/></label></td>
                <td><g:textField name="min" value="${dynamicField?.min}" maxlength="10"/></td>
            </tr>
            <tr class="constraint constraint_2">
                <td><label for="max"><g:message code="maximal.value"/></label></td>
                <td><g:textField name="max" value="${dynamicField?.max}" maxlength="10"/></td>
            </tr>
            <tr class="constraint constraint_1">
                <td><label for="maxlength"><g:message code="maxlength"/></label></td>
                <td><g:textField name="maxlength" value="${dynamicField?.maxlength}" maxlength="4"/></td>
            </tr>
            <tr class="constraint constraint_4">
                <td><g:message code="values.list"/></td>
                <td>
                    <g:message code="add.new.value" /><br/>
                    <input type="text" id="newSelectValue" maxlength="200"/>
                    <a href="javascript:void(0);" id="addNewSelectValue"><g:message code="add"/></a>
                    <ul id="selectValues">
                    <g:if test="${selectValues}">
                        <g:each in="${selectValues}" var="value" status="index">
                            <li>
                                <g:hiddenField name="selectValue" value="${value.label}"/>
                                ${value.encodeAsHTML()}
                            </li>
                        </g:each>
                    </g:if>
                    </ul>
                </td>
            </tr>
        </table>
        <a href="javascript:void(0);" id="add"><g:message code="add"/></a>
        <a href="javascript:void(0);" id="cancel"><g:message code="cancel"/></a>
    </g:form>
</div>