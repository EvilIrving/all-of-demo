<!-- 防汛研判左侧 -->
<template>
  <div class="sidebar-box">
    <div class="sidebar-list">
      <p><i></i>水库实时工情</p>
      <el-table
        :data="tableData1"
        style="width: 100%"
        height="174"
        highlight-current-row
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="data" label="水位(m)"></el-table-column>
        <el-table-column prop="flow" label="流量(m³/s)"></el-table-column>
        <el-table-column prop="gate" label="闸门开度"></el-table-column>
      </el-table>
    </div>

    <div class="sidebar-list">
      <p><i></i>水闸实时工情</p>
      <el-table
        :data="tableData2"
        style="width: 100%"
        height="174"
        highlight-current-row
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="wagaName" label="名称"></el-table-column>
        <el-table-column
          prop="upz"
          label="闸上水位(m)"
          sortable
        ></el-table-column>
        <el-table-column prop="dwz" label="闸下水位(m)"></el-table-column>
        <el-table-column prop="ovs" label="开度(m)"></el-table-column>
      </el-table>
    </div>

    <div class="sidebar-list">
      <p><i></i>泵站实时工情</p>
      <el-table
        :data="tableData1"
        style="width: 100%"
        height="174"
        highlight-current-row
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="data" label="水位(m)" sortable></el-table-column>
        <el-table-column prop="flow" label="流量(m³/s)"></el-table-column>
        <el-table-column prop="gate" label="泵组开关"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { precipitation } from "@/api/dataScreen/floodJudge/index";
export default {
  name: "JudgeLeftTable",
  props: {},
  data() {
    return {
      name: "",
      tableData1: [],
      tableData3: [],
      tableData: [
        {
          name: "积水点1",
          data: 0.12,
          flow: "21.3",
          gate: "21.3",
          latitude: 28.03914575,
          longitude: 120.5795858,
        },
        {
          name: "积水点2",
          data: 0.26,
          flow: "21.3",
          gate: "21.3",
          latitude: 28.03913661,
          longitude: 120.5796705,
        },
        {
          name: "积水点3",
          data: 0.172,
          flow: "21.3",
          gate: "21.3",
          latitude: 28.03911531,
          longitude: 120.5794466,
        },
        {
          name: "积水点4",
          data: 0.13,
          flow: "21.3",
          gate: "21.3",
          latitude: 28.03904428,
          longitude: 120.5796424,
        },
        {
          name: "积水点5",
          data: 0.1,
          flow: "21.3",
          gate: "21.3",
          latitude: 28.03158784,
          longitude: 120.5963369,
        },
      ],
      stationData: [
        {
          name: "丰门河口站",
          data: 0.6,
          time: "2021-02-07",
          latitude: 27.815746,
          longitude: 119.928272,
        },
        {
          name: "卧祺水闸上游",
          data: 0.26,
          time: "2021-02-07",
          latitude: 27.973622,
          longitude: 120.369628,
        },
        {
          name: "双屿康龙",
          data: 0.172,
          time: "2021-02-07",
          latitude: 27.845775,
          longitude: 120.390432,
        },
        {
          name: "西山2",
          data: 0.55,
          time: "2021-02-07",
          latitude: 27.76599,
          longitude: 120.652127,
        },
      ],
      tableData2: [],
    };
  },
  mounted() {
    this.getData();
    this.tableData1=[{
          name: "泽雅水库",
          data: "79.3",
          flow: "0",
          gate: "0"
},{
          name: "顺溪水库",
          data: "153.85",
          flow: "0",
          gate: "0"

},{
          name: "金溪水库",
          data: "249.86",
          flow: "0",
          gate: "0"

},{
          name: "淡溪水库",
          data: "27.13",
          flow: "0",
          gate: "0"

},{
          name: "高岭头水库（压力北斗）",
          data: "736.64",
          flow: "0",
          gate: "0"

}]
this.tableData2=[{
          name: "勤奋水闸",
          data: "2.87",
          flow: "1.67",
          gate: "0"
},{
          name: "卧旗水闸",
          data: "2.73",
          flow: "1.84",
          gate: "0"

},{
          name: "灰桥新闸",
          data: "249.86",
          flow: "2.72",
          gate: "0"

},{
          name: "三十六村水闸",
          data: "2.69",
          flow: "1.66",
          gate: "0"

},{
          name: "蒲州水闸",
          data: "736.64",
          flow: "2.64",
          gate: "0"

}]
this.tableData3=[{
          name: "瓯江翻水站",
          data: "4.2",
          flow: "10.6",
          gate: "关"
},{
          name: "潘山翻水站",
          data: "2.6",
          flow: "0.5",
          gate: "关"

}]
    // 父组件传值给地图Point;
    if (!this.$route.query.dmyl) {
      this.$parent.$refs.map.rainPoint(this.tableData, this.stationData);
    }
  },
  methods: {
    //水闸工情接口
    async getData() {
      let opt = {
        adcd: '',
        bas: '',
        projectName: ''
      }
      let res = await precipitation.listWaga(opt);
      if (res.code == 0) {
        res.data.map(item => {
          if (!item.upz) {
            item.upz = '-';
          }else if (!item.dwz) {
            item.dwz = '-';
          }else if (!item.ovs) {
            item.ovs = '-';
          }
        })
        this.tableData2 = res.data;
      }
    },
    handleRowClick(row) {
      let { startLat, startLong } = row;
      if (startLat && startLong) {
        this.$parent.$refs.map.setView(startLong, startLat);
      } else {
        this.$message.warning("暂无经纬度！");
      }
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
</style>
<style lang="scss">
$mainColor: #3f9bfc;
$tdHeight: 37px;
.sidebar-box {
  .sidebar-list {
    p {
      height: 32px;
      line-height: 32px;
      color: #20d1e9;
      i {
        display: inline-block;
        width: 16px;
        height: 16px;
        background: url(../../../../../assets/images/judge-icon.png) no-repeat;
        vertical-align: middle;
        margin-top: -2px;
        margin-right: 8px;
      }
    }
    .el-table .cell {
      height: $tdHeight;
      line-height: $tdHeight;
    }
    .el-table th,
    .el-table td {
      padding: 0;
      font-size: 14px;
      color: #fff;
      text-align: left;
    }
    .el-table th {
      padding: 3px 0;
      background-color: transparent;
      font-weight: bold;
    }
    .el-table__body tr.current-row > td {
      color: #fff;
      background-color: transparent;
    }
  }
}
.bigScreen-sel {
  .el-select-dropdown__item.selected {
    color: $mainColor;
  }
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
</style>