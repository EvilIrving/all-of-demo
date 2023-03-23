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

    <table-title title="历史相似灾情"> </table-title>

    <div class="influence-list">
      <div class="no-data" v-if="influenceList.length == 0">暂无数据</div>
      <div
        class="list-item"
        v-for="(item, index) in influenceList"
        :key="index"
      >
        <el-row class="item-line line-title">
          <!-- <el-col :span="6">
              <span>{{ item.adnm }}</span>
            </el-col>
            <el-col :span="6">
              <span>{{ item.year }}</span>
            </el-col> -->
          <el-col :span="24">
            <span>{{ item.typhoonName }} ( {{ item.year }} ) </span>
          </el-col>
          <!-- <el-col :span="6">
              <span>{{ item.community }}</span>
            </el-col> -->
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-xzqh"></i>
            影响行政区划：{{ item.adnm }}
          </div>
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-ryss"></i>
            <span>人员损失: {{ item.personLoss }}</span>
          </div>
        </el-row>
        <el-row class="item-line">
          <div>
            <i class="icon-tfyx-wzss"></i>
            <span>物资损失: {{ item.materialLoss }}</span>
          </div>
        </el-row>
        <el-row class="item-line line"></el-row>
      </div>
    </div>
  </div>
</template>
<script>
import { getTyphoonEffectedProject, getTyphoonAffects } from "@/api/typhoon";
import { TableTitle } from "../../components.js";
export default {
  name: "TyphoonEffect",
  layerName: "effectLayer",
  props: { pointData: Object, typhoonName: String },
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
      influenceList: [],
    };
  },
  watch: {
    pointData: {
      immediate: true,
      handler(val, oldVal) {
        if (val) {
          this.fitTableHeight();
          this.getProjectList();
          this.getTyphoonAffects();
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
    this.getTyphoonAffects();
    console.log(this.typhoonName, 555);
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
          if(type == '堤防' || type == '海塘'){
            this.map.removeLineLayer();
          }else{
            this.map.drawClusterPoint([], {}, `effect${type}Layer`);
          }
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
          src: require("@/assets/images/pond1big.png"),
          srcs: require("@/assets/images/pond1big-juhe.png"),
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
          src: require("@/assets/images/pump3big.png"),
          srcs: require("@/assets/images/pump3big-juhe.png"),
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
          srcs: require("@/assets/images/MapPoint/map-sluicestation-cluster.png"),
          src: require("@/assets/images/MapPoint/map-sluicestation.png"),
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

      let pointList = this.projectList.map((item) => {
        let obj = {
          name: item.projectName,
          lng: item.longitude,
          lat: item.latitude,
          type: item.projectType,

          code: item.projectCode,
        }
        if (item.projectType == '海塘') {
          obj.info = item.seawallInfo;
        } else if (item.projectType == '堤防') {
          obj.info = item.dikeInfo;
        }
        return obj;
      });
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
        if (type == '堤防') {
          typePoints[type].forEach(dike => {
            let color = "#f40305";
            if (dike.dikeGrad == '4') {
              color = "#fbda17";
            } else if (dike.dikeGrad == '5') {
              color = "#4fff01";
            }
            let style = {
              lineWidth: 4,
              lineColor: color,
            };
            let lineString = dike.info.coorpot && dike.info.coorpot.replaceAll('&quot;', "'") || "[]";
            let coordinates = lineString && eval(lineString);
            coordinates = coordinates.map(line => {
              line = line.map(item => {
                return [item.lng, item.lat];
              });
              return {
                lineList: line,
                data: {
                  type: "堤防",
                  name: dike.name,
                  code: dike.code,
                  props: {
                    '堤防类型': dike.info.dikeType,
                    '防洪标准(年)': dike.info.planFlSta || '-',
                    '堤防长(米)': dike.info.dikeLen || '-'
                  },
                  ...dike
                }
              };
            });
            this.map.drawLineEx(coordinates, style, 'dikeLineLayer');
          });
        } else if(type == "海塘") {
          typePoints[type].forEach(seawall => {
            let color = "#E80000";
            if (seawall.info.designTideStandard < 10) {
              color = "#E80000";
            } else if (seawall.info.designTideStandard == 10) {
              color = "#DD00DB";
            } else if (seawall.info.designTideStandard == 20) {
              color = "#E89100";
            } else if (seawall.info.designTideStandard == 50) {
              color = "#EEC80B";
            } else if (seawall.info.designTideStandard == 100) {
              color = "#19AF47";
            } else if (seawall.info.designTideStandard > 100) {
              color = "#4CEBC7";
            } else if (seawall.info.designTideStandard == null) {
              color = "#8D0A0A";
            }
            let style = {
              lineWidth: 4,
              lineColor: color,
            };
            let lineString = seawall.info.coorpot && seawall.info.coorpot.replaceAll('&quot;', "'") || "[]";
            let coordinates = lineString && eval(lineString);
            coordinates = coordinates.map(line => {
              line = line.map(item => {
                return [item.lng, item.lat];
              });
              return {
                lineList: line,
                data: {
                  type: "海塘",
                  name: seawall.name,
                  code: seawall.code,
                  props: {
                    海塘长度: seawall.info.seawallLength + "m",
                    防潮标准: seawall.info.designTideStandard + "m",
                    所在位置: seawall.info.village,
                  },
                  ...seawall
                }
              };
            });
            this.map.drawLineEx(coordinates, style, 'seawallLineLayer');
          });
        } else {
          this.map.drawClusterPoint(typePoints[type], styleMap[type], `effect${type}Layer`);
        }
      }
    },
    getProjectList() {
      const { pointData } = this;
      if (pointData && Object.keys(pointData).length == 0) return;
      this.projectListLoading = true;
      let { lng, lat, se, sw, ne, nw } = pointData;
      let opt = { projectType: this.projectType, type: 2, lng, lat, se, sw, ne, nw, withProjInfo: true };
      getTyphoonEffectedProject(opt).then((res) => {
        this.projectList = res.data;
        this.projectListLoading = false;

        if (this.pointActive) this.onPointActiveClick();
      });
    },
    async getTyphoonAffects() {
      let opt = {
        name: this.typhoonName,
        fetchAll: true,
      }
      let res = await getTyphoonAffects(opt);
      if (res.data.list == null) {
        this.influenceList = [];
      } else {
        this.influenceList = res.data.list;
      }
    }
  },
};
</script>
<style lang="scss">
.typhoon-effect {
  height: 100%;
  display: flex;
  flex-direction: column;
  .influence-list {
    height: 100%;
    overflow: auto;
    .no-data {
      width: 100%;
      height: 100%;
      color: #fff;
      text-align: center;
      @include flexbox;
      @include flexflow(wrap, nowrap);
      @include flexJC(center);
      @include flexAC;
    }
    .list-item {
      color: #fff;
      margin-bottom: 16px;
      font-size: 14px;
      .item-line {
        margin-top: 16px;
        line-height: 24px;
        i {
          display: inline-block;
          width: 16px;
          height: 16px;
          margin-right: 8px;
          vertical-align: middle;
        }
        span {
          vertical-align: middle;
        }
        &.line-title {
          font-size: 20px;
        }
        &.line {
          width: 100%;
          height: 1px;
          background-color: rgba(255, 255, 255, 0.3);
        }
      }
    }
  }
}
</style>
