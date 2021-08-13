<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>
export default {
  name: 'App',
  watch: {
    //刷新浏览器的时候保留路由的标签
    //因为刷新清空了state，所以只有一个Index
    //我们如果进入的是/sys/user应该有用户管理的标签
    $route(to, from) {
      if (to.path !== '/login' && to.name != null && to.path !== '/') {
        this.$store.commit('ADD_TAB', {name: to.name, title: to.meta.title});
      }
    }
  }
}
</script>
<style>
html, body, #app {
  font-family: 'Helvetica Neue', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', 'Microsoft Yahei', sans-serif;
  height: 100%;
  padding: 0;
  margin: 0;
  font-size: 15px;
}
</style>
