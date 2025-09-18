import { PluginOption } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import legacy from '@vitejs/plugin-legacy';
import purgeIcons from 'vite-plugin-purge-icons';
import windiCSS from 'vite-plugin-windicss';
import VitePluginCertificate from 'vite-plugin-mkcert';
import vueSetupExtend from 'vite-plugin-vue-setup-extend';
// import PkgConfig from 'vite-plugin-package-config'
import { configHtmlPlugin } from './html';
import { configPwaConfig } from './pwa';
import { configMockPlugin } from './mock';
import { configCompressPlugin } from './compress';
import { configStyleImportPlugin } from './styleImport';
import { configVisualizerConfig } from './visualizer';
import { configThemePlugin } from './theme';
import { configImageminPlugin } from './imagemin';
import { configSvgIconsPlugin } from './svgSprite';
import { writeFileSync } from 'fs';
import { join } from 'path'


export function createVitePlugins(viteEnv: ViteEnv, isBuild: boolean) {
  const {
    VITE_USE_IMAGEMIN,
    VITE_USE_MOCK,
    VITE_LEGACY,
    VITE_BUILD_COMPRESS,
    VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE,
  } = viteEnv;

  const vitePlugins: (PluginOption | PluginOption[])[] = [
   // have to
   vue({
        template: {
          compilerOptions: {
            // 注册自定义组件micro-app 防止控制台警告
            isCustomElement: tag => /^micro-app/.test(tag)
          }
        }
      }),
      // have to
      vueJsx(),
      // support name
      vueSetupExtend(),
      // https
      VitePluginCertificate({
        source: 'coding',
      }),
      // (function () {
      //   let basePath = ''
      //   return {
      //     name: "app-opensign",
      //     apply: "build",
      //     configResolved(config) {
      //       // 获取资源地址前缀
      //       basePath = `${config.base}${config.build.assetsDir}/`;
      //     },
      //     // renderChunk(code, chunk) {
      //     //   // build后，import会通过相对地址引入模块，需要将其补全
      //     //   if (chunk.fileName.endsWith(".js") && /(from|import)(\s*['"])(\.\.?\/)/g.test(code)) {
      //     //     code = code.replace(/(from|import)(\s*['"])(\.\.?\/)/g, (all, $1, $2, $3) => {
      //     //       return all.replace($3, new URL($3, basePath).href);
      //     //     });
      //     //   }
      //     //   return code;
      //     // },
      //     writeBundle (options, bundle) {
      //       for (const chunkName in bundle) {
      //         if (Object.prototype.hasOwnProperty.call(bundle, chunkName)) {
      //           const chunk = bundle[chunkName]
      //           if (chunk.fileName && chunk.fileName.endsWith('.js')) {
      //             chunk.code = chunk.code.replace(/(from|import\()(\s*['"])(\.\.?\/)/g, (all, $1, $2, $3) => {
      //               return all.replace($3, new URL($3, basePath))
      //             })
      //             const fullPath = join(options.dir, chunk.fileName)
      //             writeFileSync(fullPath, chunk.code)
      //           }
      //         }
      //       }
      //     },
      //   };
      // })(),
  ];


  // vite-plugin-windicss
  vitePlugins.push(windiCSS());

  // @vitejs/plugin-legacy
  VITE_LEGACY && isBuild && vitePlugins.push(legacy());

  // vite-plugin-html
  vitePlugins.push(configHtmlPlugin(viteEnv, isBuild));

  // vite-plugin-svg-icons
  vitePlugins.push(configSvgIconsPlugin(isBuild));

  // vite-plugin-mock
  VITE_USE_MOCK && vitePlugins.push(configMockPlugin(isBuild));

  // vite-plugin-purge-icons
  vitePlugins.push(purgeIcons());

  // vite-plugin-style-import
  vitePlugins.push(configStyleImportPlugin(isBuild));

  // rollup-plugin-visualizer
  vitePlugins.push(configVisualizerConfig());

  // vite-plugin-theme
  vitePlugins.push(configThemePlugin(isBuild));

  // The following plugins only work in the production environment
  if (isBuild) {
    // vite-plugin-imagemin
    VITE_USE_IMAGEMIN && vitePlugins.push(configImageminPlugin());

    // rollup-plugin-gzip
    vitePlugins.push(
      configCompressPlugin(VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE),
    );

    // vite-plugin-pwa
    vitePlugins.push(configPwaConfig(viteEnv));
  }

  return vitePlugins;
}
