<!--
 * @Author: zg
 * @Date: 2022-02-18 10:50:25
 * @LastEditTime: 2022-03-14 17:30:50
 * @LastEditors: zg
 * @Description: 数据集tab
 * @FilePath: \project_manage_web\src\views\manage\inforShare\components\Form.vue
-->
<template>
  <div>
    <SQLForm
      v-if="setType == 1"
      ref="sqlForm"
      :dialogTitle="dialogTitle"
      :dialogShow="dialogShow"
      :setType="setType"
      :setId="setId"
      :readonly="readonly"
      @close="closeDialog"
      @closeDialogAndRefresh="closeDialogRefresh"
    ></SQLForm>

    <HTTPForm
      v-if="setType == 2"
      ref="httpForm"
      :dialogTitle="dialogTitle"
      :dialogShow="dialogShow"
      :setType="setType"
      :setId="setId"
      :readonly="readonly"
      @close="closeDialog"
      @closeDialogAndRefresh="closeDialogRefresh"
    ></HTTPForm>
  </div>
</template>

<script>
import SQLForm from "./sqlForm";
import HTTPForm from "./httpForm";
export default {
  name: "tabForm",
  props: {
    readonly: {
      type: Boolean,
      default: false,
    },
    dialogTitle: {
      type: String,
    },
    dialogShow: {
      type: Boolean,
    },
    setType: {
      type: Number || String,
      default: 1,
    },
    setId: {
      type: String,
    },
  },
  components: { SQLForm, HTTPForm },
  data() {
    return {
      form: {},
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    closeDialog() {
      this.$emit("close");
    },
    closeDialogRefresh() {
      this.$emit("closeDialogAndRefresh");
    },
    reset() {
      if (this.setType == 1) {
        this.$refs.sqlForm.reset();
      } else if (this.setType == 2) {
        this.$refs.httpForm.reset();
      }
    },
    // getDetail(){
    //   this.$nextTick(() => {
    //     this.$refs.sqlForm.getDetail();
    //   });
    // }
  },
};
</script> 

<style lang='scss' scoped>
</style>
