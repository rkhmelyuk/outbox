<table>
    <tr>
        <td><g:message code="label"/></td>
        <td><g:message code="name"/></td>
        <td><g:message code="type"/></td>
        <td><g:message code="edit"/></td>
        <td><g:message code="delete"/></td>
    </tr>
    <g:each var="dynamicField" in="${dynamicFields}">
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
    </g:each>
</table>