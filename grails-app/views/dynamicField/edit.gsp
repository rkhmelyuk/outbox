<%@ page import="outbox.subscriber.field.DynamicFieldStatus; outbox.subscriber.field.DynamicFieldType" %>
<script type="text/javascript">
    $(document).ready(function() { App.createEditDynamicField() })
</script>
<script id="selectValueItemTemplate" type="text/x-jquery-tmpl">
    <li>
        <input type="hidden" id="selectValueIds" name="selectValueIds" value=""/>
        <input type="hidden" id="selectValueLabels" name="selectValueLabels" value="{{= value}}"/>
        <div class="viewItem">
            <div class="itemName">{{= value}}</div>
            <a href="javascript:void(0);" class="removeSelectedValue"><g:message code="remove"/></a>
        </div>
    </li>
</script>
<script id="editSelectValueItemTemplate" type="text/x-jquery-tmpl">
    <div class="c"></div>
    <div class="editItem">
        <div class="itemName">
            <input id="itemNameLabel" type="text" maxlength="200" value="{{= value}}"/>
        </div>
        <a href="javascript:void(0);" id="saveEditSelectedValue"><g:message code="save"/></a>
        <a href="javascript:void(0);" id="cancelEditSelectedValue"><g:message code="cancel"/></a>
    </div>
    <div class="c"></div>
</script>

<div style="width: 650px; height: 400px;">
    <h2>Edit Dynamic Field</h2>
    <div class="status" style="display: none;"> </div>
    <g:form name="saveForm" action="update" onsubmit="return false;" id="${dynamicField.id}">
        <table>
            <tr>
                <td><label for="label"><g:message code="label"/></label></td>
                <td><g:textField name="label" value="${dynamicField.label}" maxlength="200"/></td>
            </tr>
            <tr>
                <td><label for="name"><g:message code="name"/></label></td>
                <td><g:textField name="name" value="${dynamicField.name}" maxlength="200"/></td>
            </tr>
            <tr>
                <td><label for="type"><g:message code="type"/></label></td>
                <td>
                    <g:select name="type" from="${DynamicFieldType.values()}"
                            optionKey="id" optionValue="message"
                            value="${dynamicField.type.id}"/>
                </td>
            </tr>
            <tr>
                <td><label for="visible"><g:message code="visible"/></label></td>
                <td><g:checkBox name="visible" value="true" checked="${dynamicField.status == DynamicFieldStatus.Active}"/></td>
            </tr>
            <tr>
                <td><label for="mandatory"><g:message code="mandatory"/></label></td>
                <td><g:checkBox name="mandatory" value="true" checked="${dynamicField.mandatory}"/></td>
            </tr>
            <tr class="constraint constraint_2">
                <td><label for="min"><g:message code="minimal.value"/></label></td>
                <td><g:textField name="min" value="${formatNumber(number: dynamicField.min, format: '###########.##')}" maxlength="14"/></td>
            </tr>
            <tr class="constraint constraint_2">
                <td><label for="max"><g:message code="maximal.value"/></label></td>
                <td><g:textField name="max" value="${formatNumber(number: dynamicField.max, format: '###########.##')}" maxlength="14"/></td>
            </tr>
            <tr class="constraint constraint_1">
                <td><label for="maxlength"><g:message code="maxlength"/></label></td>
                <td><g:textField name="maxlength" value="${formatNumber(number: dynamicField.maxlength, format: '####')}" maxlength="4"/><</td>
            </tr>
            <tr class="constraint constraint_4">
                <td><g:message code="values.list"/></td>
                <td>
                    <h4><g:message code="add.new.value" /></h4>
                    <input type="text" id="newSelectValue" maxlength="200"/>
                    <a href="javascript:void(0);" id="addNewSelectValue"><g:message code="add"/></a><br/>
                    <h4><g:message code="items"/></h4>
                    <ul id="selectValues">
                        <g:each var="item" in="${dynamicFieldItems}">
                            <li>
                                <g:hiddenField name="selectValueIds" value="${item.id}"/>
                                <g:hiddenField name="selectValueLabels" value="${item.name}"/>
                                <div class="viewItem">
                                    <div class="itemName">${item.name}</div>
                                    <a href="javascript:void(0);" class="removeSelectedValue"><g:message code="remove"/></a>
                                </div>
                            </li>
                        </g:each>
                    </ul>
                </td>
            </tr>
        </table>
        <a href="javascript:void(0);" id="save"><g:message code="save"/></a>
        <a href="javascript:void(0);" id="cancel"><g:message code="cancel"/></a>
    </g:form>
</div>