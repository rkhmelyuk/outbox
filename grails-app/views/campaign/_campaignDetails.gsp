<g:form name="campaignForm" controller="campaign" action="update" onsubmit="return false;">
    <g:hiddenField name="id" value="${campaign.id}"/>
    <table>
        <tr>
            <td><g:message code="name"/></td>
            <td><g:textField name="name" maxlength="500" value="${campaign.name}" style="width:400px;"/></td>
        </tr>
        <tr>
            <td><g:message code="subject"/></td>
            <td><g:textField name="subject" maxlength="1000" value="${campaign.subject}" style="width:400px;"/></td>
        </tr>
        <tr>
            <td><g:message code="description"/></td>
            <td>
                <g:textArea name="description" value="${campaign.description}" style="width:400px; height: 100px"/>
                <br/>
                <g:message code="short.description.max.4000"/>
            </td>
        </tr>
        <tr>
            <td><g:message code="finish.date"/></td>
            <td><g:datePicker name="endDate" value="${campaign.endDate}"
                    years="${1900+new Date().year..2020}" default="none" 
                    noSelection="['':'']" precision="day"/>
            </td>
        </tr>
    </table>
    <a href="javascript:void(0);" id="updateCampaign"><g:message code="save.campaign"/></a>
</g:form>
