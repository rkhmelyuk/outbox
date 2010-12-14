<div class="condition">
    <input type="hidden" name="row" value="${row}"/>
    <div class="header">
        <g:select name="row[${row}].type" from="${types}" optionKey="key" optionValue="value" value="${type}"/>
        <a href="javascript:void(0);" class="removeCondition"><g:message code="remove"/></a>
    </div>

    <div class="c"></div>
    <div class="row">
        <div class="title"><label for="row[${row}].subscriptionType"><g:message code="condition" /></label></div>
        <div class="value">
            <g:select name="row[${row}].subscriptionType" from="${subscriptionTypes}"
                    optionKey="key" optionValue="value" value="${subscriptionType}" />
        </div>
    </div>

    <div class="c"></div>
    <div class="row">
        <div class="title"><label for="row[${row}].value">List</label></div>
        <div class="value">
            <g:select name="row[${row}].subscriptionList" from="${subscriptionLists}"
                    optionKey="id" optionValue="name" value="${subscriptionList}"/>
        </div>
    </div>
</div>