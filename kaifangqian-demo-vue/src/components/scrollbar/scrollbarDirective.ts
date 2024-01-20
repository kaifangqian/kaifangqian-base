import { App } from 'vue'
import usePerfectScrollbar from './usePerfectScrollbar'

export default {
    install(app: App) {
        const { install, uninstall } = usePerfectScrollbar()
        app.directive('scrollbar', {
            mounted(el) {
                install(el)
            },
            beforeUnmount() {
                uninstall()
            }
        })
    }
}