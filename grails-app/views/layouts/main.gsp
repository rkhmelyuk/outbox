<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
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
        <div class="ajaxError"> Server Error </div>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="grailsLogo"><a href="http://grails.org"><img src="${resource(dir:'images',file:'grails_logo.png')}" alt="Grails" border="0" /></a></div>

        <sec:ifLoggedIn>
            Hello <sec:loggedInUserInfo field="firstName"/> <sec:loggedInUserInfo field="lastName"/>
            | <g:link controller="profile"> MyProfile</g:link>
            | <g:link controller="logout">Logout</g:link>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:link controller="login">Login</g:link>
        </sec:ifNotLoggedIn>
        <br/>
        <div id="pageWrapper" class="${pageProperty(name: "body.class")}">
            <g:layoutBody />
        </div>
    </body>
</html>
