<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title><g:message code="search.subscribers"/></title>
        <meta name='layout' content='main'/>
        <script type="text/javascript">
            $(document).ready(function() { App.subscriberSearch() })
        </script>
        <style type="text/css">
            .left_side { float: left; width: 350px; }
            .center { float: left; }

            .condition { padding: 3px; }
            .condition .header { height: 20px; background-color: #cccccc; padding: 7px; }
            .condition .header select { float: left; width: 200px; }
            .condition .header a { float: right; line-height: 25px; }
            .condition .row  { height: 20px; background-color: #efefef; padding: 5px;}
            .condition .row .title { float: left; width: 80px; font-weight: bold; line-height: 25px; text-align: right; margin: 0 5px;}
            .condition .row .value { float: left; margin: 0 5px;}
            .condition .row .value select { width: 200px; }
            .condition .row .value input { width: 190px; }
        </style>
    </head>
    <body>
        <div class="status" style="display: none;"> </div>
        <g:hiddenField name="url" value="${createLink(controller: 'searchConditions', action:'renderRow')}"/>

        <g:form name="searchForm" action="search">
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
                    <g:include controller="searchConditions" action="renderConditions"/>
                </div>
                <div class="c"></div>
                <a href="javascript:void(0);" id="addCondition"><g:message code="add"/></a>  |
                <a href="javascript:void(0);" id="search"><g:message code="search"/></a>
            </div>
            <div class="center">
                %{-- Search results --}%
                <g:if test="${subscribers && subscribers.list}">
                    <p>${readableConditions}</p>
                    <table>
                        <tr>
                            <th><g:sortColumn field="Email" class="page_callback"><g:message code="email"/></g:sortColumn></th>
                            <th><g:sortColumn field="LastName" class="page_callback"><g:message code="name"/></g:sortColumn></th>
                            <th><g:sortColumn field="CreateDate" class="page_callback"><g:message code="create.date"/></g:sortColumn></th>
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
                            callbackClass="page_callback" currentClass="current"/>
                </g:if>
            </div>

        </g:form>

    </body>
</html>
