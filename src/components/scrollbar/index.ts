import { App } from 'vue';
import CScrollbar from './src/Index.vue';

export default {
  install(app: App) {
    app.component(CScrollbar.name, CScrollbar);
    return app;
  },
  CScrollbar
};
