<g:if test="${needTemplate || needSubscribers}">
    <div class="note need">
        <g:if test="${needSubscribers}">
            <div class="item">
                You need to <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'subscribers']">add subscribers</g:link> to start this campaign.
            </div>
        </g:if>
        <g:if test="${needTemplate}">
            <div class="item">
                You need to <g:link controller="campaign" action="show" id="${campaign.id}" params="[page: 'template']">setup template</g:link> to start this campaign.
            </div>
        </g:if>
    </div>
</g:if>
