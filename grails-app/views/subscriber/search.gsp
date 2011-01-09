<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <title><g:message code="search.subscribers"/></title>
    <meta name='layout' content='main'/>

    <g:javascript library="jquery.form"/>

    <script type="text/javascript">
        $(document).ready(function() { App.subscriberSearch() })
    </script>
</head>
<body>
    <div class="status" style="display: none;"> </div>
    <g:hiddenField name="url" value="${createLink(controller: 'subscriberSearch', action:'renderRow')}"/>

    <g:form name="searchForm" controller="subscriberSearch" action="searchResults">
        <g:hiddenField name="perPage" value="5"/>
        <g:if test="${conditions.orders?.size()}">
            <g:set var="order" value="${conditions.orders.first()}"/>
            <g:hiddenField name="column" value="${order?.column}"/>
            <g:hiddenField name="sort" value="${order?.sort?.keyword}"/>
        </g:if>
        <g:else>
            <g:hiddenField name="column" value=""/>
            <g:hiddenField name="sort" value=""/>
        </g:else>

        <div class="left_side">
            %{-- Search conditions --}%
            <div id="conditions">
                <g:include controller="subscriberSearch" action="renderConditions"/>
            </div>
            <div class="c"></div>
            <a href="javascript:void(0);" id="addCondition"><g:message code="add"/></a>  |
            <a href="javascript:void(0);" id="search"><g:message code="search"/></a>
        </div>
        <div class="center">
            %{-- Search results --}%
            <g:render template="/subscriberSearch/searchResult"/>
        </div>

    </g:form>

</body>
