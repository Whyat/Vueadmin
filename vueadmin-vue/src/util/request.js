import axios from 'axios'
import router from "../router";
import el from 'element-ui';

// axios.defaults.baseURL = 'http://localhost:8081'
axios.defaults.baseURL = 'http://1.15.157.200:8081'
// create an axios instance
const request = axios.create({
    // baseURL: baseUrl, // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 5000, // axios timeout,
    headers: {
        'Content-Type': "application/json; charset=utf-8",
        'Access-Control-Allow-Origin': '*' //cors错误是因为请求头没加Access-Control-Allow-XXX信息
    }
})

// 设置请求拦截器
request.interceptors.request.use((config) => {
    if (config.url !== "/captcha") {
        //每次请求给header添加token
        config.headers['Authorization'] = localStorage.getItem('token');
    }
    return config;
});

//根据返回的code弹窗拦截
request.interceptors.response.use(
    response => {//请求成功，判断业务code
        console.log('拦截器->返回正常')
        let resp = response.data;
        if (resp.code === 2000) {
            return response;
        } else {
            console.log('e:');
            console.log( resp);
            el.Message.error(resp.data ? resp.data : '系统异常');
            return response;
        }
    },
    error => {//http请求异常
        console.log('拦截器->返回错误！');
        console.log(error);
        const statusCode = error.response.status;
        const msg = error.response.data.data;
        //没有登录就跳转登录页面
        if (statusCode === 401) {
            el.Message.error('没有携带jwt，请重新登录', {duration: 3000})
            router.push('/login');
            //服务器报错弹出错误消息框
        } else if (statusCode >= 500 && statusCode < 600) {
            el.Message.error(msg, {duration: 3000});
        } else if (statusCode == 400) {
            el.Message.error(msg, {duration: 3000});
        }
        //传出
        return Promise.reject(error);
    })


export default request;
