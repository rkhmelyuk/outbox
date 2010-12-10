import outbox.AppConstant

beans = {

    userDetailsService(outbox.security.OutboxUserDetailsService) {
        sessionFactory = ref('sessionFactory')
        transactionManager = ref('transactionManager')
    }

    emailService(outbox.mail.EmailService)

    configuration(freemarker.template.Configuration) {
        defaultEncoding = 'UTF-8'
        outputEncoding = 'UTF-8'
        dateFormat = 'd MMMMM yyyy'
        dateTimeFormat = 'd MMMMM yyyy, HH:mm'
        timeFormat = 'HH:mm'
        strictSyntaxMode = false
        tagSyntax = freemarker.template.Configuration.ANGLE_BRACKET_TAG_SYNTAX
        whitespaceStripping = true
    }
    
    freemarkerTemplateService(outbox.template.FreemarkerTemplateService) {
        configuration = ref('configuration')
    }

    trackingLinkBuilder(outbox.template.builder.TrackingLinkBuilder) {
        grailsApplication = ref('grailsApplication')
    }

    templateTrackingHeaderFilter(outbox.template.builder.TemplateTrackingHeaderFilter) {
        trackingHeader = "<img src='${AppConstant.OPEN_PING_RESOURCE}' width='1' height='1' alt=''/>"
    }

    templateLinkFilter(outbox.template.builder.TemplateLinkFilter) {
        trackingLinkBuilder = ref('trackingLinkBuilder')
    }

    templateImageFilter(outbox.template.builder.TemplateImageFilter) {
        trackingLinkBuilder = ref('trackingLinkBuilder')
    }

    templateEngineFilter(outbox.template.builder.TemplateEngineFilter) {
        freemarkerTemplateService = ref('freemarkerTemplateService')
    }

    templateFilterChain(outbox.template.builder.TemplateFilterChain) {
        filters = [
                ref('templateTrackingHeaderFilter'),
                ref('templateLinkFilter'),
                ref('templateImageFilter'),
                ref('templateEngineFilter')
        ]
    }

    sendCampaignTaskProcessor(outbox.task.SendCampaignTaskProcessor) {
        campaignService = ref('campaignService')
        emailService = ref('emailService')
        trackingService = ref('trackingService')
        templateFilterChain = ref('templateFilterChain')
    }

    removeDynamicFieldTaskProcessor(outbox.task.RemoveDynamicFieldTaskProcessor) {
        dynamicFieldService = ref('dynamicFieldService')
    }

    removeDynamicFieldItemTaskProcessor(outbox.task.RemoveDynamicFieldItemTaskProcessor) {
        dynamicFieldService = ref('dynamicFieldService')
    }

    lookupService(com.maxmind.geoip.LookupService, '/usr/local/share/GeoIP/GeoLiteCity.dat')

    maxMindLookupService(outbox.tracking.geolocation.MaxMindLookupService) {
        lookupService = ref('lookupService')
    }

    maxMindGeoLocationService(outbox.tracking.geolocation.MaxMindGeoLocationService) {
        lookupService = ref('maxMindLookupService')
    }

    campaignTrackingInfoConverter(outbox.tracking.converter.CampaignTrackingInfoConverter) {
        geoLocationService = ref('maxMindGeoLocationService')
    }

    trackingInfoConverterFactory(outbox.tracking.converter.TrackingInfoConverterFactory) {
        campaignTrackingInfoConverter = ref('campaignTrackingInfoConverter')
    }

    reportsFactory(outbox.report.ReportsFactory) {
        sessionFactory = ref('sessionFactory')
    }

    reportsHolder(outbox.report.ReportsHolder) { bean ->
        bean.initMethod = 'init'
        reportsFactory = ref('reportsFactory')
    }

    editDynamicFieldsFormBuilder(outbox.ui.EditDynamicFieldsFormBuilder) {
        dynamicFieldService = ref('dynamicFieldService')
    }

    viewDynamicFieldsFormBuilder(outbox.ui.ViewDynamicFieldsFormBuilder)

    basicUIRender(outbox.ui.render.basic.BasicUIRender) {
        gspTagLibraryLookup = ref('gspTagLibraryLookup')
    }

    renderFactory(outbox.ui.render.RenderFactory) {
        basicUIRender = ref('basicUIRender')
    }

    singleQueryRunner(outbox.subscriber.search.runner.SingleQueryRunner) {
        sessionFactory = ref('sessionFactory')
    }

    queryRunnerDetector(outbox.subscriber.search.runner.QueryRunnerDetector) {
        singleQueryRunner = ref('singleQueryRunner')
    }

    subscriberSearchService(outbox.subscriber.search.SubscriberSearchService) {
        queryRunnerDetector = ref('queryRunnerDetector')
        dynamicFieldService = ref('dynamicFieldService')
    }

    searchConditionsFetcher(outbox.subscriber.search.SearchConditionsFetcher) {
        dynamicFieldService = ref('dynamicFieldService')
        springSecurityService = ref('springSecurityService')
    }
}
