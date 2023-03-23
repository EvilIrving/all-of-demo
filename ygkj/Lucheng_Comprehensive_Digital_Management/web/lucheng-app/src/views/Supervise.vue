<template>
  <div class="mobile">
    <img src="../assets/banner.png" alt="" />
    <div class="banner_wraper">
      <h3>问题详情</h3>
      <section class="content_box">
        <div class="content">
          <p class="bigtitle">{{ tableData.projectName }}</p>
          <van-row>
            <van-col span="24">
              <span>督办人：</span>
              <span>{{
                tableData.createName ? tableData.createName : "无"
              }}</span>
            </van-col>
          </van-row>
          <van-row>
            <van-col span="24">
              <span>督办时间：</span>
              <span>{{
                tableData.createTime ? tableData.createTime : "无"
              }}</span>
            </van-col>
          </van-row>
          <van-row>
            <van-col span="24">
              <span>截止时间：</span>
              <span>{{ tableData.deadline ? tableData.deadline : "无" }}</span>
            </van-col>
          </van-row>
          <div
            class="lookfile"
            v-for="(item, index) in tableData.problemFileList"
            :key="index"
          >
            <div class="left">
              <img src="../assets/file.png" alt="" />
              <span>{{ item.fileName }}</span>
            </div>
            <div class="right">
              <span @click="preview(item.fileUrl)">预览文件</span>
            </div>
          </div>
          <p class="title">上传问题附件：</p>
          <div class="uoloadfile">
            <div class="left">
              <span>请上传文件</span>
            </div>
            <div class="right">
              <span></span>
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="$config.uploadUrl"
                :before-upload="handleBeforeUpload"
                :on-success="workfileListSuccess"
                :accept="accept"
                :multiple="true"
                name="uploadFile"
                :file-list="tableData.problemFileList"
              >
                <div class="upload-btn">
                  <img class="uploadimg" src="../assets/right.png" alt="" />
                </div>
              </el-upload>
            </div>
          </div>
          <p class="title">问题描述：</p>
          <van-field
            class="mutiiput"
            type="textarea"
            v-model="tableData.problemDescribe"
          />
        </div>
      </section>
    </div>
    <div class="banner_wraper">
      <h3>整改上报</h3>
      <section class="content_box">
        <section class="content">
          <van-row>
            <van-col span="24">
              <span>整改人：</span>
              <span>{{
                tableData.workerName ? tableData.workerName : "无"
              }}</span>
            </van-col>
          </van-row>
          <van-row>
            <van-col span="24">
              <span>整改时间：</span>
              <el-date-picker
                size="mini"
                type="datetime"
                placeholder="请选择"
                v-model="workTime"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              ></el-date-picker>
              <!-- <span>{{ tableData.workTime ? tableData.workTime : "无" }}</span> -->
            </van-col>
          </van-row>
          <p class="title">整改说明：</p>
          <van-field
            class="mutiiput"
            type="textarea"
            v-model="tableData.workDescribe"
          />
          <div
            class="lookfile"
            v-for="(item, index) in tableData.workFileList"
            :key="index"
          >
            <div class="left">
              <img src="../assets/file.png" alt="" />
              <span>{{ item.fileName }}</span>
            </div>
            <div class="right">
              <span @click="preview(item.fileUrl)">预览文件</span>
            </div>
          </div>
          <p class="title">上传整改附件：</p>
          <div class="uoloadfile">
            <div class="left">
              <span>请上传文件</span>
            </div>
            <div class="right">
              <span></span>
              <el-upload
                :show-file-list="false"
                class="upload-demo"
                :action="$config.uploadUrl"
                :on-preview="preview"
                :before-upload="handleBeforeUpload"
                :on-success="problemfileListSuccess"
                :accept="accept"
                :multiple="true"
                name="uploadFile"
                :file-list="tableData.workFileList"
              >
                <div class="upload-btn">
                  <img class="uploadimg" src="../assets/right.png" alt="" />
                </div>
              </el-upload>
            </div>
          </div>
          <!-- 手机号码，验证码 -->
          <div class="user_verification">
            <el-input
              size="mini"
              v-model="reportPhone"
              class="input-with-select"
            >
              <span slot="prepend">手机</span>
              <el-button
                @click="sendVeri"
                slot="append"
                icon="el-icon-s-promotion"
                >发送</el-button
              >
            </el-input>
            <el-input
              size="mini"
              v-model="verification"
              placeholder="请填写验证码"
              class="input-with-select"
            >
              <span slot="prepend">验证码</span>
            </el-input>
          </div>
        </section>
      </section>
    </div>
    <div class="submit">
      <van-button
        :disabled="status == 1"
        round
        size="small"
        type="primary"
        block
        @click="save"
        >确认上报</van-button
      >
    </div>
  </div>
</template>

<script>
import { getQueryString } from "@/utils/utils";
import { localData, sessionData } from "@/plugins/storage";
import { supervisionCenter } from "@/api/projectDetail";
import { login, loginTicketApi, publicKey } from "@/api/login";
import { Base64 } from "js-base64";
import { JSEncrypt } from "jsencrypt";
import { mapActions } from "vuex";
export default {
  name: "Intor",
  props: {},
  components: {},
  data() {
    return {
      id: "",
      reportPhone: "",
      status: 0,
      workTime: "",
      tableData: {
        reportPhone: "",
        id: "",
        workTime: "",
        problemDescribe: "",
        workDescribe: "",
        workFile: "",
        problemFile: "",
        problemFileList: [],
        workFileList: [],
      },
      accept: ".jpg,.jpeg,.png,.gif,.bmp,.pdf,.JPG,.JPEG,.PBG,.GIF,.BMP,.PDF",
      fileList: [],
      verification: "",
    };
  },
  async created() {
    this.addRoutesType("normal");
    this.tableData.id = this.id = getQueryString("id");
    this.tableData.reportPhone = this.reportPhone = getQueryString("phone");
    const ticket = getQueryString("ticket");
    if (ticket) {
      await this.loginTicket(ticket);
    } else {
      await this.getPublicKey();
    }
    await this.getList();
    // this.$router.push("/");
  },
  methods: {
    ...mapActions(["addRoutesType"]),
    //保存登录信息，跳转到首页
    async setTokenInfo(res) {
      let opt = res.data;
      opt.timestamp = new Date().getTime();
      await localData("set", "userInfo", opt);
      await sessionData("set", "userInfo", opt);
    },
    //获取加密key
    async getPublicKey() {
      let res = await publicKey();
      if (res.success) {
        await this.doLogin(res.key);
      }
    },
    //默认账号密码登录
    async doLogin(key) {
      let password = "ygkj0818";
      if (key) {
        let encryptor = new JSEncrypt();
        encryptor.setPublicKey(key);
        password = encryptor.encrypt(password);
      }
      let opt = {
        username: Base64.encode("admin"),
        password: password,
        isEncryption: true,
      };
      let res = await login(opt);
      if (res.code == 0) {
        await this.setTokenInfo(res);
      } else {
        this.$router.replace("/noPermissions");
        this.$toast.fail(res.message);
      }
    },
    //单ticket票据登陆
    async loginTicket(ticket) {
      let res = await loginTicketApi({
        ticket,
        type: routesType ? 2 : 3,
      });
      if (res.code == 0) {
        this.setTokenInfo(res);
      } else {
        this.$router.replace("/noPermissions");
        this.$toast.fail(res.message);
      }
    },
    async getList() {
      let res = await supervisionCenter.list({ id: this.id });
      if (res.code == 0) {
        this.status = res.data.list[0].status;
        if (res.data.list[0].insideList.length !== 0) {
          let data = res.data.list[0].insideList.find(
            (i) => i.workerPhone == this.reportPhone
          );
          res.data.list[0].workerName = data.workerName
            ? data.workerName
            : data.workerPhone;
        }
        if (res.data.list[0].outsideList.length !== 0) {
          let data = res.data.list[0].outsideList.find(
            (i) => i.workerPhone == this.reportPhone
          );
          res.data.list[0].workerName = data.workerName
            ? data.workerName
            : data.workerPhone;
        }

        this.tableData = res.data.list[0];
      }
    },
    async sendVeri() {
      let formData = {
        phone: this.reportPhone,
        id: this.tableData.id,
      };
      let res = await supervisionCenter.sendVeri(formData);
      if (res.code == 0) {
        this.$toast(" 请填写 " + res.data);
        this.verification = res.data;
      } else {
        this.$toast("发送失败，请重新发送。");
      }
    },
    async save() {
      this.tableData.reportPhone = getQueryString("phone");
      this.tableData.workTime = this.workTime;
      this.tableData.verificationCode = this.verification;
      const {
        verificationCode,
        reportPhone,
        id,
        workTime,
        problemDescribe,
        workDescribe,
        workFile,
        problemFile,
      } = this.tableData;
      let formData = {
        verificationCode,
        reportPhone,
        id,
        workTime,
        problemDescribe,
        workDescribe,
        workFile,
        problemFile,
      };
      if (formData.workDescribe) {
        let res = await supervisionCenter.update(formData);
        if (res.code == 0) {
          this.$toast("处置成功");
          this.getList();
        } else {
          this.$toast("处置失败！");
        }
      } else {
        this.$toast("请完整填写信息");
      }
    },
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$message.warning("暂无附件");
      }
    },
    //上传之前的验证
    handleBeforeUpload(file) {
      let nameArr = file.name.split(".");
      let type = nameArr[nameArr.length - 1];
      if (this.accept.indexOf(type) == -1) {
        this.$message.error("文件格式不正确，请重新上传~");
        return false;
      }
      let sizeNum = "";
      if (this.sizeType == "MB" || this.sizeType == "mb") {
        sizeNum = file.size / 1024 / 1024;
      } else if (this.sizeType == "KB" || this.sizeType == "kb") {
        sizeNum = file.size / 1024;
      }
      if (sizeNum > this.size) {
        this.$message.error("文件过大，请重新上传~");
        return false;
      }
      return true;
    },
    //上传成功
    workfileListSuccess(res) {
      this.$toast("上传成功");
      if (this.tableData.problemFile !== "") {
        this.tableData.problemFile += `,${res.data.id}`;
      } else {
        this.tableData.problemFile = res.data.id;
      }
    },
    problemfileListSuccess(res) {
      this.$toast("上传成功");
      if (this.tableData.workFile !== "") {
        this.tableData.workFile += `,${res.data.id}`;
      } else {
        this.tableData.workFile = res.data.id;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.loading_wrap {
  text-align: center;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding-top: 200px;
}
.mobile {
  img {
    width: 100%;
    height: 100%;
  }
  span {
    letter-spacing: 2px;
    font-size: 16px;
  }
  .title {
    letter-spacing: 2px;
    padding: 8px 0px;
    font-size: 16px;
  }
  .bigtitle {
    letter-spacing: 2px;
    padding: 8px 0px;
    font-size: 18px;
  }
}
.banner_wraper {
  h3 {
    margin-left: 3%;
  }
  .content_box {
    width: 94%;
    margin: 10px auto;
    background: #fff;
    border: 1px solid #fff;
    border-radius: 10px;
    .content {
      padding: 10px;
      .van-row {
        padding: 5px 0px;
      }
      .uoloadfile {
        width: 100%;
        padding: 5px 0px;
        @include flexbox;
        @include flexJC(space-between);
        @include flexAI(center);
        .left {
          flex: 1;
          @include flexbox;
          @include flexJC(flex-start);
          @include flexAI(center);
        }
        .right {
          flex: 1;
          @include flexbox;
          @include flexJC(flex-end);
          @include flexAI(center);
          span {
            color: #409eff;
          }
          .uploadimg {
            margin-left: 5px;
            width: 40%;
            height: 80%;
            line-height: 80%;
          }
        }
      }
      .lookfile {
        padding: 10px 0px;
        @include flexbox;
        @include flexJC(space-between);
        @include flexAI(center);
        background: rgba(214, 217, 223, 0.5);
        border-radius: 5px;
        .left {
          @include flexbox;
          @include flexJC(space-between);
          @include flexAI(center);
          img {
            width: 10%;
          }
        }
        .right {
          @include flexbox;
          @include flexJC(space-between);
          @include flexAI(center);
          span {
            color: #409eff;
          }
        }
      }
      .van-field.mutiiput {
        margin: 2% auto;
        border-radius: 10px;
        background: rgb(249, 249, 249);
      }
    }
  }
}
.submit {
  width: 90%;
  margin: 10px auto;
  padding-bottom: 10px;
  .van-button {
    background: #409eff;
  }
}
.van-field__label {
  width: auto;
}
.van-button--primary {
  color: #fff;
  background-color: #409eff;
  border: 1px solid #409eff;
}
.user_verification {
  margin-top: 10px;
  .el-input {
    margin-bottom: 10px;
  }
}
</style>
