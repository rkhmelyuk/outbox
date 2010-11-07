package outbox

import outbox.ui.element.UIElement
import outbox.ui.render.RenderFactory

/**
 * @author Ruslan Khmelyuk
 */
class UITagLib {

    static namespace = 'app'

    RenderFactory renderFactory

    def renderElement = { attrs ->
        def element = attrs.element

        if (!(element instanceof UIElement)) {
            throw new IllegalArgumentException('Incorrect element to render.')
        }
        def render = attrs.render ?: 'basic'
        def uiRender = renderFactory.createRender(render)
        if (uiRender == null) {
            throw new IllegalArgumentException("Render $render is not found")
        }

        out << uiRender.render(element)
    }

}
