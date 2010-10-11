<span style="font-size: 16px;">Opened: ${opened}</span>
<br/>
<span style="font-size: 16px;">Not Opened: ${notOpened}</span>
<br/>
<span style="font-size: 16px;">Total Openings: ${totalOpens}</span>
<br/>
<span style="font-size: 16px;">Total Clicks: ${totalClicks}</span>

<br/>
<g:if test="${opensByDate}">
    <h2>Openings By Date</h2>
    <table>
        <g:each var="row" in="${opensByDate}">
        <tr>
            <td><g:formatDate date="${row.date}" format="dd EEE, HH:mm"/></td>
            <td><g:formatNumber number="${row.opens}"/></td>
        </tr>
        </g:each>
    </table>
</g:if>
<br/>
<g:if test="${clicksByDate}">
    <h2>Clicks By Date</h2>
    <table>
        <g:each var="row" in="${clicksByDate}">
        <tr>
            <td><g:formatDate date="${row.date}" format="dd EEE, HH:mm"/></td>
            <td><g:formatNumber number="${row.clicks}"/></td>
        </tr>
        </g:each>
    </table>
</g:if>
