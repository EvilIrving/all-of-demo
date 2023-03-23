<template>
  <div class="approval_suggestion">
    <div class="input_box">
      <van-field
        v-model="form.message"
        rows="5"
        autosize
        type="textarea"
        maxlength="50"
        placeholder="请输入审批意见"
        maxHeight="215"
      >
      </van-field>
    </div>
    <div class="btn_group">
      <van-button @click="goBack">返回</van-button>
      <van-button
        style="background-color: #036eff; color: #ffffff; margin-top: 23px"
        @click="submit(2)"
        >通过</van-button
      >
      <van-button
        style="background-color: #ff0000; color: #ffffff; margin-top: 23px"
        @click="submit(3)"
        >驳回</van-button
      >
    </div>
  </div>
</template>

<script>
import { propertyMt } from "@/api/appView";
import { contractApi } from "@/api/rainfall.js";
export default {
  name: "BusinessApprovalSuggestion",
  props: {},
  components: {},
  data() {
    return {
      form: {
        message: "",
        taskId: "",
        sort: "",
      },
      id: "",
      detail: {},
      type: "",
      submitType: true,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.form.sort = this.$route.query.sort;
    this.form.taskId = this.$route.query.taskId;
    this.type = this.$route.query.type;
    if (this.type && this.type == "Acceptance") {
      this.id = this.$route.query.id;
      this.getList();
    }
  },
  methods: {
    async getList() {
      let res = await contractApi.list({
        id: this.id,
      });
      if (res.code == 0) {
        this.detail = res.data.list[0];
        console.log(this.detail);
      }
    },
    goBack() {
      this.$router.go(-1);
    },
    submit(type) {
      if (this.type && this.type == "Acceptance") {
        this.submitAcceptance(type);
      } else {
        this.form["status"] = type;
        propertyMt.submitProgressInfo(this.form).then((res) => {
          if (res.code === 0) {
            if (type == 2) {
              this.$message.success("审核通过成功~");
            } else {
              this.$message.success("审核驳回成功~");
            }
          }
        });
      }
    },
    submitAcceptance(num) {
      if (!this.submitType) {
        return false;
      }
      this.submitType = false;
      let formData = {};
      formData["applicationid"] = this.detail.id;
      formData["nodeId"] = this.detail.currentnode;
      formData["comments"] = this.form.message;
      if (num == 2) {
        contractApi
          .passApplicationNode(formData)
          .then((res) => {
            if (res.code == 0) {
              this.$toast.success("通过成功~");
              this.goBack()
            } else {
              this.$toast.error(res.message);
            }
            this.submitType = true;
          })
          .catch((err) => {
            this.$toast.error(err);
            this.submitType = true;
          });
      } else {
        contractApi
          .rejectApplicationNode(formData)
          .then((res) => {
            if (res.code == 0) {
              this.$toast.success("驳回成功~");
              this.goBack()
            } else {
              this.$toast.error(res.message);
            }
            this.submitType = true;
          })
          .catch((err) => {
            this.$toast.error(err);
            this.submitType = true;
          });
      }
    },
  },
};
</script>

<style scoped lang="scss">
.approval_suggestion {
  background-color: #ffffff;
  .input_box {
    height: 215px;
    background-color: #ffffff;
    border-bottom: 1px solid #e7e7ec;
  }
  .btn_group {
    margin-left: 13px;
    margin-top: 62px;
    .van-button {
      width: 350px;
      height: 45px;
      border-radius: 5px;
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 250;
      line-height: 25px;
      letter-spacing: 2px;
      color: #333333;
      background-color: #f2f6f9;
    }
  }
}
</style>