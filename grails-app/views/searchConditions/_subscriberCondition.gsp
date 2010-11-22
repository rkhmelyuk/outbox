<div class="condition">
    <input type="hidden" name="row" value="${row}"/>
    <label for="row[${row}].type">Condition</label>
    <g:select name="row[${row}].type" from="${types}" optionKey="key" optionValue="value" value="${type}"/>
    <label for="row[${row}].field">Field</label>
    <g:select name="row[${row}].field" from="${fields}" optionKey="key" optionValue="value" value=""/>
    <label for="row[${row}].comparison">Comparison</label>
    <g:select name="row[${row}].comparison" from="${comparisons}" optionKey="key" optionValue="value" value=""/>

    <a href="javascript:void(0);" class="removeCondition"><g:message code="remove"/></a>
</div>