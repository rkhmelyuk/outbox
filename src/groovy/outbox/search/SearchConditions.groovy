package outbox.search

/**
 * @author Ruslan Khmelyuk
 * @since 2010-09-19
 */
class SearchConditions {

    List<Condition> conditions = []

    boolean includeCount = false
    boolean includeFound = true
    boolean cacheQuery = false

    /**
     * Add condition.
     * @param condition the condition.
     */
    void add(Condition condition) {
        if (condition && !conditions.contains(condition)) {
            conditions << condition
        }
    }

    /**
     * Get condition by it's type.
     * @param type the need condition type
     * @param the default value, returned if condition not found.
     * @return the found condition otherwise default value.
     */
    Condition get(Class type, Condition defaultValue = null) {
        def result = conditions.find { it.class == type}
        result ? result : defaultValue
    }

    /**
     * Checks whether contains specified condition.
     * @param type the condition type.
     * @return true if contains,otherwise false
     */
    boolean has(Class type) {
        get(type) != null
    }

    /**
     * Gets the list of results using specified builder.
     * @param builder the incoming criteria builder for need entity.
     * @return search results.
     */
    SearchResult search(def entityClazz) {
        def result = new SearchResult()

        def builder

        if (includeCount) {
            builder = entityClazz.createCriteria()
            result.total = builder.count {
                conditions.each { each ->
                    if (!each.conditionFilter) {
                        each.build(builder)
                    }
                }
                cache cacheQuery
            }
        }

        if (includeFound) {
            builder = entityClazz.createCriteria()
            result.list = builder.list {
                conditions.each { each ->
                     each.build(builder)
                }
                cache cacheQuery
            }
        }
        else {
            result.list = []
        }

        return result
    }

}
