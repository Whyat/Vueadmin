<template>
  <el-aside width="200px">
    <!--点击tab切换菜单栏的高亮-->
    <el-menu :default-active="$store.state.menu.editableTabsValue"
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
      <router-link to="/index">
        <el-menu-item index="Index" @click="selectMenu({name:'Index',title:'首页'})">
          <template slot="title"><i class="el-icon-s-home"></i> <span slot="title">首页</span></template>
        </el-menu-item>
      </router-link>

      <el-submenu v-for="subMenu in subMenuList" :index="subMenu.name">

        <template slot="title">
          <i :class="subMenu.icon"></i>
          <span>{{ subMenu.title }}</span>
        </template>

        <router-link v-for="child in subMenu.children" :to="child.path">
          <el-menu-item :index="child.name" @click="selectMenu(child)">
            <template slot="title">
              <i :class="child.icon"></i> <span slot="title">{{ child.title }}</span>
            </template>
          </el-menu-item>
        </router-link>

      </el-submenu>

    </el-menu>
  </el-aside>
</template>

<script>

export default {
  name: "SideMenu",
  methods: {
    selectMenu(item){
      this.$store.commit('ADD_TAB', item);
    }
  },
  computed: {
    subMenuList() {
      return this.$store.state.user.subMenuList;
    }
  }
}
</script>

<style scoped>
.el-aside {
  background-color: #D3DCE6;
  color: #333;
  line-height: 200px;
}

.el-menu-vertical-demo {
  height: 100%;
}

a {
  text-decoration: none;
}
</style>
