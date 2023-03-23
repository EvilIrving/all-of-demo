

<template>
  <div ref="wrapper" class="wrapper">
    <div ref="query" class="query_wrapper whitestyle" style="margin:0">
      <!-- 查询区域 -->
      <template>
        <el-select :style="{'margin-right':sgzgMapType==='闭环管控'? '55%':''}" popper-class="query_select whitestyle" clearable v-model="selectAdcd" placeholder="请选择市县" @change="getTableListByQuery">
          <el-option v-for="item in adcdList" :key="item.adcd" :label="item.label" :value="item.adcd">
          </el-option>
        </el-select>
        <el-select popper-class="query_select whitestyle" v-if="sgzgMapType!=='闭环管控'" clearable v-model="scaleSelect" placeholder="请选择规模" @change="getTableListByQuery">
          <el-option v-for="item in scaleList" :key="item.value" :label="item.scale" :value="item.value">
          </el-option>
        </el-select>

        <el-input v-model="inputName" placeholder="请输入名称" v-if="sgzgMapType!=='闭环管控'" clearable @input="getTableList"></el-input>
        <el-button plain key="querySearch" v-show="isFirstEntry" @click="getTableListByQuery">查询</el-button>
        <el-button plain key="nullSearch" v-show="!isFirstEntry" @click="getTableList">查询</el-button>
        <el-button plain key="queryExport" v-show="isFirstEntry" @click="exportListByQuery">导出</el-button>
        <el-button plain key="nullExport" v-show="!isFirstEntry" @click="exportList">导出</el-button>
      </template>
    </div>
    <!-- 表格 -->
    <div class="sk_table_wrapper whitestyle">
      <!-- 海塘概况 -->
      <el-table ref="tb_wrappers" v-if="sgzgMapType === '海塘概况'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="name" :show-overflow-tooltip="true" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="cityADNM" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryADNM" label="所在县" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column label="行政责任人" align="center">
          <el-table-column prop="executive_gov_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="executive_gov_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column label="技术责任人" align="center">
          <el-table-column prop="technical_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="techinical_position" label="职务" align="center"></el-table-column>
        </el-table-column>
        <el-table-column label="巡查责任人" align="center">
          <el-table-column prop="patrol_name" label="姓名" align="center"></el-table-column>
          <el-table-column prop="patrol_position" label="职务" align="center"></el-table-column>
        </el-table-column>
      </el-table>

      <el-table ref="tb_wrappers" v-if="sgzgMapType === '风险研判'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" align="center" width="60px"></el-table-column>
        <el-table-column prop="NAME" :show-overflow-tooltip="true" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="cityadnm" label="所在市" align="center"></el-table-column>
        <el-table-column prop="countryadnm" label="所在县" align="center"></el-table-column>
        <el-table-column prop="DESIGN_TIDE_LEVEL" label="设计潮位(m)" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.DESIGN_TIDE_LEVEL ? scope.row.DESIGN_TIDE_LEVEL.toFixed(2): 0}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="forecast" label="预报高潮位(m)" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.forecast ? scope.row.forecast.toFixed(2): 0}}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="预报超设计(m)" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.forecast - scope.row.DESIGN_TIDE_LEVEL ? (scope.row.forecast - scope.row.DESIGN_TIDE_LEVEL).toFixed(2): 0}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="forecast_time" label="出现时间" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.forecast_time ? scope.row.forecast_time.substring(0,16) : ''}}
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-table ref="tb_wrappers" v-if="sgzgMapType === '闭环管控'" v-loading="loading" :height="tableHeight" :data="siteList" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" align="center" width="90px"></el-table-column>
        <el-table-column prop="cityadnm" label="所在市" align="center" width="110px"></el-table-column>
        <!-- <el-table-column prop="countryadnm" label="所在县" align="center" width="90px"></el-table-column> -->
        <!-- <el-table-column prop="fxds" label="风险底数" align="center"></el-table-column> -->
        <el-table-column label="管控措施" align="center">
          <el-table-column prop="jmxc" label="加密巡查(人次)" align="center"></el-table-column>
          <el-table-column prop="hzgb" label="旱闸关闭(处)" align="center"></el-table-column>
          <el-table-column prop="qkfd" label="缺口封堵(处)" align="center"> </el-table-column>
          <el-table-column prop="dkgb" label="道口封闭(处)" align="center"> </el-table-column>
          <!-- <el-table-column prop="yjjg" label="应急加固(条/处)" align="center"></el-table-column>
          <el-table-column prop="gctg" label="工程停工(项/个)" align="center"></el-table-column> -->
          <el-table-column prop="ryzy" label="人员转移(人)" align="center"></el-table-column>
        </el-table-column>
      </el-table>

    </div>
    <div class="project_popup_wrap" v-if="showProjectPopup">
      <ProjectPopup :popupData="popupData"></ProjectPopup>
    </div>
  </div>
</template>

<script>
import ProjectPopup from "./ProjectPopup";
import { listADCDtreeApi } from "@/api/api_utils";
import { seawallProIndo, seawallLeftStatisticApi, zhSeawall } from "@/api/api_seawall";
export default {
  components: {
    ProjectPopup,
  },
  props: {
    projectScale: {
      type: String,
      default: "",
    },
    sgzgMapType: {
      type: String,
      required: true,
      default: "",
    },
    planId: {
      type: String,
      default: "",
    },
    checkDataType: {
      type: Boolean,
      default: true,
    },
    fullscreen: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      adcdList: [],
      scaleList: [
        { scale: "一级海塘", value: 1 },
        { scale: "二级海塘", value: 2 },
        { scale: "三级海塘", value: 3 },
        { scale: "四级海塘", value: 4 },
        { scale: "非标海塘", value: 5 },
      ],
      bihuanList: [
        {
          cityadnm: '杭州市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '11/19',
          qkfd: '0/0',
          dkgb: '0/0',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '宁波市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '0/0',
          qkfd: '84/84',
          dkgb: '103/103',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '温州市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '199/199',
          qkfd: '34/34',
          dkgb: '81/81',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '嘉兴市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '10/28',
          qkfd: '0/0',
          dkgb: '5/5',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '绍兴市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '1/1',
          qkfd: '4/4',
          dkgb: '3/3',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '舟山市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '0/0',
          qkfd: '0/0',
          dkgb: '238/238',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '台州市',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '33/33',
          qkfd: '5/5',
          dkgb: '158/161',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        },
        {
          cityadnm: '合计',
          countryadnm: '-',
          fxds: '0',
          jmxc: '0',
          hzgb: '254/280',
          qkfd: '127/127',
          dkgb: '588/591',
          yjjg: '-',
          gctg: '-',
          ryzy: '0',
        }
      ],
      bihuanFupanList: [
        {
          cityadnm: '杭州市',
          countryadnm: '-',
          fxds: '41',
          jmxc: '476',
          hzgb: '5/5',
          qkfd: '0/0',
          dkgb: '0/0',
          yjjg: '',
          gctg: '',
          ryzy: '0',
        }, {
          cityadnm: '宁波市',
          countryadnm: '-',
          fxds: '21',
          jmxc: '1917',
          hzgb: '0/0',
          qkfd: '155/155',
          dkgb: '103/103',
          yjjg: '',
          gctg: '',
          ryzy: '9650',
        }, {
          cityadnm: '温州市',
          countryadnm: '-',
          fxds: '88',
          jmxc: '1202',
          hzgb: '249/249',
          qkfd: '89/89',
          dkgb: '81/81',
          yjjg: '',
          gctg: '',
          ryzy: '438',
        }, {
          cityadnm: '嘉兴市',
          countryadnm: '-',
          fxds: '17',
          jmxc: '442',
          hzgb: '34/35',
          qkfd: '0/0',
          dkgb: '5/5',
          yjjg: '',
          gctg: '',
          ryzy: '20',
        }, {
          cityadnm: '绍兴市',
          countryadnm: '-',
          fxds: '8',
          jmxc: '75',
          hzgb: '1/1',
          qkfd: '4/4',
          dkgb: '3/3',
          yjjg: '',
          gctg: '',
          ryzy: '25',
        }, {
          cityadnm: '舟山市',
          countryadnm: '-',
          fxds: '192',
          jmxc: '2861',
          hzgb: '0/0',
          qkfd: '0/0',
          dkgb: '238/238',
          yjjg: '',
          gctg: '',
          ryzy: '69',
        }, {
          cityadnm: '台州市',
          countryadnm: '-',
          fxds: '100',
          jmxc: '246',
          hzgb: '161/161',
          qkfd: '10/10',
          dkgb: '32/32',
          yjjg: '',
          gctg: '',
          ryzy: '363',
        },
        {
          cityadnm: '合计',
          countryadnm: '-',
          fxds: '0',
          jmxc: '6743',
          hzgb: '450/450',
          qkfd: '258/258',
          dkgb: '462/462',
          yjjg: '-',
          gctg: '-',
          ryzy: '10565',
        }
      ],
      scaleSelect: "",
      selectAdcd: "",
      inputName: "",
      tableHeight: 0,
      isFirstEntry: false, // 刚进来的时候是假，搜索之后变为真
      loading: false,
      siteList: [],
      showProjectPopup: false,
      popupData: {},
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    fullscreen(val) {
      this.resizeTable();
    },
  },
  mounted() {
    this.resizeTable();
    this.selectAdcd = this.leftSelectAdcd
      ? this.leftSelectAdcd
      : this.$localData("get", "userInfo").adcd;
    this.scaleSelect = this.leftSelectProjectScale ? +this.leftSelectProjectScale : ''
    this.getAdcdOptions();
    this.getTableList();
  },
  methods: {
    resizeTable() {
      this.$nextTick(() => {
        const height =
          this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
        this.tableHeight = height;
      });
    },
    async getAdcdOptions() {
      let res = await listADCDtreeApi({
        parentadcd: this.$localData("get", "userInfo").adcd,
        fetchAll: true,
      });
      if (res.success) {
        res.listTree.map((v, i) => {
          this.adcdList.push({
            label: v.ADNM,
            adcd: v.ADCD,
            value: i,
          });
        });
      }
    },
    // 初始查询列表
    async getTableList() {
      console.log("无条件查询");
      this.loading = true;
      let opt = {
        adcd: this.selectAdcd,
        name: this.inputName,
        projectScale: this.leftSelectProjectScale ? +this.leftSelectProjectScale : ''
      };

      let riskOpt = {
        statistic: "mapList",
        itemCategory: "forecast,planId",
        itemValue: "overCnt," + this.planId,
        name: this.inputName,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      };
      switch (this.sgzgMapType) {
        case "海塘概况":
          opt.projectScale = this.scaleSelect;
          break;
        case "风险研判":
          break;
        case "闭环管控":
          break;
        default:
          break;
      }
      let res = {};
      switch (this.sgzgMapType) {
        case "海塘概况":
          res = await seawallProIndo(opt);
          break;
        case "风险研判":
          res = await seawallLeftStatisticApi(riskOpt);
          break;
        case "闭环管控":
          // 正常情况true下 三列数据是0  四列数据始终是接口返回
          // 复盘状态false下 数据是excel表格中的统计 
          if (this.checkDataType) {
            res = await zhSeawall.seawallDoorSta({ adcd: this.selectAdcd });
          }
          break;
        default:
          break;
      }
      switch (this.sgzgMapType) {
        case "海塘概况":
        case "风险研判":
          if (res.success && res.data.length) {
            this.siteList = res.data;
          }
          this.loading = false;
          break;
        case "闭环管控":
          // 正常情况true下 三列数据是0  复盘状态false下 三列数据是excel表格中的统计 
          // 另外四列数据始终是接口返回
          if (res.success && res.data.length) {
            if (this.checkDataType) {
              res.data.forEach((element, index) => {
                this.bihuanList[index].qkfd = element.qkfd
                this.bihuanList[index].hzgb = element.hzgb
                this.bihuanList[index].dkgb = element.dkgb
                this.bihuanList[index].fxds = element.a
                this.bihuanList[index].jmxc = element.b
                this.bihuanList[index].ryzy = element.c
              });
              this.siteList = this.bihuanList
            } else {
              this.siteList = res.data
            }
          } else {
            this.siteList = this.bihuanFupanList
          }
          this.loading = false;
          break;
        default:
          break;
      }


    },
    // 根据查询条件查询列表
    async getTableListByQuery() {
      console.log("条件查询");
      this.loading = true;
      if (!this.isFirstEntry) this.isFirstEntry = true;
      let opt = {
        adcd: this.selectAdcd,
        name: this.inputName,
      };
      let riskOpt = {
        statistic: "mapList",
        itemCategory: "forecast,planId",
        adcd: this.selectAdcd
      };

      let res = {};
      switch (this.sgzgMapType) {
        case "海塘概况":
          res = await seawallProIndo(opt);
          break;
        case "风险研判":
          res = await seawallLeftStatisticApi(riskOpt);
          break;
        case "闭环管控":
          // 正常情况true下 三列数据是0  四列数据始终是接口返回
          // 复盘状态false下 数据是excel表格中的统计 
          if (this.checkDataType) {
            res = await zhSeawall.seawallDoorSta({ adcd: this.selectAdcd });
          }
          break;
        default:
          break;
      }
      switch (this.sgzgMapType) {
        case "海塘概况":
        case "风险研判":
          if (res.success && res.data.length) {
            this.siteList = res.data;
          }
          this.loading = false;
          break;
        case "闭环管控":
          // 正常情况true下 三列数据是0  复盘状态false下 三列数据是excel表格中的统计 
          // 另外四列数据始终是接口返回
          if (res.success && res.data.length) {
            if (this.checkDataType) {
              res.data.forEach((element, index) => {
                this.bihuanList[index].qkfd = element.qkfd
                this.bihuanList[index].hzgb = element.hzgb
                this.bihuanList[index].dkgb = element.dkgb
                this.bihuanList[index].fxds = element.a
                this.bihuanList[index].jmxc = element.b
                this.bihuanList[index].ryzy = element.c
              });
              this.siteList = this.bihuanList
            } else {
              this.siteList = res.data
            }
          } else {
            this.siteList = this.bihuanFupanList
          }
          this.loading = false;
          break;
        default:
          break;
      }
    },
    exportList() {
      console.log("无条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "海塘概况":
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&projectScale=${this.leftSelectProjectScale}` +
            `&token=${token}`
          );
          break;
        case "闭环管控":
          if (this.checkDataType) {
            window.open(
              this.$config.host +
              `/mgt/bm/watertube/seawallGkToExcel` +
              `?adcd=${this.selectAdcd}`
            );
          } else {
            window.open(this.$config.zhImgUrl + '海塘闭环管控一键生成Resume版2.xlsx');
          }
          break;
        default:
          window.open(
            this.$config.host +
            `/mgt/bm/seawallei/toExcel` +
            `?statistic=mapList` +
            `&itemCategory=forecast,planId` +
            `&itemValue=overCnt,${this.planId}` +
            `&adcd=${this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd}` +
            `&token=${token}`
          );
          break;
      }
    },
    exportListByQuery() {
      console.log("条件导出");
      let token = this.$localData("get", "token");
      switch (this.sgzgMapType) {
        case "海塘概况":
          window.open(
            this.$config.host +
            `/mgt/bm/watertube/seawallProjectToExcel` +
            `?adcd=${this.selectAdcd}` +
            `&projectScale=${this.scaleSelect}` +
            `&token=${token}`
          );
          break;
        case "闭环管控":
          if (this.checkDataType) {
            window.open(
              this.$config.host +
              `/mgt/bm/watertube/seawallGkToExcel` +
              `?adcd=${this.selectAdcd}`
            );
          } else {
            window.open(this.$config.zhImgUrl + '海塘闭环管控一键生成Resume版2.xlsx');
          }
          break;
        default:
          if (this.scaleSelect) {
            window.open(
              this.$config.host +
              `/mgt/bm/seawallei/toExcel` +
              `?statistic=mapList` +
              `&itemCategory=forecast,planId,scale` +
              `&itemValue=overCnt,${this.planId},${this.scaleSelect}` +
              `&adcd=${this.leftSelectAdcd
                ? this.leftSelectAdcd
                : this.$localData("get", "userInfo").adcd}` +
              `&token=${token}`
            );
          } else {
            window.open(
              this.$config.host +
              `/mgt/bm/seawallei/toExcel` +
              `?statistic=mapList` +
              `&itemCategory=forecast,planId` +
              `&itemValue=overCnt,${this.planId}` +
              `&adcd=${this.leftSelectAdcd
                ? this.leftSelectAdcd
                : this.$localData("get", "userInfo").adcd}` +
              `&token=${token}`
            );
          }
          break;
      }
    },
    sortBy(attr, rev) {
      rev = rev ? 1 : -1;
      return function (a, b) {
        a = a[attr];
        b = b[attr];
        if (a < b) {
          return rev * -1;
        }
        if (a > b) {
          return rev * 1;
        }
        return 0;
      };
    },
    clickProject(row) {
      this.popupData = row;
      this.popupData.name = row.name || row.name;
      this.popupData.prcd = row.prcd || row.prcd;
      this.showProjectPopup = true;
    },
    closeProjectPopup() {
      this.showProjectPopup = false;
    },
  },

};
</script>
<style  scoped lang="scss">
.green {
  color: #58c056;
}
.red {
  color: #ff3153;
}
.yellow {
  color: #f7b500;
}

.wrapper {
  .project_popup_wrap {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.5);
    @include flexbox();
    @include flexAI(center);
    @include flexJC(center);
  }
}
</style>
<style lang="scss">
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: #fff !important;
}
.wrapper {
  // 查询样式
  font-size: 16px;
  width: 100%;
  height: 100%;
  .query_select {
    margin: 0 10px;
  }
}
</style>
