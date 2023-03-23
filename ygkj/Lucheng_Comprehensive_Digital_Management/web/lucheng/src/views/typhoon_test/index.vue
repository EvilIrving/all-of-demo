<template>
  <!-- 容器 -->
  <div
    class="disaster-prevention"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    ref="bigScreen"
  >
    <!-- 图层控件 -->
    <screen-map ref="map" />
  </div>
</template>

<script>
// 图层控件
import ScreenMap from "./components/ScreenMap";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

export default {
  components: {
    ScreenMap,
  },
  data() {
    return {
      historyTyphoonData:""
    };
  },
  mounted() {
    this.typhoons({});
  },
  beforeDestroy() {
    
  },
  computed: {
   
  },
  methods: {
    //历史台风接口
    async typhoons(val) {
      await disasterPreventionApi.typhoons(val).then((res) => {
        if (res.code == 0) {
          // this.historyTyphoonData = res.data;
          this.historyTyphoonData = res.data.slice(0, 5);
          this.getTyphoonDetail(this.historyTyphoonData[0].id)
        }
      });
    },
    //台风详情接口Z
    getTyphoonDetail(id) {
      disasterPreventionApi.getTyphoonDetail(id).then((res) => {
        if (res.code == "0") {
          let points = eval(res.data.points);
          const data = {
            ...res.data,
            points
          };
          this.$refs.map.showTestTyphoon(data);
        }
      });
    },
    test(){
      console.log(123);
    }
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
@import "@/style/sidebarStyle.scss";

.disaster-prevention {
  width: 100%;
  height: 100%;
  background: url(~assets/images/da-ping-bg.png) no-repeat top center;
  background-size: 100% 100%;
  .test-drag-container{
    width: 300px;
    height: 300px;
    z-index: 100000;
    border: 1px solid #fff;
    top: 200px;
    .test-drag-div{
      width: 50px;
      height: 50px;
      border: 1px solid #fff;
    }
  }
}
// 去掉table表格纵向滚动条
::v-deep .el-table__body-wrapper::-webkit-scrollbar {
  height: 6px; // 纵向滚动条 必写
  width: 0px;
}
//设置table表格字体样式
::v-deep.sidebar-list .el-table .cell {
  font-size: 12px;
  opacity: 0.8;
}
//设置table表格 列宽，字间距样式
::v-deep .el-table {
  .cell {
    height: $tdHeight;
    line-height: $tdHeight;
  }
  // 设置勾选框样式
  .el-checkbox__inner {
    border: 0.0625rem solid #00c1ff;
    background-color: #00c1ff;
    background: rgba($color: #000000, $alpha: 0);
  }
  // 设置选中行样式颜色
  .el-table__body tr.current-row > td {
    background-color: rgba(41, 147, 180, 0.3);
  }
  th,
  td {
    padding: 0;
    color: #fff;
    font-size: 12px;
  }
  th {
    font-size: 14px;
    color: #00c1ff;
    background-color: rgba(41, 147, 180, 0.1);
  }
}
</style>