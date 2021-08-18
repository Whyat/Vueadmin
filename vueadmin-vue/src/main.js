import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'

import axios from "axios";
//mock自定义后台数据
// import './mock/index';
import element from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import './globalFunc';

Vue.config.productionTip = false;
Vue.use(element);
Vue.prototype.$axios = axios;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
