package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.SelectItem
import outbox.ui.element.UISelectSingle

/**
 * @author Ruslan Khmelyuk
 */
class UISelectRenderTests extends GroovyTestCase {

    TagLibraryLookup gspTagLibraryLookup

    UISelectRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UISelectRender(gspTagLibraryLookup: gspTagLibraryLookup)
    }

    void testRender_Empty() {
        def input = new UISelectSingle(id: 'id', name: 'name', value: 'value', styleClass: 'class', selectItems: [], mandatory: true)
        assertEquals '<select name="name" id="id" class="class" >\r\n</select>', render.render(input)
    }

    void testRender_NotEmpty_Mandatory() {
        def input = new UISelectSingle(id: 'id', name: 'name', value: 'value', styleClass: 'class',
                selectItems: [new SelectItem(value: 'key', label: 'value')], mandatory: true)
        assertEquals '<select name="name" id="id" class="class" >\r\n<option value="key" >value</option>\r\n</select>', render.render(input)
    }

    void testRender_NotEmpty_Optional() {
        def input = new UISelectSingle(id: 'id', name: 'name', value: 'value', styleClass: 'class',
                selectItems: [new SelectItem(value: 'key', label: 'value')])
        assertEquals '<select name="name" id="id" class="class" >\r\n<option value=""></option>\r\n' +
                '<option value="key" >value</option>\r\n</select>', render.render(input)
    }

}
