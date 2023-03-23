<template>
  <div class="wrap">
    <div class="detail-menu">
      <div
        :class="['detail-menu-item', menuIndex === index ? 'active' : '']"
        v-for="(item, index) in tabs"
        :key="item.name"
        @click="clickMenu(index)"
      >
        {{ item.title }}
      </div>
    </div>
    <div class="wrap_bodys">
      <div class="appraisal" v-if="menuIndex == 0">
        <h3>安全鉴定列表</h3>
        <!-- <div class="filter_menus">
          <div @click="showPicker = true">
            {{ fileType ? fileType : "文件类型" }}
            <van-icon name="arrow-down" />
          </div>
          <van-popup v-model="showPicker" position="bottom">
            <van-picker
              show-toolbar
              :columns="fileTypes"
              @confirm="fileClick"
              @cancel="showPicker = false"
            />
          </van-popup>

          <div class="divide_line">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</div>
          <div @click="showPickerT = true">
            {{ timeChoosed ? timeChoosed : "时间选择" }}
            <van-icon name="arrow-down" />
          </div>
          <van-popup v-model="showPickerT" position="bottom">
            <van-picker
              show-toolbar
              :columns="timeChoices"
              @confirm="timeClick"
              @cancel="showPickerT = false"
            />
          </van-popup>
        </div> -->
        <div class="body_contents">
          <ul>
            <li v-for="(item, index) in tableData" :key="index">
              <div class="left">
                <img src="../../../assets/images/PDF2.png" alt="" />
              </div>
              <div class="right last_item_detail">
                <p @click="preview(item.col1505463789074url)" class="pro_title">
                  {{ item.col1505463789074originalname }}
                </p>
                <p>
                  <img src="../../../assets/images/jianding.png" alt="" />
                  鉴定结果:{{ item.col1505463789074originalname }}
                </p>
                <p>
                  <img src="../../../assets/images/shijian.png" alt="" />
                  鉴定时间:{{ item.col1505463766726 }}
                </p>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <Survey v-if="menuIndex == 1" />
      <Drill v-if="menuIndex == 2" />
      <Patrol v-if="menuIndex == 3" />
      <Hiddendanger v-if="menuIndex == 4" />
      <Maintenance v-if="menuIndex == 5" />
    </div>
  </div>
</template>

<script>
import { tblSafetyAppraisalApi } from "@/api/projectDetail";
import Drill from "./components/Drill.vue";
import Survey from "./components/Survey.vue";
import Patrol from "./components/Patrol.vue";
import Maintenance from "./components/Maintenance.vue";
import Hiddendanger from "./components/Hiddendanger.vue";
export default {
  props: {},
  data() {
    return {
      tabs: [
        { title: "安全鉴定", name: "appraisal" },
        { title: "调度记录", name: "survey" },
        { title: "预案演练", name: "drill" },
        { title: "工程巡查", name: "patrol" },
        { title: "工程隐患", name: "hiddendanger" },
        { title: "维修养护", name: "maintenance" },
      ],
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
    clickMenu(index) {
      this.menuIndex = index;
    },
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
      let res = await tblSafetyAppraisalApi.loadPage({
        pjcd: "BZ330327000009",
        col1505463766726: this.timeChoosed,
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
  components: { Drill, Survey, Patrol, Maintenance, Hiddendanger },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .detail-menu {
    @include flexbox();
    overflow-x: auto;
    overflow-y: hidden;
    padding: 12px 24px;
    box-sizing: border-box;
    &::-webkit-scrollbar {
      display: none;
    }
    .detail-menu-item {
      white-space: nowrap;
      min-width: 96px;
      height: 34px;
      font-size: 16px;
      color: #74767a;
      text-align: center;
      line-height: 32px;
      font-weight: 500;
      background: #eff3fe;
      border-radius: 4px;
      margin-right: 16px;
      &.active {
        background: #eff3fe;
        color: #036eff;
      }
      &:last-child {
        margin-right: 0;
      }
    }
  }
  .wrap_bodys {
    width: calc(100% - 48px);
    padding: 0px 24px;
    margin: 0 auto;
    h3 {
      width: calc(100% - 20px);
      font-size: 20px;
      font-weight: 700;
      padding: 10px 0;
    }
    .appraisal {
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
          .left {
            width: 20%;
            img {
              width: 60%;
            }
          }
          .right {
            width: 80%;
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
        & > div {
          p {
            font-size: 14px;
            color: rgba(31, 31, 31, 0.48);
            line-height: 28px;
            font-weight: 400;
            img {
              width: 20%;
              vertical-align: text-top;
              margin-right: 10px;
            }
          }
        }
      }
    }
  }
}
</style>
