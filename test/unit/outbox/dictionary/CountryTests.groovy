package outbox.dictionary

import grails.test.GrailsUnitTestCase

class CountryTests extends GrailsUnitTestCase {

    void testFields() {
        Country country = new Country(
                id: 1,
                code: 'UA',
                name: 'Ukraine')

        assertEquals 1, country.id
        assertEquals 'UA', country.code
        assertEquals 'Ukraine', country.name
    }

    void testFields2() {
        Country country = new Country()

        country.code = 'UKR'
        country.name = 'Ukraine'
        country.id = 10

        assertEquals 10, country.id
        assertEquals 'UKR', country.code
        assertEquals 'Ukraine', country.name
    }
}
