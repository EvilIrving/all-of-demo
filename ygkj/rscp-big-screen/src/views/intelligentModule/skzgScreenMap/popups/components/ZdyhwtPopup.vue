<!--
 * @Date: 2022-04-28 14:46:32
 * @Author: 
 * @Description: ZdyhwtPopup
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-25 10:12:04
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\ZdyhwtPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor: pointer" @click="lookDetail(popupData)">{{
        popupData.proName
      }}</span>
      <span style="cursor: pointer" @click="lookDetail(popupData)">&nbsp;{{ popupData.scale }}</span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <section class="work_left">
        <div class="right_content">
          <p class="third_title">
            <!-- <img class="point" :src="$config.imgUrl + 'point.png'" alt="" /> -->
            <span style="font-size: 16px; color: #333;padding-left: 22px;padding-bottom:10px;">隐患清单</span>
            <!-- <i></i>
            <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" /> -->
          </p>
        </div>
        <div class="top_list" ref="query">
          <div class="list_tabs">
            <div :class="['tabs_item', tabIndex == index ? 'active' : '']" v-for="(item, index) in tabList" :key="index" @click="clickTabs(index)">
              <div>
                <img :src="item.icon" alt="" />
                <p>{{ item.name }}</p>
              </div>
              <p>{{ item.num }}</p>
            </div>
          </div>
        </div>
        <div ref="tb_wrappers" class="sk_table_wrapper key_table_wrapper whitestyle" style="margin-top: 10px">
          <el-table highlight-current-row border stripe v-loading="loading" height="400px" :data="siteList" style="width: 100%" @row-click="clickProject">
            <el-table-column prop="CHECKMAIN" label="检查主体" show-overflow-tooltip align="center" width="80px"></el-table-column>
            <el-table-column prop="TM" label="时间" align="center" width="80px"></el-table-column>
            <el-table-column prop="checktype" label="检查类型" align="center" min-width="80px"></el-table-column>
            <el-table-column label="级别" align="center" width="49px">
              <template slot-scope="scope">
                <span v-if="scope.row.PROBLEMLEVEL === '一般'" class="yellow">一般</span>
                <span v-if="scope.row.PROBLEMLEVEL === '较重'" class="orange">较重</span>
                <span v-if="scope.row.PROBLEMLEVEL === '严重'" class="red">严重</span>
              </template>
            </el-table-column>
            <el-table-column :label="'结果'" align="center" width="49px">
              <template slot-scope="scope">
                <i v-if="scope.row.statusStr === '已处置'" class="disposed"></i>
                <i v-if="scope.row.statusStr === '处置中'" class="disposeing"></i>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>
      <div class="line"></div>
      <section class="work_right" v-loading="loading">
        <p class="third_title">
          <!-- <img class="point" :src="$config.imgUrl + 'point.png'" alt="" /> -->
          <span style="font-size: 16px; color: #333;padding-left:25px">处置流程闭环图</span>
          <!-- <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" /> -->
        </p>
        <!-- 流程闭环图 -->
        <div class="closed_loop_diagram">
          <ul>
            <li>
              <section>
                <i class="discover"></i>
                <span>发现</span>
              </section>
              <i class="lines"></i>
              <section>
                <p>上传人：{{ infoDetail.checktbUser }}</p>
                <p>{{ infoDetail.TM || "-" }}</p>
              </section>
              <div class="textone" :class="infoDetail.cuttentState && infoDetail.cuttentState > 1 ? '' : 'black'">
                上报后流转至处置人
              </div>
              <img v-if="infoDetail.cuttentState > 1" class="lingsone" :src="$config.imgUrl + 'lujing01.png'" alt="" />
              <img v-else class="lingsone" :src="$config.imgUrl + 'lujing01-black.png'" alt="" />
            </li>
            <li>
              <section>
                <i v-if="infoDetail.cuttentState > 1" class="handle"></i>
                <i v-else class="handle-black"></i>
                <span :class="infoDetail.cuttentState > 1 ? '' : 'black'">处置</span>
              </section>
              <i class="lines"></i>
              <section :class="infoDetail.cuttentState > 1 ? '' : 'black'">
                <p>处置人：{{ infoDetail.checkczUser }}</p>
                <p>{{ infoDetail.RESPONSETM || "-" }}</p>
              </section>
              <div class="textone" :class="infoDetail.cuttentState > 2 ? '' : 'black'">
                处置后流转至审核人
              </div>
              <img v-if="infoDetail.cuttentState > 2" class="lingsone" :src="$config.imgUrl + 'lujing01.png'" alt="" />
              <img v-else class="lingsone" :src="$config.imgUrl + 'lujing01-black.png'" alt="" />
            </li>
            <li>
              <section>
                <i v-if="infoDetail.cuttentState > 2" class="handle"></i>
                <i v-else class="handle-black"></i>
                <span :class="infoDetail.cuttentState > 2 ? '' : 'black'">结束</span>
              </section>
              <i class="lines"></i>
              <section :class="infoDetail.cuttentState > 2 ? '' : 'black'">
                <p>整改人：{{ infoDetail.resUser }}</p>
                <p>{{ infoDetail.resTm || "-" }}</p>
              </section>
            </li>
            <div class="texttwo" :class="infoDetail.cuttentState > 2 ? '' : 'black'">
              上传人可通过系统查看问题处置结果;
            </div>
            <div class="textthree" :class="infoDetail.cuttentState > 2 ? '' : 'black'">
              问题处置结束，结果将反馈至上传人
            </div>
            <img v-if="infoDetail.cuttentState > 2" class="lingstwo" :src="$config.imgUrl + 'lujing02.png'" alt="" />
            <img v-else class="lingstwo" :src="$config.imgUrl + 'lujing02-black.png'" alt="" />
          </ul>
        </div>
        <p class="third_title" style="margin: 5px">
          <!-- <img class="point" :src="$config.imgUrl + 'point.png'" alt="" /> -->
          <span style="font-size: 16px;color:#333;padding-left:25px">处置详情</span>
          <!-- <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" /> -->
        </p>
        <div class="detail_box">
          <p style="padding: 10px 10px 0px 10px">
            行政区划：
            <span>{{ popupData.cityADNM }} - {{ popupData.countryADNM }}</span>
          </p>
          <el-timeline style="padding: 10px; height: calc(100% - 49px); overflow: auto">
            <!-- 处置中 -->
            <el-timeline-item color="#2952D0;" :timestamp="infoDetail.TM" placement="top" class="timeline-item">
              <div class="card_box">
                <p>{{ infoDetail.QUESTION || "-" }}</p>
                <!-- <div class="fileUrl">
                  <el-image v-show="infoDetail.RESPONSEPHOTOS"  :src="item.filurl" v-for="(item,index) in infoDetail.RESPONSEPHOTOS" :key="index">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </div> -->
                <div class="info">
                  <i class="person"></i>
                  <span>{{ infoDetail.checktbUser }} </span>&nbsp;<span style="color: #ccc" v-show="infoDetail.checktbUser">|</span>&nbsp;<span> {{ infoDetail.checktbUserphone }}</span>
                </div>
              </div>
            </el-timeline-item>
            <el-timeline-item v-show="infoDetail.cuttentState > 1" color="#2952D0;" :timestamp="infoDetail.RESPONSETM" placement="top" class="timeline-item">
              <div class="card_box">
                <p>{{ infoDetail.RESPONSEMETHOD || "-" }}</p>
                <!-- <div class="fileUrl">
                  <el-image v-show="infoDetail.RESPONSEPHOTOS"  :src="item.filurl" v-for="(item,index) in infoDetail.RESPONSEPHOTOS" :key="index">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </div> -->
                <div class="info">
                  <i class="person"></i>
                  <span>{{ infoDetail.checkczUser }} </span>&nbsp;<span style="color: #ccc" v-show="infoDetail.checkczUser">|</span>&nbsp;<span> {{ infoDetail.checkczUserphone }}</span>
                </div>
              </div>
            </el-timeline-item>
            <!-- 四平台 -->
            <!-- <el-timeline-item color="#00cfff" :timestamp="infoDetail.create_time" placement="top" class="timeline-item">
              <div class="card_box_second">
                <p> <span class="fourPlatform">四平台</span> {{ infoDetail.RESPONSEMETHOD }}</p>
                <div class="info">
                  <i class="person"></i> <span>{{ infoDetail.RESPONSEUSER }} </span>&nbsp;<span style="color:#ccc">|</span>&nbsp;<span> {{ infoDetail.RESPONSETM }}</span>
                </div>
              </div>
            </el-timeline-item> -->
            <!-- 审核通过 -->
            <el-timeline-item v-show="infoDetail.cuttentState > 2" color="#2952D0;" :timestamp="infoDetail.resTm" placement="top" class="timeline-item">
              <div class="card_box">
                <p>{{ infoDetail.resContent || "-" }}</p>
                <div class="info">
                  <i class="person"></i>
                  <span>{{ infoDetail.resUser }} </span>&nbsp;<span style="color: #ccc" v-show="infoDetail.resUser">|</span>&nbsp;<span> {{ infoDetail.resUserPhone }}</span>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
        <!-- <p class="third_title">
          <img class="point" :src="$config.imgUrl+ 'point.png'" alt="">
          <span style="font-size:16px">相关附件</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
        </p>
        <div class="fileUrl">
          <el-image :src="item" v-for="(item,index) in infoDetail.RESPONSEPHOTOS" :key="index">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <img alt="" :src="item" v-for="(item,index) in infoDetail.RESPONSEPHOTOS" :key="index">
        </div> -->
      </section>
    </div>
  </div>
</template>

<script>
import { problemApi, problemFourApi } from "@/api/api_reservoir";
export default {
  name: "ZdyhwtPopup",
  props: {
    popupData: {
      type: Object,
      required: true,
    },
  },
  components: {},
  data() {
    return {
      tabList: [
        {
          icon: this.$config.imgUrl + "wtzs.png",
          name: "问题总数",
          num: 0,
          dealType: "",
        },
        {
          icon: this.$config.imgUrl + "zgz.png",
          name: "处置中",
          num: 0,
          dealType: 2,
        },
        {
          icon: this.$config.imgUrl + "wczg.png",
          name: "已处置",
          num: 0,
          dealType: 3,
        },
      ],
      tabIndex: 0,
      yhListIndex: 0,
      dealType: "",
      problemList: [],
      fourList: [],
      loading: true,
      siteList: [],
      infoDetail: {},
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == "zdyhwtPopup") {
        this.tabIndex = 0;
        this.dealType = "";

        this.getProblemNum();
        this.getProblemList();
      }
    },
  },
  methods: {
    clickProject(row) {
      this.infoDetail = row;
    },
    resizeTable() {
      this.$nextTick(function () {
        const height =
          this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
        this.tableHeight = height;
      });
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
    async getProblemNum() {
      let res = await problemApi({
        prcd: this.popupData.prcd,
        interType: 1,
        dealType: "",
      });
      if (res.success) {
        this.tabList[0].num = res.data.totalNum;
        this.tabList[1].num = res.data.dealingNum;
        this.tabList[2].num = res.data.dealedNum;
      }
    },
    async getProblemList() {
      this.infoDetail = {};
      this.loading = true;
      let res = await problemApi({
        prcd: this.popupData.prcd,
        interType: 2,
        dealType: this.dealType,
      });
      if (res.success && res.data.length !== 0) {
        this.siteList = res.data;
        for (let i = 0; i < this.siteList.length; i++) {
          this.siteList[i].showFour = false;
          this.siteList[i].RESPONSEPHOTOS = 5;
        }
        this.infoDetail = res.data[0];
      } else {
        this.siteList = [];
        this.infoDetail = {};
      }
      this.loading = false;
    },
    async getProblemFour(id) {
      // this.loading = true
      // let res = await problemFourApi({
      //   id: id
      // })
      // if(res.success){
      //   res.data.map(v=>{
      //     v.src1 =this.$config.imgUrl+ "ry.png"
      //     v.src2 =this.$config.imgUrl+ "pt.png"
      //   })
      //   this.fourList = res.data
      // }
      this.fourList = [];
      this.fourList = [
        {
          status: "待受理",
          handlerealname: "运管平台-度汛检查",
          handleorgname: "",
          dealMessage: "度汛检查：需要整改",
          handlemobile: "",
          dealtime: "2022-02-04 12:38:00",
          src1: this.$config.imgUrl + "pt.png",
          src2: this.$config.imgUrl + "pt.png",
        },
        {
          status: "办理中",
          handlerealname: "张里晓",
          handleorgname: "辉埠镇",
          handlemobile: "13757056827",
          dealMessage: "同意受理",
          dealtime: "2022-02-11 08:00:00",
          src1: this.$config.imgUrl + "ry.png",
          src2: this.$config.imgUrl + "ry.png",
        },
        {
          status: "办理中",
          handlerealname: "张里晓",
          handleorgname: "辉埠镇",
          handlemobile: "13757056827",
          dealMessage: "请村及时安排工作人员前往清理。",
          dealtime: "2022-02-11 14:00:00",
          src1: this.$config.imgUrl + "ry.png",
          src2: this.$config.imgUrl + "ry.png",
        },
        {
          status: "已办结",
          handlerealname: "刘志亮",
          handleorgname: "路里坑村",
          handlemobile: "13567075966",
          dealMessage: "村里得知情况后，已安排人员前去清理杂草，现已清理干净。",
          dealtime: "2021-02-11 18:22:00",
          src1: this.$config.imgUrl + "ry.png",
          src2: this.$config.imgUrl + "ry.png",
        },
      ];
      // this.loading = false
    },
    clickTabs(index) {
      this.tabIndex = index;
      this.dealType = this.tabList[index].dealType;
      // if (index !== 0) {
      this.getProblemList();
      // }
    },
    closePopup() {
      this.$parent.$parent.closeAllOverly();
    },
    clickFourBtn(index) {
      this.problemList[index].showFour = !this.problemList[index].showFour;
      this.$set(this.problemList, index, this.problemList[index]);
      if (this.problemList[index].showFour) {
        this.getProblemFour(this.problemList[index].id);
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  background-color: #fff;
  padding: 30px 20px 40px 20px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  // background-size: 100% 100%;
  width: 920px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    // background: url($imageUrl + "sk-popup-title.png") no-repeat bottom;
    // background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    // padding-left: 16px;
    font-weight: bold;
    width: 96%;
    margin: 0 10px 16px;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-weight: bold;
      // font-family: youshe;
      color: #333;
    }
    & > i {
      position: absolute;
      top: 20px;
      right: 40px;
      width: 20px;
      height: 20px;
      color: #999;
      font-size: 20px;
      cursor: pointer;
      background: url($imageUrl + "closeImg.png") no-repeat center;
      background-size: 100%;
      z-index: 10;
    }
  }
  .dialog_body {
    width: 100%;
    height: 500px;
    @include flexbox();
    .work_left {
      width: 42%;
      height: 100%;
      margin: 0 2px;
      .top_list {
        .list_tabs {
          @include flexbox();
          @include flexJC(space-around);
          width: 100%;
          .tabs_item {
            @include flexbox();
            @include flexAI(center);
            @include flexJC(space-around);
            flex-direction: column;
            height: 66px;
            background: rgba(244, 245, 248, 1);
            width: 30%;
            // border: 0px solid rgba(41, 82, 208, 1);
            box-sizing: border-box;
            cursor: pointer;
            & > div {
              @include flexbox();
              @include flexAI(center);
              img {
                width: 30px;
                height: 30px;
              }
              p {
                font-size: 14px;
                font-weight: 500;
                color: #333;
              }
            }
            & > p {
              font-size: 24px;
              font-family: "youshe";
              color: #333;
            }
            &.active {
              background: rgba(41, 82, 208, 0.1);
              width: 30%;
              // border: 1px solid rgba(41, 82, 208, 1);
              border-radius: 2px;
              & > span {
                color: #46fdff;
              }
            }
          }
        }
        .list_num {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          height: 40px;
          background: #03338d;
          border-radius: 4px;
          border: 1px solid #0051b5;
          margin-top: 10px;
          overflow: hidden;
          & > div {
            text-align: center;
            width: 25%;
            height: 40px;
            font-size: 14px;
            font-weight: 500;
            color: #333;
            letter-spacing: 2px;
            line-height: 40px;
            position: relative;
            &.active {
              background: #052176;
            }
            &.can {
              cursor: pointer;
            }
            &.cannot {
              cursor: not-allowed;
            }
            & > i {
              position: absolute;
              right: -4px;
              top: 3px;
              font-size: 24px;
              color: #041b62;
            }
          }
        }
      }
      .line {
        width: 100%;
        height: 5px;
        // background: url($imageUrl + "line.png") no-repeat center;
        // background-size: 100% 100%;
        margin: 15px 0;
      }
    }
    .line {
      width: 2px;
      height: 100%;
      // background: url($imageUrl + "work_line.png") no-repeat center;
      // background-size: auto 100%;
    }
    .work_right {
      width: calc(60% - 16px);
      box-sizing: border-box;
      padding: 0 10px 0 5px;
      height: 100%;
      // overflow-y: scroll;
      // overflow-x: hidden;
      .closed_loop_diagram {
        width: calc(100% - 16px);
        height: 150px;
        margin: 0px 0px 8px 8px;
        .black {
          color: rgba(172, 179, 198, 1);
        }
        ul {
          width: 100%;
          height: 80%;
          @include flexbox();
          @include flexAC();
          @include flexJC(space-between);
          position: relative;
          li {
            width: 22%;
            height: 70%;
            // background: rgba(0, 56, 160, 0.8);
            // box-shadow: inset 0px 0px 16px 0px rgba(46, 197, 255, 0.3);
            border-radius: 8px;
            border: 2px solid #ccc;
            @include flexbox();
            flex-direction: column;
            position: relative;

            i.lines {
              display: inline-block;
              height: 1px;
              border-bottom: 1px solid #ccc;
              margin: 0 3px;
            }
            section {
              width: 100%;
              padding: 5px 10px;
              p {
                color: #333;
              }
              i.discover {
                display: inline-block;
                width: 20px;
                height: 20px;
                margin-right: 10px;
                background: url($imageUrl + "discover-question.png") no-repeat
                  center;
                background-size: auto 100%;
              }
              i.handle {
                display: inline-block;
                width: 20px;
                height: 20px;
                margin-right: 10px;
                background: url($imageUrl + "handle-question.png") no-repeat
                  center;
                background-size: auto 100%;
              }
              i.handle-black {
                display: inline-block;
                width: 20px;
                height: 20px;
                margin-right: 10px;
                background: url($imageUrl + "handle-question-black.png")
                  no-repeat center;
                background-size: auto 100%;
              }
              span {
                font-size: 20px;
                font-family: youshe;
                color: rgba(41, 82, 208, 1);
                line-height: 26px;
              }
              &:nth-of-type(1) {
                @include flexbox();
                @include flexAC();
              }
              &:nth-of-type(2) {
                @include flexbox();
                @include flexAC();
                flex-direction: column;
                p {
                  width: 100%;
                  margin: 2px auto;
                }
              }
            }
            img.lingsone {
              // background: url($imageUrl + "lujing01.png") no-repeat center;
              // background-size: auto 100%;
              width: 60%;
              position: absolute;
              top: 45%;
              left: 110%;
            }
            div.textone {
              // background: url($imageUrl + "lujing01.png") no-repeat center;
              // background-size: auto 100%;
              width: 60px;
              position: absolute;
              top: 15%;
              left: 110%;
              font-family: youshe;
              color: rgba(25, 183, 135, 1);
              &.black {
                color: rgba(172, 179, 198, 1);
              }
            }
          }
          img.lingstwo {
            width: 80%;
            position: absolute;
            top: 90%;
            left: 10%;
          }
          div.texttwo {
            font-family: youshe;
            color: rgba(25, 183, 135, 1);
            width: 100%;
            position: absolute;
            top: 95%;
            left: 30%;
            &.black {
              color: rgba(172, 179, 198, 1);
            }
          }
          div.textthree {
            font-family: youshe;
            color: rgba(25, 183, 135, 1);
            width: 100%;
            position: absolute;
            top: 105%;
            left: 30%;
            &.black {
              color: rgba(172, 179, 198, 1);
            }
          }
        }
      }
      .detail_box {
        // background: #03338d;
        border-radius: 2px;
        border: 1px solid #ccc;
        height: calc(100% - 210px);
        width: calc(100% - 16px);
        // overflow-y: auto;
        color: #333;
        .card_box,
        .card_box_second {
          color: #333;
          p {
            line-height: 18px;
            font-size: 14px;
          }

          .info {
            @include flexbox();
            @include flexAC();
            font-size: 14px;
            margin-top: 5px;
            i.person {
              display: inline-block;
              width: 16px;
              height: 16px;
              background: url($imageUrl + "personnal-tx.png") no-repeat center;
              background-size: auto 100%;
              margin-right: 5px;
            }
          }
        }
        .card_box_second {
          background: rgba(#000515, 0.2);
          border-radius: 4px;
          .fourPlatform {
            background: #1990ff;
            border-radius: 4px;
            font-size: 14px;
            line-height: 16px;
            padding: 2px 5px;
            color: #333;
          }
        }
      }
      .fileUrl {
        margin-top: 5px;
        @include flexbox();
        @include flexAC();
        @include flexJC(flex-start);
        flex-wrap: wrap;
        img {
          width: 20%;
          margin-right: 5px;
          border: 1px solid transparent;
          border-radius: 2px;
          background: "";
        }
      }
    }
  }
}
.red {
  color: #940000;
}
.yellow {
  color: #ffb200;
}
.orange {
  color: #ff210b;
}
.disposeing {
  display: inline-block;
  width: 16px;
  height: 16px;
  background: url($imageUrl + "yellowresult.png") no-repeat center;
  background-size: auto 100%;
}
.disposed {
  // background: rgba(#f7b500, 0.2);
  // border-radius: 4px;
  // line-height: 20px;
  // padding: 2px 5px;
  // color: #f7b500;
  display: inline-block;
  width: 16px;
  height: 16px;
  background: url($imageUrl + "greenresult.png") no-repeat center;
  background-size: auto 100%;
}
</style>
<style lang="scss">
.el-timeline-item__timestamp {
  color: #333;
}
</style>