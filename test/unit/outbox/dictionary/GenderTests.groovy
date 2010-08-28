package outbox.dictionary

import grails.test.*

class GenderTests extends GrailsUnitTestCase {

    void testFields() {
        Gender gender = new Gender(id: 1, name: 'Male')
        assertEquals 1, gender.id
        assertEquals 'Male', gender.name
    }

    void testFields2() {
        Gender gender = new Gender()
        gender.id = 3
        gender.name = 'Female'
        
        assertEquals 3, gender.id
        assertEquals 'Female', gender.name
    }
}
