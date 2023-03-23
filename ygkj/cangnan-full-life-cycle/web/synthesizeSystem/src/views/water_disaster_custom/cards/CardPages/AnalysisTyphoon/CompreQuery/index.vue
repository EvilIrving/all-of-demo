<template>
  <div class="comprehensive">
    <el-row>
      <el-col
        :span="advanced ? 24 : 12"
        :class="{ 'justify-none': advanced }"
      >
        <span style="white-space: nowrap">影响温州：</span>
        <el-select
          style="width: 110px"
          v-model="affectedWz"
          @change="baseQuery"
        >
          <el-option label="全部" :value="1" />
          <el-option label="是" :value="2" />
          <el-option label="否" :value="3" />
        </el-select>
      </el-col>
      <el-col v-if="!advanced" :span="11.5">
        <span style="margin-left: 0.375rem">年份：</span>
        <el-select
          style="width: 110px"
          v-model="year"
          clearable
          @change="baseQuery"
        >
          <el-option
            v-for="(item, index) in yearList"
            :key="index"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-col>
      <el-col :span="12" style="margin-top:12px">
         <span style="white-space: nowrap">台风名称： </span>
         <el-input v-model="searchVal" @change="baseQuery"></el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <span
          @click="advanced = !advanced"
          :class="[advanced ? 'advanced' : 'base', 'query']"
          >{{ advanced ? "高级查询" : "基本查询" }}
          <i class="el-icon-caret-bottom"
        /></span>
      </el-col>
      <el-col :span="12">
        <span
          :class="['risk-area', riskType == true ? 'active' : '']"
          @click="riskClick"
          >台风风险</span
        >
      </el-col>
    </el-row>
    <template v-if="advanced">
      <advanced-form @query="advancedQuery" />
    </template>
    <table-title title="台风列表">
      <template slot="append">
        <el-button
          type="text"
          style="color: #fff; padding: 0"
          @click="clearSelection"
        >
          <i class="icon-delete" style="vertical-align: bottom" />
          删除
        </el-button>
      </template>
    </table-title>
    <div ref="typhoonTable" style="height: 100%">
      <u-table
        v-loading="loading"
        ref="uTable"
        use-virtual
        stripe
        element-loading-background="rgba(0, 0, 0, 0.5)"
        element-loading-text="数据正在加载中"
        element-loading-spinner="el-icon-loading"
        :data="typhoonList"
        :height="typhoonListHeight"
        :row-height="35"
        @selection-change="handleSelectionChange"
      >
        <!-- @row-click="rowClick" -->
        <u-table-column type="selection" />
        <u-table-column label="编号" prop="code">
          <template slot-scope="{ row }">
            <span>{{ row.code }}</span>
            <i
              v-if="row.current"
              class="icon-current-typhoon"
              style="vertical-align: middle"
            />
          </template>
        </u-table-column>
        <u-table-column label="中文名" prop="name" />
        <u-table-column label="英文名" prop="englishName" />
      </u-table>
    </div>
  </div>
</template>
<script>
import { TableTitle } from "../../components.js";
import AdvancedForm from "./AdvancedForm.vue";
import { getTyhoonList, landTimesEachAdcd} from "@/api/typhoon.js";
import mapConfig from "@/config/mapConfig";
import { queryMapserver } from "@/api/loadMapServe";
import moment from "moment";
export default {
  name: "CompreQuery",
  components: { AdvancedForm, TableTitle },
  // inject from ../index.vue
  inject: ["analysisPage"],
  data() {
    return {
      multipleSelection: [],

      affectedWz: 1,
      year: "",
      yearList: [
        { value: "", label: "全部" },
        ...new Array(100).fill(0).map((item, index) => {
          let value = new Date().getFullYear() - index + "";
          return { value, label: value };
        }),
      ],

      advanced: false,

      // 台风表格
      loading: false,
      typhoonList: [],
      typhoonListHeight: 0,
      currentRow: "",
      timer: null,
      curSelectTyphoon: [],
      riskType:false,
      areaGeometryArr:[],
      searchVal:"",
    };
  },
  watch: {
    multipleSelection: {
      handler(val, oldVal) {
        let newChecked = val
          .filter((item) => !oldVal.includes(item))
          .map((item) => ({ code: item.code, name: item.name }));
        let unChecked = oldVal
          .filter((item) => !val.includes(item))
          .map((item) => ({ code: item.code, name: item.name }));

        if (oldVal.length - val.length < 2) {
          if (newChecked.length > 0) {
            let idx = this.curSelectTyphoon.indexOf(newChecked[0].code);
            if (idx == -1) {
              this.curSelectTyphoon.push(newChecked[0].code);
            }
          }
          if (unChecked.length > 0) {
            let idx = this.curSelectTyphoon.indexOf(unChecked[0].code);
            this.curSelectTyphoon.splice(idx, 1);
          }
        }
        this.analysisPage.$emit("selectTyphoon", { newChecked, unChecked });
      },
    },
  },
  created(){
    this.getAreaGeometry();
  },
  mounted() {
    this.fitTableHeight();
    this.baseQuery(true);
    this.timer = setInterval(() => {
      this.baseQuery(true);
    }, 300000);
  },
  computed:{
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  updated() {
    this.fitTableHeight();
  },
  methods: {
    //获取行政区划边界
    getAreaGeometry() {
      const searchUrl =
        mapConfig.mapServer + "wenzhou/wenzhou_fangxunend/MapServer/3/query";
      const opt = {
        where: "1=1",
        outFields: "*",
        f: "pjson",
      };
      queryMapserver(searchUrl, opt).then((res) => {
        if (res.features) {
          this.areaGeometryArr = res.features;
        }
      });
    },
    clearSelection() {
      this.$refs.uTable.clearSelection();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      // console.log(val,222)
    },
    rowClick(row, column) {
      if (column.type !== "selection") {
        this.analysisPage.$emit("rowClick", { code: row.code, name: row.name });
      }
    },
    fitTableHeight() {
      this.$nextTick(() => {
        this.typhoonListHeight = this.$refs.typhoonTable.offsetHeight - 10;
      });
    },
    baseQuery() {
      this.loading = true;
      let opt = { affectedWz: this.affectedWz,name:this.searchVal };
      if (this.year != "") {
        opt.begin = moment(`${this.year}-01-01`).format("yyyy-MM");
        opt.end = moment(`${this.year}-12-31`).format("yyyy-MM");
      }
      //先前台风code列表 如果有 加载 
      let curTyphoonCode = Array.from(this.curSelectTyphoon);
      getTyhoonList(opt).then((res) => {
        this.typhoonList = res.data;
        this.loading = false;
        if (curTyphoonCode.length > 0) {
          //加载之前的台风
          this.typhoonList.forEach(item => {
            if (curTyphoonCode.includes(item.code)) {
              this.$nextTick(() => {
                this.$refs.uTable.toggleRowSelection([{ row: item, selected: true }]);
                this.analysisPage.$emit("rowClick", { code: item.code, name: item.name, firstFlag: true });
              })
            }
          })
        } else {
          for (let i = 0; i < this.typhoonList.length; i++) {
            //如果有实时台风 选中第一个实时台风
            if (this.typhoonList[i].current) {
              this.$nextTick(() => {
                this.$refs.uTable.toggleRowSelection([{ row: this.typhoonList[i], selected: true }]);

                this.analysisPage.$emit("rowClick", { code: this.typhoonList[i].code, name: this.typhoonList[i].name, firstFlag: true });
              })
              break;
            }
          }
        }
      });
    },
    advancedQuery(opt) {
      this.loading = true;
      getTyhoonList({ affectedWz: this.affectedWz, ...opt }).then((res) => {
        this.typhoonList = res.data;
        this.loading = false;
      });
    },
    riskClick(){
      this.riskType = !this.riskType;
      if(this.riskType){
        this.landTimesEachAdcd();
      }else{
        this.map.drawAreaPolygon([], 'typhoonRiskAreaLayer');
        this.analysisPage.$emit("legend-change", "台风登陆次数", {});
      }
    },
    async landTimesEachAdcd(){
      let res = await landTimesEachAdcd();
      if(res.code == 0){
        res.data.map(item=>{
          item.geometry = this.getGeometryByName(item.key);
          item.attr = item;
          item.style = this.getColor(item.value);
        })
        this.map.drawAreaPolygon(res.data, 'typhoonRiskAreaLayer');
        this.analysisPage.$emit("legend-change", "台风登陆次数", {
        label: "台风登陆次数",
        children: [
          { label: "≤ 1", icon: "typhoon-type5" },
          { label: "1~3", icon: "typhoon-type4" },
          { label: "3~5", icon: "typhoon-type3" },
          { label: "＞ 5", icon: "typhoon-type2" },
        ],
      });
      }
    },
    getGeometryByName(name) {
      let data = this.areaGeometryArr.filter(item => {
        return item.attributes.NAME == name;
      })
      return data[0].geometry.rings;
    },
    getColor(val){
      const lineColors = ['#00DC00', '#00A0FF', '#FF994A', '#FF0120'];
      const fillColors = ['rgba(0,220,0,0.5)', 'rgba(0,160,255,0.5)', 'rgba(255,153,74,0.5)', 'rgba(255,1,32,0.5)'];
      let colorObj = {
        fillColor: "",
        lineColor: ""
      }
      if(val<=1){
        colorObj.fillColor = fillColors[0];
        colorObj.lineColor = lineColors[0];
      }else if(val<=3){
        colorObj.fillColor = fillColors[1];
        colorObj.lineColor = lineColors[1];
      }else if(val <= 5){
        colorObj.fillColor = fillColors[2];
        colorObj.lineColor = lineColors[2];
      }else{
        colorObj.fillColor = fillColors[3];
        colorObj.lineColor = lineColors[3];
      }
      return colorObj;
    }
  },
};
</script>
<style lang="scss">
.comprehensive {
  height: 100%;
  font-size: 14px;
  display: flex;
  flex-direction: column;
  .risk-area {
    @include flexbox;
    @include flexAC;
    flex-direction: row-reverse;
    height: 32px;
    cursor: pointer;
    width: 100%;
    &.active {
      color: #56fefe;
    }
  }
  .el-col {
    display: flex;
    justify-content: space-between;
    align-items: center;
    &.justify-none {
      justify-content: unset;
    }
  }
  .query {
    cursor: pointer;
    line-height: 32px;
    height: 32px;

    margin-right: 16px;
    > i {
      transition: all 0.3s ease;
    }
    &.base {
      /* margin-left: auto; */
      color: #fff;
    }
    &.advanced {
      color: #56fefe;
      > i {
        transform: rotate(-180deg);
      }
    }
  }
}
.el-table--border,
.el-table--group,
.el-table--border th.gutter:last-of-type,
.el-table--border th,
.el-table__fixed-right-patch {
  border: 0;
}
.el-table--border::after {
  background-color: transparent;
}
</style>
