import Vue from 'vue'
import Vuex from 'vuex'
import user from "@/store/modules/user";
import menu from "@/store/modules/menu";

Vue.use(Vuex)

const getDefaultState = () => {
    return {
        token: '',
        hasRoutes: false
    }
}

export default new Vuex.Store({
    state: getDefaultState,
    mutations: {
        //设置token
        SET_TOKEN(state, token) {
            state.token = token;
            localStorage.setItem('token', token);
        },
        //设置是否有路由
        SET_HASROUTES(state, boolean) {
            state.hasRoutes = boolean;
        },
        RESET_INDEX_STATE(state) {
            Object.assign(state,getDefaultState())
        }
    },
    actions: {},
    modules: {
        user,
        menu
    }
})
