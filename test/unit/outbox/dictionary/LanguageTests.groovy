package outbox.dictionary

import grails.test.*

class LanguageTests extends GrailsUnitTestCase {

    void testFields() {
        Language language = new Language(
                id: 1,
                code: 'EN',
                name: 'English')

        assertEquals 1, language.id
        assertEquals 'EN', language.code
        assertEquals 'English', language.name
    }

    void testFields2() {
        Language language = new Language()

        language.code = 'UKR'
        language.name = 'Ukrainian'
        language.id = 10

        assertEquals 10, language.id
        assertEquals 'UKR', language.code
        assertEquals 'Ukrainian', language.name
    }
}
