beans = {

    userDetailsService(outbox.security.OutboxUserDetailsService) {
        sessionFactory = ref('sessionFactory')
        transactionManager = ref('transactionManager')
    }

    emailService(outbox.mail.EmailService)

    sendCampaignTaskProcessor(outbox.task.SendCampaignTaskProcessor) {
        campaignService = ref('campaignService')
        emailService = ref('emailService')
        trackingService = ref('trackingService')
    }
}
