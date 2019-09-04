<template>
  <div>
    <el-menu class="navbar">
      <div class="title">智慧食品安全监管服务平台</div>

      <el-dropdown class="avatar-container" trigger="click" @command="handleCommand">
        <div class="avatar-wrapper">
          <!--<img class="user-avatar" :src="avatar+'?imageView2/1/w/80/h/80'">-->
          <!--<span class="user-title">欢迎您，{{name}}</span>
          <i class="el-icon-caret-bottom"></i>-->
          <el-button size="small" type="info" icon="el-icon-info">
            {{name}}
            <!--<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
          </el-button>
        </div>
        <el-dropdown-menu class="user-dropdown" slot="dropdown">
          <router-link class='inlineBlock' to="/">
            <el-dropdown-item>前往首页</el-dropdown-item>
          </router-link>
          <!--<el-dropdown-item divided command="portal">平台门户</el-dropdown-item>-->
          <el-dropdown-item divided command="updatepass">修改密码</el-dropdown-item>
          <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <!--<el-button type="success" size="small" ><a data-v-eecbd78e="" :href="'#/personal/register'" class="">test</a></el-button>-->
      <el-dropdown class="avatar-container" trigger="click" @command="personalHandleCommand">
        <div class="avatar-wrapper">
          <!--<template v-if="(user.userType == '3' && (count_register + count_verify + count_cert + count_onsite) > 0) || (user.userType == '4' && count_cert > 0 )">-->
          <template v-if="role.isSuperviseRole || role.isEnterpriseRole">
            <el-badge class="message" :value="count_register+count_verify+(res_count_cert)+(user.personnelId>0?count_onsite:0)" :hidden="(count_register+count_verify+(res_count_cert)+(user.personnelId>0?count_onsite:0))<=0">
              <el-button size="small" type="primary" icon="el-icon-edit-outline" :loading="dataLoading">
                待办
              </el-button>
            </el-badge>
          </template>
          <!--<template v-else>-->
          <!--<el-button size="small" type="info" icon="el-icon-edit-outline" :loading="dataLoading">-->
          <!--个人事务-->
          <!--</el-button>-->
          <!--</template>-->

        </div>
        <el-dropdown-menu class="user-dropdown" slot="dropdown">
          <!--<el-dropdown-item  command="register" v-if="count_register > 0 && user.userType == '3'">-->
          <el-dropdown-item command="register" v-if="role.isSuperviseRole">
            <el-badge :value="count_register" :max="99" :hidden="count_register<=0">
              企业注册
            </el-badge>
          </el-dropdown-item>
          <!--<el-dropdown-item divided command="modify" v-if="count_register > 0 && user.userType == '3'">-->
          <el-dropdown-item divided command="modify" v-if="role.isSuperviseRole">
            <el-badge :value="count_verify" :max="99" :hidden="count_verify<=0">
              档案更新
            </el-badge>
          </el-dropdown-item>
          <el-dropdown-item  divided command="cert" v-if="isshowtype_four&&(role.isSuperviseRole || role.isEnterpriseRole)">
              <el-badge  :value="count_cert" :max="99" :hidden="count_cert<=0">
                证照预警
              </el-badge>
          </el-dropdown-item>
          <!--<el-dropdown-item divided command="safety" v-if="user.userType == '3' && count_onsite > 0">-->
          <el-dropdown-item divided command="safety" v-if="role.isSuperviseRole || role.isEnterpriseRole">
            <el-badge :value="count_onsite" :max="99" :hidden="count_onsite<=0">
              安全检查
            </el-badge>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <!--<div class="avatar-container" v-if="(user.userType == '3' || user.userType == '4')">
        <label style="color:#fff;font-size: 14px;">报警提醒:
          <a :href="'#/admin/alarm/index?queryTime='+lookAlarm_time" @click="handleClick">
            <span style="color:#f56c6c">
              <u style="width:10px;">
                <b>{{count_alarm}}</b>
              </u>
            </span>
          </a>
        </label>
      </div>-->
      <div class="avatar-container" v-if="role.isSuperviseRole || role.isEnterpriseRole">
        <el-badge style="margin-top: 5px;" class="message" :value="count_alarm" :max="99" :hidden="count_alarm<=0">
          <div @click="handleClick">
            <el-button size="small" type="danger" icon="el-icon-bell">
              报警
            </el-button>
          </div>
        </el-badge>
      </div>

      <!-- <div class="avatar-container" v-if="role.isSuperviseRole || role.isEnterpriseRole">
        <el-badge style="margin-top: 5px;" class="message" :value="count_alarm" :max="99" :hidden="count_alarm<=0">
          <a :href="'#/admin/alarm/index?queryTime='+lookAlarm_time" @click="handleClick">
            <el-button size="small" type="danger" icon="el-icon-bell">
              报警{{lookAlarm_time}}
            </el-button>
          </a> -->
          <!--<el-button v-else="count_alarm?<=9" size="small" type="warning" icon="el-icon-bell">
            报警
          </el-button>-->
        <!-- </el-badge>
      </div> -->
    </el-menu>
    <tabs-view></tabs-view>

    <el-dialog title="修改密码" :visible.sync="dialogVisible" @close="closeDialog" width="30%" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="旧密码" prop="userPwd_old">
          <el-input type="password" v-model="form.userPwd_old" placeholder="请输入旧密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="userPwd">
          <el-input type="password" v-model="form.userPwd" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="userPwd2">
          <el-input type="password" v-model="form.userPwd2" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible=false">取 消</el-button>
        <el-button type="primary" @click="updatePass()">确 定</el-button>
      </div>
    </el-dialog>
    <audio ref="audio" v-if="currMediaName" :src="currShowMusic" autoplay="autoplay" controls="controls" loop="loop" style="width: 100%; height: 100%; object-fit: fill;"></audio>
  </div>

</template>

<script>
import { mapGetters } from "vuex";
import Levelbar from "./Levelbar";
import TabsView from "./TabsView";
import errLogStore from "store/errLog";
import { getToken } from "utils/auth";
import { updateUserPwd } from "api/admin/user/index";
import { getEnterpriseCount, getSuperviseCount } from "api/admin/personal/index";
import axios from "axios";
import { Message, MessageBox } from "element-ui";
import { parseTime } from "utils/index";
export default {
  components: {
    Levelbar,
    TabsView
  },
  data() {
    let confirmPass = (rule, p2, callback) => {
      if (this.form.userPwd != p2) {
        callback(new Error("确认密码不一致"));
      } else callback();
    };
    let contrastPass = (rule, newPassword, callback) => {
      if (this.form.userPwd_old == newPassword) {
        callback(new Error("旧密码与输入的新密码一致"));
      } else callback();
    };
    return {
      isshowtype_four:false, //是否是企业版
      role: {
        isEnterpriseRole: false,
        isSuperviseRole: false,
        isOthersRole: false
      },
      dataLoading: false,
      expand: false,
      log: errLogStore.state.errLog,
      dialogVisible: false,
      count_register: 0,
      count_verify: 0,
      count_cert: 0,
      count_onsite: 0,
      count_alarm: 0,
      form: { userPwd: "", userPwd_old: "", userPwd2: "" },
      rules: {
        userPwd_old: [
          { required: true, message: "请输入旧密码", trigger: "blur" }
        ],
        userPwd: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          {
            validator: contrastPass,
            message: "旧密码与输入的新密码一致",
            trigger: "blur"
          }
        ],
        userPwd2: [
          { required: true, message: "请确认密码", trigger: "blur" },
          { validator: confirmPass, message: "确认密码不一致", trigger: "blur" }
        ]
      },
      lookAlarm_time: undefined,
      currMediaName: false,
      currShowMusic:process.env.SERVERIMAGEURL + "/alerm.wav",
      wait_lookAlarm_time:undefined //
    };
  },
  created() {
    // 处理角色权限
    if (this.user.userType == "3") {
      this.role.isSuperviseRole = true;
    } else if (this.user.userType == "4") {
      this.role.isEnterpriseRole = true;
      this.isshowtype_four=true;
    } else {
      this.role.isOthersRole = true;
    }
    this.initCount();
    let timer = setInterval(() => {
      this.getCount();
    }, 1000 * 30);
  },
  computed: {
    ...mapGetters(["sidebar", "name", "avatar", "user"]),
    res_count_cert(){
      if(this.user.userType == "4"){
          return this.count_cert 
      }else{
        return 0
      }
    }

  },
  methods: {
    getCount() {
      
      if (this.role.isEnterpriseRole) {

        getEnterpriseCount({ alermStartTime: this.lookAlarm_time }).then(response => {
          this.count_cert = response.count_cert;
          this.count_onsite = response.count_onsite;
          this.count_alarm = response.count_alarm;
          this.wait_lookAlarm_time = response.count_alarm_Time;
          if (this.count_alarm > 0) {
            this.currMediaName = true;
          } else {
            this.currMediaName = false;
          }
        });
      } else if (this.role.isSuperviseRole) {
        getSuperviseCount({ alermStartTime: this.lookAlarm_time }).then(response => {
          this.count_register = response.count_register;
          this.count_verify = response.count_verify;
          this.count_cert = response.count_cert;
          this.count_onsite = response.count_onsite;
          this.count_alarm = response.count_alarm;
          this.wait_lookAlarm_time = response.count_alarm_Time;
          if (this.count_alarm > 0) {
            this.currMediaName = true;
          } else {
            this.currMediaName = false;
          }
        });
      }
    },

    initCount(countAlarmStartTime) {
      if(countAlarmStartTime){
        this.lookAlarm_time = parseTime(countAlarmStartTime);
      }else{
        this.lookAlarm_time = parseTime(new Date());
      }
      this.getCount();
    },
    // handleExpand() {
    //   if (this.expand) this.expand = false;
    //   else this.expand = true;
    // },
    handleCommand(command) {
      switch (command) {
        case "logout":
          this.logout();
          break;
        case "updatepass":
          this.dialogVisible = true;
          break;
        // case "portal":
        //   this.portal();
        //   break;
      }
    },
    personalHandleCommand(command) {
      //console.log(command);
      switch (command) {
        case "register":
          this.openTabPage("/personal/register");
          break;
        case "modify":
          this.openTabPage("/personal/modify");
          break;
        case "cert":
          this.openTabPage("/personal/cert");
          break;
        case "safety":
          this.openTabPage("/personal/safety");
          break;
      }
    },
    openTabPage(path) {
      //console.log(this.user);
      this.$store.dispatch("addVisitedViews", this.getRouterByPath(path));
      this.$router.push({ path: path });
    },
    getRouterByPath(path) {
      let r;
      this.$router.options.routes.forEach(function(item) {
        if (item.path == path) {
          r = item;
        }
      });
      return r;
    },
    logout() {
      this.$store.dispatch("LogOut").then(() => {
        location.reload(); // 为了重新实例化vue-router对象 避免bug
      });
    },
    // portal() {
    //   axios
    //     .get(
    //       "/Portal/publish/Untitled2.html"
    //     )
    //     .then(function(response) {
    //       console.log(response);
    //     })
    //     .catch(function(error) {
    //       //console.log(error);
    //     });
    // },
    closeDialog() {
      this.form = { userPwd: "", userPwd_old: "", userPwd2: "" };
      this.$refs["form"].resetFields();
    },
    handleClick() {
      this.currMediaName = false;
      // let href=`#/admin/alarm/index?queryTime=${this.lookAlarm_time}`;
      let path='/alarm/now_index';
      this.$store.dispatch("addVisitedViews", this.getRouterByPath(path));
      this.$router.push({ path: path,query:{queryTime:this.lookAlarm_time} });
      this.initCount(this.wait_lookAlarm_time);
    },
    updatePass() {
      let _this = this;
      this.$refs["form"].validate(valid => {
        if (valid) {
          axios
            .put(
              process.env.BASE_API + "/api/admin/user/updateUserPwd",
              {
                userId: this.user.userId,
                userPwd_old: this.form.userPwd_old,
                userPwd: this.form.userPwd
              },
              { headers: { "access-token": getToken() } },
              {
                // 单独配置
                withCredentials: true
              }
            )
            .then(function(response) {
              //console.log(response);
              if (
                response.status === 401 ||
                response.data.status === 40101 ||
                response.data.status === 40301
              ) {
                MessageBox.confirm(
                  "你已被登出，可以取消继续留在该页面，或者重新登录",
                  "确定登出",
                  {
                    confirmButtonText: "重新登录",
                    cancelButtonText: "取消",
                    type: "warning"
                  }
                ).then(() => {
                  _this.logout();
                });
              }
              if (response.status !== 200) {
                //console.log(response.data);
                Message({
                  message: response.data.message,
                  type: "error",
                  duration: 5 * 1000
                });
              } else if (response.data.status && response.data.status !== 200) {
                Message({
                  message: response.data.message,
                  type: "error",
                  duration: 5 * 1000
                });
              } else {
                if (response.data.rel) {
                  _this.$alert("密码修改成功，请重新登录", "提示", {
                    confirmButtonText: "确定",
                    callback: action => {
                      _this.logout();
                    }
                  });
                  _this.dialogVisible = false;
                } else {
                  _this.$message({
                    type: "info",
                    showClose: true,
                    message: response.data.message
                  });
                }
              }
            })
            .catch(function(error) {
              //console.log(error);
            });
        } else {
          return false;
        }
      });

      /*
      this.$refs['form'].validate(valid => {
        if(valid){
          this.updateUserPwd({userId:this.user.userId,userPwd_old:this.form.userPwd_old,userPwd:this.form.userPwd}).then(response=>{
            if(response.rel){
              this.$alert('密码修改成功，请重新登录', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.logout();
                }
              });
              this.dialogVisible=false;
            }else{
              this.$message({
                type:'error',
                showClose: true,
                message: response.message
              });
            }
          });
        }else{
          return false;
        }
      });*/
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.message {
  line-height: 30px;
}
.title {
  position: absolute;
  left: 25px;
  line-height: 60px;
  color: #fff;
  font-size: 25px;
}

.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  height: 60px;
  line-height: 60px;
  background-color: #324157;
  box-shadow: 0 0 5px #222;
  border-radius: 0 !important;
  z-index: 2;
  .avatar-container {
    height: 40px;
    display: inline-block;
    float: right;
    margin-right: 10px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 2px;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 100px;
      }
      .user-title {
        color: #fff;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        color: #fff;
        font-size: 15px;
      }
    }
  }
}
.alerm el-badge__content.is-fixed {
  position: absolute;
  top: 0;
  right: 10px;
  -webkit-transform: translateY(-50%) translateX(100%);
  transform: translateY(-50%) translateX(100%);
  margin-top: 10px;
}
</style>
