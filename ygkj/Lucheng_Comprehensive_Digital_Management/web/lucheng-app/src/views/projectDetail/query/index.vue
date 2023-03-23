<template>
  <div
    class="wrap"
    :style="{
      height: menuIndex == 4 ? '100%' : 'auto',
      'overflow-y': menuIndex == 4 ? 'hidden' : 'auto',
    }"
  >
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
    <div :class="['wrap_bodys', menuIndex == 4 ? 'wrap_bodys_panorama' : '']">
      <div class="overview" v-if="menuIndex == 0">
        <h3>总体概况</h3>
        <div class="body_contents">
          <img :src="imgURL" alt="" />
          <span class="article">
            {{ overallProfile }}
          </span>
        </div>
      </div>
      <div class="event" v-if="menuIndex == 1">
        <div class="choose_time">
          <div class="choose_years">
            <van-icon name="arrow-left" @click="preYear" />
            <span>{{ year }}</span>
            <van-icon name="arrow" @click="nextYear" />
          </div>
          <van-tabs
            v-model="chooseMonth"
            class="choose_month"
            line-width="0"
            line-height="0"
            @click="checkDot"
            title-active-color="#1989fa"
          >
            <van-tab
              v-for="item in eventTabs"
              :key="item.title"
              :title="item.title"
              title-style="month_title"
              >{{ item.title + "月" }}</van-tab
            >
          </van-tabs>
        </div>
        <div class="event_line">
          <el-timeline :reverse="true">
            <el-timeline-item
              v-for="(activity, index) in textList"
              type="success"
              color="#036eff"
              :key="index"
              :timestamp="activity.bigEvent"
            >
              <span class="time_title">{{ activity.date }}</span>
              {{ activity.name }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div class="archives" v-if="menuIndex == 2">
        <div class="back_list">
          <h3 v-if="fileList.length">全部文件</h3>
          <h4 @click="backFiles" v-if="this.preBtn.length - 1 > 0">
            返回上一级
          </h4>
        </div>
        <ul class="top_file" v-if="fileList.length">
          <li
            v-for="(item, index) in fileList"
            ref="fileTree"
            :key="index"
            @click="getFileTree(item)"
          >
            <img src="../../../assets/images/wenjianjia.png" alt="" />
            <span class="van-multi-ellipsis--l2">{{ item.name }}</span>
          </li>
        </ul>
        <div class="appraisal" v-if="tableData.length">
          <h3>{{ filesName }}文件列表</h3>
          <div class="body_contents">
            <ul>
              <li v-for="(item, index) in tableData" :key="index">
                <div class="left">
                  <img src="../../../assets/images/PDF2.png" alt="" />
                </div>
                <div class="right last_item_detail">
                  <p @click="preview(item.fileUrl)" class="pro_title">
                    {{ item.fileName }}
                  </p>
                  <p>
                    <img src="../../../assets/images/jianding.png" alt="" />
                    归档编号:{{ item.fileCode }}
                  </p>
                  <p>
                    <img src="../../../assets/images/shijian.png" alt="" />
                    上传时间:{{ item.updateTime }}
                  </p>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="video" v-if="menuIndex == 3">
        <Video />
      </div>
      <div class="panorama" v-if="menuIndex == 4">
        <Panorama></Panorama>
      </div>
    </div>
  </div>
</template>

<script>
import {
  SurveyApi,
  bigEventApi,
  getFileTree,
  getUpFileListApi,
} from "@/api/projectDetail";

import Panorama from "../look/components/PanoramaNew.vue";
import Video from "../look/components/Video.vue";
export default {
  props: {},
  data() {
    return {
      tabs: [
        { title: "概况", name: "overview" },
        { title: "大事件", name: "event" },
        { title: "档案", name: "archives" },
        { title: "视频", name: "video" },
        { title: "全景图", name: "panorama" },
      ],
      imgURL: require("../../../assets/images/qiaodun.jpeg"),
      overallProfile: "桥墩水库位于鳌江流域南港的最",
      eventTabs: [
        { title: "01", value: "月" },
        { title: "02", value: "月" },
        { title: "03", value: "月" },
        { title: "04", value: "月" },
        { title: "05", value: "月" },
        { title: "06", value: "月" },
        { title: "07", value: "月" },
        { title: "08", value: "月" },
        { title: "09", value: "月" },
        { title: "10", value: "月" },
        { title: "11", value: "月" },
        { title: "12", value: "月" },
      ],
      preFilesId: "",
      preName: "",
      fileList: [],
      menuIndex: 0,
      year: 2021,
      yearIndex: 0,
      yearList: [],
      chooseMonth: 1,
      dataList: [],
      timeList: [],
      textList: [],
      selectFile: "",
      selectFileId: "",
      selectFileName: "",
      expandedKeys: [],
      achievementId: 0,
      flag: true,
      tableData: [],
      filesName: "",
      preBtn: [],
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$toast("暂无报告");
      }
    },
    async backFiles() {
      this.preBtn.pop();
      let res = await getFileTree({
        parentId: this.preBtn[this.preBtn.length - 1].id,
        projectCode: this.preBtn[this.preBtn.length - 1].projectCode,
      });
      this.achievementId = this.preBtn[this.preBtn.length - 1].id;
      if (res.code == 0) {
        this.flag = false;
        this.fileList = res.data.list;
        this.filesName = this.preBtn[this.preBtn.length - 1].name;
      }
      this.getFileList();
    },
    clickMenu(index) {
      this.menuIndex = index;
      if (index == 0) {
        this.flag = true;
        this.getOverviewList();
      } else if (index == 1) {
        this.flag = true;
        this.getBigEventList();
      } else {
        this.preBtn.pop();
        this.getFileTree({ id: 0, name: "全部" });
      }
    },
    monthClick(index, title) {
      this.checkDot();
    },
    async getOverviewList() {
      let opt = {
        //   projCode:this.info.depId
        pjcd: this.$localData("get", "projectCode"),
      };
      let res = await SurveyApi.getSurvey(opt);
      if (res.code === 0) {
        this.overallProfile = res.data.list[0]
          ? res.data.list[0].overallProfile
          : "桥墩水库位于鳌江流域南港的最";
        this.imgURL = require(res.data.list[0]);
      }
    },
    async getBigEventList() {
      let opt = {
        //   projCode:this.info.depId
        pjcd: this.$localData("get", "projectCode"),
      };
      let res = await bigEventApi.getBigEventList(opt);
      if (res.code === 0) {
        this.dataList = res.data.list;
        this.dataList.sort(function (a, b) {
          return b.time < a.time ? 1 : -1;
        });
        let map = new Map();
        this.dataList.forEach((item) => {
          item.time = item.time.replace(/\-/g, ".").substring(0, 10);
          map.set(item.time.substring(0, 7), item.time);
        });
        let yearMap = new Map();
        this.dataList.forEach((item) => {
          item.time = item.time.replace(/\-/g, ".").substring(0, 10);
          yearMap.set(item.time.substring(0, 4), item.time);
        });
        map.forEach((value, key) => {
          let data = {};
          data["time"] = value;
          data["timeTitle"] = key;
          this.timeList.push(data);
        });
        yearMap.forEach((value) => {
          let data = {};
          data["year"] = value.substring(0, 4);
          this.yearList.push(data);
        });
        this.checkDot();
      }
    },
    preYear() {
      if (this.yearIndex <= 0) {
        this.yearIndex = this.yearList.length - 1;
        this.year = this.yearList[this.yearList.length - 1].year;
      } else {
        this.yearIndex--;
        this.year = this.yearList[this.yearIndex].year;
      }
      this.checkDot();
    },
    nextYear() {
      if (this.yearIndex >= this.yearList.length - 1) {
        this.yearIndex = 0;
        this.year = this.yearList[0].year;
      } else {
        this.yearIndex++;
        this.year = this.yearList[this.yearIndex].year;
      }
      this.$nextTick(() => {
        this.checkDot();
      });
    },
    checkDot() {
      this.textList = [];
      let arr = [];
      for (let item of this.dataList) {
        let res = {};
        res["year"] = item.time.substring(0, 4);
        res["date"] = item.time.substring(5);
        res["month"] = item.time.substring(5, 7);
        res["name"] = item.projName;
        res["bigEvent"] = item.bigEvent.slice(0, item.bigEvent.length - 1);
        arr.push(res);
      }

      arr.map((item) => {
        if (item.year == this.year && item.month == this.chooseMonth + 1) {
          this.textList.push(item);
        }
      });
    },
    //  获取文件树
    async getFileTree(item) {
      this.preBtn.push({
        id: item.id,
        name: item.name,
        projectCode: this.flag ? -1 : this.$localData("get", "projectCode"),
      });
      this.achievementId = item.id;
      let res = await getFileTree({
        parentId: item.id,
        projectCode: this.flag ? -1 : this.$localData("get", "projectCode"),
      });
      if (res.code == 0) {
        this.flag = false;
        this.fileList = res.data.list;
        this.filesName = item.name;
      }
      this.getFileList();
    },
    //获取文件列表
    async getFileList() {
      await getUpFileListApi({
        achievementId: this.achievementId == 0 ? "" : this.achievementId,
        fetchAll: true,
      }).then((res) => {
        if (res.code == 0) {
          if (res.data.list.length > 0) {
            res.data.list.map((item) => {
              if (item.achievementName) {
                let achievementName = item.achievementName.split("/");
                if (achievementName.length == 1) {
                  item.achievementName = achievementName.join("");
                } else if (!achievementName[achievementName.length - 1]) {
                  achievementName.pop();
                  console.log(achievementName);
                  item.achievementName = achievementName.join("/");
                } else {
                  item.achievementName = achievementName.join("/");
                }
              }
            });
          }
          this.tableData = res.data.list;
        }
      });
    },
  },
  components: { Panorama, Video },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include flexbox();
  flex-direction: column;
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
      background: #f6f6f6;
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

    &_panorama {
      flex: 1;
      .panorama {
        height: 100%;
      }
    }
    h3 {
      width: calc(100% - 80px);
      font-size: 20px;
      font-weight: 700;
      padding: 10px 0;
    }
    .overview {
      width: 100%;
      .body_contents {
        width: 100%;
        margin: 0 auto;
        img {
          width: 100%;
          padding: 5px 0;
        }
        span {
          line-height: 1.5;
          word-spacing: 3px;
          letter-spacing: 2px;
          font-size: 14px;
          font-weight: 500;
        }
      }
    }
    .event {
      width: 100%;
      .choose_time {
        .choose_years {
          @include flexbox();
          @include flexJC(space-between);
          @include flexAI(center);
        }
        .choose_month
          .van-tabs__wrap
          > .van-tabs__nav--line.van-tabs__nav--complete {
          padding: 0;
        }
        .choose_month .van-tab__pane {
          //   background: #eff3fe;
          //   color: #036eff;
          font-size: 20px;
          font-weight: 600;
        }
      }
      .event_line {
        margin-left: 70px;
        padding: 16px 0;
        .time_title {
          position: absolute;
          left: -40px;
        }
      }
    }
    .archives {
      width: 100%;
      .back_list {
        // display: flex;
        // justify-content: space-between;
        // align-items: center;
        h3 {
          float: left;
        }
        h4 {
          float: right;
          color: #036eff;
        }
      }
      .van-multi-ellipsis--l2 {
        display: -webkit-box;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
      }
      .top_file {
        width: 100%;
        @include flexbox();
        @include flexJC(flex-start);
        @include flexAI(center);
        flex-wrap: wrap;
        li {
          width: calc(33.333% - 20px);
          padding: 5px;
          @include flexbox();
          @include flexJC(center);
          @include flexAI(center);
          flex-direction: column;
          img {
            width: 100%;
          }
          span {
            padding: 10px 0;
          }
        }
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
}
</style>
