<script type="text/javascript">
    $(document).ready(function() {
        <r:openedNotOpened container='opened-notOpened' opened="${opened}" notOpened="${notOpened}"/>
        <r:opensClicks container='opens-clicks' opens="${opensByDate}" clicks="${clicksByDate}" period="${period}"/>
    });
</script>

<div id="opens-clicks-report" style="width: 800px;">
    <div class="opens-clicks-legend">
        <div class="item" title="<g:message code="opens.title"/>">
            <div class="opensRectangleSmall"></div>
            <div class="title"><g:message code="opens"/></div>
        </div>
        <div class="item" title="<g:message code="clicks.title"/>">
            <div class="clicksRectangleSmall"></div>
            <div class="title"><g:message code="clicks"/></div>
        </div>
    </div>
    <div style="clear: both;"></div>
    <div id="opens-clicks" style="width: 800px; height: 180px"></div>
</div>
<div style="clear: both;"></div>

<div style="padding-top: 25px;">
    <div class="openedNotOpenedChart">
        <div id="opened-notOpened" style="width: 200px; height: 200px"></div>
    </div>
    <div class="openedNotOpened">
        <div class="item">
            <div class="opensRectangle"></div>
            <div class="title"><g:message code="opened"/></div>
            <span class="value"><g:formatNumber number="${opened}"/></span>
            <span class="percent">
                <g:formatNumber number="${(opened / (notOpened + opened))}"
                    type="percent" maxFractionDigits="1"/></span>
        </div>
        <div class="item">
            <div class="notOpenedRectangle"></div>
            <div class="title"><g:message code="notOpened"/></div>
            <span class="value"><g:formatNumber number="${notOpened}"/></span>
            <span class="percent">
                <g:formatNumber number="${(notOpened / (notOpened + opened))}"
                        type="percent" maxFractionDigits="1"/></span>
        </div>
    </div>
    <div class="openedNotOpened">
        <div class="item">
            <div class="opensRectangle"></div>
            <div class="title"><g:message code="totalOpens"/></div>
            <span class="value"><g:formatNumber number="${totalOpens}"/></span>
        </div>
        <div class="item">
            <div class="clicksRectangle"></div>
            <div class="title"><g:message code="totalClicks"/></div>
            <span class="value"><g:formatNumber number="${totalClicks}"/></span>
        </div>
    </div>
</div>
