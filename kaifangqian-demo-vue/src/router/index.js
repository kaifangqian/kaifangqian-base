import { createRouter, createWebHistory } from 'vue-router'
import main from '../views/main.vue'
import msg from '../views/message.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: main
    },
	{
	  path: '/msg',
	  name: 'msg',
	  component: msg
	},
  ]
})

export default router
