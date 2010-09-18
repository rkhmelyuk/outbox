<div class="type">
    <input type="hidden" name="id" value="${it.id}"/>
    <span class="editType">
        <input type="text" name="name" value="${it.name?.encodeAsHTML()}" maxlength="200" class="editNameInput"/>
        <a href="javascript:void(0);" class="updateSubscriberType"><g:message code="save"/></a>
        <a href="javascript:void(0);" class="cancelEditSubscriberType"><g:message code="cancel"/></a>
    </span>
    <span class="name">${it.name?.encodeAsHTML()}</span>
    <span class="remove">
        <a href="javascript:void(0);" class="removeSubscriberType"><g:message code="remove"/></a>
    </span>
</div>
