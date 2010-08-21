dataSource {
    pooled = true
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    jdbc.batch_size = 50
}
// environment specific settings
environments {
    development {
        dataSource {
            url = 'jdbc:postgresql://localhost:5432/outbox'
            driverClassName = 'org.postgresql.Driver'
            username = 'outbox'
            password = 'outbox'
        }
        hibernate {
            show_sql = true
        }
    }
    test {
        dataSource {
            driverClassName = "org.hsqldb.jdbcDriver"
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
}
