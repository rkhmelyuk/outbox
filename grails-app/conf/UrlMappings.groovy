class UrlMappings {

	static mappings = {
        "/subscriber/$id"(controller: 'subscriber', action: 'show') {
            constraints {
                id matches: /[0-9a-fA-F]{40}/
            }
        }

        "/subscribers"(controller: 'subscriptionList', action: 'freeSubscribers')
        "/list/$id?"(controller: 'subscriptionList', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }
        "/list/$action?/$id?"(controller: 'subscriptionList')
        '/list'(controller: 'subscriptionList', action: 'list')
        '/template'(controller: 'template', action: 'list')
        "/template/$id?"(controller: 'template', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }

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
