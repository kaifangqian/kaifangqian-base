/**
 * @description : WebSocketService.ts
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
// WebSocketService.ts

import { ref, onBeforeUnmount } from 'vue';

export interface WebSocketServiceOptions {
    url: string;
}

export class WebSocketService {
    private socket: WebSocket | null = null;
    private messageCallback: ((data: any) => void) | null = null;

    public isConnected = ref(false);

    constructor(options: WebSocketServiceOptions) {
        this.connect(options.url);
    }

    private connect(url: string): void {
        var wsUrl = url.replace("https://", "wss://").replace("http://", "ws://");
        console.log('wsUrl地址', wsUrl)
        this.socket = new WebSocket(wsUrl);

        this.socket.addEventListener('open', () => {
            this.isConnected.value = true;
            console.log('QR socket链接已建立')
        });

        this.socket.addEventListener('message', (event) => {
            console.log(event, '有消息')
            if (this.messageCallback) {
                this.messageCallback(JSON.parse(event.data));
            }
        });

        this.socket.addEventListener('close', () => {
            this.isConnected.value = false;
            console.log('QR socket链接已关闭')
        });
    }

    public setMessageCallback(callback: (data: any) => void): void {
        this.messageCallback = callback;
    }

    public close(): void {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
        }
    }
}

export function useWebSocketService(options: WebSocketServiceOptions): WebSocketService {
    const service = new WebSocketService(options);

    // Automatically close the WebSocket when the component is unmounted
    onBeforeUnmount(() => {
        service.close();
    });

    return service;
}
