<template>
  <div class="reservoir_dialog" v-if="value">
    <div class="reservoir_title">
      <img
        @click="closeDetailDialog"
        src="@/assets/images/back2.png"
        alt=""
      />
      <p>{{ projectDetail.projectTitle }}</p>
    </div>
    <iframe
      class="reservoir_dialog_iframe"
      :src="projectDetail.detailUrl"
      frameborder="0"
    ></iframe>
  </div>
</template>

<script>
export default {
  name: "ProjectDetail",
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    projectDetail: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  methods:{
    closeDetailDialog(){
      this.$emit('input', false)
    }
  }
};
</script>

<style lang="scss" scoped>
.reservoir_dialog {
  background: #fff;
  z-index: 999999;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  // .back{
  //   cursor: pointer;
  //   position: absolute;
  //   top: 4px;
  //   left: 0px;
  //   width: 30px;
  //   height: 30px;
  // }
  .reservoir_title {
    padding: 16px;
    background: #2478f0;
    // padding-left: 16px;
    color: #fff;
    font-size: 20px;
    height: 40px;
    line-height: 40px;
    @include flexbox();
    @include flexAC();
    // @include flexJC(center);
    img {
      margin-right: 10px;
      cursor: pointer;
    }
  }
  .reservoir_dialog_iframe {
    height: calc(100% - 120px);
    width: 100%;
    overflow: hidden;
  }
}
</style>