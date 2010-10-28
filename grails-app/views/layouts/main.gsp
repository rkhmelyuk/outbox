<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

        <title>Outbox: <g:layoutTitle default="Your Own Mail World" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'styles.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />

        <script type="text/javascript" src="${request.contextPath}/messages.js"></script>

        <g:javascript library="jquery" />
        <g:javascript library="application" />

        <g:layoutHead />
    </head>
    <body>
        <div class="ajaxError">Server Error</div>
        <div id="grailsLogo"><g:link controller="dashboard"><img src="${resource(dir:'images',file:'grails_logo.png')}" alt="Grails" border="0" /></g:link></div>

        <sec:ifLoggedIn>
            Hello <sec:loggedInUserInfo field="firstName"/> <sec:loggedInUserInfo field="lastName"/>
            | <g:link controller="profile"> <g:message code="my.profile"/></g:link>
            | <g:link controller="logout"><g:message code="logout"/></g:link>
        </sec:ifLoggedIn>
        <br/>
        <div id="pageWrapper" class="${pageProperty(name: "body.class")}">
            <g:layoutBody />
        </div>
    </body>
</html>
