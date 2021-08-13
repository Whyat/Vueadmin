//拦截user请求
import mock from 'mockjs';
import baseResult from './base';

//获取用户基本信息
mock.mock('/sys/userInfo', 'get', () => {
    const data = {
        userInfo: {
            id: '123',
            avatarUrl: 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg',
            username: 'whyat'
        }
    }
    return {
        ...baseResult,
        data
    }
});


mock.mock(RegExp('/sys/user/info/*'), 'get', () => {

    baseResult.data = {
        "id": 2,
        "created": "2021-01-30T08:20:22",
        "updated": "2021-01-30T08:55:57",
        "status": 1,
        "username": "test",
        "password": "$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO",
        "avatar": "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg",
        "email": "test@qq.com",
        "city": null,
        "lastLogin": null,
        "roles": [6,3]
    }
    return baseResult
})

//获取用户路由
mock.mock('/user/menu', 'post', () => {
    const subMenuList = [
        {
            name: 'SysMng',
            title: '系统管理',
            icon: 'el-icon-s-operation',
            children: [
                {
                    name: 'SysUser',
                    title: '用户管理',
                    path: '/sys/user',
                    component: 'sys/SysUser',
                    icon: 'el-icon-s-custom'
                },
                {
                    name: 'SysRole',
                    title: '角色管理',
                    path: '/sys/role',
                    component: 'sys/SysRole',
                    icon: 'el-icon-rank'
                },
                {
                    name: 'SysMenu',
                    title: '菜单管理',
                    path: '/sys/menu',
                    component: 'sys/SysMenu',
                    icon: 'el-icon-menu'
                }
            ]
        },
        {
            name: 'SysTool',
            title: '系统工具',
            icon: 'el-icon-s-tools',
            children: [
                {
                    name: 'SysDict',
                    title: '数字字典',
                    icon: 'el-icon-s-order',
                    path: '/sys/tool',
                    component: 'sys/SysTool'
                }
            ]
        }];
    // const permList = ['', "sys:user:save", "sys:user:delete"];
    const permList = [];
    return {
        ...baseResult,
        data: {
            subMenuList,
            permList
        }
    }
});


//userCenter 修改api
mock.mock('/sys/user/updatePass', 'post', () => {
    const data = {}
    return {
        ...baseResult,
        data
    }
});

mock.mock(RegExp('/sys/user/*'), 'post', () => {
    return baseResult
})

mock.mock(RegExp('/sys/user/list*'), 'get', () => {
    baseResult.data = {
        "records": [
            {
                "id": 1,
                "created": "2021-01-12T22:13:53",
                "updated": "2021-01-16T16:57:32",
                "status": 1,
                "username": "admin",
                "password": "$2a$10$R7zegeWzOXPw871CmNuJ6upC0v8D373GuLuTw8jn6NET4BkPRZfgK",
                "avatar": "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg",
                "email": "123@qq.com",
                "city": "广州",
                "lastLogin": "2020-12-30T08:38:37",
                "roles": [
                    {
                        "id": 6,
                        "created": "2021-01-16T13:29:03",
                        "updated": "2021-01-17T15:50:45",
                        "status": 1,
                        "name": "超级管理员",
                        "code": "admin",
                        "remark": "系统默认最高权限，不可以编辑和任意修改",
                        "menuIds": []
                    },
                    {
                        "id": 3,
                        "created": "2021-01-04T10:09:14",
                        "updated": "2021-01-30T08:19:52",
                        "status": 1,
                        "name": "普通用户",
                        "code": "normal",
                        "remark": "只有基本查看功能",
                        "menuIds": []
                    }
                ]
            },
            {
                "id": 2,
                "created": "2021-01-30T08:20:22",
                "updated": "2021-01-30T08:55:57",
                "status": 1,
                "username": "test",
                "password": "$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO",
                "avatar": "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg",
                "email": "test@qq.com",
                "city": null,
                "lastLogin": null,
                "roles": [
                    {
                        "id": 3,
                        "created": "2021-01-04T10:09:14",
                        "updated": "2021-01-30T08:19:52",
                        "status": 1,
                        "name": "普通用户",
                        "code": "normal",
                        "remark": "只有基本查看功能",
                        "menuIds": []
                    }
                ]
            }
        ],
       "pagination":{
           "total": 2,
           "size": 10,
           "current": 1,
           "orders": [],
           "optimizeCountSql": true,
           "hitCount": false,
           "countId": null,
           "maxLimit": null,
           "searchCount": true,
           "pages": 1
       }
    }

    return baseResult
})


//拦截登出
mock.mock('/user/logout', 'post', () => {
    return {
        ...baseResult,
        data: null
    }
});


//拦截登录请求
mock.mock('/login', 'post', () => {
    return loginResult;
});

const loginResult = {
    code: 200,
    msg: '错误',
    data: null
}

