<!--
 * @Date: 2022-06-08 19:36:41
 * @Author: 
 * @Description: 评价页面
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-22 16:27:21
 * @FilePath: \lucheng-app\src\views\appView\propertysupervision\archive\EvaluateArchive.vue
-->
<template>
  <div class="user">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="user_item">
      <div class="person_left">
        <img src="../../../../assets/images/person-head.png" alt="" />
        <div>
          <h3>{{ form.responsiblePeopleName }}</h3>
          <p>
            <span>处理人</span>
          </p>
        </div>
      </div>
    </div>
    <div class="rate">
      <div>
        <span>督办效率</span>
        <van-rate
          v-model="sefficiency"
          :readonly="
            !deptIdType || form.evaluationStatus == 2
          "
          allow-half
          :size="25"
          color="#ffd21e"
          void-icon="star"
          void-color="#eee"
        />
      </div>
      <div>
        <span>督办质量</span>
        <van-rate
          :readonly="
            !deptIdType || form.evaluationStatus == 2
          "
          v-model="qulitity"
          allow-half
          :size="25"
          color="#ffd21e"
          void-icon="star"
          void-color="#eee"
        />
      </div>
      <div>
        <span>服务态度</span>
        <van-rate
          v-model="serviceAttitude"
          :readonly="
            !deptIdType || form.evaluationStatus == 2
          "
          allow-half
          :size="25"
          color="#ffd21e"
          void-icon="star"
          void-color="#eee"
        />
      </div>
    </div>
    <div>
      <van-field
        v-model="message"
        rows="3"
        autosize
        type="textarea"
        maxlength="50"
        placeholder="说说处理人的亮点和不足吧"
        show-word-limit
        :readonly="!deptIdType || form.evaluationStatus == 2"
      />
    </div>
    <van-button
      type="primary"
      :loading="loading"
      block
      @click="evaluating(form.id)"
      v-if="form.evaluationStatus != 2"
      >提交评价</van-button
    >
  </div>
</template>

<script>
import { superviseApi } from "@/api/rainfall.js";
import { supervisedCheck } from "@/api/projectDetail";
import { localData } from "@/plugins/storage";

export default {
  name: "EvaluateArchive",
  props: {},
  components: {},
  data() {
    return {
      message: "",
      qulitity: 2,
      sefficiency: 0,
      serviceAttitude: 0,
      form: {},
      loading: false,
      deptIdType: false
    };
  },
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  watch: {},
  created() {},
  mounted() {
    this.addZwlog(this)
    this.getList();
    this.getWaterDept()
  },
  methods: {
    async getWaterDept() {
      let res = await superviseApi.getWaterDept({ deptId: this.userInfo.deptId });
      if (res.code == 0) {
        this.deptIdType = res.data == 'true';
      }
    },
    async getList() {
      let res = await supervisedCheck.list({ id: this.$route.query.id });
      if (res.code == 0) {
        this.form = res.data.list[0];
      }
    },
    evaluating() {
      this.loading = true;
      let ruleForm = {
        dbId: this.form.id,
        pj: this.message,
        fwtd: this.serviceAttitude,
        dbzl: this.qulitity,
        dbxl: this.sefficiency,
        userId: this.userInfo.id,
      };
      superviseApi
        .savePjById(ruleForm)
        .then((res) => {
          if (res.code == 0) {
            let _this = this;
            this.$toast.success({
              message: "提交成功~",
              duration: 1000,
              onClose: () => {
                _this.loading = false;
                _this.$router.go(-1);
              },
            });
          } else {
            this.loading = false;
            this.$toast.fail(res.message);
          }
        })
        .catch((err) => {
          this.loading = false;
          this.$toast.fail(err);
        });
    },
  },
};
</script>

<style scoped lang="scss">
.user {
  padding: 12px 20px;
  background-color: #fff;
}
.user_item {
  background-color: #fff;
  @include flexbox();
  @include flexAI(center);
  @include flexJC(space-between);
  padding-top: 16px;
  .person_left {
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);
    img {
      margin-right: 10px;
    }
    & > div {
      h3 {
        font-size: 14px;
        color: rgba(31, 31, 31, 0.48);
        font-weight: 400;
        margin-bottom: 8px;
      }
      span {
        font-size: 14px;
        font-weight: 400;
      }
    }
  }
}
.rate {
  padding: 16px 0;
  border-bottom: 1px solid #e5e5e5;
  display: flex;
  flex-direction: column;
  align-items: center;
  > div {
    width: 100%;
  }
  span {
    font-size: 14px;
    padding: 0 10px;
  }
}
.van-button--block {
  width: 94%;
  background: #036eff;
  border-radius: 4px;
  margin: auto;
}
.van-button--primary {
  width: 94%;
  border: #036eff solid 1px;
}
.van-button::before {
  background-color: #fff;
}
</style>
