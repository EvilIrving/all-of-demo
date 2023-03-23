<!-- 模型分析 -->
<template>
  <div class="sidebar-box">
    <div class="sidebar-head">
      <span>模拟结果</span>
      <i class="icon-simulation-setting"></i>
    </div>
    <ul class="model-info">
      <li v-for="(item, index) in modelInfo" :key="index">
        <p>
          <i :class="item.icon"></i>
          {{ item.label }}:
        </p>
        <p>
          <span class="model-num">{{ item.value }}</span>
          {{ item.unit }}
        </p>
      </li>
      <li></li>
    </ul>
    <div class="sidebar-head">
      <span>模型信息</span>
      <!-- <span>
        <i class="icon-water-drop"></i>
        <i class="icon-bar-count"></i>
      </span> -->
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="260"
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="forecastTime" label="时间"></el-table-column>
        <el-table-column prop="flow" label="入库流量"></el-table-column>
        <el-table-column prop="outFlow" label="出库流量"></el-table-column>
      </el-table>
    </div>
   
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ModelAnalysis",
  props: {
    lookDetailsData: { type: Array },
  },
  components: {  },
  data() {
    return {
      name: "",
      startRange: null,
      endRange: null,
      sidebarTabArr: [
        {
          label: "防汛仓库",
          value: 0,
        },
        {
          label: "防汛队伍",
          value: 1,
        },
      ],
      activeSidebar: 0,
      areaCode: "",
      areaArr: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "市本级",
          value: "330300000000",
        },
        {
          label: "鹿城区",
          value: "330302000000",
        },
        {
          label: "龙湾区",
          value: "330303000000",
        },
        {
          label: "瓯海区",
          value: "330304000000",
        },
        {
          label: "洞头区",
          value: "330322000000",
        },
        {
          label: "瑞安市",
          value: "330381000000",
        },
        {
          label: "乐清市",
          value: "330382000000",
        },
        {
          label: "永嘉县",
          value: "330324000000",
        },
        {
          label: "平阳县",
          value: "330326000000",
        },
        {
          label: "苍南县",
          value: "330327000000",
        },
        {
          label: "文成县",
          value: "330328000000",
        },
        {
          label: "泰顺县",
          value: "330329000000",
        },
      ],
      tableData: [],
      loading: false,
      modelInfo: [],
      stInfo: {},
      // lookDetails:'',
      // tokenObj: {
      //   三插溪水库:
      //     "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.OKs9IHcga4UmqHHm-SOwThk5Y1WsX4AbnbQjmVpueNHwepddDVv7vI9gbGwanwwzxr1sJXDq0M-Rv3CsotU_BWM6t8DztKN6ZLtpVlVPX_1MXFktIvkPotT6kxaP3b8wtsOJ7wo3PUzp-UXHUHJuDppTGy-5utKLc-ZrhpcENnc.VO_RFDdwRQL4GQYw.x687bDNarEJyAhdCJMWhKN1ZH73wpCNYdOI0jXOa7cIOQAqWV1r2KEuS03DBNsR_711w9D1G099AOXZqySGnIeJRNTUvumiYhAk2OOU8rl2PO90_aO0UgljPrgCwxlxe3gcr5OfaPH943SOQt4r4j8dJ46ZPfw.SP5nzy4NZfq1Ca15gUNrgA",
      //   金溪水库:
      //     "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.TBwD6Q9V9Rfki2m8ii0TTAVYE--nshtP7YyRmJuEaPdotcc51jmvxKFjSCSINfC6Hit_aPObaQGgCd6eS7X0ykj5LTqopN36924rdRcMzxoMva8dGZQrL88iKPP5URoiSYjoGg5pDIR7CNFH2-JVLgH9NvOL1WEJpBNxdKKFPLQ.WztTIPZTnY7z7rNg.qIV11_bEpG5yIh2vifEcYCxN2P_REBQYJq8spvK-LH_h16wktS3YVEn4w0Hshf2SRfTSi5iII6zY46jxFBFNObe8cLYE4VGALHdJ7n-CBQwJw4CrimvXKpPUfqpCNDRYS5zQAFvR4jH23MVO5QrLJXsd5g.0zal3_wGS7OHS_yB6II93w",
      //   仙居水库:
      //     "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.duy7MGEsXX1Eka3O43QhKl3zHp20zIKaTipSl5rg3vrWykJ6MxrnnTPNVIw_D0845IBVQ3KU6aOFq-DWHL6HSkIqpnt6BMpZMngs0UW1AjuX9rL6cGHEp6N9WxWOfoKzvYNClGpYDyp8u6hWCO80TPNnkXprMDQg2u3U7ZB8-N0.rYych5xd7cVAIZxi.AXA3Z32esYMkRBMCjqnZY_jqhvzTzvfVD59r00Bv-DconbHWvP8e128tgJ1H0ExtEhSPeOuHubGbNRM9VqD2i8ysD5FWlFRq9NQtlAmFMP6EUwU-82mZb-tCaz95ZDvP4KNvxJyV8wNgGSMBzG4Z5GKjxiAy7Sc.ml73o9ytMt0b6vNo9eMjDg",
      //   桥墩水库:
      //     "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.UPyJvaqOXJgeqwcBPJQRPIbIQ_8CTvIxIXdpgkKf22XrA01zPG9mMtr8NixOY9wgvyG4II_DiSJ9gctcqObed2qdnokAugC6ZJza_3ifOfXGy5SXWXnzkAoGAhqGLw2GdyItBRyzebbI0C-AShmZu8dHUpY6eFmvfZEZjRfSrts.F1VrlTRS0h45-iWM.ieCSj7nZnRLtUqRbP26dnD4nPy02-n0HBrcP1T3cqTz7z3jrWnI8bYC--3sqk7R53CbDU25oTy_54b0Z7-bJLuz1NVsteKk3HurqK0kMmvJWTzMhK0og37f1I5GCTelEZ6ZIsfysFnYga5GtQPrm6942Nw.DS3h0h3PBlL3JCGDWkznsQ",
      // },
      info:{},
      tokenStr: 'eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw'
    };
  },
  mounted() {
    this.info = this.$store.state.modelInfo;
    let sTime = this.$moment().subtract(3, "days").format("YYYY-MM-DD HH") + ":00:00";
    let eTime = this.$moment().subtract(-2, "days").format("YYYY-MM-DD HH") + ":00:00";
    let   limitWater = '';
    let projectId = '';
    this.lookDetailsData.forEach((val)=> {
      limitWater = val.limitLevel;
      projectId = val.floodId;
    })

  // this.$nextTick(()=> {
    this.getDetails(sTime,eTime,1.00,limitWater,1.00,projectId);
  // })
    
  },
  methods: {
    async getDetails(sTime,eTime,flowZoom,limitWater,outflowZoom,projectId) {
      this.tableData = [];
      this.name = this.info.projectName;
      await axios({
        url: `http://192.168.0.103:8888/mgt/bus/floodDispatch/dispatch?token=${this.tokenStr}&startTime=${sTime}&endTime=${eTime}&flowZoom=${flowZoom}&limitWater=${limitWater}&jsonFloodArray=[]&jsonWaveArray=[]&jsonSplitWaveArray=[]&outflowZoom=${outflowZoom}&outflowArray=[]&projectId=${projectId}`,
        method: "get",
      }).then((res) => {
        if (res.data.success) {
          const info = res.data;
          this.tableData = info.data;
          this.modelInfo = [
            {
              label: "预报最高水位",
              icon: "icon-water-level",
              unit: "m",
              value: info.highWater,
            },
            // {
            //   label: "目标水位",
            //   icon: "icon-water-level",
            //   prop: "targetLevel",
            //   unit: "m",
            //   value: 87,
            // },
            {
              label: "预报最大库容",
              icon: "icon-analysis-count",
              prop: "maxStorage",
              unit: "万m³",
              value: info.capacity,
            },
            {
              label: "富余库容",
              icon: "icon-analysis-count",
              prop: "targetStorage",
              unit: "万m³",
              value: info.overCapacity,
            },
            {
              label: "入库洪峰",
              icon: "icon-flood-peak",
              prop: "floodPeak",
              unit: "m³/s",
              value: info.highFlow,
            },
            {
              label: "入库洪量",
              icon: "icon-flood-peak",
              prop: "floodPeak",
              unit: "m³/s",
              value: info.inFlowVolume,
            },
            {
              label: "达到目标水位",
              icon: "icon-alarm-clock",
              prop: "achieveLevel",
              unit: "h",
              value: info.overWaterTime,
            },
          ];
          this.$emit("updateChart",res.data.data);
        }
      });
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
.model-info {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  color: #4898fb;
  font-size: 14px;
  > li {
    width: 33.33%;
    text-align: center;
    > p {
      line-height: 34px;
      > i {
        vertical-align: middle;
      }
    }
    .model-num {
      font-size: 18px;
    }
  }
}
</style>
<style lang="scss">
$searchInpHeight: 25px;
$mainColor: #3f9bfc;
$tdHeight: 37px;
.sidebar-box {
  .sidebar-list {
    .el-table .cell {
      height: $tdHeight;
      line-height: $tdHeight;
    }
    .el-table th,
    .el-table td {
      padding: 0;
      font-size: 16px;
      color: #2c5a90;
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
.sidebar-box .el-loading-mask {
  background-color: transparent;
}
</style>