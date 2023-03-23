<template>
  <el-dialog
    title="预警"
    append-to-body
    destroy-on-close
    :close-on-click-modal="false"
    modal-append-to-body
    :visible.sync="dialogVisible"
    width="50%"
    :before-close="handleClose"
  >
    <div class="content-sty">
      <div
        class="listContent"
        v-for="(item, index) in policeDataName"
        :key="index"
      >
        <p>{{ item }}</p>
        <div class="temporary-sty">
          <i class="icon-empty-nothing"></i>
          <p>暂无预警</p>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  props: ["dialogVisible"],
  data() {
    return {
      policeDataName: [
        "水库预警",
        "水闸预警",
        "潮位预警",
        "台风预警",
        "城市积水预警",
        "内河水位预警",
      ],
    };
  },
  methods: {
    handleClose(done) {
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-icon-close {
  color: #fff;
  opacity: 0.8;
}
::v-deep .el-dialog__title {
  color: #fff;
  opacity: 0.8;
}
::v-deep .el-dialog,
.el-pager li {
  height: 750px;
  background: rgba(37, 35, 71, 0);
  background-image: url(~assets/images/ScreenAsset/dialog-bg.png);
  background-repeat: no-repeat;
  background-size: 100%;
}
.content-sty {
  width: 100%;
  height: 630px;
  overflow: auto;
  color: #fff;
  margin-bottom: 30px;
  padding: 20px 50px;
  > div > p {
    width: 100%;
    height: 40px;
    padding: 0 20px;
    font-size: 16px;
    line-height: 40px;
    background: rgba(69, 194, 231, 0.3);
  }
  > div > div {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .listContent {
    margin-bottom: 20px;
    line-height: 30px;
    background: rgba(10, 58, 103, 0.2);
  }
}
.temporary-sty {
  padding-bottom: 20px;
}
</style>