<template>
  <div class="primary-sidebar">
    <div class="sidebar-header">
      <primary-tabs
        v-show="!backText"
        :list="list"
        v-model="tabActive"
        @input="changeView"
        ref="primaryTab"
      ></primary-tabs>
      <span
        v-show="backText"
        class="back-title"
        @click="back"
        :title="backText"
      >
        <i class="icon-back"></i>
        {{ backText }}</span
      >
    </div>
    <div class="sidebar-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { PrimaryTabs } from "@/components";
export default {
  name: "PrimarySidebar",
  props: {
    list: {
      type: Array,
    },
    tabActive: {
      type: [String, Number],
    },
    backText: {
      type: String,
    },
    backRouter: {
      type: String,
    },
    backType: {
      type: String,
    },
  },
  data() {
    return {
      //   list: [
      //     { title: "雨量测站", value: 0 },
      //     { title: "水位测站", value: 1 },
      //     { title: "闸门工情", value: 2 },
      //   ],
      //   tabActive: "",
    };
  },
  components: {
    PrimaryTabs,
  },
  watch: {
    list(val) {
      if (this.list.length == 0) {
        this.$emit("nolist");
      } else {
        if (this.list[this.tabActive]) {
          this.$nextTick(() => {
            this.$router.push(this.list[this.tabActive].url);
          });
        }
      }
    },
    tabActive(val) {
      let index = 0;
      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].value == val) {
          index = i;
        }
      }
      // console.log(this.list, 333);
      this.$nextTick(() => {
        this.$router.push(this.list[index].url);
      });
    },
    backRouter(val, oldVal) {
      if (val) {
        this.$nextTick(() => {
          this.$router.push(val);
        });
      }
    },
  },
  methods: {
    changeView(value) {
      this.list.map((item) => {
        if (item.value == value) {
          this.$router.push(item.url);
        }
      });
      this.$emit("clearLayer");
      this.$emit("showShadeLayer");
      this.$emit("changeLayer", value);
    },
    showMapPoint(dataList, type, flag, clearFlag) {
      // flag 是否不显示弹框 clearFlag是否清除图层 true为不清除
      const opt = {
        layer: type,
        pointList: dataList,
      };
      this.$emit("showMapPoint", opt, flag, clearFlag);
    },
    showMapLine(data, lineType) {
      this.$emit("showMapLine", data, lineType);
    },
    showMapArea(dataList) {
      this.$emit("showMapArea", dataList);
    },
    showRiverLayer() {
      this.$emit("showRiverLayer");
    },
    removeLayers() {
      this.$emit("removeLayers");
    },
    clearOneLayer(layerName) {
      this.$emit("clearOneLayer", layerName);
    },
    showSidebarInfo(curRow, router, goRouter, tabActive) {
      this.$emit("showSidebarInfo", curRow, router, goRouter, tabActive);
    },
    clearPointLayers() {
      this.$emit("clearPointLayers");
    },
    clearLandLayer() {
      this.$emit("clearLandLayer");
    },
    clearLineLayers(layerArr) {
      this.$emit("clearLineLayers", layerArr);
    },
    toggleShadeLayer(type) {
      this.$emit("toggleShadeLayer", type);
    },
    back() {
      this.$emit("back");
    },
    showDialog(data) {
      this.$emit("showDialog", data);
    },
    showVideo(data) {
      this.$emit("showVideo", data);
    },
    clearLineLayer(typeArr) {
      //河湖库保护清岸线方法
      this.$emit("clearLineLayer", typeArr);
    },
    clearRiverLayer() {
      this.$emit("clearRiverLayer");
    },
    goRouter(val) {
      if (val) {
        this.$nextTick(() => {
          this.$router.push(val);
        });
      }
    },
    showTyphoon(data) {
      this.$emit("showTyphoon", data);
      // console.log(data, 666);
    },
    showAreaBorder(areaName) {
      this.$emit("showAreaBorder", areaName);
    },
    _addGeoJson() {
      this.$emit("_addGeoJson");
    },
    goCenter(center, zoom) {
      this.$emit("goCenter", center, zoom);
    },
  },
};
</script>

<style lang="scss" scoped>
.primary-sidebar {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, wrap);
  position: relative;
  background: rgba(38, 118, 113, 0);
  .sidebar-header {
    width: 100%;
    height: 60px;
    border-radius: 20px 0 0 0;
    background: #267671;
    background-image: linear-gradient(
      180deg,
      rgba(78, 255, 244, 0.64) 0%,
      #267671 100%
    );
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.5);
    opacity: 0.8;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .sidebar-content {
    width: 100%;
    background: rgba(38, 118, 113, 0.56);
    padding: 0 18px;
    height: calc(100% - 60px);
    overflow: auto;
    border: 1px solid #3ab378;
  }
  .back-title {
    letter-spacing: 1px;
    font-size: 24px;
    line-height: 60px;
    font-weight: normal;
    color: #fff;
    cursor: pointer;
    padding-left: 20px;
  }
}
</style>