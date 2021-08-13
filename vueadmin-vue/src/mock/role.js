//拦截user请求
import mock from 'mockjs';
import baseResult from './base';

//角色的增删
mock.mock(RegExp('/sys/role/*'), 'post', () => {
    return baseResult;
})
//////////////// 角色管理 ////////////////

mock.mock(RegExp('/sys/role/list*'), 'get', () => {

    baseResult.data = {
        "records": [
            {
                "id": 3,
                "created": "2021-01-04T10:09:14",
                "updated": "2021-01-30T08:19:52",
                "status": 1,
                "name": "普通用户",
                "code": "normal",
                "remark": "只有基本查看功能",
                "menuIds": []
            },
            {
                "id": 6,
                "created": "2021-01-16T13:29:03",
                "updated": "2021-01-17T15:50:45",
                "status": 1,
                "name": "超级管理员",
                "code": "admin",
                "remark": "系统默认最高权限，不可以编辑和任意修改",
                "menuIds": []
            }
        ],
        'pagination':{
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


//用户表格数据获取
mock.mock(RegExp('/sys/role/info/*'), 'get', () => {
    baseResult.data = {
        "id": 6,
        "created": "2021-01-16T13:29:03",
        "updated": "2021-01-17T15:50:45",
        "status": 1,
        "name": "超级管理员",
        "code": "admin",
        "discription": "系统默认最高权限，不可以编辑和任意修改",
        "menuIds": [3]
    }
    return baseResult
})
