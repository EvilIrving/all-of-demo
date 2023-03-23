<template>
  <div class="riverway-box">
    <div class="riverway-01">
      <div class="riverway-01-name">瓯江</div>
      <div class="riverway-01-img">
        <img class="riverway-01-image" src="@/assets/new_images/projectScheduling/riverway-01.png" alt="" />
        <div class="point-info-list">
          <pointItem
            v-for="(item, index) in ouRiver"
            :key="item.name"
            :class="{ margin36: index == 0, margin39: index == 1, margin51: index == 2 }"
            :position="index == 1 ? 'bottom' : 'top'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
    </div>
    <div class="riverway-center">
      <div class="riverway-02">
        <div class="sign-image">
          <div>荫溪小流域</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-02.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in yingXiBasins"
            :key="item.code"
            :class="{ marginTop12: index == 0, marginTop45: index == 1, marginTop20: index > 1 }"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
      <div class="riverway-03">
        <div class="sign-image">
          <div>戍浦江</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-03.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in ourPujiang"
            :key="item.code"
            :class="index == 0 ? 'marginTop12' : 'marginTop15'"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
      <div class="riverway-04">
        <div class="sign-image">
          <div>仰义河</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-04.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in angYiRiver"
            :key="item.code"
            :class="index == 0 ? 'marginTop130' : 'marginTop101'"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
      <div class="riverway-05">
        <div class="sign-image">
          <div>卧旗大河</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-04.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in woQiRiver"
            :key="item.code"
            :class="index == 0 ? 'marginTop45' : 'marginTop20'"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
      <div class="riverway-06">
        <div class="sign-image">
          <div>温瑞塘河</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-05.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in wenRuitangRiver"
            :key="item.code"
            :class="index == 0 ? 'marginTop12' : 'marginTop10'"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
      <div class="riverway-07">
        <div class="sign-image">
          <div>七都内河</div>
          <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        </div>
        <img class="riverway-image" src="@/assets/new_images/projectScheduling/riverway-06.png" alt="" />
        <div class="riverway-center-list">
          <pointItem
            v-for="(item, index) in qiDuRiver"
            :key="item.code"
            :class="index == 0 ? 'marginTop12' : 'marginTop20'"
            :position="index % 2 == 0 ? 'right' : 'left'"
            :pointData="item"
            :sluiceType="sluiceType"
          />
        </div>
      </div>
    </div>
    <div class="riverway-08">
      <img class="riverway-08-image" src="@/assets/new_images/projectScheduling/riverway-09.png" alt="" />
      <div class="riverway-08-name">
        <img src="@/assets/new_images/projectScheduling/riverway-07.png" alt="" />
        <div>丰门河</div>
      </div>
      <div class="riverway-08-list">
        <pointItem
          v-for="(item, index) in fengRiverDoor"
          :key="item.code"
          :class="setClassName(index)"
          :position="setPosition(index)"
          :pointData="item"
          :sluiceType="sluiceType"
        />
      </div>
    </div>
    <legend-box />
  </div>
</template>

<script>
import pointItem from './pointItem.vue';
import legendBox from '../components/legendBox.vue';
import { SluiceDistributionMap } from '@/api/bigScreenPart';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index';
import { mapMutations, mapGetters } from 'vuex';
import { cloneObj } from '@/utils/utils';
export default {
  components: { pointItem, legendBox },
  computed: {
    ...mapGetters(['sluiceActiveArr'])
  },
  watch: {
    sluiceActiveArr: {
      handler: function (val) {
        if (val) {
          this.handleChangeSluiceType(val[0]);
        }
      },
      deep: true
    }
  },
  data() {
    return {
      ouRiver: [
        { name: '紧水滩', noData: true },
        { name: '滩坑水库', noData: true },
        { name: '青田水利枢纽工程', noData: true }
      ], //瓯江
      yingXiBasins: [], //荫溪小流域
      ourPujiang: [], //戍浦江
      angYiRiver: [], //仰义河
      woQiRiver: [], //卧旗大河
      wenRuitangRiver: [], //温瑞塘河
      qiDuRiver: [], //七都内河
      fengRiverDoor: [], //丰门河
      allRiver: {}, //概化图数据
      latestGatage: {}, //水闸分析数据
      sluiceType: 0 //1 勾选了水闸分析
    };
  },
  async created() {
    await this.getHydrProjStaDiagram();
    await this.getLatestGatage();
  },
  methods: {
    ...mapMutations({
      setInfoDetail: 'SET_INFO_DETAIL',
      setSluiceInfoList: 'SET_SLUICE_INFO_LIST'
    }),
    async getHydrProjStaDiagram() {
      let res = await disasterPreventionApi.getHydrProjStaDiagram();
      if (res.code == 0) {
        this.allRiver = res.data;
        this.setDrainageBasin(res.data.list);
        this.setInfoDetail(this.allRiver);
      }
    },
    setDrainageBasin(obj, type) {
      let { yingXiBasins, ourPujiang, angYiRiver, woQiRiver, wenRuitangRiver, qiDuRiver, fengRiverDoor } = obj;
      this.yingXiBasins = yingXiBasins;
      this.ourPujiang = ourPujiang;
      this.angYiRiver = angYiRiver;
      this.woQiRiver = woQiRiver;
      this.wenRuitangRiver = wenRuitangRiver;
      this.qiDuRiver = qiDuRiver;
      this.fengRiverDoor = fengRiverDoor;
      if (!type) {
        this.ouRiver = [
          { name: '紧水滩', noData: true },
          { name: '滩坑水库', noData: true },
          { name: '青田水利枢纽工程', noData: true }
        ];
        this.sluiceType = 0;
      } else {
        this.ouRiver = [];
        this.sluiceType = 1;
      }
    },
    setSluiceTypeList(type) {
      let allRiver = cloneObj(this.allRiver.list);
      for (let key in allRiver) {
        let arr = allRiver[key];
        arr.forEach((item) => {
          let data = this.latestGatage.find((ite) => {
            if (key != 'angYiRiver') {
              return ite.wagaCode === item.code;
            } else {
              item.codeType = '水闸工程';
              return ite.wagaName === item.name;
            }
          });
          if (data) {
            switch (type) {
              case '控运分析':
                item['status'] = data.kyStatus == 0 ? 'green' : data.kyStatus == 1 ? 'error' : 'warning';
                break;
              case '负荷分析':
                item['status'] =
                  data.llStatus == 0 ? 'green' : data.llStatus == 1 ? 'error' : data.llStatus == 2 ? 'red' : 'warning';
                break;
              default: {
                item['status'] = data.psStatus == 0 ? 'green' : 'red';
                break;
              }
            }
            item['gatage'] = data;
          }
        });
      }

      this.setDrainageBasin(allRiver, 1);
    },
    handleChangeSluiceType(type) {
      let infoList = [];
      if (type) {
        this.setSluiceTypeList(type);
        switch (type) {
          case '控运分析':
            const ky_blueArr = this.latestGatage.filter((item) => item.kyStatus == 0);
            const ky_yellowArr = this.latestGatage.filter((item) => item.kyStatus == 1);
            const ky_greenArr = this.latestGatage.filter((item) => item.kyStatus == 2);
            infoList = [
              {
                name: '控运正常',
                className: 'open-span',
                num: ky_blueArr.length
              },
              {
                name: '无控运',
                className: 'normal-span',
                num: ky_greenArr.length
              },
              {
                name: '超控运',
                className: 'warn-span',
                num: ky_yellowArr.length
              }
            ];
            break;
          case '负荷分析':
            const ll_blueArr = this.latestGatage.filter((item) => item.llStatus == 0);
            const ll_yellowArr = this.latestGatage.filter((item) => item.llStatus == 1);
            const ll_greenArr = this.latestGatage.filter((item) => item.llStatus == -1);
            const ll_redArr = this.latestGatage.filter((item) => item.llStatus == 2);
            infoList = [
              {
                name: '负荷正常',
                className: 'open-span',
                num: ll_blueArr.length
              },
              {
                name: '无数据',
                className: 'normal-span',
                num: ll_greenArr.length
              },
              {
                name: '接近满负荷',
                className: 'warn-span',
                num: ll_yellowArr.length
              },
              {
                name: '满负荷',
                className: 'error-span',
                num: ll_redArr.length
              }
            ];
            break;
          default: {
            const ps_blueArr = this.latestGatage.filter((item) => item.psStatus == 0);
            const ps_redArr = this.latestGatage.filter((item) => item.psStatus == 1);
            infoList = [
              {
                name: '排水正常',
                className: 'open-span',
                num: ps_blueArr.length
              },
              {
                name: '强排水',
                className: 'error-span',
                num: ps_redArr.length
              }
            ];
            break;
          }
        }
      } else {
        this.setDrainageBasin(this.allRiver.list);
      }
      this.setSluiceInfoList(infoList);
    },
    async getLatestGatage() {
      let res = await SluiceDistributionMap.latestGatage();
      if (res.code === 0) {
        this.latestGatage = res.data;
      }
    },
    setClassName(index) {
      if ([0, 4, 11].includes(index)) {
        return 'margin20';
      } else if ([1, 2, 3, 5].includes(index)) {
        return 'margin70';
      } else if ([10, 12].includes(index)) {
        return 'margin51';
      } else if ([13, 14].includes(index)) {
        return 'margin80';
      } else if ([16].includes(index)) {
        return 'margin25';
      } else {
        return 'margin36';
      }
    },
    setPosition(index) {
      if ([0, 1, 2, 4, 6, 8, 10, 12, 13, 14, 16].includes(index)) {
        return 'bottom';
      } else {
        return 'top';
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.riverway-box {
  padding-top: 80px;
}
img {
  vertical-align: bottom;
}
.margin36 {
  margin-left: 36px;
}
.margin39 {
  margin-left: 39px;
}
.margin51 {
  margin-left: 51px;
}
.margin20 {
  margin-left: 20px;
}
.margin25 {
  margin-left: 30px;
}
.margin70 {
  margin-left: 80px;
}
.margin80 {
  margin-left: 90px;
}
.marginTop12 {
  margin-top: 12px;
}
.marginTop45 {
  margin-top: 40px;
}
.marginTop20 {
  margin-top: 20px;
}
.marginTop15 {
  margin-top: 16px;
}
.marginTop130 {
  margin-top: 130px;
}
.marginTop101 {
  margin-top: 101px;
}
.marginTop10 {
  margin-top: 10px;
}
.img-box {
  width: 24px;
  cursor: pointer;
}
.sign-image {
  position: absolute;
  top: -79px;
  width: 100px;
  left: 50%;
  margin-left: -50px;
  text-align: center;
  color: #ffffff;
  div {
    padding-bottom: 5px;
    font-size: 16px;
  }
  img {
    width: 22px;
  }
}
.riverway-01 {
  padding: 0 30px 0 10px;
  display: flex;
  align-items: center;
  .riverway-01-name {
    font-size: 24px;
    font-weight: 500;
    color: #ffffff;
    position: relative;
    padding-right: 33px;
    &::after {
      content: '';
      position: absolute;
      right: 3px;
      top: -3px;
      width: 25px;
      height: 29px;
      background-image: url(~assets/new_images/projectScheduling/riverway-08.png);
      background-size: 100% 100%;
      background-repeat: no-repeat;
    }
  }
  .riverway-01-img {
    position: relative;
    width: calc(100% - 85px);
    .riverway-01-image {
      width: 100%;
    }
    .point-info-list {
      position: absolute;
      z-index: 2;
      top: 5px;
      width: 100%;
      display: flex;
    }
  }
}
.riverway-center {
  display: flex;
  height: 523px;
  .riverway-image {
    height: 100%;
  }
  > div {
    position: relative;
  }
  .riverway-02 {
    height: 188px;
    margin-left: 352px;
  }
  .riverway-03 {
    height: 464px;
    margin-left: 238px;
  }
  .riverway-04 {
    height: 100%;
    margin-left: 236px;
  }
  .riverway-05 {
    height: 100%;
    margin-left: 357px;
    .riverway-image {
      transform: rotate(180deg);
    }
  }
  .riverway-06 {
    height: 434px;
    margin-left: 234px;
  }
  .riverway-07 {
    height: 340px;
    margin-left: 253px;
  }
  .riverway-center-list {
    position: absolute;
    height: 100%;
    top: 0;
    width: 24px;
    left: -7px;
  }
}
.riverway-08 {
  margin-left: 433px;
  width: 1292px;
  position: relative;
  .riverway-08-image {
    width: 100%;
    vertical-align: top;
  }
  .riverway-08-name {
    position: absolute;
    font-size: 16px;
    right: -75px;
    top: 50%;
    margin-top: -14px;
    color: #ffffff;
    display: flex;
    align-items: center;
    img {
      width: 22px;
      transform: rotate(90deg);
      margin-right: 5px;
    }
  }
  .riverway-08-list {
    position: absolute;
    height: 24px;
    top: -7px;
    width: 100%;
    left: 0;
    display: flex;
  }
}
</style>

