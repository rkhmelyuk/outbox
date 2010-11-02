<%@ page import="outbox.subscriber.field.DynamicFieldType" %>
<script type="text/javascript">
    $(document).ready(function() { App.dynamicFields() })
</script>
<div id="dynamicFields">
<g:each var="dynamicField" in="${dynamicFields}">
    <div class="item" id="${dynamicField.id}">
        <div class="handler"> </div>
        <div class="name">
            <g:fieldValue bean="${dynamicField}" field="label"/>
        </div>
        <div class="type">
            <g:message code="${dynamicField.type?.messageCode}"/>
        </div>
        <div class="column">
            <g:if test="${dynamicField.mandatory}"><g:message code="mandatory"/></g:if>
        </div>
        <div class="column">
            %{--<g:if test="${dynamicField.hidden}"><g:message code="hidden"/></g:if>--}%
        </div>
        <div class="editLink">
            <g:link class="edit" controller="dynamicField" action="edit" id="${dynamicField.id}"><g:message code="edit"/></g:link>
        </div>
    </div>
</g:each>
</div>

<g:form name="reorderForm" controller="dynamicField" action="move">
    <g:hiddenField name="fieldId"/>
    <g:hiddenField name="afterFieldId"/>
</g:form>
<g:form name="removeForm" controller="dynamicField" action="remove">
    <g:hiddenField name="fieldId"/>
</g:form>

<div style="display:none">
    <a id="removeConfirm" href="#removeConfirmContent"><g:message code="delete"/></a>
    <div id="removeConfirmContent">
        <h2>Remove Field</h2>
        There can be related subscriber data with this dynamic field.<br/>
        After removing, this data will be removed and there will be no opportunity to restore them.<br/>
        <br/>
        <b>Do you really want to remove this dynamic field?</b><br/>
        <br/>

        <a href="javascript:void(0);" style="float: left;" id="hideField"><g:message code="just.hide.field" /></a>
        <div style="float: right;">
            <a href="javascript:void(0);" id="cancelRemove"><g:message code="cancel"/></a> |
            <a href="javascript:void(0);" id="removeField"><g:message code="delete.now"/></a>
        </div>
    </div>
</div>
