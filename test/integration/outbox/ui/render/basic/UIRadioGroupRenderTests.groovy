package outbox.ui.render.basic

import org.codehaus.groovy.grails.web.pages.TagLibraryLookup
import outbox.ui.element.SelectItem
import outbox.ui.element.UISelectSingle

/**
 * @author Ruslan Khmelyuk
 */
class UIRadioGroupRenderTests extends GroovyTestCase {

    TagLibraryLookup gspTagLibraryLookup

    UIRadioGroupRender render

    @Override protected void setUp() {
        super.setUp()
        render = new UIRadioGroupRender(gspTagLibraryLookup: gspTagLibraryLookup)
    }

    void testRender_Empty() {
        def input = new UISelectSingle(id: 'id', name: 'name', value: 'value', styleClass: 'class', selectItems: [])
        def result = render.render(input)
        println result
        assertEquals '', result
    }

    void testRender_NotEmpty() {
        def input = new UISelectSingle(id: 'id', name: 'name', value: 'value', styleClass: 'class',
                selectItems: [new SelectItem(value: 'key', label: 'value')])
        def result = render.render(input)
        println result
        assertEquals '<input type="radio" name="name" value="key" class="class" id="id_0"  /><label for="id_0" >value</label>', result
    }

}
