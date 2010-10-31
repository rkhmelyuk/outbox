<script type="text/javascript">
    $(document).ready(function() { App.dynamicFields() })
</script>
<style type="text/css">
    #dynamicFields {
        width: 200px;
    }
    #dynamicFields .item {
        width: 200px;
        height: 30px;
        border: 1px solid #afafaf;
        margin: 2px 0;
        background-color: #ffffff;
    }

    #dynamicFields .item .handler {
        float: left;
        width: 10px;
        height: 26px;
        background-color: #dfdfdf;
        margin: 2px;
        cursor: move;
    }

    #dynamicFields .item .name {
        float: left;
        font-size: 14px;
        font-weight: bold;
    }

</style>
<div id="dynamicFields">
<g:each var="dynamicField" in="${dynamicFields}">
    <div class="item" id="${dynamicField.id}">
        <div class="handler"> </div>
        <div class="name"><g:fieldValue bean="${dynamicField}" field="label"/></div>
        <g:message code="${dynamicField.type?.messageCode}"/>
        <g:link class="edit" controller="dynamicField" action="edit" id="${dynamicField.id}"><g:message code="edit"/></g:link>
    </div>
</g:each>
</div>
<g:form name="reorderForm" controller="dynamicField" action="reOrderField">
    <g:hiddenField name="fieldId"/>
    <g:hiddenField name="afterFieldId"/>
</g:form>
%{--

        <tr>
            <td><g:fieldValue bean="${dynamicField}" field="label"/></td>
            <td><g:fieldValue bean="${dynamicField}" field="name"/></td>
            <td><g:message code="${dynamicField.type?.messageCode}"/></td>
            <td>
                <g:link class="edit" controller="dynamicField" action="edit" id="${dynamicField.id}">
                    <g:message code="edit"/></g:link>
            </td>
            <td></td>
        </tr>
--}%
