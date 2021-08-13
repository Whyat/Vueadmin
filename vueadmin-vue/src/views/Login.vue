<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :xl="6" :lg="7">
      <h2>欢迎来到VueAdmin管理系统</h2>
      <el-image style="height: 180px;width: 180px" :src="require('@/assets/MarkerHub.jpg')"/>
      <p>公众号 MarkerHub</p>
      <p>扫码二维码，回复【 VueAdmin 】获取登录密码</p>
    </el-col>
    <el-col :span="1">
      <el-divider direction="vertical"></el-divider>
    </el-col>
    <el-col :xl="6" :lg="7">
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item class="input" label="用户名" prop="username">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item class="input" label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item class="input" label="验证码" prop="captchaCode">
          <el-input v-model="loginForm.captchaCode" style="width: 172px;float: left" maxlength="5"></el-input>
          <el-image :src="captchaImg" @click="getCaptcha" class="captchaImg"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')" :disabled="loginButtonDisabled">登录</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
import request from "@/util/request.js";

export default {
  name: "Login",
  data() {
    return {
      loginButtonDisabled:true,
      loginForm: {
        username: '',
        password: '',
        //验证码的唯一标识
        key: '',
        captchaCode: ''
      },
      captchaImg: '',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
        captchaCode: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 5, max: 5, message: '长度在为5个字符', trigger: 'blur'}
        ],
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.loginButtonDisabled = true;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request.post('/login', this.loginForm).then(res => {
            if (res.data.code === 2000) {
              //请求成功取得jwt
              const jwt = res.headers['authorization'];
              this.$store.commit('SET_TOKEN', jwt);
              this.$router.push('/index');
            }else{
              //登陆失败，再次获取验证码
              this.getCaptcha()
            }
          }).catch(err => {
            console.log(err);
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //发起请求获取验证码图片(Base64字符串)和验证码的唯一标识uuid
    getCaptcha() {
      request.get('/captcha').then((res) => {
        let data = res.data.data;
        console.log(data);
        this.captchaImg = data.captchaImg;
        this.loginForm.key = data.key;
        this.loginForm.captchaCode = '';
        //防止用户在验证码刷出来之前登录
        this.loginButtonDisabled = false;
      });
    }
  },
  created() {
    //创建成功访问接口获得验证码和验证码的唯一标识key
    this.getCaptcha();
  }
}
</script>

<style scoped>
.el-row {
  background: #fafafa;
  height: 100vh;
  display: flex;
  align-items: center;
  text-align: center;
}

.el-divider {
  height: 200px;
}

.input {
  width: 380px;
}

.captchaImg {
  float: left;
  margin-left: 8px;
  border-radius: 4px;
}
</style>
