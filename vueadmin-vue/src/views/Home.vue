<template>
  <el-container>
    <side-menu/>
    <el-container>
      <el-header>
        <strong>VueAdmin后台管理系统</strong>
        <div class="avatar-tab">
          <el-avatar :src="userInfo.avatarUrl"></el-avatar>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ userInfo.username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">

              <router-link to="/usercenter">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item command="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="https://element.eleme.io" target="_blank">链接</el-link>
        </div>
      </el-header>
      <el-main>
        <tabs/>
        <div class="router-view">
          <router-view/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from "./inc/SideMenu";
import Tabs from './inc/Tabs';

export default {
  name: "Home",
  components: {SideMenu,Tabs},
  data() {
    return {
      message: '123',
      userInfo:{
        id :'',
        avatarUrl:'',
        username:''
      }
    }
  },
  methods: {
    //存储用户信息到store
    getUserInfo() {
      this.$store.dispatch('getUserInfo').then(
          res => {
            this.userInfo = res.data.data.userInfo;
          }
      )
    },
    //处理注销
    handleCommand(command) {
      switch (command){
        case 'logout':
          this.$store.dispatch('logout');
          break;
      }
    }
  },
  created() {
    this.getUserInfo();
  },
  computed:{
  }
}
</script>

<style scoped>
.router-view{
  margin:0 2%;
}

.avatar-tab {
  float: right;
  display: flex;
  width: 210px;
  justify-content: space-around;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.el-container {
  margin: 0;
  padding: 0;
  height: 100%;
}

.el-header {
  background-color: #17b3a3;
  color: #333;
  text-align: center;
  line-height: 60px;
}


.el-main {
  color: #333;
  padding:0;
}

a {
  text-decoration: none;
}

</style>
