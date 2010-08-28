package outbox.dictionary

import grails.test.*

class TimezoneTests extends GrailsUnitTestCase {

    void testFields() {
        Timezone timezone = new Timezone(id: 1, name: 'Ivano-Frankivsk', timeOffset: 1.5)

        assertEquals 1, timezone.id
        assertEquals 'Ivano-Frankivsk', timezone.name
        assertEquals 1.5, timezone.timeOffset
    }

    void testFields2() {
        Timezone timezone = new Timezone()
        timezone.id = 2
        timezone.name = 'Kyiv'
        timezone.timeOffset = 2.0

        assertEquals 2, timezone.id
        assertEquals 'Kyiv', timezone.name
        assertEquals 2.0, timezone.timeOffset
    }
}
