class UrlMappings {

	static mappings = {
        "/subscriber/$id"(controller: 'subscriber', action: 'show') {
            constraints {
                id matches: /[0-9a-fA-F]{40}/
            }
        }

        "/list/$id?"(controller: 'subscribersList', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }
        "/list/$action?/$id?"(controller: 'subscribersList')
        "/list"(controller: 'subscribersList', action: 'list')

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
