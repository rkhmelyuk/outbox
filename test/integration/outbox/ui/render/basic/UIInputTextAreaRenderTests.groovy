package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIInputTextArea

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTextAreaRenderTests extends GroovyTestCase  {

    TagLibraryLookup gspTagLibraryLookup

    UIInputTextAreaRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UIInputTextAreaRender(gspTagLibraryLookup: gspTagLibraryLookup)
    }

    void testRender() {
        def input = new UIInputTextArea(id: 'id', name: 'name', value: 'value', styleClass: 'class')
        assertEquals '<textarea id="id" name="name" class="class" >value</textarea>', render.render(input)
    }

    void testRender_NoParams() {
        def input = new UIInputTextArea(name: 'name')
        assertEquals '<textarea name="name" id="name" ></textarea>', render.render(input)
    }

}
