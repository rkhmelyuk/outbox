package outbox.ui.render

import outbox.ui.render.basic.BasicUIRender

/**
 * @author Ruslan Khmelyuk
 */
class RenderFactory {

    UIRender createRender(String name) {
        if (name == 'basic') {
            return new BasicUIRender()
        }
        return null
    }

}
