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
            url = 'jdbc:postgresql://127.0.0.1:5432/outbox'
            driverClassName = 'org.postgresql.Driver'
            username = 'outbox'
            password = 'outbox123'
        }
        hibernate {
            show_sql = true
            dialect = 'org.hibernate.dialect.PostgreSQLDialect'
        }
    }
    staging {
        dataSource {
            url = 'jdbc:postgresql://127.0.0.1:5432/outboxstg'
            driverClassName = 'org.postgresql.Driver'
            username = 'outbox'
            password = '84znOsrIo'
        }
        hibernate {
            dialect = 'org.hibernate.dialect.PostgreSQLDialect'
        }
    }
    test {
        dataSource {
            dbCreate = "create"
            url = 'jdbc:postgresql://127.0.0.1:5432/outbox_test'
            driverClassName = 'org.postgresql.Driver'
            username = 'outbox'
            password = 'outbox123'
        }
        hibernate {
            show_sql = true
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
}
