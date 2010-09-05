package outbox.dictionary

import grails.test.GrailsUnitTestCase

/**
 * @author Ruslan Khmelyuk
 */
class NamePrefixTests extends GrailsUnitTestCase {

    void testFields() {
        NamePrefix namePrefix = new NamePrefix()
        namePrefix.id = 1
        namePrefix.name = 'Mr.'

        assertEquals 1, namePrefix.id
        assertEquals 'Mr.', namePrefix.name
    }
}
