package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UIInputText

/**
 * @author Ruslan Khmelyuk
 */
class UIInputTextRenderTests extends GroovyTestCase {

    TagLibraryLookup gspTagLibraryLookup

    UIInputTextRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UIInputTextRender(gspTagLibraryLookup: gspTagLibraryLookup)
    }

    void testRender() {
        def input = new UIInputText(id: 'id', name: 'name', value: 'value', styleClass: 'class')
        assertEquals '<input type="text" id="id" name="name" value="value" class="class" />', render.render(input)
    }

    void testRender_NoParams() {
        def input = new UIInputText(name: 'name')
        assertEquals '<input type="text" name="name" id="name" value="" />', render.render(input)
    }
}
