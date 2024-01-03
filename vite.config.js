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
		open: true, // vue项目启动时自动打开浏览器
		proxy: {
			"/openSign": {
				target: "http://127.0.0.1:8666",
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