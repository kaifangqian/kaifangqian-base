import PerfectScrollbar from 'perfect-scrollbar'
import 'perfect-scrollbar/css/perfect-scrollbar.css'

export default function usePerfectScrollbar() {
    let ps: null | PerfectScrollbar
    const resize = () => {
        ps && ps.update()
    }
    return {
		
        install(el: Element | string) {
            if(el){
                ps = new PerfectScrollbar(el, {
                    wheelSpeed: 1,
                    wheelPropagation: false
                })
                window.addEventListener('resize', resize, false)
                return ps
            }
        },
        uninstall() {
			console.log("uninstall");
            window.removeEventListener('resize', resize, false)
            ps && ps.destroy()
        }
    }
}