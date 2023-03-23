<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-19 18:03:59
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\sgzgScreen\tideAnalysis\TideAnalysisScreen.vue
-->
<template>
  <div class='screen_wrap'>
    <section class="drawer_left" :class="{showLeft: drawerShowLeft}">
      <tide-analysis-left></tide-analysis-left>
      <img class="drawer_left_btn" v-show="drawerShowLeft" @click="close('Left')" src="../../../assets/images/drawer_left1.png" alt="">
      <img class="drawer_left_btn" v-show="!drawerShowLeft" @click="open('Left')" src="../../../assets/images/drawer_left2.png" alt="">
      <div class="map_legend">
        <div class="map_legend_title">
          <div class="map_legend_title_left" @click="showLegend = !showLegend">
            <img src="../../../assets/images/legend_icon.png" alt="">
            <span>图例</span>
          </div>
          <div class="map_legend_title_right">
            <span>图标聚合</span>
            <el-switch @click.stop @change="changeSwitch" v-model="jhFlag" active-color="#193CC5" inactive-color="#999999">
            </el-switch>
            <i v-show="!showLegend" @click="showLegend = !showLegend" class="el-icon-caret-top"></i>
            <i v-show="showLegend" @click="showLegend = !showLegend" class="el-icon-caret-bottom"></i>
          </div>
        </div>
        <transition name="fade">
          <div v-show="showLegend" class="map_legend_list">
            <div class="map_legend_item" v-for="(item, index) in legendList" :key="index">
              <p :style="{backgroundColor: item.color}"></p>
              <span>{{item.name}}</span>
            </div>
          </div>
        </transition>
      </div>
    </section>
    <tide-analysis-map ref="seawallMap"></tide-analysis-map>
    <section class="drawer_right" :class="{showRight: drawerShowRight}">
      <img class="drawer_right_btn" v-show="drawerShowRight" @click="close('Right')" src="../../../assets/images/drawer_right1.png" alt="">
      <img class="drawer_right_btn" v-show="!drawerShowRight" @click="open('Right')" src="../../../assets/images/drawer_right2.png" alt="">
      <!-- <img class="drawer_right_home" @click="goHome()" src="../../../assets/images/home.png" alt=""> -->
      <img class="right_select_open" v-if="!isShowHideLayer" src="../../../assets/images/right_select.png" @click="showHideLayer()">
      <div class="right_select" v-if="isShowHideLayer" v-loading="selectLoading" v-show="drawerShowRight">
        <div class="right_select_title">
          <img src="../../../assets/images/title_ht.png" alt="">
          海塘预警
        </div>
        <div class="right_select_item" :class="index === rightIndex ? 'active' :  ''" v-for="(item, index) in rightSelectList1" :key="index" @click="rightSelectClick(index)">
          <p>{{item.name}}</p>
          <span>
            <span>
              {{item.num ? item.num : 0}}
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_title">
          <img src="../../../assets/images/title_yj.png" alt="">
          海塘浏览
        </div>
        <div class="right_select_item" :class="idx + 2 === rightIndex ? 'active' :  ''" v-for="(ite, idx) in rightSelectList2" :key="ite.name" @click="rightSelectClick(idx+2)">
          <p>{{ite.name}}</p>
          <span>
            <span>
              {{ite.num ? ite.num : 0}}
              <i class="zuo">(条)</i>
            </span>
            <!-- <span v-else>暂无数据</span> -->
          </span>
        </div>
        <div class="right_select_close" @click="showHideLayer()">
          收起
          <img src="../../../assets/images/up.png" alt="">
        </div>
      </div>
      <!-- <keep-alive> -->
      <!-- <seawall-right-tflj v-if="rightIndex === 3"></seawall-right-tflj> -->
      <!-- <seawall-right-photo ref="seawallRightPhoto" v-if="rightIndex === 2"></seawall-right-photo> -->
      <!-- <seawall-right-video ref="seawallRightVideo" v-if="rightIndex === 2"></seawall-right-video> -->
      <seawall-right-cwyj v-if="rightIndex === 0" @openFanganManage="openFanganManage" :rightSelectList1.sync="rightSelectList1"></seawall-right-cwyj>
      <seawall-right-sscw v-if="rightIndex === 1"></seawall-right-sscw>
      <seawall-right-default v-if="rightIndex === 2"></seawall-right-default>
      <seawall-right-aljs v-if="rightIndex === 3"></seawall-right-aljs>
      <!-- </keep-alive> -->
      <div class="map_btns">
        <div class="map_btns_tool">
          <div class="tool_list" v-show="showTool">
            <div class="tool_item" @click="selectTool('length')">长度测量</div>
            <div class="tool_item" @click="selectTool('area')">面积测量</div>
            <div class="tool_item" @click="selectTool('none')">清除测量</div>
            <!-- <div class="tool_item" @click="showHideLayer()">图层显隐</div> -->
          </div>
          <div class="tool_title" @click="showTool = !showTool">
            <i class="el-icon-suitcase-1"></i>
            <span>工具箱</span>
            <i v-show="!showTool" class="el-icon-caret-top"></i>
            <i v-show="showTool" class="el-icon-caret-bottom"></i>
          </div>
        </div>
        <div class="map_btns_zoom">
          <div @click="setZoom('plus')"><i class="el-icon-plus"></i></div>
          <div @click="setZoom('minus')"><i class="el-icon-minus"></i></div>
        </div>
      </div>
    </section>
    <preview-photo v-if="showLookPhotosPopup" :loading="lookPhotosLoading" :photoList="photosList" :seawallName="seawallName" @closeLookPhotosPopup="closeLookPhotosPopup"></preview-photo>
    <preview-video v-if="showLookVideoPopup" :loading="lookVideoLoading" :videoUrl="videoUrl" @closeLookVideoPopup="closeLookVideoPopup"></preview-video>
    <el-dialog title="方案管理" custom-class="fangan_dialog" :visible.sync="showFanganDialog" width="1200px">
      <iframe class="fangan_iframe" :src="fanganUrl" frameborder="0"></iframe>
    </el-dialog>
    <el-dialog title="海塘工程详情" custom-class="seawall_dialog" :visible.sync="showDetailDialog" fullscreen>
      <iframe class="seawall_dialog_iframe" :src="detailUrl" frameborder="0"></iframe>
    </el-dialog>
  </div>
</template>

<script>
import TideAnalysisLeft from './tideAnalysisLeft/TideAnalysisLeft.vue'
import TideAnalysisMap from './tideAnalysisMap/TideAnalysisMap.vue'
import SeawallRightDefault from './tideAnalysisRight/SeawallRightDefault'
import SeawallRightCwyj from './tideAnalysisRight/SeawallRightCwyj'
import SeawallRightSscw from './tideAnalysisRight/SeawallRightSscw'
import SeawallRightAljs from './tideAnalysisRight/SeawallRightAljs'
import SeawallRightTflj from './tideAnalysisRight/SeawallRightTflj'
import SeawallRightPhoto from './tideAnalysisRight/SeawallRightPhoto'
import SeawallRightVideo from './tideAnalysisRight/SeawallRightVideo'
import PreviewPhoto from '@/components/PreviewPhoto'
import PreviewVideo from '@/components/PreviewVideo'
import PreviewBrief from '@/components/PreviewBrief'

import { seawallLeftStatisticApi } from '@/api/api_seawall'

export default {
  name: 'TideAnalysisScreen',
  props: {

  },
  components: {
    TideAnalysisMap, TideAnalysisLeft, SeawallRightDefault, SeawallRightCwyj, SeawallRightSscw, SeawallRightAljs, SeawallRightTflj, SeawallRightPhoto, SeawallRightVideo, PreviewPhoto, PreviewVideo, PreviewBrief
  },
  computed: {
    seawallSelectFlag() {
      return this.$store.state.seawallSelectFlag
    },
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale
    },
    rightSelectList1() {
      return this.$store.state.rightSelectList1
    },
    rightSelectList2() {
      return this.$store.state.rightSelectList2
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData('get', 'userInfo').adcd
      if (val == '33') {
        this.opt.superviseLevel = 1
      }
      this.getRightSelectList()
    },
    leftSelectProjectScale(val) {
      this.setOptions('scale', val)
      this.getRightSelectList()
    }
  },
  data() {
    return {
      drawerShowLeft: true,
      drawerShowRight: true,
      rightIndex: 0,
      selectLoading: true,
      showLookPhotosPopup: false,
      lookPhotosLoading: true,
      photosList: [],
      seawallName: '',
      showLookVideoPopup: false,
      lookVideoLoading: true,
      videoUrl: '',
      showLegend: true,
      showTool: false,
      jhFlag: true,
      legendList: [
        {
          name: '一级海塘',
          color: '#E02020'
        },
        {
          name: '二级海塘',
          color: '#EB7E65'
        },
        {
          name: '三级海塘',
          color: '#F7C739'
        },
        {
          name: '四级海塘',
          color: '#193CC5'
        },
        {
          name: '五级海塘',
          color: '#1BC57E'
        },
        {
          name: '规模以下',
          color: '#C2C2C2'
        }
      ],
      opt: {
        statistic: 'mapTabs',
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
      },
      showFanganDialog: false,
      fanganUrl: '',
      showDetailDialog: false,
      detailUrl: '',
      isShowHideLayer: true
    };
  },
  mounted() {
    this.getRightSelectList()
  },
  methods: {
    showHideLayer() {
      this.isShowHideLayer = !this.isShowHideLayer
    },
    setOptions(type, val) {
      if (val) { // val不为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) == -1) { // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + ',' + type
            this.opt.itemValue = this.opt.itemValue + ',' + val
          } else { // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(',')
            let index = itemArr.indexValue(type)
            let valueArr = this.opt.itemValue.toString().split(',')
            valueArr[index] = val
            this.opt.itemValue = valueArr.join(',')
          }
        } else { // 没有参数
          this.opt.itemCategory = type
          this.opt.itemValue = val
        }
      } else { // 为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) != -1) { // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(',')
            let valueArr = this.opt.itemValue.toString().split(',')
            let index = itemArr.indexValue(type)
            itemArr.splice(index, 1)
            valueArr.splice(index, 1)
            this.opt.itemCategory = itemArr.join(',')
            this.opt.itemValue = valueArr.join(',')
          }
        }
      }
    },
    async getRightSelectList() {
      if (this.opt.adcd === '33') {
        this.opt.superviseLevel = '1'
      } else {
        this.opt.superviseLevel = ''
      }
      this.selectLoading = true
      let res = await seawallLeftStatisticApi(this.opt)
      if (res.success) {
        this.$nextTick(() => {
          this.$store.commit('SET_RIGHT_LIST_ONE', [
            {
              name: '预测预警',
              num: res.data[0].forecast ? res.data[0].forecast : 0
            },
            {
              name: '实时报警',
              num: res.data[0].overCnt ? res.data[0].overCnt : 0
            },
          ])
          this.$store.commit('SET_RIGHT_LIST_TWO', [
            {
              name: '海塘分布',
              num: res.data[0].proCnt ? res.data[0].proCnt : 0
            },
            {
              name: '安澜建设',
              num: res.data[0].constructionCnt ? res.data[0].constructionCnt : 0
            },
          ])
        })
        // this.rightSelectList2 = [
        //   {
        //     name: '海塘分布',
        //     num: res.data[0].proCnt
        //   },
        //   {
        //     name: '安澜建设',
        //     num: res.data[0].constructionCnt
        //   },
        // ]
        // this.rightSelectList1 = [
        //   {
        //     name: '预测预警',
        //     num: res.data[0].forecast
        //   },
        //   {
        //     name: '实时报警',
        //     num: res.data[0].overCnt
        //   },
        // ]
      }
      this.selectLoading = false
    },
    rightSelectClick(index) {
      console.log(index);
      if (this.seawallSelectFlag) {
        this.rightIndex = index;
      } else {
        return false;
      }
    },
    open(type) {
      this['drawerShow' + type] = true
    },
    close(type) {
      this['drawerShow' + type] = false
    },

    // 画点 聚合
    showMapPointJh(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.seawallMap.newMapJh(pointArr, type);
      });
    },
    // 画点 不聚合
    showMapPoints(pointArr, type) {
      this.$nextTick(() => {
        this.$refs.seawallMap.newMapPoints(pointArr, type);
      });
    },

    // 画线
    showMapLines(lineArr) {
      this.$nextTick(() => {
        this.$refs.seawallMap._drawLines(lineArr);
      });
    },

    //显示弹窗
    showOverlays(pointData) {
      this.$nextTick(() => {
        this.$refs.seawallMap._drawOverlays(pointData);
      });
    },

    // 查看图片
    openLookPhotosPopup() {
      this.showLookPhotosPopup = false
      this.$nextTick(() => {
        this.lookPhotosLoading = true
        this.showLookPhotosPopup = true
      })
    },
    closeLookPhotosPopup() {
      this.showLookPhotosPopup = false
    },
    showPhotosList(arr, seawallName) {
      setTimeout(() => {
        this.lookPhotosLoading = false
        this.photosList = arr
        this.seawallName = seawallName
      })
    },

    // 查看视频
    openLookVideoPopup() {
      this.showLookVideoPopup = false
      this.$nextTick(() => {
        this.lookVideoLoading = true
        this.showLookVideoPopup = true
      })
    },
    closeLookVideoPopup() {
      this.showLookVideoPopup = false
    },
    showVideo(url) {
      this.lookVideoLoading = false
      this.videoUrl = url
    },

    // 查看工程详情
    openDetailPopup(prcd, name) {
      let token = this.$localData('get', 'token')
      name = encodeURI(encodeURI(name))
      this.detailUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${prcd}&name=${name}&fromMap=&pName=&cName=&token=${token}`
      console.log(this.detailUrl);
      this.showDetailDialog = true
    },

    // 方案管理
    openFanganManage(url) {
      this.fanganUrl = url
      console.log(this.fanganUrl);
      this.showFanganDialog = true
    },

    // 地图右下角操作
    setZoom(type) {
      this.$refs.seawallMap._setZoom(type)
    },
    selectTool(type) {
      this.showTool = false
      this.$refs.seawallMap.mapToolBar(type)
    },
    changeSwitch(val) {
      if (val) {
        this.setCluster(100)
      } else {
        this.setCluster(0)
      }
    },
    setCluster(val) {
      this.$refs.seawallMap.setCluster(val)
    },

    // 查看点位
    setView(lng, lat) {
      this.$refs.seawallMap._setView(lng, lat)
    },

    // 设置弹窗
    setPopup(pointData) {
      this.$refs.seawallMap._setPopup(pointData)
    },

    // 设置选中点位
    showOneSelectPoint(points, flag, type) {
      this.$refs.seawallMap._drawPoints(points, flag, type)
    },

    // 返回首页
    goHome() {
      this.$router.push('/menu')
    }
  }
}
</script>
<style lang="scss" scoped>
.fangan_iframe {
  width: 100%;
  height: 600px;
}
.seawall_dialog_iframe {
  height: 100%;
  width: 100%;
}
.right_select_title {
  @include flexbox();
  @include flexAC();
  padding: 8px 16px;
  color: #193cc5;
  font-size: 14px;
  line-height: 16px;
  background: -webkit-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -o-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: -moz-linear-gradient(
    90deg,
    #e4f3fc 13%,
    rgba(201, 219, 250, 0) 98%
  );
  background: linear-gradient(90deg, #e4f3fc 13%, rgba(201, 219, 250, 0) 98%);
  img {
    margin-right: 8px;
  }
}
.right_select_close {
  cursor: pointer;
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  padding: 8px 0;
  border-top: 1px solid #d6d6d6;
  color: #333;
  background: #f9f9f9;
  font-size: 14px;
  line-height: 16px;
  img {
    margin-left: 6px;
  }
}
.drawer_right_home {
  position: absolute;
  top: 6px;
  left: -85px;
  cursor: pointer;
}

.right_select_open {
  position: absolute;
  top: 75px;
  left: -78px;
  cursor: pointer;
}
</style>
<style lang="scss">
.fade-enter-active {
  transition: all 0.6s ease;
}
.fade-leave-active {
  transition: all 0.2s ease;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.seawall_dialog .el-dialog__header {
  padding: 16px;
  background: #2478f0;
  .el-dialog__title {
    color: #fff;
  }
  .el-dialog__close {
    color: #fff;
  }
}
.seawall_dialog .el-dialog__body {
  height: calc(100% - 120px);
  overflow: hidden;
}
.fangan_dialog .el-dialog__header {
  padding-bottom: 0;
}
.fangan_dialog .el-dialog__body {
  padding-top: 0;
}
.right_select {
  top: 130px !important;
}
</style>