<div class="condition">
    <input type="hidden" name="row" value="${row}"/>
    <div class="header">
        <g:select name="row[${row}].type" from="${types}" optionKey="key" optionValue="value" value="${type}"/>

        <a href="javascript:void(0);" class="removeCondition"><g:message code="remove"/></a>
    </div>
    <div class="c"></div>
    <div class="row">
        <div class="title"><label for="row[${row}].field">Field</label></div>
        <div class="value">
            <g:select name="row[${row}].field" from="${dynamicFields}"
                    optionKey="id" optionValue="label"
                    value="${field}" noSelection="['':'']"/>
        </div>
    </div>
    <g:if test="${field}">
        <div class="c"></div>
        <div class="row">
            <div class="title"><label for="row[${row}].comparison">Comparison</label></div>
            <div class="value">
                <g:select name="row[${row}].comparison" from="${comparisons}"
                        optionKey="key" optionValue="value"
                        value="${comparison}" noSelection="['':'']"/>
            </div>
        </div>
        <g:if test="${showValue}">
            <div class="c"></div>
            <div class="row">
                <div class="title"><label for="row[${row}].value">Value</label></div>
                <div class="value">
                    <g:if test="${values}">
                        <g:select name="row[${row}].value" from="${values}" optionKey="id" optionValue="name" value="${value}"/>
                    </g:if>
                    <g:else>
                        <g:textField name="row[${row}].value" maxlength="1000" value="${value}"/>
                    </g:else>
                </div>
            </div>
        </g:if>
    </g:if>
</div>