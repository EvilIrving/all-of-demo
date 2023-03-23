<template>
  <div class='popup_map'>
    <div class="dialog_title">
      <span style="font-size: 16px;" @click="lookDetail(popupData)">{{ popupData.proName }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <p class="small_title">基本信息</p>
      <div class="top_msg">
        <div class="msg_item">
          <p>行政区划： <span>{{popupData.cityADNM}} - {{popupData.countryADNM}}</span></p>
        </div>
        <!-- <div class="msg_item">
          <p>工程规模：
            <span v-if="popupData.project_scale == '1'">大(1)型</span>
            <span v-if="popupData.project_scale == '2'">大(2)型</span>
            <span v-if="popupData.project_scale == '3'">中型</span>
            <span v-if="popupData.project_scale == '4'">小(1)型</span>
            <span v-if="popupData.project_scale == '5'">小(2)型</span>
          </p>
        </div> -->
      </div>
      <p class="small_title">隐患清单</p>
      <div class="top_list">
        <div class="list_tabs">
          <div :class="['tabs_item', tabIndex == index ? 'active' : '']" v-for="(item, index) in tabList" :key="index" @click="clickTabs(index)">
            <div>
              <img :src="item.icon" alt="">
              <p>{{item.name}}</p>
            </div>
            <span>{{item.num}}</span>
          </div>
        </div>
        <!-- <div class="list_num">
          <div :class="[yhListIndex == index && (index == 0 || index == 1) ? 'active can' : 'can', index == 2 || index == 3 ? 'cannot' : '']" v-for="(item, index) in yhListData" :key="index" @click="clickYhList(index)">
            {{item.name}}：{{item.num}}
            <i>|</i>
          </div>
        </div> -->
      </div>
      <!-- <div class="line"></div> -->
      <div class="problem_box" v-loading="loading">
        <div class="problem_list" v-for="(item, index) in problemList" :key="index">
          <div class="problem_title">
            <div class="title_left">
              <p>{{index + 1}}</p>
              <div>2020-12-23</div>
            </div>
            <div class="title_right">
              <p class="four_btn" v-if="item.sourceData == 2" @click="clickFourBtn(index)">
                四平台
                <img v-show="!item.showFour" :src="$config.imgUrl+ 'four_down.png'" alt="">
                <img v-show="item.showFour" :src="$config.imgUrl+ 'four_up.png'" alt="">
              </p>
              <div class="status1">现场检查</div>
              <div class="status2">{{item.CHECKMAIN}}</div>
              <div class="status3">{{item.problemStatus}}</div>
            </div>
          </div>
          <div class="problem_content">
            <div class="problem_content_left">
              <div class="content_title">
                <span>问题描述</span>
                <p>{{item.QUESTION || '-'}}</p>
              </div>
              <!-- 普通 -->
              <div class="check_in_live" v-show="!item.showFour">
                <div class="schedul_box" v-for="(ite,index) in item.scheduleList" :key="index">
                  <h3 class="title">{{ite.title}}</h3>
                  <img :src="ite.src" alt="">
                  <div v-show="index !== 2" class="lines"></div>
                  <div class="msg">
                    <p class="name">
                      {{ite.name}}
                      <!-- <span class="cj" v-if="ite.CHECKLEVEL">{{ite.CHECKLEVEL}}</span> -->
                    </p>
                    <p class="time">{{ite.time}}</p>
                  </div>
                </div>
              </div>
              <!-- 四平台 -->
              <div class="four_box" v-show="item.showFour">
                <div class="four_item" v-for="(fourItem,fourIndex) in fourList" :key="fourItem.detail_id">
                  <h3 class="title">{{fourItem.status}}</h3>
                  <div class="msg_box">
                    <img v-if="fourItem.handlemobile" :src="fourItem.src1" alt="">
                    <img v-else :src="fourItem.src2" alt="">
                    <div class="msg_right" v-if="fourItem.handlemobile">
                      <p class="ry_name">{{fourItem.handlerealname}}<span>{{fourItem.handleorgname}}</span></p>
                      <p class="ry_phone">{{fourItem.handlemobile}}</p>
                    </div>
                    <div class="msg_right" v-else>
                      <p class="pt_name">{{fourItem.handlerealname}}</p>
                    </div>
                  </div>
                  <p class="text">{{fourItem.dealMessage}}</p>
                  <p class="time">{{fourItem.dealtime}}</p>
                  <div v-show="fourIndex != fourList.length - 1" class="lines"></div>
                </div>
              </div>
            </div>
            <!-- <div class="problem_content_right">
              <div class="pdf">专项检查.pdf</div>
              <p>整改照片({{item.imgList.length}})</p>
              <div class="pic">
                <img v-for="(imgitem, index) in item.imgList" :key="index" :src="imgitem" alt="">
              </div>
            </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { problemApi, problemFourApi } from "@/api/api_reservoir";
export default {
  name: 'ZdyhwtPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components: {

  },
  watch: {
    popupData(val) {
      if (val.mapType == 'zdyhwtPopup') {
        this.tabIndex = 0;
        this.dealType = '';
        this.getProblemNum()
        this.getProblemList()
        this.tabIndex = 0;
        console.log('111');
      }
    }
  },
  data() {
    return {
      tabList: [
        {
          icon: this.$config.imgUrl + 'wtzs.png',
          name: '问题总数(项)',
          num: 0,
          dealType: ''
        },
        // {
        //   icon: this.$config.imgUrl + 'wkz.png',
        //   name: '未处置',
        //   num: 0,
        //   dealType: 1
        // },
        {
          icon: this.$config.imgUrl + 'zgz.png',
          name: '处置中',
          num: 0,
          dealType: 2
        },
        {
          icon: this.$config.imgUrl + 'wczg.png',
          name: '已处置',
          num: 0,
          dealType: 3
        }
      ],

      tabIndex: 0,
      yhListIndex: 0,
      yhListData: [
        {
          name: '现场检查（项）',
          num: 0
        },
        {
          name: '安全鉴定（项）',
          num: 0
        },
        {
          name: '监测分析（项）',
          num: 0
        },
        {
          name: '公众报料（项）',
          num: 0
        },

      ],
      dealType: '',
      problemList: [],
      fourList: [],
      loading: true
    };
  },
  mounted() {
  },
  methods: {
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
    async getProblemNum() {
      let res = await problemApi({
        prcd: this.popupData.prcd,
        interType: 1,
        dealType: ''
      })
      if (res.success) {
        this.tabList[0].num = res.data.totalNum
        this.tabList[1].num = res.data.dealingNum
        this.tabList[2].num = res.data.dealedNum
        this.tabList[3].num = res.data.stayNum
        this.yhListData[0].num = res.data.totalNum
      }
    },
    async getProblemList() {
      this.loading = true
      let res = await problemApi({
        prcd: this.popupData.prcd,
        interType: 2,
        dealType: this.dealType
      })
      if (res.success) {
        this.problemList = res.data
        for (let i = 0; i < this.problemList.length; i++) {
          this.problemList[i].showFour = false
          this.problemList[i].scheduleList =
            [
              {
                title: '发现',
                name: this.problemList[i].checkUser,
                time: this.problemList[i].create_time,
                src: this.$config.imgUrl + "discover.png"
              },
              {
                title: '处置',
                name: this.problemList[i].checkUserd,
                time: this.problemList[i].RESPONSETM,
                src: this.$config.imgUrl + "handle.png"
              },
              {
                title: '结束',
                name: this.problemList[i].resUser,
                time: this.problemList[i].resTm,
                src: this.$config.imgUrl + "endliu.png"
              }
            ]
        }
        console.log(this.problemList,'problemList');
      }
      this.loading = false
    },
    async getProblemFour(id) {
      this.loading = true
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
      this.fourList = []
      this.fourList = [
        {
          status: '待受理',
          handlerealname: '运管平台-度汛检查',
          handleorgname: '',
          dealMessage: '度汛检查：需要整改',
          handlemobile: '',
          dealtime: '2022-02-04 12:38:00',
          src1:this.$config.imgUrl+ "pt.png",
          src2:this.$config.imgUrl+ "pt.png"
        },
        {
          status: '办理中',
          handlerealname: '张里晓',
          handleorgname: '辉埠镇',
          handlemobile: '13757056827',
          dealMessage: '同意受理',
          dealtime: '2022-02-11 08:00:00',
          src1:this.$config.imgUrl+ "ry.png",
          src2:this.$config.imgUrl+ "ry.png"
        },
        {
          status: '办理中',
          handlerealname: '张里晓',
          handleorgname: '辉埠镇',
          handlemobile: '13757056827',
          dealMessage: '请村及时安排工作人员前往清理。',
          dealtime: '2022-02-11 14:00:00',
          src1:this.$config.imgUrl+ "ry.png",
          src2:this.$config.imgUrl+ "ry.png"
        },
        {
          status: '已办结',
          handlerealname: '刘志亮',
          handleorgname: '路里坑村',
          handlemobile: '13567075966',
          dealMessage: '村里得知情况后，已安排人员前去清理杂草，现已清理干净。',
          dealtime: '2021-02-11 18:22:00',
          src1:this.$config.imgUrl+ "ry.png",
          src2:this.$config.imgUrl+ "ry.png"
        }
      ]
      this.loading = false
    },
    clickTabs(index) {
      this.tabIndex = index
      this.dealType = this.tabList[index].dealType
      this.getProblemList()
    },
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    clickYhList(index) {
      if (index == 0 || index == 1) {
        this.yhListIndex = index
        if (index == 0) {
          this.getProblemList()
        } else {
          this.problemList = []
        }
      }
    },
    clickFourBtn(index) {
      this.problemList[index].showFour = !this.problemList[index].showFour
      this.$set(this.problemList, index, this.problemList[index])
      if (this.problemList[index].showFour) {
        this.getProblemFour(this.problemList[index].id)
      }
    }
  }
}
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  // background: #fff;
  padding: 30px 40px 40px 40px;
  @include shadow(#ccc, 0px, 0px, 2px);
  background: url($imageUrl + "sk-popup-bg.png") no-repeat
    center;
  background-size: 100% 100%;
  width: 730px;
  height: 550px;
  border-radius: 4px;
  .small_title {
    font-size: 20px;
    font-weight: 500;
    color: #ffffff;
    background: url($imageUrl + "small_title.png")
      no-repeat left 23px;
    background-size: 8px 8px;
    padding: 15px 0 15px 20px;
    position: relative;
        &:before {
          top: 22px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
  }
  .dialog_title {
    background: transparent;
    background: url($imageUrl + "sk-popup-title.png")
      no-repeat bottom;
    background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    padding-left: 16px;
    font-weight: bold;
    width: 100%;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-weight: bold;
      // font-family: youshe;
      color: #fff;
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
    height: calc(100% - 40px);
    .top_msg {
      @include flexbox();
      .msg_item {
        box-sizing: border-box;
        padding-left: 14px;
        width: 262px;
        height: 40px;
        line-height: 40px;
        margin-right: 8px;
        background: #03338d;
        border: 1px solid #0051b5;
        font-size: 14px;
        color: #ffffff;
      }
    }
    .top_list {
      .list_tabs {
        @include flexbox();
        @include flexJC(space-between);
        width: 100%;
        .tabs_item {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          height: 56px;
          background: #081c5e;
          width: 31%;
          border: 1px solid #081c5e;
          box-sizing: border-box;
          padding-right: 20px;
          cursor: pointer;
          & > div {
            @include flexbox();
            @include flexAI(center);
            img {
              width: 48px;
              height: 48px;
              margin-right: 4px;
            }
            p {
              font-size: 14px;
              font-weight: 500;
              color: #ffffff;
            }
          }
          & > span {
            font-size: 24px;
            // font-family: youshe;
            font-family: "Microsoft YaHei", 微软雅黑;
            color: #fff;
          }
          &.active {
            border: 1px solid #00f0ff;
            & > span {
              color: #69e3ee;
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
          color: #ffffff;
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
      background: url($imageUrl + "line.png") no-repeat
        center;
      background-size: 100% 100%;
      margin: 15px 0;
    }
    .problem_box {
      margin-top: 16px;
      height: calc(100% - 210px);
      overflow: auto;
      .problem_list {
        margin-bottom: 20px;
        .problem_title {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          margin-bottom: 10px;
          .title_left {
            @include flexbox();
            @include flexAI(center);
            p {
              border-radius: 2px;
              border: 1px solid #00daff;
              height: 26px;
              line-height: 26px;
              font-family: youshe;
              font-size: 24px;
              color: #00daff;
              padding: 0 8px;
              margin-right: 10px;
            }
            div {
              font-size: 20px;
              font-weight: 500;
              color: #ffffff;
              line-height: 28px;
            }
          }
          .title_right {
            @include flexbox();
            @include flexAI(center);
            .four_btn {
              width: 96px;
              height: 32px;
              background: rgba(0, 56, 160, 0.8);
              box-shadow: inset 0px 0px 16px 0px rgba(46, 228, 255, 0.5);
              border: 1px solid #448ef7;
              font-size: 14px;
              font-weight: 500;
              color: #ffffff;
              line-height: 32px;
              text-align: center;
              cursor: pointer;
              img {
                width: 18px;
                height: 12px;
                margin-left: 10px;
              }
            }
            & > div {
              // width: 88px;
              padding: 0 10px;
              height: 32px;
              background: rgba(0, 5, 21, 0.2);
              border-radius: 4px;
              font-size: 14px;
              font-weight: 500;
              color: #00daff;
              letter-spacing: 2px;
              text-align: center;
              line-height: 32px;
              margin-left: 10px;
              &.status1 {
                color: #00daff;
                background: rgba(0, 218, 255, 0.2);
              }
              &.status2 {
                color: #609dff;
                background: rgba(96, 157, 255, 0.2);
              }
              &.status3 {
                color: #fff;
                background: #03338d;
              }
            }
          }

          & > p {
            width: 64px;
            height: 32px;
            background: #f7b500;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 500;
            color: #ffffff;
            letter-spacing: 2px;
            text-align: center;
            line-height: 32px;
          }
        }
        .problem_content {
          @include flexbox();
          .problem_content_left {
            background: #03338d;
            border: 1px solid #0051b5;
            padding: 0 14px;
            width: 100%;
            box-sizing: border-box;
            .content_title {
              border-bottom: 1px solid #3390e1;
              padding: 10px 0;
              span {
                font-size: 14px;
                font-weight: 500;
                color: #999;
                letter-spacing: 2px;
              }
              p {
                font-size: 16px;
                font-weight: 500;
                color: #ffffff;
                letter-spacing: 3px;
                padding-top: 10px;
              }
            }
            .check_in_live {
              text-align: center;
              @include flexbox();
              @include flexJC(space-between);
              @include flexflow(row nowrap);
              .schedul_box {
                position: relative;
                padding: 10px 0;
                .title {
                  font-size: 16px;
                  font-family: PingFangSC-Medium, PingFang SC;
                  color: #ffffff;
                  line-height: 16px;
                  position: relative;
                  bottom: -5px;
                }
                img {
                  width: 115px;
                  height: 120px;
                }
                .lines {
                  position: absolute;
                  top: 40%;
                  left: 120px;
                  transform: translate(0% -50%);
                  width: 180px;
                  height: 3px;
                  border-radius: 3px;
                  border: 1px linear-gradient(270deg, #289bff 0%, #0c459d 100%)
                    solid;
                  background: linear-gradient(270deg, #289bff 0%, #0c459d 100%);
                }
                .msg {
                  position: relative;
                  top: -10px;
                  .name {
                    font-size: 16px;
                    color: #fff;
                    .cj {
                      height: 18px;
                      background: #00daff;
                      border-radius: 2px;
                      padding: 0 3px;
                      font-size: 14px;
                      color: #041b62;
                      margin-left: 5px;
                    }
                  }
                  .time {
                    margin-top: 5px;
                    font-size: 14px;
                    font-weight: 400;
                    color: #00daff;
                    line-height: 20px;
                  }
                }
              }
            }
            .four_box {
              text-align: center;
              // @include flexbox();
              // @include flexflow(row nowrap);
              width: 100%;
              overflow-y: hidden;
              overflow-x: auto;
              white-space: nowrap;
              box-sizing: border-box;
              .four_item {
                padding: 16px 0;
                display: inline-block;
                width: 230px;
                position: relative;
                .title {
                  font-size: 16px;
                  font-family: PingFangSC-Medium, PingFang SC;
                  color: #ffffff;
                  line-height: 16px;
                  position: relative;
                }
                .msg_box {
                  @include flexbox();
                  @include flexAI(center);
                  @include flexJC(center);
                  padding: 16px 0;
                  img {
                    width: 50px;
                    height: 50px;
                    margin-right: 10px;
                  }
                  .msg_right {
                    .pt_name {
                      text-align: left;
                      font-size: 16px;
                      line-height: 22px;
                      width: 100px;
                      height: 50px;
                      white-space: break-spaces;
                    }
                    .ry_name {
                      text-align: left;
                      font-size: 16px;
                      line-height: 22px;
                      span {
                        height: 18px;
                        background: #69e3ee;
                        border-radius: 2px;
                        padding: 0 3px;
                        color: #041b62;
                        margin-left: 5px;
                      }
                    }
                    .ry_phone {
                      text-align: left;
                      font-size: 16px;
                      line-height: 22px;
                      margin-top: 8px;
                    }
                  }
                }
                .text {
                  width: 75%;
                  height: 65px;
                  margin: 0 auto;
                  font-size: 16px;
                  color: #ffffff;
                  line-height: 22px;
                  white-space: break-spaces;
                }
                .time {
                  margin-top: 5px;
                  font-size: 14px;
                  font-weight: 400;
                  color: #00daff;
                  line-height: 20px;
                }
                .lines {
                  position: absolute;
                  top: 50%;
                  left: 200px;
                  transform: translate(0% -50%);
                  width: 50px;
                  height: 3px;
                  border-radius: 3px;
                  border: 1px linear-gradient(270deg, #289bff 0%, #0c459d 100%)
                    solid;
                  background: linear-gradient(270deg, #289bff 0%, #0c459d 100%);
                }
              }
            }
          }
          // .problem_content_right {
          //   width: 172px;
          //   margin-left: 8px;
          //   background: #03338d;
          //   border: 1px solid #0051b5;
          //   padding: 10px;
          //   box-sizing: border-box;
          //   .pdf {
          //     width: 100%;
          //     height: 40px;
          //     background: #0b2a70;
          //     font-size: 14px;
          //     font-weight: 500;
          //     color: #ffffff;
          //     line-height: 40px;
          //     text-align: center;
          //   }
          //   & > p {
          //     font-size: 14px;
          //     font-weight: 500;
          //     color: #999;
          //     line-height: 20px;
          //     letter-spacing: 2px;
          //     margin-top: 8px;
          //   }
          //   .pic {
          //   }
          // }
        }
      }
    }
  }
}
</style>