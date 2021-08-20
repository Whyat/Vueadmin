import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home'
import Index from '../views/Index'
import store from '@/store/index';
import axios from "axios";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: 'index',
                name: 'Index',
                component: Index
            },
            {
                path: '/userCenter',
                name: 'UserCenter',
                meta: {
                    title: "个人中心"
                },
                component: () => import('@/views/UserCenter.vue')
            }
            // {
            //   path:'usercenter',
            //   name:'UserCenter',
            //   component:UserCenter
            // },
            // {
            //   path: 'sys/user',
            //   name:'SysUser',
            //   component: SysUser
            // },
            // {
            //   path: 'sys/role',
            //   name:'SysRole',
            //   component: SysRole
            // },
            // {
            //   path: 'sys/menu',
            //   name:'SysMenu',
            //   component: SysMenu
            // },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    {
        path: '/*',
        name: '404',
        component: () => import("@/views/error/404")
    }
]

const router = new VueRouter({
    mode: 'history',
    // base: process.env.BASE_URL,
    base:'vueadmin',
    routes
})

//
router.beforeEach(async (to, from, next) => {
        const hasRoutes = store.state.hasRoutes;
    const token = localStorage.getItem('token');
    if(to.path == '/login')
        next();
    //没有token返回登陆页面
    else if(!token)
        next({path:'/login'});
    else if (!hasRoutes) {
        if (await addRoutes(next)) {
        } else {
            next();
        }
    } else {
        next();
    }
});

function addRoutes(next) {
    store.dispatch('getSubMenuList').then(data => {
        // 动态绑定路由
        let menuList =  data.data.subMenuList;
        menuList.forEach(menu => {
            if (menu.children) {
                menu.children.forEach(e => {
                    // 转成路由
                    let route = menuToRoute(e)

                    // 吧路由添加到路由管理中
                    if (route) {
                        router.addRoute('Home',route);
                    }
                })
            }
        })
        next();
    })
}

// 导航转成路由
const menuToRoute = (menu) => {

    if (!menu.component) {
        return null
    }

    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        }
    }
    route.component = () => import('@/views/' + menu.component + '.vue')

    return route
}
export default router
