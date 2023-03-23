<template>
  <div class="typhoon-effect">
    <div>
      <span>工程类型:</span>
      <el-select
        remote
        v-model="projectType"
        :loading="typeOptionLoading"
        style="width: 8.75rem; margin-bottom: 0.5rem"
        @change="getProjectList"
      >
        <el-option
          v-for="(item, index) in typeOptions"
          :key="index"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <table-title title="关联工程">
      <template slot="append">
        <i
          :class="pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1'"
          @click="onPointActiveClick"
        />
      </template>
    </table-title>

    <div ref="typhoonTable" style="height: 100%">
      <el-table
        v-loading="projectListLoading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :data="projectList"
        :height="projectListHeight"
      >
        <el-table-column type="index" label="序号" />
        <el-table-column label="工程类型" prop="projectType" />
        <el-table-column label="名称" prop="projectName" />
      </el-table>
    </div>
  </div>
</template>
<script>
import { getTyphoonEffectedProject } from "@/api/typhoon";
import { TableTitle } from "../../components.js";
export default {
  name: "TyphoonEffect",
  layerName: "effectLayer",
  props: { pointData: Object },
  components: { TableTitle },
  data() {
    return {
      projectType: "",
      typeOptionLoading: false,
      typeOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "水库",
          value: "水库",
        },
        {
          label: "山塘",
          value: "山塘",
        },
        {
          label: "堤防",
          value: "堤防",
        },
        {
          label: "海塘",
          value: "海塘",
        },
        {
          label: "泵站",
          value: "泵站",
        },
        {
          label: "水闸",
          value: "水闸",
        },
        {
          label: "闸站",
          value: "闸站",
        },
        {
          label: "灌区",
          value: "灌区",
        },
        {
          label: "农饮水",
          value: "农饮水",
        },
        {
          label: "水电站",
          value: "水电站",
        },
      ],

      projectListLoading: false,
      projectList: [],
      projectListHeight: 0,

      pointActive: false,
      typePoints: {},
    };
  },
  watch: {
    pointData: {
      immediate: true,
      handler(val, oldVal) {
        if (val) {
          this.fitTableHeight();
          this.getProjectList();
        }
      },
    },
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.fitTableHeight();
  },
  updated() {
    this.fitTableHeight();
  },
  methods: {
    fitTableHeight() {
      this.$nextTick(() => {
        this.projectListHeight = this.$refs.typhoonTable.offsetHeight - 10;
      });
    },
    onPointActiveClick() {
      this.pointActive = !this.pointActive;
      if (this.pointActive) {
        this.pointDownMap();
      } else {
        for (let type in this.typePoints) {
          this.map.drawClusterPoint([], {}, `effect${type}Layer`);
        }
      }
    },
    pointDownMap() {
      let styleMap = {
        水库: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        山塘: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/torrents.png"),
          srcs: require("@/assets/images/MapPoint/group/torrents.png"),
        },
        堤防: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/seawall.png"),
          srcs: require("@/assets/images/MapPoint/seawall.png"),
        },
        海塘: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/seawall.png"),
          srcs: require("@/assets/images/MapPoint/seawall.png"),
        },
        泵站: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/seawall.png"),
          srcs: require("@/assets/images/MapPoint/seawall.png"),
        },
        水闸: {
          scale: 0.7,
          anchorXUnits: "pixels",
          anchor: [27, 30],
          src: require("@/assets/images/MapPoint/sluice.png"),
          srcs: require("@/assets/images/shuizha-dianwei-guanbi.png"),
        },
        闸站: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        灌区: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        农饮水: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
        水电站: {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/dianwei-juhe.png"),
          src: require("@/assets/images/legend/shuiku-juhe.png"),
        },
      };

      let pointList = this.projectList.map((item) => ({
        name: item.projectName,
        lng: item.longitude,
        lat: item.latitude,
        type: item.projectType,

        code: item.projectCode,
      }));
      let typePoints = {};
      pointList.forEach((item, index) => {
        if (typePoints.hasOwnProperty(item.type)) {
          typePoints[item.type].push(item);
        } else {
          typePoints[item.type] = [item];
        }
      });
      this.typePoints = typePoints;
      for (let type in typePoints) {
        this.map.drawClusterPoint(typePoints[type], styleMap[type], `effect${type}Layer`);
      }
    },
    getProjectList() {
      const { pointData } = this;
      if (pointData && Object.keys(pointData).length == 0) return;
      this.projectListLoading = true;
      let { lng, lat, se, sw, ne, nw } = pointData;
      let opt = { projectType: this.projectType, type: 2, lng, lat, se, sw, ne, nw };
      getTyphoonEffectedProject(opt).then((res) => {
        this.projectList = res.data;
        this.projectListLoading = false;

        if (this.pointActive) this.onPointActiveClick();
      });
    },
  },
};
</script>
<style>
.typhoon-effect {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
