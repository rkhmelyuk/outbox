package outbox.ui.render

/**
 * @author Ruslan Khmelyuk
 */
class RenderFactory {

    def basicUIRender

    UIRenderFactory createRender(String name) {
        if (name == 'basic') {
            return basicUIRender
        }
        return null
    }

}
