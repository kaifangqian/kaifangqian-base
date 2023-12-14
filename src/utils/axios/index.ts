import axios from 'axios';
// import jsonBig  from 'json-bigint';
import {message} from 'ant-design-vue';
	
const service = axios.create({
    baseURL:"/openSign",  //开发环境api 生产环境配置服务域名
    timeout: 30000,
	// transformResponse:function(data){
	// 	try {
	// 	  const json = jsonBig({
	// 	    storeAsString: true
	// 	  })
	// 	  return json.parse(data);
	// 	} catch (err) {
	// 		console.log("jsonBig error",err,data);
	// 	  return data;
	// 	} 
	// },
});

service.interceptors.request.use(
    (config:any) => {
		console.log(config);
        return config;
    },
    (error:any) => {
        return Promise.reject();
    }
);

service.interceptors.response.use(
    (response:any) => {
        if (response.status === 200) {
            return response.data;
        } else {
           return  Promise.reject('请求失败');
        }
    },
    (error:any) => {
        if (error && error.response) {
			if(error.response.status == 401){
				message.warning("系统检测到未登录，请登录PaaS平台进行跳转");
				console.error(error);
			}
            switch (error.response.status) {
              case 400:
                error.message = '错误请求'
                break;
              case 401:
                //error.message = '未授权，请重新登录'
                break;
              case 403:
                error.message = '拒绝访问'
                break;
              case 404:
                error.message = '请求错误,未找到该资源'
                break;
              case 405:
                error.message = '请求方法未允许'
                break;
              case 408:
                error.message = '请求超时'
                break;
              case 431:
                error.message = '请求错误,未找到该资源'
                break;				
              case 500:
                error.message = '服务器端出错'
                break;
              case 501:
                error.message = '网络未实现'
                break;
              case 502:
                error.message = '网络错误'
                break;
              case 503:
                error.message = '服务不可用'
                break;
              case 504:
                error.message = '网络超时'
                break;
              case 505:
                error.message = 'http版本不支持该请求'
                break;
              default:
                error.message = `连接错误${error.response.status}`
            }
        } else {
            error.message = "连接到服务器失败"
        }
        return Promise.reject(error);
    }
);

export default service
