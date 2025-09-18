import { MockMethod } from 'vite-plugin-mock';
import { resultError, resultSuccess, getRequestToken, requestParams } from '../_util';
import captcha from 'gzm-captcha'

export function createFakeUserList() {
  return [
    {
      userId: '1',
      username: 'admin1',
      realName: 'admin',
      avatar: '',
      desc: 'manager',
      password: '123456',
      token: 'fakeToken1',
      homePath: '/dashboard/analysis',
      roles: [
        {
          roleName: 'Super Admin',
          value: 'super',
        },
      ],
    },
    {
      userId: '2',
      username: 'test',
      password: '123456',
      realName: 'test user',
      avatar: '',
      desc: 'tester',
      token: 'fakeToken2',
      homePath: '/dashboard/workbench',
      roles: [
        {
          roleName: 'Tester',
          value: 'test',
        },
      ],
    },
    {
      userId: '3',
      username: 'admin',
      password: '123456',
      realName: 'admin',
      avatar: '',
      desc: 'tester',
      token: 'fakeToken3',
      homePath: '/dashboard/analysis',
      roles: [
        {
          roleName: '测试',
          value: 'test',
        },
      ],
      departs:[
        {  id:'2f3f7fca94ee4a94a390094794d9c29d',departName:'CRM',orgCode:'A045' },
        {  id:'2f3f7fca94ee4a94a390094794d9c294',departName:'北京恒丰科技',orgCode:'A046' },
        {  id:'2f3f7fca94ee4a94a390094794d9c23d',departName:'北京资源科技',orgCode:'A047' },
      ]
    },
  ];
}

const fakeCodeList: any = {
  '1': ['1000', '3000', '5000'],

  '2': ['2000', '4000', '6000'],
};
const accounntList = [
  {  id:'2f3f7fca94ee4a94a390094794d9c29d',departName:'CRM',orgCode:'A045' },
  {  id:'2f3f7fca94ee4a94a390094794d9c294',departName:'北京恒丰科技',orgCode:'A046' },
  {  id:'2f3f7fca94ee4a94a390094794d9c23d',departName:'北京资源科技',orgCode:'A047' },
]
export default [
  // mock user login
  {
    url: '/resrun-paas/sys/login',
    timeout: 200,
    method: 'post',
    response: ({ body }) => {
      const { username, password } = body;
      const checkUser = createFakeUserList().find(
        (item) => item.username === username && password === item.password,
      );
      if (!checkUser) {
        return resultError('Incorrect account or password！');
      }
      const { userId, username: _username, token, realName, desc, roles, departs} = checkUser;
      return resultSuccess({
        roles,
        userId,
        username: _username,
        token,
        realName,
        desc,
        departs
      });
    },
  },
  {
    url: '/resrun-paas/getAccounts',
    timeout: 200,
    method: 'get',
    response: () => {
      return resultSuccess(accounntList);
    },
  },
  {
    url: '/resrun-paas/sys/user/getUserInfo',
    method: 'get',
    response: (request: requestParams) => {
      console.log(request,'33333333333333')
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('The corresponding user information was not obtained!');
      }
      return resultSuccess(checkUser);
    },
  },
  {
    url: '/resrun-paas/SelectAccount',
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('The corresponding user information was not obtained!');
      }
      return resultSuccess(checkUser);
    },
  },
  {
    url: '/resrun-paas/sys/randomImage',
    method: 'get',
    timeout: 500,
    response: async() => {
     // 配置
      // const conf = { 
      //   width: 300, 
      //   height: 100, 
      //   len: 6,
      //   type: '',
      //   lineShow: false, 
      //   pointShow: false 
      // }
      // // 获得图片验证码base64数据
      // const imagedata = await captcha(conf).value()
      return resultSuccess(1);
    },
  },
  {
    url: '/resrun-paas/getPermCode',
    timeout: 200,
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      const codeList = fakeCodeList[checkUser.userId];

      return resultSuccess(codeList);
    },
  },
  {
    url: '/resrun-paas/logout',
    timeout: 200,
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      return resultSuccess(undefined, { message: 'Token has been destroyed' });
    },
  },
  {
    url: '/resrun-paas/testRetry',
    statusCode: 405,
    method: 'get',
    response: () => {
      return resultError('Error!');
    },
  },
] as MockMethod[];
