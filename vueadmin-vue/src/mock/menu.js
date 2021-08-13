//拦截user请求
import mock from 'mockjs';
import baseResult from './base';

//获取某行数据
mock.mock(RegExp('/sys/menu/info/*'), 'get', () => {

    baseResult.data = {
        "id": 3,
        "status": 1,
        "parentId": 1,
        "name": "角色管理",
        "path": "/sys/roles",
        "perms": "sys:role:list",
        "component": "sys/Role",
        "type": 1,
        "icon": "el-icon-rank",
        "orderNum": 2,
        "children": []
    }

    return baseResult
})


mock.mock(RegExp('/sys/menu/*'), 'post', () => {

    return baseResult;
})

mock.mock('/sys/menu/list', 'get', () => {
    let tableData = [
        {
            "id": 1,
            "created": "2021-01-15T18:58:18",
            "updated": "2021-01-15T18:58:20",
            "statuss": 1,
            "parentId": 0,
            "name": "系统管理",
            "path": "",
            "perms": "sys:manage",
            "component": "",
            "type": 0,
            "icon": "el-icon-s-operation",
            "ordernum": 1,
            "children": [
                {
                    "id": 2,
                    "created": "2021-01-15T19:03:45",
                    "updated": "2021-01-15T19:03:48",
                    "status": 1,
                    "parentId": 1,
                    "name": "用户管理",
                    "path": "/sys/users",
                    "perms": "sys:user:list",
                    "component": "sys/User",
                    "type": 1,
                    "icon": "el-icon-s-custom",
                    "ordernum": 1,
                    "children": [
                        {
                            "id": 9,
                            "created": "2021-01-17T21:48:32",
                            "updated": null,
                            "status": 1,
                            "parentId": 2,
                            "name": "添加用户",
                            "path": null,
                            "perms": "sys:user:save",
                            "component": null,
                            "type": 2,
                            "icon": null,
                            "ordernum": 1,
                            "children": []
                        },
                        {
                            "id": 10,
                            "created": "2021-01-17T21:49:03",
                            "updated": "2021-01-17T21:53:04",
                            "status": 1,
                            "parentId": 2,
                            "name": "修改用户",
                            "path": null,
                            "perms": "sys:user:update",
                            "component": null,
                            "type": 2,
                            "icon": null,
                            "ordernum": 2,
                            "children": []
                        },
                        {
                            "id": 11,
                            "created": "2021-01-17T21:49:21",
                            "updated": null,
                            "status": 1,
                            "parentId": 2,
                            "name": "删除用户",
                            "path": null,
                            "perms": "sys:user:delete",
                            "component": null,
                            "type": 2,
                            "icon": null,
                            "ordernum": 3,
                            "children": []
                        },
                        {
                            "id": 12,
                            "created": "2021-01-17T21:49:58",
                            "updated": null,
                            "status": 1,
                            "parentId": 2,
                            "name": "分配角色",
                            "path": null,
                            "perms": "sys:user:role",
                            "component": null,
                            "type": 2,
                            "icon": null,
                            "ordernum": 4,
                            "children": []
                        },
                        {
                            "id": 13,
                            "created": "2021-01-17T21:50:36",
                            "updated": null,
                            "status": 1,
                            "parentId": 2,
                            "name": "重置密码",
                            "path": null,
                            "perms": "sys:user:repass",
                            "component": null,
                            "type": 2,
                            "icon": null,
                            "ordernum": 5,
                            "children": []
                        }
                    ]
                },
                {
                    "id": 3,
                    "created": "2021-01-15T19:03:45",
                    "updated": "2021-01-15T19:03:48",
                    "status": 1,
                    "parentId": 1,
                    "name": "角色管理",
                    "path": "/sys/roles",
                    "perms": "sys:role:list",
                    "component": "sys/Role",
                    "type": 1,
                    "icon": "el-icon-rank",
                    "ordernum": 2,
                    "children": []
                },

            ]
        },
        {
            "id": 5,
            "created": "2021-01-15T19:06:11",
            "updated": null,
            "status": 1,
            "parentId": 0,
            "name": "系统工具",
            "path": "",
            "perms": "sys:tools",
            "component": null,
            "type": 0,
            "icon": "el-icon-s-tools",
            "ordernum": 2,
            "children": [
                {
                    "id": 6,
                    "created": "2021-01-15T19:07:18",
                    "updated": "2021-01-18T16:32:13",
                    "status": 1,
                    "parentId": 5,
                    "name": "数字字典",
                    "path": "/sys/dicts",
                    "perms": "sys:dict:list",
                    "component": "sys/Dict",
                    "type": 1,
                    "icon": "el-icon-s-order",
                    "ordernum": 1,
                    "children": []
                }
            ]
        }
    ]

    baseResult.data = tableData;

    return baseResult
})
