class UrlMappings {

	static mappings = {
        
        '/members'(controller: 'member', action: 'list')
        '/subscribers'(controller: 'subscriptionList', action: 'freeSubscribers')
        '/templates'(controller: 'template', action: 'list')
        '/campaigns'(controller: 'campaign', action: 'index')
        '/list'(controller: 'subscriptionList', action: 'list')

        "/subscriber/$id"(controller: 'subscriber', action: 'show') {
            constraints {
                id matches: /[0-9a-fA-F]{40}/
            }
        }
        "/list/$id?"(controller: 'subscriptionList', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }
        "/template/$id"(controller: 'template', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }
        "/campaign/$id/$page?"(controller: 'campaign', action: 'show') {
            constraints {
                id matches: /\d+/
            }
        }

        "/tracking/$id"(controller: 'tracking', action: 'track')
        "/list/$action?/$id?"(controller: 'subscriptionList')
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
