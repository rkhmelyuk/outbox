<script type="text/javascript">
    $(document).ready(function() { App.dynamicFields() })
</script>
<div id="dynamicFields">
<g:each var="dynamicField" in="${dynamicFields}">
    <div class="item" id="${dynamicField.id}">
        <div class="handler"> </div>
        <div class="name">
            <g:fieldValue bean="${dynamicField}" field="label"/>
            <g:if test="${dynamicField.mandatory}">*</g:if>
        </div>
        <g:message code="${dynamicField.type?.messageCode}"/>
        <g:link class="edit" controller="dynamicField" action="edit" id="${dynamicField.id}"><g:message code="edit"/></g:link>
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
