package outbox.ui.render.basic

import outbox.ui.element.UILabel
import outbox.ui.element.UIOutput

/**
 * @author Ruslan Khmelyuk
 */
class UIOutputRenderTests extends GroovyTestCase {

     UIOutputRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UIOutputRender()
    }

    void testRender() {
        def element = new UIOutput(id: 'id', text: 'Label Text', styleClass: 'class')
        element.args.test = '1'
        assertEquals '<span id="id" class="class" test="1" >Label Text</span>', render.render(element)
    }

    void testRender_AttributesLess() {
        def element = new UILabel()
        assertEquals '<span ></span>', render.render(element)
    }
}
