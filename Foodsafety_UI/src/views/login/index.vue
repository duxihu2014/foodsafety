<template>
  <div>
    <div class="login-container">
      <el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="0px" class="card-box login-form">
        <h3 class="title">系统登录</h3>
        <el-form-item prop="username">
      <span class="svg-container">
         <i class="fa fa-user" style="" aria-hidden="true"></i>
      </span>
          <el-input name="username" type="text" v-model="loginForm.username" autoComplete="on" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
      <span class="svg-container">
       <i class="fa fa-unlock-alt" aria-hidden="true"></i>
      </span>
          <el-input name="password" type="password" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="new-password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleLogin">
            登录
          </el-button>
        </el-form-item>
        <a v-on:click="register" style="color: white;font-size: 13px">企业注册</a>
        <a v-on:click="registerQuery" style="color: white;font-size: 13px;margin-left: 290px;">注册查询</a>

      </el-form>
      <el-dialog  title="第三方验证" :visible.sync="showDialog">
        邮箱登录成功,请选择第三方验证
        <!--  <social-sign></social-sign> -->
        <faceLogin></faceLogin>
      </el-dialog>
      <!--下载APP二维码-->
      <!--<div class="apkDownload-container" v-show="apkDownloadShow">
        <p>Android手机APP下载 <span  @click="deleteApk">x</span></p>
        <img :src="apkDownloadImg" alt="">
      </div>-->
      <div class="apkDownload-container" v-show="apkDownloadShow">
        <p>企业版APP下载</p>
        <img :src="apkDownloadImg_qy" alt="">
        <p>监管版APP下载</p>
        <img :src="apkDownloadImg_jg" alt="">
      </div>
    </div>
    <el-dialog   :visible.sync="showRegisterDialog" :fullscreen="true">
      <register :dictData="dictData" :area="area" v-if="showRegisterDialog" @closeDialog="closeDialog"></register>
    </el-dialog>
    <el-dialog   :visible.sync="registerQueryDialog" :fullscreen="true">
      <registerUserLogin :dictData="dictData" :area="area" v-if="registerQueryDialog"></registerUserLogin>
    </el-dialog>
  </div>

</template>

<script>
import socialSign from './socialsignin';
import register from './register';
import registerUserLogin from './registerUserLogin';
import iconSvg from 'components/Icon-svg/index';
import {getDict,getAreaTree} from "api/admin/register/index"

export default {
  components: {
    register,registerUserLogin,
    socialSign,iconSvg
  },
  name: 'login',
  data() {
    // const validateEmail = (rule, value, callback) => {
    //   if (!isWscnEmail(value)) {
    //     callback(new Error('请输入正确的合法邮箱'));
    //   } else {
    //     callback();
    //   }
    // };
    const validateUser = (rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('用户名不能为空'));
      } else {
        callback();
      }
    };
    const validatePass = (rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('密码不能为空'));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{
          required: true,
          trigger: 'blur',
          validator: validateUser
        }],
        password: [{
          required: true,
          trigger: 'blur',
          validator: validatePass
        }]
      },
      loading: false,
      showDialog: false,
      showRegisterDialog:false,
      registerQueryDialog:false,
      dictData:null,
      area:null,
      apkDownloadShow:true,
      apkDownloadImg:require('assets/apk-download.png'),
      apkDownloadImg_qy:require('assets/apk-download-qy.png'),
      apkDownloadImg_jg:require('assets/apk-download-jg.png'),
    }
  },
  methods: {
    handleLogin() {
      //debugger
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch('LoginByEmail', this.loginForm).then(() => {
            this.loading = false;
            this.$router.push({
              path: '/'
            });
            //this.showDialog = true;
          }).catch(() => {
            this.loading = false;
          });
        } else {
          //console.log('error submit!!');
          return false;
        }
      });
    },
    register() {
      this.showRegisterDialog=true;
    },
    initData(){
      getDict().then(response => {
        this.dictData=response;
      })
      getAreaTree().then(response=> {
        this.area=response;
      })
    },
    closeDialog(){
      this.showRegisterDialog=false;
    },
    registerQuery(){
      this.registerQueryDialog=true;
    },
    deleteApk(){
      this.apkDownloadShow=false;
    },
    afterQRScan() {
      // const hash = window.location.hash.slice(1);
      // const hashObj = getQueryObject(hash);
      // const originUrl = window.location.origin;
      // history.replaceState({}, '', originUrl);
      // const codeMap = {
      //   wechat: 'code',
      //   tencent: 'code'
      // };
      // const codeName = hashObj[codeMap[this.auth_type]];
      // if (!codeName) {
      //   alert('第三方登录失败');
      // } else {
      //   this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
      //     this.$router.push({ path: '/' });
      //   });
      // }
    }
  },
  mounted() {
    //this.handleLogin();
  },
  created() {
    this.initData();
    // window.addEventListener('hashchange', this.afterQRScan);
  },
  destroyed() {
    // window.removeEventListener('hashchange', this.afterQRScan);
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/mixin.scss";
.tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 5px;
}

.login-container {
    @include relative;
    height: 100vh;
    background-color: #2d3a4b;
    input:-webkit-autofill {
        -webkit-box-shadow: 0 0 0 1000px #293444 inset !important;
        -webkit-text-fill-color: #fff !important;
    }
    input {
        background: transparent;
        border: 0;
        -webkit-appearance: none;
        border-radius: 0;
        padding: 12px 5px 12px 15px;
        color: #eeeeee;
        height: 47px;
    }
    .el-input {
        display: inline-block;
        height: 47px;
        width: 85%;
    }
    .svg-container {
        padding: 6px 5px 6px 15px;
        color: #889aa4;
    }
    .title {
        font-size: 26px;
        font-weight: 400;
        color: #eeeeee;
        margin: 0 auto 40px;
        text-align: center;
        font-weight: bold;
    }
    .login-form {
        position: absolute;
        left: 0;
        right: 0;
        width: 400px;
        padding: 35px 35px 15px;
        margin: 120px auto;
    }
    .el-form-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        background: rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        color: #454545;
    }
    .forget-pwd {
        color: #fff;
    }
  .apkDownload-container{
    position: fixed;
    right: 50px;
    top: 30px;
    p{
      font-size: 15px;
      color: white;
      text-align: center;
      span{
        cursor: pointer;
      }
    }
    img{
      height: 180px;
      width:180px;
    }
  }
}
</style>
