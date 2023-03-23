<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="860px"
    :before-close="close"
    top="10vh"
    :title="title"
    class="list-dialog warehouse-dialog"
  >
    <div class="dialog-content"></div>
  </el-dialog>
</template>

<script>
export default {
  name: "RescueSupport",
  props: {
    dialogVisible: {
      type: Boolean,
      value: false,
    },
    title: {
      type: Boolean,
      value: "抢险支持",
    },
  },
  components: {},
  data() {
    return {};
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss" scoped>
</style>