package outbox.ui.render

import outbox.ui.render.basic.BasicUIRender

/**
 * @author Ruslan Khmelyuk
 */
class RenderFactoryTests extends GroovyTestCase {

    void testCreateRender() {
        def renderFactory = new RenderFactory()
        renderFactory.basicUIRender = new BasicUIRender()

        assertEquals renderFactory.basicUIRender, renderFactory.createRender('basic')
        assertNull renderFactory.createRender(null)
        assertNull renderFactory.createRender('not-existing')
    }
}
