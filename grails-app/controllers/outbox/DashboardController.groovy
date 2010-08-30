package outbox

import grails.plugins.springsecurity.Secured

/**
 * Controller for dashboard page.
 */
@Secured(['ROLE_USER'])
class DashboardController {

    static defaultAction = 'index'

    def springSecurityService

    def index = {
        
    }
}
