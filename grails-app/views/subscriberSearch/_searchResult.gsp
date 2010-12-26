<div id="searchResult">
    <p>${readableConditions}</p>
    <g:if test="${subscribers && subscribers.list}">
        <table>
            <tr>
                <th><g:sortColumn field="Email" class="search_callback"><g:message code="email"/></g:sortColumn></th>
                <th><g:sortColumn field="LastName" class="search_callback"><g:message code="name"/></g:sortColumn></th>
                <th><g:sortColumn field="CreateDate" class="search_callback"><g:message code="create.date"/></g:sortColumn></th>
            </tr>
            <g:each in="${subscribers.list}" var="subscriber">
                <tr>
                    <td><g:link controller="subscriber" action="show" id="${subscriber?.id}">
                        <g:fieldValue bean="${subscriber}" field="email" /></g:link></td>
                    <td><g:fieldValue bean="${subscriber}" field="fullName"/></td>
                    <td><g:formatDate date="${subscriber?.dateCreated}" format="d MMMMM yyyy"/></td>
                </tr>
            </g:each>
        </table>

        <g:formPagination name="page" total="${subscribers.total}" max="${subscribers.perPage}"
                page="${subscribers.page}" maxsteps="10"
                callbackClass="search_callback" currentClass="current"/>
    </g:if>
</div>