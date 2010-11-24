<div class="condition">
    <input type="hidden" name="row" value="${row}"/>

    <label for="row[${row}].type">Condition</label>
    <g:select name="row[${row}].type" from="${types}"
            optionKey="key" optionValue="value"
            value="${type}"/>

    <label for="row[${row}].field">Field</label>
    <g:select name="row[${row}].field" from="${dynamicFields}"
            optionKey="id" optionValue="label"
            value="${field}" noSelection="['':'']"/>

    <g:if test="${field}">
        <label for="row[${row}].comparison">Comparison</label>
        <g:select name="row[${row}].comparison" from="${comparisons}"
                optionKey="key" optionValue="value"
                value="${comparison}" noSelection="['':'']"/>

        <g:if test="${showValue}">
            <label for="row[${row}].value">Value</label>
            <g:if test="${values}">
                <g:select name="row[${row}].value" from="${values}"
                        optionKey="id" optionValue="name" value="${value}"/>
            </g:if>
            <g:else>
                <g:textField name="row[${row}].value" maxlength="1000" value="${value}"/>
            </g:else>
        </g:if>
    </g:if>

    <a href="javascript:void(0);" class="removeCondition"><g:message code="remove"/></a>
</div>