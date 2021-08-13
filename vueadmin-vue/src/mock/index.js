import mock from 'mockjs';
import baseResult from './base';
import './user';
import './menu';
import './role'



const data = {
    //验证码图片
    captcha: mock.Random.dataImage('120x40', 'p7n5w'),
    //随机数token
    token: mock.Random.string(32)
}


//拦截get的验证码请求
mock.mock('/captcha', 'get', () => {
    return {
        ...baseResult,
       data
    }
});


//在访问登录页面的时候得到的
//返回data携带的验证码和随机码
