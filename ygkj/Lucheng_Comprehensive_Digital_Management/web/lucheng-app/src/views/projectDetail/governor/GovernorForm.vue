<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-18 11:24:44
 * @Description: 新增督查
 * @FilePath: /cangnan-app/src/views/projectDetail/governor/GovernorForm.vue
-->
<template>
  <div class="wrap">
    <van-form validate-first @submit="onSubmit">
      <van-field
        v-model="rectificationPeople"
        label="督查人"
        name="pattern"
        placeholder="请输入督查人"
      />
      <van-field
        readonly
        clickable
        :value="rectificationTime"
        label="督查时间"
      />
      <van-field
        v-model="question"
        label="问题描述"
        rows="4"
        autosize
        maxlength="150"
        type="textarea"
        show-word-limit
        placeholder="请输入问题描述"
      />
      <van-field name="uploader" label="文件上传">
        <template #input>
          <van-uploader v-model="fileList" :after-read="afterRead" />
        </template>
      </van-field>
      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit"
          >提交</van-button
        >
      </div>
    </van-form>
  </div>
</template>

<script>
import { busCodeSupervisionApi } from "@/api/projectDetail";
export default {
  name: "GovernorForm",
  props: {},
  components: {},
  data() {
    return {
      showPicker: false,
      rectificationPeople: "",
      rectificationTime: new Date().format("yyyy-MM-dd hh:mm"),
      question: "",
      fileList: [],
    };
  },
  mounted() {},
  methods: {
    afterRead(file) {
      // 此时可以自行将文件上传至服务器
      console.log(file);
    },
    // 提交成功
    onSubmit() {
      this.save();
    },
    async save() {
      let res = await busCodeSupervisionApi.save({
        projectId: this.$localData("get", "projectCode"),
        rectificationPeople: this.rectificationPeople,
        // rectificationTime: this.rectificationTime,
        question: this.question,
        solveUrl: "",
      });
      if (res.code == 0) {
        this.$toast("保存成功");
        // 重新定位到诊-督查
        this.$router.push({
          path: "/projectIndex",
          query: {
            model: "big",
            pagePath: "/projectIndex/diagnosis",
          },
        });
      } else {
        this.$toast("保存失败");
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
}
</style>