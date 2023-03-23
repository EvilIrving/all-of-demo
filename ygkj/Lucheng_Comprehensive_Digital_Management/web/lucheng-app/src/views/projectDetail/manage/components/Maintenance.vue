<template>
  <div class="appraisal">
    <div class="unit_data">
      <h3>维修养护</h3>
      <h5>单位：万元</h5>
    </div>
    <div class="body_contents">
      <ul>
        <li v-for="(item, index) in tableData" :key="index">
          <div class="right last_item_detail">
            <p>
              <img src="../../../../assets/images/money.png" alt="" />
              申报资金: <span style="float:right">{{ item.dclrFunds }}</span>
            </p>
            <p>
              <!-- 巡查开始时间、巡查结束时间、巡查人员、是否有隐患 -->
              <img src="../../../../assets/images/money.png" alt="" />
              上级下达资金:<span style="float:right">{{ item.superiorFunds }}</span>
            </p>
            <p>
              <img src="../../../../assets/images/money.png" alt="" />
              已使用资金:<span style="float:right">{{ item.usedamount }}</span>
            </p>
            <p>
              <img src="../../../../assets/images/money.png" alt="" />
              已支付资金:<span style="float:right">{{ item.paidamount }}</span>
            </p>
            <p>
              <img src="../../../../assets/images/money.png" alt="" />
              自筹资金:<span style="float:right">{{ item.selfFunds }}</span>
            </p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { lgMfdApi } from "@/api/projectDetail";

export default {
  props: {},
  data() {
    return {
      fileType: "文件类型",
      showPicker: false,
      showPickerT: false,
      fileTypes: ["pdf", "word", "txt", "excel"],
      timeChoices: [2018, 2019, 2020, 2021],
      timeChoosed: "时间选择",
      menuIndex: 0,
      tableData: [],
    };
  },
  computed: {},
  created() {},
  mounted() {
    this._loadPage();
  },
  watch: {},
  methods: {
    timeClick(index) {
      this.timeChoosed = index;
      this._loadPage();
      this.showPickerT = false;
    },
    fileClick(value) {
      this.fileType = value;
      this._loadPage();
      this.showPicker = false;
    },
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$toast("暂无报告");
      }
    },
    async _loadPage() {
      // this.loading = true;
      let res = await lgMfdApi.loadPage({
        // col1505463766726: this.timeChoosed,
        // types:this.fileType
      });
      if (res) {
        this.loading = false;
        if (res.code == 0) {
          this.tableData = res.data.list;
        }
      }
    },
  },
  components: {},
};
</script>

<style scoped lang="scss">
.appraisal {
  .unit_data {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 {
      // width: calc(100% - 20px);
      font-size: 20px;
      font-weight: 700;
      padding: 10px 0;
    }
    h5 {
      font-family: PingFangSC-Regular;
      font-size: 14px;
      color: rgba(31, 31, 31, 0.48);
      text-align: right;
      line-height: 24px;
      font-weight: 400;
    }
  }

  .filter_menus {
    @include flexbox();
    @include flexJC(flex-start);
    @include flexAI(center);
    .el-dropdown-link {
      cursor: pointer;
      //   color: #409eff;
    }
    .el-icon-arrow-down {
      font-size: 12px;
    }
  }
  .divide_line {
    color: rgb(238, 237, 237);
  }
  .body_contents {
    width: 100%;
    padding: 10px;
    ul > li {
      display: flex;
      justify-content: flex-start;
      border-bottom: 1px solid #e5e5e5;
      padding: 12px 0;
      .right {
        width: 90%;
      }
    }
  }
  .last_item_detail {
    .pro_title {
      font-family: PingFangSC-Medium;
      font-size: 18px;
      color: #1f1f1f;
      font-weight: 500;
    }
    p {
      font-size: 14px;
      color: rgba(31, 31, 31, 0.48);
      line-height: 28px;
      font-weight: 400;
      img {
        width: 5%;
        vertical-align: text-top;
        margin-right: 10px;
      }
      .last_item_title {
        
      }
    }
  }
}
</style>
