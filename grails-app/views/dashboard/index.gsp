<head>
    <meta name='layout' content='main' />
    <style type="text/css" media="screen">

        #nav {
            margin-top:20px;
            margin-left:30px;
            width:228px;
            float:left;

        }
        .homePagePanel * {
            margin:0px;
        }
        .homePagePanel .panelBody ul {
            list-style-type:none;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody h1 {
            text-transform:uppercase;
            font-size:1.1em;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody {
            background: url(images/leftnav_midstretch.png) repeat-y top;
            margin:0px;
            padding:15px;
        }
        .homePagePanel .panelBtm {
            background: url(images/leftnav_btm.png) no-repeat top;
            height:20px;
            margin:0px;
        }

        .homePagePanel .panelTop {
            background: url(images/leftnav_top.png) no-repeat top;
            height:11px;
            margin:0px;
        }
        h2 {
            margin-top:15px;
            margin-bottom:15px;
            font-size:1.2em;
        }
        #pageBody {
            margin-left:280px;
            margin-right:20px;
        }
        </style>
    <title>Dashboard</title>
</head>
<body>
    <div id="nav">
        <div class="homePagePanel">
            <div class="panelTop"></div>
            <div class="panelBody">
                <h1>Application Status</h1>
                <ul>
                    <li><g:link controller="member" action="list">Members</g:link></li>
                    <li><g:link controller="subscriber" action="types">Subscriber Types</g:link></li>
                    <li><g:link controller="dynamicField" action="index">Dynamic Fields</g:link></li>
                    <li><g:link controller="subscriptionList" action="list">Subscriptions</g:link></li>
                    <li><g:link controller="template" action="list">Templates</g:link></li>
                    <li><g:link controller="campaign" action="index">Campaigns</g:link></li>
                </ul>
            </div>
            <div class="panelBtm"></div>
        </div>
    </div>
    <div id="pageBody">
        <h1>Welcome to Grails</h1>
        <p>Congratulations, you have successfully started your first Grails application! At the moment
        this is the default page, feel free to modify it to either redirect to a controller or display whatever
        content you may choose. Below is a list of controllers that are currently deployed in this application,
        click on each to execute its default action:</p>
    </div>
</body>
