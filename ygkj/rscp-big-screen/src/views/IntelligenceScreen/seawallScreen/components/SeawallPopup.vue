<!--
 * @Date: 2023-01-04 15:05:52
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2023-01-05 18:32:29
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\components\SeawallPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="proj_content">
      <div class="dialog_title">
        <!-- 海塘信息 -->
        <span style="cursor:pointer;" @click="lookDetail(pointData)">{{ pointData.name }}</span>
        <span style="font-size: 16px;" @click="lookDetail(pointData)"> {{pointData.scale}}</span>
        <div class="title_img_box">
          <img class="title_img" @click="lookStatus(pointData.threeYards)" v-if="pointData.threeYards && pointData.threeYards == '红色'" :src="$config.imgUrl + 'status-red.png'" alt="">
          <img class="title_img" @click="lookStatus(pointData.threeYards)" v-if="pointData.threeYards && pointData.threeYards == '黄色'" :src="$config.imgUrl + 'status-yellow.png'" alt="">
          <img class="title_img" v-if="pointData.threeYards && pointData.threeYards == '绿色'" :src="$config.imgUrl + 'status-green.png'" alt="">
        </div>
        <i class="el-icon-close" @click="closePopup()"></i>
      </div>
      <!-- 上部分 -->
      <div class="proj_top">
        <!-- 上部分-左侧 -->
        <div class="proj_left">
          <!-- 内容 -->
          <div class="proj_title">
            <p><span>{{pointData.cityADNM}} - {{pointData.countryADNM}}</span></p>
          </div>
          <div class="proj_detail">
            <!-- <div class="popup_item">
              <p>潮位：</p>
              <span>{{
                  pointData.waterLevel
                    ? pointData.waterLevel.toFixed(2) + "m"
                    : "- "
                }} </span>
            </div>
            <div class="popup_item">
              <p>更新时间：</p>
              <span>{{ pointData.updateTime ? pointData.updateTime : "-" }}</span>
            </div> -->
            <div class="popup_item">
              <p>海塘类型：</p>
              <span>{{ pointData.seawall_ct_type ? pointData.seawall_ct_type : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>海塘形式：</p>
              <span>{{
                pointData.seawall_pattern
                  ? pointData.seawall_pattern
                  : "-"
              }}</span>
            </div>

            <div class="popup_item">
              <p>跨界形式：</p>
              <span>{{
                pointData.dts
                  ? pointData.dts 
                  : "-"
              }}</span>
            </div>

            <div class="popup_item">
              <p>海塘长度：</p>
              <span>{{ pointData.seawall_length ? pointData.seawall_length + 'km' : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>安全管理状态：</p>
              <span>{{
                pointData.status ? pointData.status : "-"
              }}</span>
            </div>
          </div>
        </div>
        <!-- 上部分-右侧 -->
        <div class="proj_right">
          <div style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(pointData)">
            <div v-if="photoList.length" class="img_box">
              <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeItem">
                <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
                  <div class="swiper_img" :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }" :title="item.name"></div>
                </el-carousel-item>
              </el-carousel>
              <div class="preview_words">
                <span>{{ imgIndex + 1 }}</span> / {{ photoList.length }}
              </div>
            </div>
            <div v-if="!loadingImg && !photoList.length" class="img_nodata">
              <img src="@/assets/images/noData.png" alt="" />
              <p>暂无图片</p>
            </div>
            <div v-if="loadingImg" class="img_loading">
              <p>图片加载中...</p>
            </div>
          </div>

        </div>
      </div>
      <!-- 下部分 -->
      <div class="proj_bottom">
        <div class="person_list" v-loading="!loadingPerson">
          <div class="person_item" @click="sendMsg(personData.executiveGovName,'政府责任人',personData.executiveGovPhone,personData.executiveGovIsZzd)">
            <div class="person_item_top">
              <h4>政府责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveGovName }}</p>
            <span>{{ personData.executiveGovPhone }}</span>
          </div>
          <div class="person_item" @click="sendMsg(personData.executiveWaterName,'水行政主管部门负责人',personData.executiveWaterPhone,personData.executiveWaterIsZzd)">
            <div class="person_item_top">
              <h4>水行政主管部门负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveWaterName }}</p>
            <span>{{ personData.executiveWaterPhone }}</span>
          </div>
          <div class="person_item" @click="sendMsg(personData.executiveDepartName,'主管部门产权责任人',personData.executiveDepartPhone,personData.executiveDepartIsZzd)">
            <div class="person_item_top">
              <h4>主管部门产权责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveDepartName }}</p>
            <span>{{ personData.executiveDepartPhone }}</span>
          </div>
          <div class="person_item" @click="sendMsg(personData.executiveManageName,'管理单位责任人',personData.executiveManagePhone,personData.executiveManageIsZzd)" v-if="personData.ifDepartment == '有'">
            <div class="person_item_top">
              <h4>管理单位责任人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.executiveManageName }}</p>
            <span>{{ personData.executiveManagePhone }}</span>
          </div>
          <div class="person_item" @click="sendMsg(personData.technicalName,'技术负责人',personData.techinicalPhone,personData.techinicalIsZzd)">
            <div class="person_item_top">
              <h4>技术负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.technicalName }}</p>
            <span>{{ personData.techinicalPhone }}</span>
          </div>
          <div class="person_item" @click="sendMsg(personData.patrolName,'巡查负责人',personData.patrolPhone,personData.patrolIsZzd)">
            <div class="person_item_top">
              <h4>巡查负责人</h4>
              <img :src="$config.imgUrl + 'personnal-white.png'" alt="" />
            </div>
            <p>{{ personData.patrolName }}</p>
            <span>{{ personData.patrolPhone }}</span>
          </div>
        </div>
      </div>
    </div>

    <primary-dialog mainTitle="发送消息" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" :visible="showDialog" @handleClose="showDialog = false" width="500px" append-to-body top="30vh">
      <div class="dialog-content send_dialog">
        <div>
          <el-input type="textarea" v-model="sendText" :rows="4"></el-input>
        </div>
        <div slot="footer" style="float: left;">
          <el-button size="mini" @click="showDialog = false">取 消</el-button>
          <el-button size="mini" class="button_primary" type="primary" @click="sureSend2Times()">确 定</el-button>
        </div>
      </div>

    </primary-dialog>

    <div v-show="drawer" tabindex="0" @focus='focusDiv' @blur='blurDiv' outline="none" hidefocus="false" class="drawer">
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="">
        <span>三色码详情</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="">
      </p>
      <ul>
        <h4>安全鉴定</h4>
        <li v-for="(item) in reasionList.slice(0,2)" :key="item.reason">
          <img v-show="item.brighting ==='yellow' " :src="$config.imgUrl + 'huang.png'" alt="">
          <img v-show="item.brighting ==='red' " :src="$config.imgUrl + 'cheng.png'" alt="">
          <img v-show="item.brighting ==='off' " :src="$config.imgUrl + 'offpoint.png'" alt="">
          <!-- <span :class="item.brighting">{{item.reason}}</span> -->
          {{item.reason}}
        </li>
        <h4>除险加固</h4>
        <li v-for="(item) in reasionList.slice(2,5)" :key="item.reason">
          <img v-show="item.brighting ==='yellow' " :src="$config.imgUrl + 'huang.png'" alt="">
          <img v-show="item.brighting ==='red' " :src="$config.imgUrl + 'cheng.png'" alt="">
          <img v-show="item.brighting ==='off' " :src="$config.imgUrl + 'offpoint.png'" alt="">
          {{item.reason}}
        </li>
        <h4>限制运用</h4>
        <li v-for="(item) in reasionList.slice(5,7)" :key="item.reason">
          <img v-show="item.brighting ==='yellow' " :src="$config.imgUrl + 'huang.png'" alt="">
          <img v-show="item.brighting ==='red' " :src="$config.imgUrl + 'cheng.png'" alt="">
          <img v-show="item.brighting ==='off' " :src="$config.imgUrl + 'offpoint.png'" alt="">
          {{item.reason}}
        </li>
        <h4>工程检查</h4>
        <li v-for="(item) in reasionList.slice(7,9)" :key="item.reason">
          <img v-show="item.brighting ==='yellow' " :src="$config.imgUrl + 'huang.png'" alt="">
          <img v-show="item.brighting ==='red' " :src="$config.imgUrl + 'cheng.png'" alt="">
          <img v-show="item.brighting ==='off' " :src="$config.imgUrl + 'offpoint.png'" alt="">
          {{item.reason}}
        </li>
      </ul>
    </div>
    <div class="mask" v-show="drawer"></div>
    <img v-show="drawer" class="left_menu cursor" :src="$config.imgUrl + 'left_colse_menu.png'" alt="">
  </div>
</template>

<script>
import {
  reservoirPhotoListApi,
  reservoirPersonListApi,
  reservoirLeftStatisticApi,
  riskHiddenDanger,
  oaMsgApi
} from "@/api/api_reservoir";
import PrimaryDialog from '../components/PrimaryDialog.vue'
export default {
  name: 'SeawallPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components: {
    PrimaryDialog
  },
  data() {
    return {
      photoList: [],
      imgIndex: 0,
      loadingImg: true,

      fullscreen: false,
      pointData: {},
      personData: {},
      loadingPerson: true,
      showColorDetail: false,
      reason: [],
      sendText: '',
      sendPhone: '',
      showDialog: false,
      drawer: false,
      isZzd: true,
      reasionList: [
        { brighting: 'off', code: 'R1', reason: '超期未鉴定' },
        { brighting: 'off', code: 'Y1', reason: '一年内到期' },
        { brighting: 'off', code: 'R2', reason: '三类塘未开工' },
        { brighting: 'off', code: 'R3', reason: '二类塘三年未开工' },
        { brighting: 'off', code: 'Y2', reason: '二类塘未开工' },
        // { brighting: 'off', code: 'R6', reason: '超限制运行5天及以上' },
        // { brighting: 'off', code: 'Y3', reason: '超限制水位' },
        { brighting: 'off', code: 'R4', reason: '预报潮位超设计' },
        { brighting: 'off', code: 'R5', reason: '实时潮位超设计' },
        { brighting: 'off', code: 'R7', reason: '严重隐患未整改' },
        { brighting: 'off', code: 'Y4', reason: '较重隐患未整改' },

      ],
    };
  },
  watch: {},
  mounted() {
    this.pointData = this.popupData
    this.getNewPopupData()
    this.getPhotoDetailList(this.popupData.prcdd, this.popupData.name);
    this.getPersonList(this.popupData.prcdd);
    this.riskHIddenData(this.popupData.prcdd)
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    async getNewPopupData() {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        prcd: this.pointData.prcdd,
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success && res.data.length !== 0) {
        this.pointData = res.data[0]
      }
    },
    // 三色码显示原因
    async riskHIddenData(prcd) {
      this.reasionList.forEach((i) => {
        i.brighting = 'off'
      })
      let tempArr = this.pointData.tyCauseCode ? this.pointData.tyCauseCode.split(',') : []
      if (tempArr.length) {
        tempArr.forEach((i) => {
          this.reasionList.forEach((j) => {
            if (i === j.code) {
              switch (i) {
                case 'R1':
                case 'R2':
                case 'R3':
                case 'R4':
                case 'R5':
                case 'R6':
                case 'R7':
                  j.brighting = 'red'
                  break;
                case 'Y1':
                case 'Y2':
                case 'Y3':
                case 'Y4':
                  j.brighting = 'yellow'
                  break;
                default:
                  j.brighting = 'off'
                  break;
              }
            }

          })
        })
      }
    },
    // 点击显示三色码抽屉
    lookStatus(row) {
      this.drawer = true
      this.$nextTick(() => {
        document.querySelector('.drawer').focus()
      })
    },
    blurDiv() {
      this.drawer = false
    },
    focusDiv() {
      this.drawer = true
    },
    async sendMsg(userName, job, phone, type = true) {
      this.showDialog = true
      this.sendText = `${userName}同志，您是${this.pointData.name}的${job}。【浙江省水利厅】`
      this.sendPhone = phone
      this.isZzd = +type
    },
    sureSend2Times() {
      this.sureSend();
      this.sureSend2();
    },
    async sureSend() {
      let res = await oaMsgApi({
        prcd: this.pointData.prcdd,
        phone: this.sendPhone,
        context: this.sendText
      })
      if (res.success) {
        this.$message.success('发送成功！')
      } else {
        this.$message.error('发送失败！')
      }
      this.showDialog = false
    },
    async sureSend2() {
      let res = await oaMsgApi({
        prcd: this.pointData.prcdd,
        // phone: '17764505772', //李汪洋
        phone: '15158047752', //张庆和
        context: this.sendText
      })
    },
    closePopup() {
      this.$parent.closeProjectPopup()
    },
    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.$parent.$parent.openLookPhotosPopup();
      this.$parent.$parent.$parent.showPhotosList(this.photoList, row.name);
    },

    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },

    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    // 轮播图弹窗方法
    changeVideoItem(index) {
      this.videoIndex = index;
    },

    //获取工程图片
    async getPhotoDetailList(prcd, reservoirName) {
      this.imgIndex = 0;
      this.photoList = [];
      this.loadingImg = true;
      let res = await reservoirPhotoListApi({
        viewType: "latestAll",
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let options = [
          {
            name: `${res.rows[0].damBehind}`,
            url: `${res.rows[0].damBehindFullurl}`,
            smallUrl: `${res.rows[0].damBehindUrl ? res.rows[0].damBehindUrl : res.rows[0].damBehindFullurl}`,
          },
          {
            name: `${res.rows[0].damCrest}`,
            url: `${res.rows[0].damCrestFullurl}`,
            smallUrl: `${res.rows[0].damCrestUrl ? res.rows[0].damCrestUrl : res.rows[0].damCrestFullurl}`,
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl ? res.rows[0].damFrontUrl : res.rows[0].damFrontFullurl}`,
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl ? res.rows[0].damToeUrl : res.rows[0].damToeFullurl}`,
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url ? res.rows[0].extphoto5Url : res.rows[0].extphoto5Fullurl}`,
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url ? res.rows[0].extphoto6Url : res.rows[0].extphoto6Fullurl}`,
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl ? res.rows[0].outletDrainageFacilitiesUrl : res.rows[0].outletDrainageFacilitiesFullurl}`,
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl ? res.rows[0].spillwayUrl : res.rows[0].spillwayFullurl}`,
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl ? res.rows[0].spillwayInletSectionUrl : res.rows[0].spillwayInletSectionFullurl}`,
          },
        ];
        options.map((v) => {
          // 缩略图
          if (v.smallUrl && v.smallUrl != "null") {
            if (v.smallUrl.indexOf("https://file.ygwjg.com/") != -1) {
              v.smallUrl = v.smallUrl + "?imageView2/2/w/300/h/200";
            } else if (v.smallUrl.indexOf("fileOss") !== -1) {
              v.smallUrl = "http://sk.zjwater.com/" + v.smallUrl;
            } else {
              v.smallUrl = "http://sk.zjwater.com/fileUpds/" + v.smallUrl;
            }
          }

          if (v.url.indexOf("fileOss") !== -1) {
            v.url = "http://sk.zjwater.com/" + v.url;
          } else if (v.url && v.url.indexOf("https://file.ygwjg.com/") == -1) {
            v.url = "http://sk.zjwater.com/fileUpds/" + v.url;
          }
        });
        this.photoList = options;
        this.loadingImg = false;
      } else {
        this.loadingImg = true;
      }
    },

    // 获取人员信息
    async getPersonList(prcd) {
      let res = await reservoirPersonListApi({
        prcd: prcd,
        year: new Date().format("yyyy"),
      });
      if (res.success) {
        this.personData = res.rows[0];
      }
    },

    //弹窗tabs点击事件
    clickTabs(index, length) {
      if (length) {
        this.popupTabsIndex = index;
      }
    },
  }
}
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  padding: 30px 40px 40px 40px;
  .mask {
    width: 100%;
    background: rgba($color: #0431a0, $alpha: 0.3);
    z-index: 100;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
  }
  @include shadow(#ccc, 0px, 0px, 2px);
  background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  background-size: 100% 100%;
  width: 770px;
  border-radius: 4px;
  position: relative;
  .proj_content {
    font-size: 14px;
    .dialog_title {
      background: url($imageUrl + "sk-popup-title.png") no-repeat bottom;
      background-size: 100% auto;
      height: 40px;
      line-height: 40px;
      font-weight: bold;
      width: 100%;
      padding-left: 16px;
      box-sizing: border-box;
      .title_img_box {
        display: inline-block;
        margin-left: 16px;
        width: 26px;
        height: 26px;
        cursor: pointer;
        .title_img {
          width: 26px;
          height: 26px;
        }
      }
      .dialog__close {
        width: 20px;
        height: 20px;
        background-size: 100% 100%;
        cursor: pointer;
        background: url($imageUrl + "closeImg.png") no-repeat center;
        background-size: 100%;
      }
      span {
        font-size: 24px;
        font-family: youshe;
        color: #fff;
      }
      & > i {
        position: absolute;
        top: 40px;
        right: 40px;
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        z-index: 10;
      }
    }
    .proj_top {
      width: 100%;
      @include flexbox;
      @include flexJC(space-between);
      margin-top: 16px;
      .proj_left {
        width: 240px;
        .proj_title {
          min-height: 44px;
          box-sizing: border-box;
          padding-top: 5px;
          // cursor: pointer;
          font-weight: bold;
          font-size: 18px;
          // color: rgba(41, 82, 208, 1);
          color: #fff;
          line-height: 24px;
          // @include flexbox();
          // @include flexAI(center);
        }
        .proj_detail {
          // @include flexbox;
          // @include flexJC(space-between);
          // @include flexflow(row wrap);
          position: relative;
          .qrCode {
            position: absolute;
            top: 0;
            right: 0;
            width: 63px;
            height: 63px;
          }
          .popup_item {
            height: 38px;
            line-height: 38px;
            @include flexbox;
            padding-left: 5px;
            box-sizing: border-box;
            p {
              // color: #333;
              color: #fff;
              font-weight: 600;
            }
            span {
              // color: #333;
              color: #fff;
            }
            &:nth-child(odd) {
              background: #03338d;
              // background: #f4f5f8;
              box-sizing: border-box;
              // border: 1px solid #ccc;
            }
          }
        }
      }
      .proj_right {
        width: calc(100% - 250px);
        .carousel_box {
          height: 300px;
          .carousel_item {
            height: 100%;
            .swiper_img {
              // background: #03338d;
              width: 100%;
              height: 100%;
              background-repeat: no-repeat;
              background-size: auto 100%;
              background-position: center;
              border-radius: 4px;
            }
            .swiper_video {
              width: 100%;
              height: 100%;
              border-radius: 4px;
            }
          }
        }
        .camera_box {
          height: 300px;
          width: 100%;
          img {
            width: 100%;
          }
        }
        .img_nodata {
          height: 300px;
          text-align: center;
          width: 100%;
          img {
            width: 35%;
            margin-top: 20px;
          }
          p {
            font-size: 16px;
            // color: #999;
            color: #fff;
            padding-top: 15px;
          }
        }
        .img_loading {
          height: 300px;
          width: 100%;
          text-align: center;
          box-sizing: border-box;
          padding-top: 90px;
          p {
            font-size: 16px;
            color: #999;
          }
        }
        .img_box {
          position: relative;
          .preview_words {
            text-align: center;
            position: absolute;
            width: 100%;
            bottom: 10px;
            z-index: 9;
            // color: #333;
            color: #fff;
          }
        }
        .proj_tabs {
          width: 100%;
          @include flexbox;
          @include flexAC;
          @include flexJC(center);
          padding-bottom: 10px;
          & > div {
            // width: 80px;
            padding-right: 10px;
            @include flexbox;
            @include flexAC;
            // background: #1641ab;
            border: 1px solid #ccc;
            height: 30px;
            border-radius: 20px;
            cursor: pointer;
            color: #333;
            margin-right: 15px;
            & > div {
              width: 30px;
              height: 30px;
              border-radius: 20px;
              text-align: center;
              // background: #1641ab;
              border: 1px solid #ccc;
              box-sizing: border-box;
              margin-right: 8px;
              img {
                width: 50%;
                margin-top: 7px;
              }
            }
          }
          & > div.notClick {
            cursor: not-allowed;
          }
          & > div.active {
            color: rgba(41, 82, 208, 1);
            font-weight: bold;
            & > div {
              background: rgba(41, 82, 208, 1);
              border: 1px solid rgba(41, 82, 208, 1);
              box-sizing: border-box;
            }
          }
        }
      }
    }
    .proj_bottom {
      .person_list {
        @include flexbox;
        margin-top: 10px;
        .person_item {
          width: 120px;
          box-sizing: border-box;
          padding: 0 5px;
          background: #2952d026;
          border: 1px solid #ccc;
          border-radius: 4px;
          margin-right: 10px;
          cursor: pointer;
          &:last-child {
            margin-right: 0;
          }
          .person_item_top {
            @include flexbox;
            padding-top: 6px;
            height: 33px;
            h3 {
              font-size: 14px;
              line-height: 18px;
            }
            h4 {
              font-size: 13px;
              line-height: 16px;
              // color: rgba(51, 51, 51, 1);
              color: #fff;
            }
            img {
              padding-left: 5px;
              width: 26px;
              height: 26px;
            }
          }
          p {
            // color: rgba(51, 51, 51, 1);
            color: #fff;
            font-size: 14px;
            font-weight: 600;
            margin-top: 10px;
          }
          span {
            // color: rgba(51, 51, 51, 1);
            color: #fff;
            display: block;
            font-weight: 600;
            font-size: 14px;
            padding: 4px 0;
          }
        }
      }
    }
  }
  .drawer {
    width: 38%;
    // height: 80%;
    position: absolute;
    top: 5%;
    right: 0%;
    bottom: 5%;
    background-color: #fff;
    color: #333;
    font-size: 16px;
    overflow-y: auto;
    outline: none;
    padding: 20px;
    z-index: 101;
    border: 0px solid #fff;
    // border-top-left-radius: 5px;
    // border-bottom-left-radius: 5px;
    border-radius: 4px;
    .third_title::before {
      content: "";
      display: none;
    }
    // box-shadow: -30px 0px 64px 0px rgba(81, 165, 255, 0.33);
    ul {
      position: relative;
      margin-top: 20px;
      h4 {
        margin: 12px 0px 12px 20px;
        position: relative;
      }
      h4::before {
        content: "";
        position: absolute;
        top: 5px;
        left: -16px;
        width: 8px;
        height: 8px;
        // background: #00daff;
      }
      li {
        margin: 8px 0px 8px 30px;
        font-size: 16px;
        img {
          width: 16px;
        }
      }
    }
  }
  .left_menu {
    width: 18px;
    position: absolute;
    right: 42.5%;
    top: 44%;
    z-index: 100;
  }
  .dialog-content {
    height: 650px;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
  }
}
</style>
<style lang="scss">
.color_detail_popper {
  background: #fff;
  // box-shadow: inset 0px 0px 16px 0px rgba(46, 228, 255, 0.5);
  border: 1px solid #ccc;
  color: #333;
  font-family: none;
  .color_detail_box {
    p {
      padding: 5px 10px;
    }
  }
}
.color_detail_popper[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: #ccc;
}
.color_detail_popper[x-placement^="bottom"] .popper__arrow::after {
  border-bottom-color: #ccc;
}
.send_dialog {
  .el-textarea {
    background: #fff;
    color: #333;
    border: 1px solid #ccc;
    border-radius: 4px;
    .el-textarea__inner {
      background: #fff;
      color: #333;
      border: 1px solid #ccc;
    }
    margin-bottom: 15px;
  }
  // 按钮样式
  .el-button {
    color: #fff;
    background: #ff9255;
    border: 0px solid #ff9255;
    font-size: 16px;
    &.button_primary {
      color: #fff;
      background-color: #409eff;
      border-color: #409eff;
    }
  }
  .el-button:focus,
  .el-button:hover {
    background: #ff9255a8;
    border: 0px solid #ff9255a8;
    color: #fff;
  }
}
.nosend_dialog {
  img {
    width: 150px;
  }
  span {
    font-size: 20px;
    color: #333;
  }
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>