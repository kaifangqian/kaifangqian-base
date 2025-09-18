import { MockMethod } from 'vite-plugin-mock';
import { resultError, resultPageSuccess, resultSuccess } from '../_util';

const accountList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 20; index++) {
    result.push({
      id: `${index}`,
      account: '@first',
      email: '@email',
      nickname: '@cname()',
      role: '@first',
      createTime: '@datetime',
      remark: '@cword(10,20)',
      'status|1': ['0', '1'],
    });
  }
  return result;
})();

const roleList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 4; index++) {
    result.push({
      id: index + 1,
      orderNo: `${index + 1}`,
      roleName: ['超级管理员', '管理员', '文章管理员', '普通用户'][index],
      roleValue: '@first',
      createTime: '@datetime',
      remark: '@cword(10,20)',
      menu: [['0', '1', '2'], ['0', '1'], ['0', '2'], ['2']][index],
      'status|1': ['0', '1'],
    });
  }
  return result;
})();
const roleUserCountList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 4; index++) {
    result.push({
      id: index + 1,
      orderNo: `${index + 1}`,
      roleName: ['超级管理员', '管理员', '文章管理员', '普通用户'][index],
      count: '@integer(10, 100)',
      createTime: '@datetime',
    });
  }
  return result
})()

const authGroupList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 4; index++) {
    result.push({
      id: index + 1,
      orderNo: `${index + 1}`,
      authGroupName: ['系统', '流程管理', '运维', 'OA'][index],
      authName: ['系统管理员', '流程专家', '运维专家', 'OA员工'][index],
      desc:'@cword(10,20)',
      createTime: '@datetime',
    });
  }
  return result
})()

const userList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      orderNo: `${index + 1}`,
      userName: '@cname()',
      userType: '@integer(0,5)',
      roleType: '@integer(0,5)',
      jobNumber: '@guid',
      phone: /^1(3\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/,
      email:'@email',
      'status|1': ['0', '0', '1'],
      desc:'@cword(10,20)',
      createTime: '@datetime',
    });
  }
  return result
})()
const operationList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      operContent:'@cword(10,20)',
      orderNo: `${index + 1}`,
      operType: '@integer(0,5)',
      operAcount: '@first',
      name: '@cname()',
      ip: '@ip',
      createTime: '@datetime',
      methods:['get','post','put','delete'][Math.floor(index/10)],
      params:'@cname()',
      time:'@now()',
      result:''
    });
  }
  return result
})()
const abnormalList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      title:'@cword(10,20)',
      orderNo: `${index + 1}`,
      createTime: '@datetime',
      message:'@cword(10,20)'
    });
  }
  return result
})()

const safeSetting = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      module:'@cword(5,10)',
      orderNo: `${index + 1}`,
      createTime: '@datetime',
      desc:'@cword(10,20)',
      'status|1': ['0', '0', '1'],
    });
  }
  return result
})()

const securityList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      title:'@cword(10,20)',
      operType: '@integer(0,5)',
      operAcount: '@first',
      name: '@cname()',
      ip: '@ip',
      orderNo: `${index + 1}`,
      createTime: '@datetime',
      message:'@cword(10,20)'
    });
  }
  return result
})()


const deptList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 3; index++) {
    result.push({
      id: `${index}`,
      deptName: ['华东分部', '华南分部', '西北分部'][index],
      orderNo: index + 1,
      userCount:'@integer(10,100)',
      manager:'@cname()',
      createTime: '@datetime',
      remark: '@cword(10,20)',
      'status|1': ['0', '0', '1'],
      children: (() => {
        const children: any[] = [];
        for (let j = 0; j < 4; j++) {
          children.push({
            id: `${index}-${j}`,
            deptName: ['研发部', '市场部', '商务部', '财务部'][j],
            orderNo: j + 1,
            userCount:'@integer(10,100)',
            manager:'@cname()',
            createTime: '@datetime',
            remark: '@cword(10,20)',
            'status|1': ['0', '1'],
            parentDept: `${index}`,
            children: undefined,
          });
        }
        return children;
      })(),
    });
  }
  return result;
})();

const msgTplTreeList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 4; index++) {
    result.push({
      id: `${index}`,
      tplName: ['账号安全通知', '流程消息通知', 'CRM','流量控制通知'][index],
      orderNo: index + 1,
      icon:'ant-design:folder-view-outlined',
      'status|1': ['0', '0', '1'],
      children: (() => {
        const children: any[] = [];
        for (let j = 0; j < 4; j++) {
          children.push({
            id: `${index}-${j}`,
            tplName: ['任务接收通知', '任务催办通知', '任务办理超时预警通知', '财务部'][j],
            icon:'ant-design:safety-certificate-twotone',
            orderNo: j + 1,
            'status|1': ['0', '1'],
            children: undefined,
          });
        }
        return children;
      })(),
    });
  }
  return result;
})();

const msgTplList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      tplTitle:'@cword(2,5)',
      tplContent:'@cword(10,20)',
      orderNo: `${index + 1}`,
      tplCode: '@integer(0,5)',
      tplSource: '@first'
    });
  }
  return result
})()

const myMessageList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      msgTitle:'@cword(2,5)',
      msgContent:'@cword(10,20)',
      orderNo: `${index + 1}`,
      createTime: '@datetime',
      tplSource: '@first',
      'status|1': ['0', '0', '1'],
    });
  }
  return result
})()

const announceTreeList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 4; index++) {
    result.push({
      id: `${index}`,
      announceName: ['法治宣传', '假期宣传', '公司文化','规章制度'][index],
      orderNo: index + 1,
      icon:'ant-design:folder-view-outlined',
      'status|1': ['0', '0', '1'],
      children: (() => {
        const children: any[] = [];
        for (let j = 0; j < 4; j++) {
          children.push({
            id: `${index}-${j}`,
            announceName: ['公告库', '任务催办通知', '任务办理超时预警通知', '财务部'][j],
            icon:'ant-design:folder-view-outlined',
            orderNo: j + 1,
            'status|1': ['0', '1'],
            children: undefined,
          });
        }
        return children;
      })(),
    });
  }
  return result;
})();


const announceList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      annTitle:'@cword(2,5)',
      annType:'@cword(10,20)',
      orderNo: `${index + 1}`,
      name: '@cname()',
      'level|1':['0','1','2'],
      'receive|1':['全体用户','admin','user'],
      revokeTime:'@datetime',
      createTime: '@datetime',
      tplSource: '@first',
      'status|1': ['0', '0', '1'],
    });
  }
  return result
})()

const authList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      desc:'@cword(10,20)',
      orderNo: `${index + 1}`,
      name: '@cname()',
      'level|1':['0','1','2'],
      'receive|1':['全体用户','admin','user'],
      revokeTime:'@datetime',
      createTime: '@datetime',
      tplSource: '@first',
      'status|1': ['0', '0', '1'],
    });
  }
  return result
})()

const authUserList = (()=>{
  const result: any[] = [];
  for (let index = 0; index < 40; index++) {
    result.push({
      id: index + 1,
      desc:'@cword(10,20)',
      orderNo: `${index + 1}`,
      name: '@cname()',
      'level|1':['0','1','2'],
      'receive|1':['全体用户','admin','user'],
      revokeTime:'@datetime',
      createTime: '@datetime',
      tplSource: '@first',
      'status|1': ['0', '0', '1'],
    });
  }
  return result
})()

const menuList = (() => {
  const result: any[] = [];
  for (let index = 0; index < 3; index++) {
    result.push({
      id: `${index}`,
      icon: ['ion:layers-outline', 'ion:git-compare-outline', 'ion:tv-outline'][index],
      component: 'LAYOUT',
      // router: ['Dashboard', 'auth', 'system'][index],
      type: '0',
      menuName: ['Dashboard', '权限管理', '系统'][index],
      permission: '',
      orderNo: index + 1,
      createTime: '@datetime',
      'status|1': ['0', '0', '1'],
      children: (() => {
        const children: any[] = [];
        for (let j = 0; j < 4; j++) {
          children.push({
            id: `${index}-${j}`,
            type: '1',
            menuName: ['菜单1', '菜单2', '菜单3', '菜单4'][j],
            icon: 'ion:document',
            permission: ['menu1:view', 'menu2:add', 'menu3:update', 'menu4:del'][index],
            component: [
              '/dashboard/welcome/index',
              '/dashboard/analysis/index',
              '/dashboard/workbench/index',
              '/dashboard/test/index',
            ][j],
            // router: ['Dashboard', 'auth', 'system'][j],
            orderNo: j + 1,
            createTime: '@datetime',
            'status|1': ['0', '1'],
            parentMenu: `${index}`,
            children: (() => {
              const children: any[] = [];
              for (let k = 0; k < 4; k++) {
                children.push({
                  id: `${index}-${j}-${k}`,
                  type: '2',
                  menuName: '按钮' + (j + 1) + '-' + (k + 1),
                  icon: '',
                  permission:
                    ['menu1:view', 'menu2:add', 'menu3:update', 'menu4:del'][index] +
                    ':btn' +
                    (k + 1),
                  component: [
                    '/dashboard/welcome/index',
                    '/dashboard/analysis/index',
                    '/dashboard/workbench/index',
                    '/dashboard/test/index',
                  ][j],
                  orderNo: j + 1,
                  createTime: '@datetime',
                  'status|1': ['0', '1'],
                  parentMenu: `${index}-${j}`,
                  children: undefined,
                });
              }
              return children;
            })(),
          });
        }
        return children;
      })(),
    });
  }
  return result;
})();

export default [
  {
    url: '/resrun-paas/system/getAccountList',
    timeout: 100,
    method: 'get',
    response: ({ query }) => {
      const { page = 1, pageSize = 20 } = query;
      return resultPageSuccess(page, pageSize, accountList);
    },
  },
  {
    url: '/resrun-paas/system/getRoleListByPage',
    timeout: 100,
    method: 'get',
    response: ({ query }) => {
      const { page = 1, pageSize = 20 } = query;
      return resultPageSuccess(page, pageSize, roleList);
    },
  },
  {
    url: '/resrun-paas/system/setRoleStatus',
    timeout: 500,
    method: 'post',
    response: ({ query }) => {
      const { id, status } = query;
      return resultSuccess({ id, status });
    },
  },
  {
    url: '/resrun-paas/system/getAllRoleList',
    timeout: 100,
    method: 'get',
    response: () => {
      return resultSuccess(roleList);
    },
  },
  {
    url: '/resrun-paas/system/getDeptLevelList',
    timeout: 100,
    method: 'get',
    response: () => {
      return resultSuccess(deptList);
    },
  },
  {
    url: '/resrun-paas/system/getMenuList',
    timeout: 100,
    method: 'get',
    response: () => {
      return resultSuccess(menuList);
    },
  },
  {
    url: '/resrun-paas/system/getRoleUserCount',
    timeout: 100,
    method: 'get',
    response: () => {
      return resultSuccess(roleUserCountList);
    },
  },
  {
    url: '/resrun-paas/system/getOrgAuthList',
    timeout: 100,
    method: 'get',
    response: () => {
      return resultSuccess(authGroupList);
    },
  },
  {
    url: '/resrun-paas/system/accountExist',
    timeout: 500,
    method: 'post',
    response: ({ body }) => {
      const { account } = body || {};
      if (account && account.indexOf('admin') !== -1) {
        return resultError('该字段不能包含admin');
      } else {
        return resultSuccess(`${account} can use`);
      }
    },
  },
  {
    url: '/resrun-paas/system/getUserList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(userList);
    },
  },
  {
    url: '/resrun-paas/system/getOperationList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(operationList);
    },
  },
  {
    url: '/resrun-paas/system/getAbnormalList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(abnormalList);
    }
  },
  {
    url: '/resrun-paas/system/getSecurityList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(securityList);
    }
  },
  {
    url: '/resrun-paas/system/getSafeSetting',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(safeSetting);
    }
  },
  {
    url: '/resrun-paas/system/getMsgTplTreeList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(msgTplTreeList);
    }
  },
  {
    url: '/resrun-paas/system/getMsgTplList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(msgTplList);
    }
  },
  {
    url: '/resrun-paas/system/getMyMessage',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(myMessageList);
    },
  },
  {
    url: '/resrun-paas/system/getAnnounceTreeList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(announceTreeList);
    },
  },
  {
    url: '/resrun-paas/system/getAnnounceList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(announceList);
    },
  },
  {
    url: '/resrun-paas/system/getAuthGroupList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(authList);
    },
  },
  {
    url: '/resrun-paas/system/getAuthUserList',
    timeout: 500,
    method: 'get',
    response: () => {
      return resultSuccess(authUserList);
    },
  },
] as MockMethod[];
