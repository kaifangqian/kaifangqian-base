/**
 * @description 水印模块
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
let mo = null;

// 添加水印
function add({
    container = document.body,
    width = '200',
    height = '200',
    rotate = -20,
    style = 'font-family: Arial; font-weight: bold',
    fontSize = '16px',
    opacity = 0.12,
    content = '开放签',
    zIndex = 1000,
} = {}) {
    const svgStr = `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}">
                    <text x="10" y="50%"
                      transform="rotate(${rotate}, ${width / 2} ${height / 2})"
                      style="${style}; font-size: ${fontSize}; opacity: ${opacity}">
                      ${content}
                    </text>
                  </svg>`;
    const base64Url = `data:image/svg+xml;base64,${window.btoa(
        unescape(encodeURIComponent(svgStr))
    )}`;

    const __wm = document.querySelector('.__wm');
    const watermarkDiv = __wm || document.createElement('div');
    const styleStr = `
    position:absolute;
    top:0px;
    left:0px;
    width:100%;
    height:100%;
    z-index:${zIndex};
    pointer-events:none;
    background-repeat:repeat;
    background-image:url('${base64Url}')`;

    watermarkDiv.setAttribute('style', styleStr);
    watermarkDiv.classList.add('__wm');

    container.style.position = 'relative';
    if (!__wm) {
        container.appendChild(watermarkDiv);
    }

    const MutationObserver = window.MutationObserver || window.WebKitMutationObserver;
    if (MutationObserver) {
        const args = arguments[0];
        mo = new MutationObserver(function () {
            const __wm = document.querySelector('.__wm');
            if (
                (__wm && __wm.getAttribute('style') !== styleStr) ||
                !__wm ||
                container.style.position !== 'relative'
            ) {
                mo.disconnect();
                mo = null;
                add(args);
            }
        });
        mo.observe(container, {
            attributes: true,
            subtree: true,
            childList: true,
        });
    }
}

// 移除水印
function remove() {
    const __wm = document.querySelector('.__wm');
    if (__wm) {
        mo.disconnect();
        mo = null;
        document.body.removeChild(__wm);
    }
}

export default { add, remove };
