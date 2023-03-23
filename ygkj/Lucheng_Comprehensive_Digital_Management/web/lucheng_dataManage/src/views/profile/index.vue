<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div slot="header">
            <span>个人信息</span>
          </div>
          <div class="info-detail">
            <div>
              <span>用户名称</span>
              <span>{{ userInfo.username }}</span>
            </div>
            <div>
              <span>手机号码</span>
              <span>{{ userInfo.phoneNumber }}</span>
            </div>
            <div>
              <span>用户邮箱</span>
              <span>{{ userInfo.email }}</span>
            </div>
            <div>
              <span>所属部门</span>
              <span>{{ userInfo.deptName }}</span>
            </div>
            <div>
              <span>所属角色</span>
              <span>{{ userInfo.roleName }}</span>
            </div>
            <div>
              <span>创建日期</span>
              <span>{{ userInfo.createDate }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <div slot="header">
            <span>基本信息</span>
          </div>
          <div>
            <el-tabs v-model="activeName">
              <el-tab-pane label="基本信息" name="first">
                <el-form
                  ref="form"
                  :model="form"
                  :rules="rules"
                  label-width="80px"
                >
                  <el-form-item label="昵称" prop="nickName">
                    <el-input
                      v-model="form.nickName"
                      placeholder="请输入昵称"
                    />
                  </el-form-item>
                  <el-form-item label="手机号" prop="phoneNumber">
                    <el-input
                      v-model="form.phoneNumber"
                      placeholder="请输入手机号"
                    />
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入邮箱" />
                  </el-form-item>
                  <el-form-item label="性别" prop="sex">
                    <el-select
                      v-model="form.sex"
                      placeholder="请选择性别"
                      style="width: 100%"
                    >
                      <template v-for="item in sexOptionList">
                        <el-option
                          :value="item.dictValue"
                          :label="item.dictLabel"
                        />
                      </template>
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="saveInfo">保存</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="second">
                <el-form
                  ref="formPass"
                  :model="formPass"
                  :rules="rulesPass"
                  label-width="80px"
                >
                  <el-form-item label="旧密码" prop="currentPass">
                    <el-input
                      v-model="formPass.currentPass"
                      placeholder="请输入旧密码"
                      show-password
                      @input="change($event)"
                    />
                  </el-form-item>
                  <el-form-item label="新密码" prop="newPass">
                    <el-input
                      v-model="formPass.newPass"
                      placeholder="请输入新密码"
                      show-password
                      @input="change($event)"
                    />
                  </el-form-item>
                  <el-form-item label="确认密码" prop="confPass">
                    <el-input
                      v-model="formPass.confPass"
                      placeholder="请输入确认密码"
                      show-password
                      @input="change($event)"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="savePass">保存</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { updateInfo, updatePass } from "_a/admin/user/user";
import { MessageError, MessageSuccess } from "_l/message";

export default {
  name: "Profile",
  data() {
    const validatePassCheck = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入确认密码"));
      } else if (value !== this.formPass.newPass) {
        callback(new Error("两次密码不一致，请重新输入"));
      } else {
        callback();
      }
    };
    return {
      activeName: "first",
      form: {},
      formPass: {},
      rules: {
        nickName: [this.$ruler("昵称")],
        sex: [this.$ruler("性别", "change")],
        phoneNumber: [this.$ruler("手机号")],
      },
      rulesPass: {
        currentPass: [
          this.$ruler("旧密码"),
          {
            type: "string",
            max: 16,
            min: 6,
            message: "请输入6-16个字符",
            trigger: "blur",
          },
        ],
        newPass: [
          this.$ruler("新密码"),
          {
            type: "string",
            max: 16,
            min: 6,
            message: "请输入6-16个字符",
            trigger: "blur",
          },
        ],
        confPass: [
          this.$ruler("确认密码"),
          { validator: validatePassCheck, trigger: "blur" },
        ],
      },
      sexOptionList: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    ...mapActions(["getDictListByType"]),
    change(e) {
      this.$forceUpdate();
    },
    saveInfo() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateInfo(this.form).then((res) => {
            if (res.code) {
              MessageSuccess("upd");
            } else {
              MessageError("upd");
            }
          });
        }
      });
    },
    savePass() {
      this.$refs["formPass"].validate((valid) => {
        if (valid) {
          updatePass(this.formPass).then((res) => {
            if (res.code) {
              MessageSuccess("upd");
            } else {
              MessageError("upd");
            }
          });
        }
      });
    },
  },
  mounted() {
    this.getDictListByType("sex").then((res) => {
      this.sexOptionList = res;
    });
    this.form = { ...this.userInfo };
    this.formPass.userId = this.userInfo.id;
  },
};
</script>
<style lang="scss" scoped>
.info-detail {
  div {
    border-bottom: 1px solid #ebeef5;
    height: 30px;
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
  }
}
</style>
