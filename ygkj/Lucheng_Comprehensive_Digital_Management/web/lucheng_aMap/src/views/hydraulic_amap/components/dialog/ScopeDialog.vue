<template>
  <div class="scope-content">
    <div class="layer-tree">
      <div class="select-box" v-if="rangeShow">
        距离范围:
        <el-select v-model="rangeVal" class="primary-select" @change="rangeHandle">
          <el-option v-for="item in rangeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <el-tree ref="searchTree" @node-click="handleNodeClick" :data="scopeTreeData" default-expand-all node-key="name" :props="defaultProps" v-loading="treeLoading" element-loading-text="数据正在加载中...">
      </el-tree>
    </div>
    <div class="table-box">
      <div class="select-group">
        <!-- <div class="select-box">
          行政区划:
          <el-select
            v-model="areaVal"
            class="primary-select"
            @change="areaTypeHandle"
          >
            <el-option
              v-for="item in areaoOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div> -->
        <div class="select-box">
          流域:
          <el-select v-model="basinVal" class="primary-select" @change="areaTypeHandle">
            <el-option v-for="item in basinOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <primary-map-table :columnList="columnList" :dataList="dataList" :tableLoading="tableLoading"></primary-map-table>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

export default {
  name: "ScopeDialog",
  props: {
    scopeTreeData: {
      type: Array,
      default: [],
    },
    rangeShow: {
      type: Boolean,
      default: false,
    }
  },
  mounted() {
    this.getAreaList();
  },
  data() {
    return {
      rangeVal: "3", // 距离范围
      rangeOptions: [
        {
          value: "1",
          label: "1KM",
        },
        {
          value: "2",
          label: "2KM",
        },
        {
          value: "3",
          label: "3KM",
        },
      ],
      areaVal: "", // 行政区划
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      basinVal: "", // 流域
      basinOptions: [
        {
          value: "",
          label: "全部",
        }, {
          value: "鳌江",
          label: "鳌江",
        }, {
          value: "飞云江",
          label: "飞云江",
        }, {
          value: "瓯江",
          label: "瓯江",
        }
      ],
      engScalVal: "全部", // 工程规模
      engScalOptions: [
        {
          value: "全部",
          label: "全部",
        },
      ],
      columnList: [
        {
          prop: "projectName",
          label: "名称",
        },
        {
          width: 160,
          prop: "bas",
          label: "流域",
        },
        {
          prop: "analysisType",
          label: "类别",
        },
        {
          prop: "engScal",
          label: "工程等级",
        },
      ],
      dataList: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      stationData: [],
      tableLoading: true, // 表格loading
      treeLoading: true, // 树loading
      selectNode: "",
      scopeType: "水库",
    };
  },
  components: {
    PrimaryMapTable,
  },
  watch: {},
  methods: {
    doFirstNodeClick() {
      let node = this.$refs.searchTree.getNode(this.scopeTreeData[0].name);
      this.$nextTick(() => {
        this.handleNodeClick(this.scopeTreeData[0], node);
      });
    },
    async getAreaList() {
      let opt = {
        areaCode: '3303',
        level: 3,
      }
      let res = await hydraulicApi.getArea(opt);
      if (res.code == 0) {
        for (let item of res.data) {
          const opt = {
            label: item.adnm,
            value: item.adcd
          }
          this.areaoOptions.push(opt);
        }
      }
    },
    //测面树结构点击
    async handleNodeClick(val, obj) {
      this.tableLoading = true;
      this.selectNode = val.name;
      this.dataList = this.scopeType == "水库" ? val.data : await this.getRiverAnalysisTableData();
      this.dataList = this.formatTableData(this.dataList)
      this.$refs.searchTree.setCurrentKey(val.name);
      this.treeLoading = false;
      this.tableLoading = false;
    },
    //周边分析数据
    async getAnalysisTreeData(val) {
      this.scopeType = "水库"
      this.treeLoading = true;
      val ? this.stationData = val : ''
      let opt = {
        longitude: this.stationData.longitude, //中心点的经度
        latitude: this.stationData.latitude, //中心点的经纬度
        distance: this.rangeVal, //公里半径（1/2/3）
      }
      let res = await hydraulicApi.getperipheralAnalysisTree(opt)
      this.$emit('setscopeTreeData', res.data);
      this.treeLoading = false;
    },
    // 河道周边分析数据
    async getRiverAnalysisTreeData(val) {
      this.scopeType = "河道"
      this.treeLoading = true;
      val ? this.stationData = val : ''
      let opt = {
        riverSegId: this.stationData.id, //中心点的经度
        distance: this.rangeVal, //公里半径（1/2/3）
      }
      let res = await hydraulicApi.riverPeripheralAnalysis(opt)
      this.$emit('setscopeTreeData', res.data.map(item => {
        item.name = item.type
        item.children && item.children.map(sItem => {
          sItem.name = sItem.type
          return item
        })
        return item
      }));
      this.treeLoading = false;
    },
    getRiverAnalysisTableData() {
      let opt = {
        riverSegId: this.stationData.id, //中心点的经度
        type: this.selectNode,
        distance: this.rangeVal, //公里半径（1/2/3）
      }
      return new Promise(async (resolve, reject) => {
        let res = await hydraulicApi.selectPoints(opt)
        resolve(res.data.map(item => {
          // 入河排污口 longitude latitude
          // 农饮水 cws_long cws_lat
          // 取水口 wain_long wain_lat
          // 在建工程 longitude latitude
          // 堤防 start_long start_lat
          // 山塘 dt_mid_long dt_mid_lat
          // 水电站 hyst_long hyst_lat
          // 水闸 start_long start_lat
          // 河道河景 longitude latitude
          // 泵站 pust_long pust_lat
          // 海塘 longitude latitude
          // 视频 longitude latitude
          // 雨水口 longitude latitude
          item.longitude = item.longitude || item.dt_mid_long || item.low_left_long || item.cws_long || item.wain_long || item.start_long
            || item.hyst_long || item.pust_long
          item.latitude = item.latitude || item.dt_mid_lat || item.low_left_lat || item.cws_lat || item.wain_lat || item.start_long
            || item.hyst_lat || item.pust_lat
          item.analysisType = this.selectNode
          return item
        }))
      })
    },
    //格式化表格数据
    formatTableData(data) {
      data.map(item => {
        item.projectName = item.projectName || item.stName || item.name || item.cameraName;
        item.code = item.cameraIndexCode || item.projectCode || item.stCode;
        item.type = item.analysisType;
        return item
      })
      return data;
    },
    //设置表头
    setColumnList(val) {
      switch (val.name) {
        case '潮位': case '水库水位': case '河道水位': case '雨情': case '工情':
          this.columnList[1].prop = 'stName'
          this.columnList[2].prop = 'stLoc'
          this.columnList[3].prop = 'stType'
          break;
        case '水质':
          this.columnList[1].prop = 'name'
        case '断面':
          this.columnList[1].prop = 'name'
          break;
        case '视频':
          this.columnList[1].prop = 'cameraName'
          this.columnList[3].prop = 'cameraTypeName'
          break;
        case '积水':
          this.columnList[1].prop = 'name'
          this.columnList[1].prop = 'streetName'
          break;
        case '水库':
          this.columnList[1].prop = 'resName'
          this.columnList[3].prop = 'resType'
          break;
        case '堤防':
          this.columnList[1].prop = 'dikeName'
          this.columnList[3].prop = 'dikeType'
          this.columnList[4].prop = 'dikeGrad'
          break;
        case '水电站':
          this.columnList[1].prop = 'hystName'
          this.columnList[3].prop = 'hystType'
          break;
        case '山塘':
          this.columnList[1].prop = 'mnpdName'
          break;
        case '农村供水工程':
          this.columnList[1].prop = 'cwsName'
          this.columnList[3].prop = 'projectCategory'
          break;
        case '泵站':
          this.columnList[1].prop = 'pustName'
          break;
        case '水闸':
          this.columnList[1].prop = 'wagaName'
          this.columnList[3].prop = 'mainBuildGrad'
          break;
        case '海塘':
          this.columnList[1].prop = 'seawallName'
          this.columnList[2].prop = 'startLocation'
          this.columnList[3].prop = 'seawallType'
          this.columnList[4].prop = 'seawallLevel'
          break;
        case '灌区':
          this.columnList[1].prop = 'irrName'
          this.columnList[2].prop = 'bas'
          break;
        case '闸站':
          this.columnList[1].prop = 'gateName'
          this.columnList[2].prop = 'bas'
          break;
        case '仓库':
          this.columnList[1].prop = 'name'
          this.columnList[2].prop = 'areaName'
          this.columnList[3].prop = 'type'
          break;
        case '队伍':
          this.columnList[1].prop = 'name'
          this.columnList[2].prop = 'address'
          this.columnList[3].prop = 'typeName'
          break;
      }
    },
    // select选择
    areaTypeHandle() {
      this.scopeType == "水库" ? this.getAnalysisTreeData() : this.getRiverAnalysisTreeData()
    },
    // 距离范围调整
    rangeHandle(val) {
      this.scopeType == "水库" ? this.getAnalysisTreeData() : this.getRiverAnalysisTreeData()
      this.$emit("changeRange", val)
    }
  },
};
</script>

<style lang="scss" scoped>
.scope-content {
  display: flex;
  height: 568px;
  ::v-deep .el-input__inner {
    width: 91px;
    height: 32px;
    border: 1px solid #3f9bfc;
    border-radius: 4px;
  }
  ::v-deep .el-input__icon {
    line-height: 24px;
  }
  .layer-tree {
    overflow: auto;
    width: 250px;
    height: 568px;
    padding: 16px;
    ::v-deep .el-tree {
      background: rgba(0, 0, 0, 0);
      color: #fff;
      .el-tree-node.is-current > .el-tree-node__content {
        background: rgba(0, 0, 0, 0.5);
      }
      .el-tree-node:focus > .el-tree-node__content {
        background: rgba(0, 0, 0, 0.5);
      }
      .el-checkbox__inner {
        background: rgba(0, 0, 0, 0.1);
        border: 1px solid #308dcc;
      }
      .el-tree-node__content {
        &:hover {
          background: rgba(0, 0, 0, 0.5);
        }
      }
      .el-tree-node {
        margin: 6px 0;
      }
    }
    .select-box {
      width: 213px;
      color: #fff;
      ::v-deep .el-input__inner {
        width: 137px;
      }
    }
  }

  .table-wrap {
    height: 424px;
  }

  ::v-deep .el-table {
    tr {
      background-color: transparent;
    }
  }
  .table-box {
    @include flex(1);
    @include flexbox;
    @include flexflow(column, nowrap);
    color: #fff;
    padding-right: 6px;
    .select-group {
      padding: 16px 0 24px;
      display: flex;
      .select-box {
        width: 161px;
        margin-right: 51px;
      }
    }
    ::v-deep .table-wrap {
      @include flex(1);
    }
  }
  ::v-deep .el-loading-mask {
    background: transparent;
  }
}
</style>
