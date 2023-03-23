<template>
  <div class="water-research-card">
    <audio ref="audio" class="audio-content" loop src="~assets/audio/new12310.wav">您的浏览器不支持 audio 标签。</audio>
    <panel-item title="水文报警" has-switch tc @tclick="tclick">
      <!-- 落点及详情按钮 -->
      <template #title-switch>
        <span :class="[ isWarn ? 'icon-alarm-began' : 'icon-end-alarm', 'warn-box' ]" @click="doWarn"/>
        <i style="margin: 0 10px;" :class="[pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1']" @click="warnClick"/>
        <i class="icon-liebiao" @click.stop="openDetailsDialog" />
      </template>
      <!-- 站点详表 -->
      <div v-for="(item, i) in titleList" :key="i">
        <table-title :title="item.title">
          <template #append>
            <i :class="foldActive == item.title ? 'icon-up-icon' : 'icon-down-icon'" @click="foldActiveData(item.title)" />
          </template>
        </table-title>
        <transition name="fadeHeight" mode="out-in">
          <div class="ceshi" v-if="foldActive == item.title">
            <!-- 统计 -->
            <div class="early-warning-icon">
              <p>站点预警总数<span>{{item.num || '-'}}</span>个</p>
            </div>
            <!-- 表格 -->
            <el-table stripe
              class="table-sty"
              ref="multipleTable"
              :data="item.warningList"
              element-loading-text="拼命加载中"
              :header-cell-style="{color:'#1CFFF5'}"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
            >
              <el-table-column prop="index" label="序号" width="55">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column 
                v-for="(val,i) in item.column" 
                :key="i" 
                :show-overflow-tooltip="true" 
                :label="val.label">
                <template slot-scope="scope">
                  <span :style="{'color':(
                    scope.row[val.prop] == '蓝色警戒' ? '#008fe8' : 
                    scope.row[val.prop] == '黄色警戒' ? '#eec80b' : 
                    scope.row[val.prop] == '橙色警戒' ? '#ff551f' : 
                    scope.row[val.prop] == '红色警戒' ? '#e80000' : 
                    '#ffffff')}"
                  >{{ scope.row[val.prop] }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </transition>
      </div>
    </panel-item>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from "vuex";
  import PanelItem from "../components/PanelItem.vue";
  import { TableTitle } from "./CardPages/components.js";
  import { programmeApi } from "@/api/programme_api/index.js";
  
  export default {
    name: "HydrologicalAlarmCard",
    components: { TableTitle, PanelItem },
    computed: {
      ...mapGetters(['currentWarningListPoint', 'currentWarningDoWarn', 'ScreenMap', 'StatisticFlag'])
    },
    watch: {
      currentWarningListPoint: {
        handler(val) {
          this.isWarn = this.currentWarningDoWarn;
          this.warningListShow(this.currentWarningDoWarn);
          this.$nextTick(() => {
            if (this.isWarn == false) {
              this.warningListShow(this.isWarn);
            }
          });
        }
      }
    },
    data() {
      return {
        isWarn: true,
        pointActive: false,
        checkArr: [],
        foldActive: '1小时超30mm的站点',
        droppointList: [
          {
            type: '暴雨',
            list: []
          },{
            type: '水库',
            list: []
          },{
            type: '河网',
            list: []
          },{
            type: '潮位',
            list: []
          },
        ],
        typeMap: {
          暴雨: {
            src: require('@/assets/images/MapPoint/risk-rain.gif'),
            lat: 'latitude',
            lng: 'longitude',
            type: '雨量',
            code: 'stcd',
            name: 'stationName',
            props: {
              雨量: {
                name: 'drp',
                unit: 'mm'
              }
            }
          },
          预报山洪: {
            src: require('@/assets/images/MapPoint/torrents_higher.png'),
            name: 'avi',
            type: '山洪',
            code: 'stationCode',
            lng: 'lon',
            lat: 'lat',
            props: {
              预报描述: { name: 'desc', unit: '' },
              行政区划: { name: 'adnm', unit: '人' }
            }
          },
          监测山洪: {
            src: require('@/assets/images/MapPoint/torrents_higher.png'),
            name: 'avi',
            type: '山洪',
            code: 'stationCode',
            lng: 'lon',
            lat: 'lat',
            props: {
              总户数: { name: 'tho', unit: '户' },
              总人口: { name: 'tpo', unit: '人' },
              防汛责任人: { name: 'chargerName', unit: '' },
              防汛责任人电话: { name: 'chargerPhone', unit: '' }
            }
          },
          水库: {
            name: 'resName',
            type: '水库',
            props: {
              汛限水位: { name: 'currentLimitLevel', unit: 'm' },
              实时水位: { name: 'waterLevel', unit: 'm' }
            },
            code: 'resCode',
            lng: 'lowLeftLong',
            lat: 'lowLeftLat',
            src: require('@/assets/images/MapPoint/reservoir-fault.png')
          },
          河网: {
            name: 'rnName',
            type: '河网',
            props: {
              警戒水位: { name: 'warningLevel', unit: 'm' },
              实时水位: { name: 'waterLevel', unit: 'm' }
            },
            lng: 'stLong',
            lat: 'stLat',
            src: require('@/assets/images/MapPoint/rivernet-warn.png')
          },
          潮位: {
            name: 'stName',
            type: '潮位',
            props: {
              警戒水位: { name: 'warningLevel', unit: 'm' },
              实时水位: { name: 'waterLevel', unit: 'm' }
            },
            lng: 'stLong',
            lat: 'stLat',
            src: require('@/assets/images/MapPoint/tide.png')
          }
        },
        typeLayerMap: {
          水库: 'reservoir-dynamic',
          暴雨: 'rainrisk-dynamic',
          山洪: 'flood-dynamic'
        },
        titleList: [
          {
            num: null,
            warningList: [],
            title: '1小时超30mm的站点',
            column: [
              {
                prop: 'stationName',
                label: '站点名称'
              },
              {
                prop: 'areaName',
                label: '行政区划'
              },
              {
                prop: 'rainfallOneData',
                label: '雨量(mm)'
              }
            ]
          },{
            num: null,
            warningList: [],
            title: '3小时超60mm的站点',
            column: [
              {
                prop: 'stationName',
                label: '站点名称'
              },
              {
                prop: 'areaName',
                label: '行政区划'
              },
              {
                prop: 'rainfallThreeData',
                label: '雨量(mm)'
              }
            ]
          },{
            num: null,
            warningList: [],
            title: '大中型水库水位',
            column: [
              {
                prop: 'resName',
                label: '水库名称'
              },
              {
                prop: 'areaName',
                label: '行政区划'
              },
              {
                prop: 'waterLevel',
                label: '水位(m)'
              },
              {
                prop: 'currentLimitLevel',
                label: '汛限水位(m)'
              }
            ]
          },{
            num: null,
            warningList: [],
            title: '江河水位',
            column: [
              {
                prop: 'rnName',
                label: '站点名称'
              },
              {
                prop: 'areaName',
                label: '行政区划'
              },
              {
                prop: 'waterLevel',
                label: '水位(m)'
              },
              {
                prop: 'warningLevel',
                label: '警戒水位(m)'
              }
            ]
          },{
            num: null,
            warningList: [],
            title: '潮位',
            column: [
              {
                prop: 'stName',
                label: '站点名称'
              },
              {
                prop: 'areaName',
                label: '行政区划'
              },
              {
                prop: 'tdz',
                label: '实时潮位(m)'
              },
              {
                prop: 'tideWarnType',
                label: '预警状态'
              }
            ]
          }
        ]
      }
    },
    mounted() {
      if (this.isWarn) {
        this.$refs.audio.play();
      }
      this.$nextTick(()=>{
        this.$store.commit('warnData', false);
        // this.warnMapPointStats();
      })
    },
    beforeDestroy() {
      this.$store.commit('warnData', true);
    },
    methods: {
      ...mapActions([ "SetAlarmDialog", "SetAlarmDialog" ]),
      // 折叠面板
      foldActiveData(val) {
        this.foldActive = val;
      },
      // 详情弹窗
      openDetailsDialog() {
        this.titleType('报警');
        this.SetAlarmDialog(true);
      },
      // 报警
      doWarn() {
        this.isWarn = !this.isWarn;
        this.warningListShow(this.isWarn);
      },
      warningListShow(val) {
        let audioPlay = this.$refs.audio;
        if (val) {
          this.$nextTick(() => {
            audioPlay.play();
          });
        } else {
          audioPlay.pause();
          audioPlay.currentTime = 0;
        }
      },
      // 落点
      warnClick() {
        this.pointActive = !this.pointActive;
        this.droppointList.forEach((val)=>{
          let idx = this.checkArr.indexOf(val.type);
          if (idx != -1) {
            this.checkArr.splice(idx, 1);
            this.ScreenMap.clearDynamicPoint();
          } else {
            this.checkArr.push(val.type);
            //加载点位
            let list = val.list;
            const type = val.type;
            list = list.map(point => {
              let props = {};
              let propsList = Object.getOwnPropertyNames(this.typeMap[type].props || {});
              propsList.forEach(prop => {
                props[prop] = (point[this.typeMap[type].props[prop].name] || '-') + this.typeMap[type].props[prop].unit;
              });
              let result = {
                lng: point[this.typeMap[type].lng],
                lat: point[this.typeMap[type].lat],
                name: point[this.typeMap[type].name],
                code: point[this.typeMap[type].code],
                type: this.typeMap[type].type,
                src: this.typeMap[type].src,
                props,
                ...point
              };
              return result;
            });
            if (list.length > 0) {
              this.ScreenMap.drawDynamicPoint(list, `${this.typeLayerMap[type]}`);
            }
          }
        });
      },
      // 列表数据接口
      async warnMapPointStats() {
        await programmeApi.warnMapPointStats().then((res) => {
          if (res.code == 0) {
            this.droppointList[0].list = [...res.data.list.rainfall1Warn, ...res.data.list.rainfall3Warn];
            this.droppointList[1].list = res.data.list.rsvrWarn;
            this.droppointList[2].list = res.data.list.riverNetWarn;
            this.droppointList[3].list = res.data.list.tideWarn;
            this.titleList[0].warningList = res.data.list.rainfall1Warn;
            this.titleList[1].warningList = res.data.list.rainfall3Warn;
            this.titleList[2].warningList = res.data.list.rsvrWarn;
            this.titleList[3].warningList = res.data.list.riverNetWarn;
            this.titleList[4].warningList = res.data.list.tideWarn;
            this.titleList[0].num = res.data.stats.rainfall1Warn;
            this.titleList[1].num = res.data.stats.rainfall3Warn;
            this.titleList[2].num = res.data.stats.rsvrWarn;
            this.titleList[3].num = res.data.stats.riverNetWarn;
            this.titleList[4].num = res.data.stats.tideWarn;
          }
        })
      }
    }
  }
</script>

<style lang="scss">
  .water-research-card {
    position: relative;
    .table-sty .el-table__body-wrapper {
      height: 24.5rem;
      overflow: auto;
      position: relative;
    }
    .early-warning-icon {
      background-image: url(~assets/images/early-warning-icon.png);
      background-repeat: no-repeat;
      background-size: 100%;
      height: 3rem;
      line-height: 3rem;
      text-align: center;
      margin-bottom: 10px;
      >p {
        font-size: 17px;
        >span {
          color: #e89100;
          padding: 0 10px;
          font-size: 20px;
        }
      }
    }
    .fadeHeight-enter-active,.fadeHeight-leave-active {
      transition: all 0.2s;
      max-height: 230px;
    }
    .fadeHeight-enter,.fadeHeight-leave-to
    {
      opacity: 0;
      max-height: 0px;
    }
  }
</style>