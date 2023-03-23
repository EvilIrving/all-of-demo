<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-12 19:47:47
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\VideoCount.vue
-->
<template>
  <count-wrap title="报警" class="warn-count">
    <audio  
      ref="audio"
      class="audio-content"
      loop
      src="~assets/audio/new12310.wav"
    >
      您的浏览器不支持 audio 标签。
    </audio>
    <template #title-right>
      <span
        :class="[
          isWarn ? 'icon-notification-warning' : 'icon-notification',
          'warn-box',
        ]"
        @click="doWarn"
      ></span>
    </template>

    <div class="has-count">
      <div
        @click="warnClick(item)"
        :class="[
          'warn-item',
          checkArr.indexOf(item.type) != -1 ? 'active' : '',
        ]"
        v-for="item in currentWarningListPoint"
        :key="item.type"
      >
        <div class="warn-item_content">
          <p>{{ item.type }}</p>
          <p>{{ item.list.length }}个</p>
        </div>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { mapGetters } from 'vuex';
export default {
  name: 'WarnCount',
  data() {
    return {
      checkArr: [],
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
      isWarn: true
    };
  },
  components: { CountWrap },
  computed: {
    ...mapGetters(['currentWarningListPoint', 'currentWarningDoWarn', 'ScreenMap', 'StatisticFlag'])
  },
  mounted() {
    if (this.isWarn) {
      this.$refs.audio.play();
    }
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
  methods: {
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
    warnClick(item) {
      console.log(509,item,item.type);
      let idx = this.checkArr.indexOf(item.type);
      if (idx != -1) {
        this.checkArr.splice(idx, 1);
        this.ScreenMap.clearDynamicPoint();
      } else {
        this.checkArr.push(item.type);
        //加载点位
        let list = item.list;
        const type = item.type;
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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.warn-count {
  background-image: url(~assets/images/count-card-red-bg.png);
  ::v-deep.title-name {
    color: #fff;
  }
  .warn-box {
    display: inline-block;
    width: 50px;
    height: 24px;
  }
  .has-count {
    height: 100%;
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    @include flexJC(space-between);
    .warn-item {
      width: 104px;
      cursor: pointer;
      .warn-item_content {
        p {
          text-align: center;
          padding: 4px 0;
        }
      }
      &.active {
        .warn-item_content {
          background: #bb020565;
        }
        $color: #bb0206;
        background: linear-gradient(to bottom, $color, $color) left top
            no-repeat,
          linear-gradient(to bottom, $color, $color) left top no-repeat,
          linear-gradient(to bottom, $color, $color) right top no-repeat,
          linear-gradient(to bottom, $color, $color) right top no-repeat,
          linear-gradient(to bottom, $color, $color) left bottom no-repeat,
          linear-gradient(to bottom, $color, $color) left bottom no-repeat,
          linear-gradient(to bottom, $color, $color) right bottom no-repeat,
          linear-gradient(to bottom, $color, $color) right bottom no-repeat;
        /*设置大小*/
        background-size: 12px 2px, 2px 12px;
        border-radius: 3px;
      }
    }
  }
}
</style>
