<template>
  <div class="popup_map">
    <div class="proj_content">
      <div class="dialog_title">
        <!-- 水库信息 -->
        <span style="cursor:pointer;" @click="lookDetail(pointData)">{{ pointData.NAME }}</span>
        <!-- <el-popover 
          popper-class="color_detail_popper" 
          placement="bottom" 
          v-if="popupData.CODE !== '绿色' "
          width="160">
          <div class="color_detail_box">
            <ul>
              <li v-for="(item,index) in reason" :key="index">{{item}}</li>
            </ul>
          </div>
          <div slot="reference" class="title_img_box">
          </div>
        </el-popover> -->
        <span style="font-size: 16px;" @click="lookDetail(pointData)" v-if="pointData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
        <span style="font-size: 16px;" @click="lookDetail(pointData)" v-if="pointData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
        <span style="font-size: 16px;" @click="lookDetail(pointData)" v-if="pointData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
        <span style="font-size: 16px;" @click="lookDetail(pointData)" v-if="pointData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
        <span style="font-size: 16px;" @click="lookDetail(pointData)" v-if="pointData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
        <div class="title_img_box">
          <img class="title_img" @click="lookStatus(popupData.CODE)" v-if="popupData.CODE && popupData.CODE == '红色'" :src="$config.imgUrl + 'status-red.png'" alt="">
          <img class="title_img" @click="lookStatus(popupData.CODE)" v-if="popupData.CODE && popupData.CODE == '黄色'" :src="$config.imgUrl + 'status-yellow.png'" alt="">
          <img class="title_img" v-if="popupData.CODE && popupData.CODE == '绿色'" :src="$config.imgUrl + 'status-green.png'" alt="">
        </div>

        <i class="dialog__close" @click="closePopup()"></i>
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
            <!-- <img class="qrCode" src="../../../../../assets/images/skzg/qrcode_green.png" alt=""> -->
            <div class="popup_item">
              <p>水位：</p>
              <span>{{
                  pointData.waterLevel
                    ? pointData.waterLevel.toFixed(2) + "m"
                    : "- "
                }} </span>
              <!-- 括号填写是否上升及数字 -->
              <!-- <div v-if="parseFloat(popupData.waterChange)">
                <span v-html="'&nbsp'"></span>(
                <span>{{
                  popupData.waterChange
                    ? popupData.waterChange.toFixed(2)
                    : popupData.waterChange
                }}</span>
                <span v-if="popupData.waterChange"
                  ><i
                    class="el-icon-top"
                    style="color: green;font-size:16px"
                    v-if="
                      popupData.waterChange > 0 ||
                        popupData.waterChange == null
                    "
                  ></i>
                  <i
                    class="el-icon-bottom"
                    style="color: red;font-size:16px"
                    v-else
                  ></i></span
                >)
              </div> -->
              <!-- 加个箭头 -->
            </div>
            <div class="popup_item">
              <p>集雨面积：</p>
              <span>{{ pointData.RCA ? pointData.RCA + "km²" : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>水库功能：</p>
              <span>{{ pointData.ACTFUNCMC ? pointData.ACTFUNCMC : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>时间：</p>
              <span>{{
                pointData.waterleveltm
                  ? pointData.waterleveltm.slice(5, 16)
                  : "-"
              }}</span>
            </div>
            <!-- <div class="popup_item">
              <p>规模：</p>
              <span v-if="popupData.project_scale == '1'">大(1)型</span>
              <span v-if="popupData.project_scale == '2'">大(2)型</span>
              <span v-if="popupData.project_scale == '3'">中型</span>
              <span v-if="popupData.project_scale == '4'">小(1)型</span>
              <span v-if="popupData.project_scale == '5'">小(2)型</span>
            </div> -->
            <div class="popup_item">
              <p>总库容：</p>
              <span>{{
                pointData.TATOL_STORAGE
                  ? pointData.TATOL_STORAGE + "万m³"
                  : "-"
              }}</span>
            </div>

            <div class="popup_item">
              <p>坝型：</p>
              <span>{{ pointData.DTBS ? pointData.DTBS : "-" }}</span>
            </div>

            <!-- <div class="popup_item">
              <p>所在市：</p>
              <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
            </div>
            <div class="popup_item">
              <p>所在县：</p>
              <span>{{
                popupData.countryADNM ? popupData.countryADNM : "-"
              }}</span>
            </div> -->
            <div class="popup_item">
              <p>安全鉴定：</p>
              <span v-if="
                  pointData.status && pointData.status.slice(0, 3) == '三类坝'
                ">{{ pointData.status.slice(0, 3) }}</span>
              <span v-else>{{
                pointData.status ? pointData.status : "-"
              }}</span>
            </div>
            <div class="popup_item" v-if="
                pointData.status && pointData.status.slice(0, 3) == '三类坝'
              ">
              <p>除险加固：</p>
              <span>{{
                pointData.status ? pointData.status.slice(3) : "-"
              }}</span>
            </div>
            <div class="popup_item" v-if="pointData.rightPopType && pointData.rightPopType == 'xsl'">
              <p>蓄水率：</p>
              <span>{{ pointData.xsl ? pointData.xsl : "-" }}</span>
            </div>
            <div class="popup_item" v-if="
                pointData.rightPopType && pointData.rightPopType == 'kgkn'
              ">
              <p>可纳雨：</p>
              <span>{{
                pointData.rainContains
                  ? pointData.rainContains.toFixed(2) + "mm"
                  : "0"
              }}</span>
            </div>
            <div class="popup_item" v-if="
                pointData.rightPopType && pointData.rightPopType == 'kgkn'
              ">
              <p>可纳洪：</p>
              <span>{{
                pointData.knx ? pointData.knx + pointData.knxUnit : "0"
              }}</span>
            </div>

            <div class="popup_item" v-if="
                pointData.rightPopType && pointData.rightPopType == 'kgkn'
              ">
              <p>可供水：</p>
              <span>{{
                pointData.kgs ? pointData.kgs + pointData.kgsUnit : "0"
              }}</span>
            </div>
            <!-- 预测预警 -->
            <div class="popup_item" v-if="pointData.isYcyj">
              <p>{{pointData.overName}}：</p>
              <span>{{ pointData.overValue ? pointData.overValue.toFixed(2) + 'm' : '0' }}</span>
            </div>
            <div class="popup_item" v-if="pointData.isYcyj">
              <p>预报水位：</p>
              <span style="color:#ff0000">{{ pointData.ybWater ? pointData.ybWater.toFixed(2) + 'm' : '0' }}</span>
            </div>
          </div>
        </div>
        <!-- 上部分-右侧 -->
        <div class="proj_right">
          <div class="proj_tabs">
            <div :class="popupTabsIndex == 0 ? 'active' : ''" @click="clickTabs(0, 1)">
              <div>
                <img v-if="popupTabsIndex == 0" src="@/assets/images/image0.png" alt="" />
                <img v-else :src="$config.imgUrl + 'image1.png'" alt="" />
              </div>
              照片
            </div>
            <div :class="[
                popupTabsIndex == 1 ? 'active' : '',
                videoList.length ? '' : 'notClick',
              ]" @click="clickTabs(1, videoList.length)">
              <div>
                <img v-if="popupTabsIndex == 1" src="@/assets/images/yx0.png" alt="" />
                <img v-else :src="$config.imgUrl + 'yx1.png'" alt="" />
              </div>
              影像
            </div>
            <div :class="[
                popupTabsIndex == 2 ? 'active' : '',
                cameraList.length ? '' : 'notClick',
              ]" @click="clickTabs(2, cameraList.length)">
              <div>
                <img v-if="popupTabsIndex == 2" src="@/assets/images/video0.png" alt="" />
                <img v-else :src="$config.imgUrl + 'video1.png'" alt="" />
              </div>
              视频
            </div>
          </div>
          <!-- 照片 -->
          <div v-show="popupTabsIndex == 0" style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(pointData)">
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
          <!-- 影像 -->
          <div v-show="popupTabsIndex == 1" v-loading="loadingVideo">
            <div v-if="videoList.length" class="img_box">
              <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeVideoItem">
                <el-carousel-item class="carousel_item" v-for="(item, index) in videoList" :key="index">
                  <video class="swiper_video" :src="item.url" controls></video>
                </el-carousel-item>
              </el-carousel>
              <div class="preview_words">
                <span>{{ videoIndex + 1 }}</span> / {{ videoList.length }}
              </div>
            </div>
            <div v-if="!loadingVideo && !videoList.length" class="img_nodata">
              <img src="@/assets/images/noData.png" alt="" />
              <p>暂无视频影像</p>
            </div>
            <div v-if="loadingVideo" class="img_loading">
              <p>视频影像加载中...</p>
            </div>
          </div>
          <!-- 视频 -->
          <div v-show="popupTabsIndex == 2" style="cursor: pointer">
            <div class="camera_box" @click="lookCamera(pointData)">
              <img src="@/assets/images/sk-camera.png" alt="" />
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
      <!-- <div v-if="!isZzd" class="nosend_dialog">
        <img src="@/assets/images/skzg/notZddUser.png" alt="">
        <span>该负责人非浙政钉用户，无法发送信息</span>
      </div> -->
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
        <li v-for="(item) in reasionList.slice(5,9)" :key="item.reason">
          <img v-show="item.brighting ==='yellow' " :src="$config.imgUrl + 'huang.png'" alt="">
          <img v-show="item.brighting ==='red' " :src="$config.imgUrl + 'cheng.png'" alt="">
          <img v-show="item.brighting ==='off' " :src="$config.imgUrl + 'offpoint.png'" alt="">
          {{item.reason}}
        </li>
        <h4>工程检查</h4>
        <li v-for="(item) in reasionList.slice(9,11)" :key="item.reason">
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
  reservoirVideoListApi,
  reservoirCameraListApi,
  reservoirPersonListApi,
  getFile,
  reservoirLeftStatisticApi,
  riskHiddenDanger,
  oaMsgApi
} from "@/api/api_reservoir";
import PrimaryDialog from '../../../components/PrimaryDialog.vue'
export default {
  name: 'ReservoirPopup',
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
      videoList: [],
      videoIndex: 0,
      loadingVideo: true,
      fullscreen: false,
      popupTabsIndex: 0,
      cameraList: [],
      personData: {},
      loadingPerson: true,
      pointData: [],
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
        { brighting: 'off', code: 'R2', reason: '三类坝未开工' },
        { brighting: 'off', code: 'R3', reason: '二类坝三年未开工' },
        { brighting: 'off', code: 'Y2', reason: '二类坝未开工' },
        { brighting: 'off', code: 'R4', reason: '超设计洪水位' },
        { brighting: 'off', code: 'R5', reason: '三类坝超限制水位（汛期中）' },
        { brighting: 'off', code: 'R6', reason: '超限制运行5天及以上' },
        { brighting: 'off', code: 'Y3', reason: '超限制水位' },
        { brighting: 'off', code: 'R7', reason: '严重隐患未整改' },
        { brighting: 'off', code: 'Y4', reason: '较重隐患未整改' },
      ],
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'reservoirPopup') {
        this.popupTabsIndex = 0;
        this.getPhotoDetailList(val.PRCD, val.NAME);
        this.getVideoDetailList(val.PRCD);
        this.getCameraList(val.PRCD);
        this.getPersonList(val.PRCD);
        this.getNewPopupData()
        this.riskHIddenData(val.PRCD)
      }
    }
  },
  mounted() {
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
        prcd: this.popupData.PRCD,
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
      let res = await riskHiddenDanger({
        prcd: prcd,
      })
      if (res.success) {
        let tempArr = res.data.tyCause ? res.data.tyCause.split(',') : []
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
      this.sendText = `${userName}同志，您是${this.pointData.NAME}的${job}。【浙江省水利厅】`
      this.sendPhone = phone
      this.isZzd = +type
    },
    sureSend2Times() {
      this.sureSend();
      this.sureSend2();
    },
    async sureSend() {
      let res = await oaMsgApi({
        prcd: this.popupData.PRCD,
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
        prcd: this.popupData.PRCD,
        // phone: '17764505772', //李汪洋
        phone: '15158047752', //张庆和
        context: this.sendText
      })
    },
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.$parent.$parent.openLookPhotosPopup();
      this.$parent.$parent.$parent.showPhotosList(this.photoList, row.NAME);
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
            smallUrl: `${res.rows[0].damCrestUrl?res.rows[0].damCrestUrl:res.rows[0].damCrestFullurl}`,
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl?res.rows[0].damFrontUrl:res.rows[0].damFrontFullurl}`,
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl?res.rows[0].damToeUrl:res.rows[0].damToeFullurl}`,
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url?res.rows[0].extphoto5Url:res.rows[0].extphoto5Fullurl}`,
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url?res.rows[0].extphoto6Url:res.rows[0].extphoto6Fullurl}`,
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl?res.rows[0].outletDrainageFacilitiesUrl:res.rows[0].outletDrainageFacilitiesFullurl}`,
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl?res.rows[0].spillwayUrl:res.rows[0].spillwayFullurl}`,
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl?res.rows[0].spillwayInletSectionUrl:res.rows[0].spillwayInletSectionFullurl}`,
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

    // 获取工程视频影像
    async getVideoDetailList(prcd) {
      this.videoIndex = 0;
      this.videoList = [];
      this.loadingVideo = true;
      let res = await reservoirVideoListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let arr = [];
        if (res.rows.length) {
          res.rows.map((v) => {
            arr.push(v.videouri);
          });
          this.getVideoFileList(arr.join(","));
        } else {
          this.loadingVideo = false;
        }
      } else {
        this.loadingVideo = false;
      }
    },
    async getVideoFileList(ids) {
      let res = await getFile({
        // dataId: '9D06FE1A368848B6A7E097DC30F32FDE',
        dataId: ids,
      });
      let videoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl;
          if (
            v.filurl &&
            v.filurl.indexOf("https://file.ygwjg.com/") == -1 &&
            v.filurl.indexOf("/fileUpds/") != -1
          ) {
            url = "https://sk.zjwater.com" + v.filurl;
          }
          videoList.push({
            name: v.filnm,
            url: url,
          });
        });
        this.videoList = videoList;
      }
      this.loadingVideo = false;
    },

    // 获取摄像头数据
    async getCameraList(prcd) {
      let res = await reservoirCameraListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        this.cameraList = res.rows;
      }
    },
    lookCamera(popupData) {
      this.$parent.$parent.$parent.openLookCameraPopup(popupData.PRCD, popupData.NAME);
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
  background: #fff;
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
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat
  //   center;
  // background-size: 100% 100%;
  width: 770px;
  border-radius: 4px;
  position: relative;
  .proj_content {
    font-size: 14px;
    .dialog_title {
      background: transparent;
      // background: url($imageUrl + "sk-popup-title.png")
      //   no-repeat bottom;
      // background-size: 100% auto;
      height: 40px;
      line-height: 40px;
      padding-left: 16px;
      font-weight: bold;
      width: 100%;
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
      span {
        font-size: 24px;
        font-family: youshe;
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
          color: rgba(41, 82, 208, 1);
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
              color: #333;
              font-weight: 600;
            }
            span {
              color: #333;
            }
            &:nth-child(odd) {
              // background: #03338d;
              background: #f4f5f8;
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
            color: #999;
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
            color: #333;
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
              color: rgba(51, 51, 51, 1);
            }
            img {
              padding-left: 5px;
              width: 26px;
              height: 26px;
            }
          }
          p {
            color: rgba(51, 51, 51, 1);
            font-size: 14px;
            font-weight: 600;
            margin-top: 10px;
          }
          span {
            color: rgba(51, 51, 51, 1);
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