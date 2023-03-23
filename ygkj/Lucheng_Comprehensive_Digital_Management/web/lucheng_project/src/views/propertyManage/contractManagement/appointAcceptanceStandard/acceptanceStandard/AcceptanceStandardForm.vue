<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-25 16:13:56
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\appointAcceptanceStandard\acceptanceStandard\AcceptanceStandardForm.vue
-->
<template>
  <div>
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="验收事项：">
        <el-input
          v-model="form.items"
          placeholder="请输入"
          :readonly="title == '查看'"
        ></el-input>
      </el-form-item>
      <el-form-item label="验收要求：">
        <el-input
          v-model="form.requirement"
          placeholder="请输入"
          :readonly="title == '查看'"
        ></el-input>
      </el-form-item>
      <el-form-item label="标准分：">
        <el-input
          v-model="form.standardScore"
          placeholder="请输入"
          :readonly="title == '查看'"
        ></el-input>
      </el-form-item>
      <el-form-item label="赋分原则：">
        <el-input
          type="textarea"
          placeholder="请输入"
          rows="4"
          v-model="form.scoreCriteria"
          :readonly="title == '查看'"
        ></el-input>
      </el-form-item>
      <el-form-item v-if="title !== '查看'">
        <div class="btn-box">
          <el-button size="small" @click="closeDialog">取 消</el-button>
          <el-button size="small" type="primary" @click="submitForm('form')"
            >确 定</el-button
          >
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { contractOperate } from "@/api/propertyManage.js";
export default {
  props: {
    id: String,
    title: String,
  },
  data() {
    return {
      form: {
        items: "",
        requirement: "",
        standardScore: "",
        scoreCriteria: "",
      },
      submitType: false,
    };
  },
  mounted() {
    if (this.id) {
      this.loadPageOverhaul();
    }
  },
  methods: {
    submitForm() {
      console.log("submit!");
      if (this.submitType) {
        return false;
      }
      this.submitType = true;
      contractOperate
        .saveOrUpdateCriteria(this.form)
        .then((res) => {
          if (res.code == 0) {
            this.$message.success(`${this.title}成功~`);
            this.$emit("close", 1);
          } else {
            this.$message.error(res.message);
          }
          this.submitType = true;
        })
        .catch((err) => {
          this.submitType = true;
          this.$message.error(err);
        });
    },
    closeDialog() {
      this.$emit("close");
    },
    async loadPageOverhaul() {
      let res = await contractOperate.loadPageCriteria({
        id: this.id,
      });
      if (res.code == 0) {
        let data = res.data.list[0];
        let { items, requirement, standardScore, scoreCriteria, id } = data;
        this.form = {
          items,
          requirement,
          standardScore,
          scoreCriteria,
          id,
        };
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.btn-box {
  text-align: right;
}
</style>