class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		'/'(controller:'dashboard')
		'500'(view:'/error')
	    "404"(view:'/error404')
        '/messages'(uri:'/messages.gsp')
	}
}
