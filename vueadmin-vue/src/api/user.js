import request from "../util/request";

//登录的方法
export function login(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data
    });
}

export function getSubMenu() {
    return request({
        url: '/user/menu',
        method: 'post',
    })
}
