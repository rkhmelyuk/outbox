package outbox.ui.render

import outbox.ui.render.basic.BasicUIRender

/**
 * @author Ruslan Khmelyuk
 */
class RenderFactoryTests extends GroovyTestCase {

    void testCreateRender() {
        def renderFactory = new RenderFactory()

        assertTrue renderFactory.createRender('basic') instanceof BasicUIRender
        assertNull renderFactory.createRender(null)
        assertNull renderFactory.createRender('not-existing')
    }
}
