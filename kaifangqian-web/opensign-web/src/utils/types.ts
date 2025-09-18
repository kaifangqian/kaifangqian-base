/*
 * @description 开放签
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
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

// copy from element-plus

import type { CSSProperties, Plugin } from 'vue';

type OptionalKeys<T extends Record<string, unknown>> = {
  [K in keyof T]: T extends Record<K, T[K]> ? never : K;
}[keyof T];

type RequiredKeys<T extends Record<string, unknown>> = Exclude<keyof T, OptionalKeys<T>>;

type MonoArgEmitter<T, Keys extends keyof T> = <K extends Keys>(evt: K, arg?: T[K]) => void;

type BiArgEmitter<T, Keys extends keyof T> = <K extends Keys>(evt: K, arg: T[K]) => void;

export type EventEmitter<T extends Record<string, unknown>> = MonoArgEmitter<T, OptionalKeys<T>> &
  BiArgEmitter<T, RequiredKeys<T>>;

export type AnyFunction<T> = (...args: any[]) => T;

export type PartialReturnType<T extends (...args: unknown[]) => unknown> = Partial<ReturnType<T>>;

export type SFCWithInstall<T> = T & Plugin;

export type Nullable<T> = T | null;

export type RefElement = Nullable<HTMLElement>;

export type CustomizedHTMLElement<T> = HTMLElement & T;

export type Indexable<T> = {
  [key: string]: T;
};

export type Hash<T> = Indexable<T>;

export type TimeoutHandle = ReturnType<typeof global.setTimeout>;

export type ComponentSize = 'large' | 'medium' | 'small' | 'mini';

export type StyleValue = string | CSSProperties | Array<StyleValue>;

export type Mutable<T> = { -readonly [P in keyof T]: T[P] };
