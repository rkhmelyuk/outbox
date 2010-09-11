class UrlMappings {

	static mappings = {
        "/subscriber/$id"(controller: 'subscriber', action:'show') {
            constraints {
                id matches: /[0-9a-fA-F]{40}/
            }
        }

        "/list"(controller: 'subscribersList', action: 'list')
        "/list/$action?/$id?"(controller: 'subscribersList')
        
        '/profile'(controller: 'profile', action: 'edit')
        "/$controller"(action: 'index')
		"/$controller/$action?/$id?"()

		'/'(controller:'dashboard')
		'500'(view:'/500')
	    '404'(view:'/404')
	    '403'(view:'/403')
        '/messages'(uri:'/messages.gsp')
	}
}
