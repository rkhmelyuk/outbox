package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.UICheckbox

/**
 * @author Ruslan Khmelyuk
 */
class UICheckboxRenderTests extends GroovyTestCase {

    TagLibraryLookup gspTagLibraryLookup

    UICheckboxRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UICheckboxRender(gspTagLibraryLookup: gspTagLibraryLookup)
    }

    void testRender_Checked() {
        def input = new UICheckbox(id: 'id', name: 'name', value: true, styleClass: 'class')
        input.args.test = '1'
        assertEquals '<input type="hidden" name="_name" /><input type="checkbox" name="name" checked="checked" value="true" id="id" class="class" test="1"  />',
                render.render(input)
    }

    void testRender_NotChecked() {
        def input = new UICheckbox(id: 'id', name: 'name', value: false, styleClass: 'class')
        assertEquals '<input type="hidden" name="_name" /><input type="checkbox" name="name" value="true" id="id" class="class"  />',
                render.render(input)
    }
}
