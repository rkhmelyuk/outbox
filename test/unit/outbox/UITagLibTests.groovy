package outbox

import grails.test.TagLibUnitTestCase
import outbox.ui.element.UIInputText
import outbox.ui.render.RenderFactory
import outbox.ui.render.basic.BasicUIRender

/**
 * @author Ruslan Khmelyuk
 */
class UITagLibTests extends TagLibUnitTestCase {

    void testRender() {
        def element = new UIInputText()

        def basicRenderControl = mockFor(BasicUIRender)
        basicRenderControl.demand.render { el ->
            assertEquals element, el
            return '<rendered>'
        }

        def renderFactoryControl = mockFor(RenderFactory)
        renderFactoryControl.demand.createRender { renderName ->
            assertEquals 'basic', renderName
            return basicRenderControl.createMock()
        }
        tagLib.renderFactory = renderFactoryControl.createMock()

        def result = tagLib.renderElement([element: element, render: 'basic'])

        renderFactoryControl.verify()
        basicRenderControl.verify()

        assertEquals '<rendered>', result.toString()
    }

    void testRender_NoRenderer() {
        def element = new UIInputText()

        def basicRenderControl = mockFor(BasicUIRender)
        basicRenderControl.demand.render { el ->
            assertEquals element, el
            return '<rendered>'
        }

        def renderFactoryControl = mockFor(RenderFactory)
        renderFactoryControl.demand.createRender { renderName ->
            assertEquals 'basic', renderName
            return basicRenderControl.createMock()
        }
        tagLib.renderFactory = renderFactoryControl.createMock()

        def result = tagLib.renderElement([element: element])

        renderFactoryControl.verify()
        basicRenderControl.verify()

        assertEquals '<rendered>', result.toString()
    }

    void testRender_NoElement() {
        try {
            tagLib.renderElement([])
            fail 'Must be fail, because no element.'
        }
        catch (IllegalArgumentException e) {
            // that's ok
        }
    }
}
