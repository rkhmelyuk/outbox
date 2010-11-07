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
        def element = new UILabel(
                id: 'id',
                forId: 'forId',
                text: 'Label Text',
                styleClass: 'class')

        def result = render.render(element)

        println result

        assertEquals '<label id="id" for="forId" class="class" >Label Text</label>', result
    }

    void testRender_AttributesLess() {
        def element = new UILabel()

        def result = render.render(element)

        println result

        assertEquals '<label ></label>', result
    }
}
