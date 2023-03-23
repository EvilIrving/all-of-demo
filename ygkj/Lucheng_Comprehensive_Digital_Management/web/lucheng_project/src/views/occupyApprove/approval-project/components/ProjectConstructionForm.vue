<template>
  <div class="project-construction">
    <el-radio-group v-model="radioType">
      <el-radio-button label="计划"></el-radio-button>
      <el-radio-button label="资金"></el-radio-button>
      <el-radio-button label="建设"></el-radio-button>
    </el-radio-group>
    <div class="form-box">
      <form-plan
        v-if="radioType == '计划'"
        :id="id"
        :isLook="isLook"
        @close="close"
      ></form-plan>
      <form-finance
        v-if="radioType == '资金'"
        :id="id"
        :isLook="isLook"
        @close="close"
      ></form-finance>
      <form-construction
        v-if="radioType == '建设'"
        :id="id"
        :isLook="isLook"
        @close="close"
      ></form-construction>
    </div>
  </div>
</template>

<script>
import formPlan from "./formPlan.vue";
import formFinance from "./formFinance.vue";
import formConstruction from "./formConstruction.vue";
export default {
  props: {
    id: {
      type: String,
      default: "",
    },
    isLook: {
      type: Boolean,
      default: false,
    },
  },
  components: { formPlan, formFinance, formConstruction },
  data() {
    return {
      radioType: "计划",
    };
  },
  methods:{
    close(){
      this.$emit('close')
    }
  }
};
</script>

<style lang="scss" scoped>
.form-box {
  padding-top: 20px;
}
</style>