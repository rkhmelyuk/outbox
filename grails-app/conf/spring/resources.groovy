beans = {

    userDetailsService(outbox.security.OutboxUserDetailsService) {
        sessionFactory = ref('sessionFactory')
        transactionManager = ref('transactionManager')
    }
}
