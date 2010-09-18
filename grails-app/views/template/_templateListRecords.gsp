<g:link controller="template" action="show" id="${template.id}">
    <div class="template">
        <span class="name"><g:fieldValue bean="${template}" field="name"/></span>
        <span class="created"><g:formatDate date="${template.dateCreated}" format="d MMM yyyy"/></span>
    </div>
</g:link>
