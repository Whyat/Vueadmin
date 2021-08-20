import request from '@/util/request'
import axios from "_axios@0.21.1@axios";
import router from "@/router";
import el from 'element-ui'
//初始状态
const getDefaultState = () => {
    return {
        userInfo: {},
        subMenuList: [],
        permList: []
    }
}

const mutations = {

    SET_USER_INFO(state, userInfo) {
        state.userInfo = userInfo;
    },
    SET_SUBMENULIST(state, subMenuList) {
        state.subMenuList = subMenuList;
    },
    SET_PERMLIST(state, list) {
        state.permList = list;
    },
    //重置state
    RESET_USER_STATE(state) {
        Object.assign(state, getDefaultState())
    }
}

const actions = {
    //获取用户信息，存为state
    getUserInfo(context, userInfo) {
        return new Promise((resolve, reject) => {
            request.get('/sys/userInfo').then(response => {
                const data = response.data
                context.commit('SET_USER_INFO', data.data.userInfo)
                resolve(response)
            }).catch(error => {
                reject(error)
            })
        })
    },
    //获取用户的菜单列表
    //存入state
    getSubMenuList(context) {
        return new Promise((resolve, reject) => {
            axios.get('/sys/user/menu', {
                headers: {
                    Authorization: localStorage.getItem("token")
                }
            }).then(res => {
                    //把路由列表放到store中
                    context.commit('SET_SUBMENULIST', res.data.data.subMenuList);
                    context.commit('SET_PERMLIST', res.data.data.permList);
                    context.commit('SET_HASROUTES', true);
                resolve(res.data);
                }
            ).catch(err => {
                reject(err);
            })
        })
    },
    //退出清空state
    logout({commit}) {
        //1.axios异步登出请求
        request.post('/logout').then(
            res => {
                sessionStorage.clear();
                localStorage.clear();
                commit('RESET_INDEX_STATE');
                commit('RESET_MENU_STATE');
                commit('RESET_USER_STATE');
                el.Message.success('成功登出！');
                router.push('/login');
            },
            error =>{

            }
        )

    }
}

const user = {
    state: getDefaultState(),
    mutations,
    actions
}
export default user;
