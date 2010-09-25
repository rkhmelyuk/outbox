<div id="template">
    <g:if test="${campaign.notStarted && proposedTemplates}">
        <g:form name="useTemplateForm" action="selectTemplate" onsubmit="return false;">
            <g:hiddenField name="campaignId" value="${campaign.id}"/>
            <g:message code="select.template"/>
            <g:select from="${proposedTemplates}" optionKey="id" optionValue="name" name="template"/>
            <a href="javascript:void(0);" id="useTemplate"><g:message code="use"/></a>
            <g:link controller="template" action="create" params="[campaign: campaign.id]"><g:message code="add.new.template"/></g:link>
        </g:form>
    </g:if>
    <g:if test="${campaign.template}">
        <g:message code="current.template"/><br/>
        <g:link controller="template" action="show" id="${campaign.template.id}">
            <g:fieldValue bean="${campaign}" field="template.name"/>
        </g:link>
        <br/>
        <div class="templateContent">
            ${campaign.template.templateBody}
        </div>
    </g:if>
    <g:if test="${!proposedTemplates && !campaign.template}">
        <g:message code="no.templates"/>
        <g:link controller="template" action="create" params="[campaign: campaign.id]"><g:message code="add.template"/></g:link>
    </g:if>
</div>
