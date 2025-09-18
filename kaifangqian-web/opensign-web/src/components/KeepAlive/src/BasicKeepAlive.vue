<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<script lang="tsx">

function isEmpty(...str) {
  return str.some((i) => i === undefined || i === null || i === "");
}
function getFirstComponentChild(children) {
  if (Array.isArray(children)) {
    return children.find(
      (c) =>
        !isEmpty(c) &&
        (!isEmpty(c.componentOptions) || (c.isComment && c.asyncFactory))
    );
  }
}
function removeCache(cache, key) {
  const cached = cache[key];
  cached && cached.componentInstance && cached.componentInstance.$destroy();
  delete cache[key];
}
function getRouterViewCacheKey(route) {
 // 这里可以控制自己想要的key
 console.log(route.path,'-----路由地址----')
 return route.path 
}
import {   defineComponent,  ref, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name:'BasicKeepAlive',
  components:{},
  props:{
    include: Array
  },
  setup(props,{slots}){
    const cache = ref(Object.create(null));
    const router = useRouter();
    const { currentRoute } = router;
    onUnmounted(()=>{
      for (const key of Object.keys(cache.value)) {
        removeCache(cache.value, key);
      }
    })
    return () => {
      const slot = slots;
      console.log(props,'333333')
      const vnode = getFirstComponentChild(slot.default);
      let componentOptions = vnode && vnode.componentOptions;
      if (componentOptions) {
        const child =
          componentOptions.tag === "transition"
            ? componentOptions.children[0]
            : vnode;
        componentOptions = child && child.componentOptions;
        if (componentOptions) {
          const key = getRouterViewCacheKey(currentRoute)
          // const { cache,include } = this;
        
          if (props.include && !props.include.includes(key)) {
            console.log('不含有缓存返回',props.include,key)
            return vnode
          }
          
          if (cache.value[key]) {
            child.componentInstance = cache.value[key].componentInstance;
          } else {
            cache.value[key] = child;
          }
          child.data.keepAlive = true;
        }
      }
      console.log(vnode,'11111')
      return vnode || (slot && slot[0]);
    }
  }
})
</script>


