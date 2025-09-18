import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';
import { createHtmlPlugin } from 'vite-plugin-html';
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers';
import AutoImport from 'unplugin-auto-import/vite';
import { viteVConsole } from 'vite-plugin-vconsole';
import { babel } from '@rollup/plugin-babel';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import path from 'path';

const pathResolve = (dir: string) => resolve(__dirname, dir);

const getViteEnv = (mode, target) => {
  return loadEnv(mode, process.cwd())[target];
};
// https://vitejs.dev/config/
export default ({ command, mode }) => {
  // 获取环境变量
  const env: Partial<ImportMetaEnv> = loadEnv(mode, process.cwd());

  return defineConfig({
    base: '/mobile/',
    define: {
      'process.env': env,
    },
    resolve: {
      alias: {
        '@': pathResolve('src'), // path.resolve中，'./src' 等于 'src'
        // '@router': pathResolve('src/router'),
      },
    },
    plugins: [
      vue(),
      createHtmlPlugin({
        inject: {
          data: {
            //将环境变量 VITE_APP_TITLE 赋值给 title 方便 html页面使用 title 获取系统标题
            title: getViteEnv(mode, 'VITE_APP_TITLE'),
          },
        },
      }),

      Components({
        resolvers: [VantResolver({ importStyle: false })],
      }),
      // 自动导入api
      AutoImport({
        imports: ['vue', 'vue-router'],
        dts: 'src/auto-import.d.ts',
        eslintrc: {
          enabled: true,
        },
      }),
      viteVConsole({
        entry: pathResolve('src/main.ts'),
        localEnabled: true,
        enabled: env.VITE_BUILD_VCONSOLE === 'true',
        config: {
          maxLogNumber: 1000,
          theme: 'dark',
        },
      }),
      createSvgIconsPlugin({
        // Specify the icon folder to be cached
        iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
        // Specify symbolId format
        symbolId: 'icon-[dir]-[name]',

        /**
         * custom insert position
         * @default: body-last
         */
        // inject?: 'body-last' | 'body-first'

        /**
         * custom dom id
         * @default: __svg__icons__dom__
         */
        // customDomId: '__svg__icons__dom__',
      }),

      /* babel({
              babelHelpers: 'bundled',
              plugins: ['@babel/plugin-proposal-optional-chaining'],
              include: [/\.vue$/, /\.ts$/],
              extensions: ['.vue', '.ts'],
            }), */
    ],
    server: {
      port: 3000, // 默认 // vite3已改为默认5173
      host: true, // 支持从ip启动
      open: false,
      proxy: {
        '/resrun-paas': {
          target: 'http://192.168.0.200:8806', // 后台服务地址
          changeOrigin: true, // 是否允许不同源
          secure: false, // 支持https
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
    build: {
      outDir: 'dist/mobile', // 指定打包路径，默认为项目根目录下的 dist 目录
      sourcemap: env.VITE_BUILD_SOURCEMAP === 'true',

      minify: 'terser',
      terserOptions: {
        compress: {
          keep_infinity: true, // 防止 Infinity 被压缩成 1/0，这可能会导致 Chrome 上的性能问题
          drop_console: env.VITE_BUILD_DROP_CONSOLE === 'true', // 去除 console
          drop_debugger: true, // 去除 debugger
        },
      },
      chunkSizeWarningLimit: 1500, // chunk 大小警告的限制（以 kbs 为单位）
    },
    css: {
      preprocessorOptions: {
        less: {
          javascriptEnabled: true,
          additionalData: `@import "${pathResolve('src/styles/index.less')}";`,
        },
      },
    },
  });
};
