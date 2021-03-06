package outbox.ui.render.basic

import outbox.ui.element.UILabel

/**
 * @author Ruslan Khmelyuk
 */
class UILabelRenderTests extends GroovyTestCase {

     UILabelRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UILabelRender()
    }

    void testRender() {
        def element = new UILabel(id: 'id', forId: 'forId', text: 'Label Text', styleClass: 'class')
        element.args.test = '1'
        assertEquals '<label id="id" for="forId" class="class" test="1" >Label Text</label>', render.render(element)
    }

    void testRender_AttributesLess() {
        def element = new UILabel()
        assertEquals '<label ></label>', render.render(element)
    }
}
