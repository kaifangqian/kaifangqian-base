import {
	fileURLToPath,
	URL
} from 'node:url'

import {
	defineConfig
} from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
	outputDir: "dist/esign",
	transpileDependencies: true,
	server: {
		host: '0.0.0.0',
		port: 8080,
		open: false, // vue项目启动时自动打开浏览器
		proxy: {
			"/openSign": {
				target: "http://192.168.0.2:8666",
				changeOrigin: true,
			}
		},
	},
	plugins: [
		vue(),
	],
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url))
		}
	}
})