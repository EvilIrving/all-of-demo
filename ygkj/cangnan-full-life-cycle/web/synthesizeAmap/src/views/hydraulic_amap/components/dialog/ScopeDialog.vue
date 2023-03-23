<template>
  <div class="scope-content">
    <div class="layer-tree">
      <div class="select-box" v-if='rangeShow'>
        距离范围:
        <el-select
          v-model="rangeVal"
          class="primary-select"
          @change="rangeHandle"
        >
          <el-option
            v-for="item in rangeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <el-tree
        ref="searchTree"
        @node-click="handleNodeClick"
        :data="scopeTreeData"
        default-expand-all
        node-key="label"
        :props="defaultProps"
        v-loading='treeLoading'
        element-loading-text="数据正在加载中..."
      >
      </el-tree>
    </div>
    <div class="table-box">
      <div class="select-group">
        <div class="select-box">
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
        </div>
        <div class="select-box">
          流域:
          <el-select
            v-model="basinVal"
            class="primary-select"
            @change="areaTypeHandle"
          >
            <el-option
              v-for="item in basinOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <!-- <div class="select-box">
          工程规模:
          <el-select
            v-model="engScalVal"
            class="primary-select"
            @change="areaTypeHandle"
          >
            <el-option
              v-for="item in engScalOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div> -->
      </div>
      <primary-map-table
        :columnList="columnList"
        :dataList="dataList"
        :tableLoading='tableLoading'
      ></primary-map-table>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components";
import {hydraulicApi} from "@/api/hydraulic_amap/index.js";

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
  mounted(){
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
        },{
          value: "鳌江",
          label: "鳌江",
        },{
          value: "飞云江",
          label: "飞云江",
        },{
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
          width: 160,
          prop: "tm",
          label: "时间",
        },
        {
          width: 160,
          prop: "projectName",
          label: "名称",
        },
        {
          width: 160,
          prop: "bas",
          label: "地址",
        },
        {
          prop: "projectType",
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
      selectNode:"",
    };
  },
  components: {
    PrimaryMapTable,
  },
  watch: {},
  methods: {
    async getAreaList(){
      let opt = {
        areaCode:'3303',
        level:3,
      }
      let res = await hydraulicApi.getArea(opt);
      if(res.code == 0){
        for(let item of res.data){
          const opt = {
            label:item.adnm,
            value:item.adcd
          }
          this.areaoOptions.push(opt);
        }
      }
    },
    //测面树结构点击
    handleNodeClick(val) {
      // console.log(val,333)
      this.tableLoading = true;
      this.selectNode = val.name;
      if(val.name == '全部'){
        this.getAnalysisTableData(val)
      }else{
        if(val.codes || val.number){
          this.getAnalysisTableData(val)
        }else{
          this.$nextTick(function () {
            this.$refs["searchTree"].setCurrentKey(val.label);
          });
          val.data.map((item) => {
            item.lat = item.latitude;
            item.lng = item.longitude;
          });
          let treeData = [];
          this.dataList = val.data;
          this.dataList.length > 0;
          for (let item of this.scopeTreeData[0].children) {
            treeData.push(item.label);
          }
          this.treeLoading = false
          // this.$emit("showScopePoint", val, treeData);
        }
      }

      
      this.tableLoading = false
    },
    //周边分析数据
    async getAnalysisTreeData(val) {
      this.treeLoading = true;
      val ? this.stationData = val : ''
      let opt = {
        longitude: this.stationData.longitude, //中心点的经度
        latitude: this.stationData.latitude, //中心点的经纬度
        distance: this.rangeVal, //公里半径（1/2/3）
      }
      let res = await hydraulicApi.getperipheralAnalysisTree(opt)
      this.$emit('setscopeTreeData',res.data);
      this.treeLoading = false
    },
    //周边分析数据
    async getAnalysisTableData(val) {
      let opt = {};
      if(val.name == '全部'){
        opt = {
          codes:'',
          type:'',
          bas:this.basinVal,
          adcd:this.areaVal,
        }
      }else{
        opt = {
          codes: val.codes, //对应叶子节点中的codes
          type: val.name, //对应叶子节点中的name
          bas:this.basinVal,
          adcd:this.areaVal,
        };
      }
      let res = await hydraulicApi.getperipheralAnalysisTable(opt);
      // console.log(this.scopeTreeData);
      this.dataList = res.data;
      this.setColumnList(val)
      // this.$emit("showScopePoint", {
      //   name: val.name,
      //   data: this.dataList
      // });
    },
    //设置表头
    setColumnList(val) {
      switch(val.name){
        case '潮位' : case '水库水位' : case '河道水位' : case '雨情' : case '工情' :
          this.columnList[1].prop = 'stName'
          this.columnList[2].prop = 'stLoc'
          this.columnList[3].prop = 'stType'
          break;
        case '水质':
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
      this.getAnalysisTreeData()
    },
    // 距离范围调整
    rangeHandle(val) {
      this.getAnalysisTreeData()
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
    @include flexflow(column,nowrap);
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
    ::v-deep .table-wrap{
      @include flex(1);
    }
  }
  ::v-deep .el-loading-mask {
    background: transparent;
  }
}
</style>
