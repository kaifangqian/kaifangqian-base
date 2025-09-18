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

// import Vuex from 'vuex'
import { createStore } from 'vuex'
import document from "/@/views/contract/document/data/menu"
import template from "@/pages/template/data/menu"
import users from "@/pages/users/data/menu"
import storage from '@/utils/storage/SessionStorage'
import {getEmnuActive} from "@/utils/MenuActiveIndex"
import {groupBy,uuid} from "@/utils";
// import {getToken,setToken,removeToken} from '@/utils/storage/Cookie' 

const store = createStore({
	state: {
		menuActive:storage.getItem("MenuActive"),
		windowHeight:storage.getItem("windowHeight"),
		leftMenu:[],
		leftButton:[],
		leftFolder:[],
	},
	getters: {
		
	},
	mutations: {
		setMenuActive: (state, content) => {
			state.menuActive = content
		},
		setLeftMenu: (state, content) => {
			state.leftMenu = content
		},
		setLeftButton: (state, content) => {
			state.leftButton = content
		},
		setLeftFolder: (state, content) => {
			state.leftFolder = content
		},
		
	},
	actions: {
		commitMenuActive({commit},content) {
			const EmnuActive = getEmnuActive();
			if(content == EmnuActive.document){
				// console.log();
				commit('setLeftMenu', groupBy(document.menu,"tag"));
				commit('setLeftButton', document.button);
				commit('setLeftFolder', document.folder);
			}else if(content == EmnuActive.template){
				console.log(template.menu);
				commit('setLeftMenu', groupBy(template.menu,"tag"));
				commit('setLeftButton', template.button);
				commit('setLeftFolder', template.folder);
			}else if(content == EmnuActive.users){
				commit('setLeftMenu', groupBy(users.menu,"tag"));
				commit('setLeftButton', users.button);
				commit('setLeftFolder', []);
			}else{
				commit('setLeftMenu', []);
				commit('setLeftButton', []);
			}
			commit('setMenuActive', content);
		}
	},
	modules: {}
})
export default store;