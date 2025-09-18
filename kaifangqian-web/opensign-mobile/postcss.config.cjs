const autoprefixer = require('autoprefixer');
const pxtorem = require('postcss-pxtorem');

module.exports = {
    plugins: [
        autoprefixer(),
        pxtorem({
            rootValue({ file }) {
                // 特别注意：如果用vant官网示例 `file.indexOf('vant')` 来匹配，请确保你的项目名或文件名没有包含'vant'
                // 建议改为 `file.indexOf('node_modules/vant')`
                return file.indexOf('vant') !== -1 ? 37.5 : 75;
            },
            unitPrecision: 5,
            propList: ['*'],
            selectorBlackList: ['.ignore', 'keep-px'],
            minPixelValue: 1,

            mediaQuery: false,
            // exclude: (file) => /response\.less/i.test(file),
        }),
    ],
};
