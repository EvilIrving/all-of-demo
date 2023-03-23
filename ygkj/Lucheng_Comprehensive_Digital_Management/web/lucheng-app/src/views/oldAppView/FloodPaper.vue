<template>
  <div class="flood_wrap">
    <van-nav-bar placeholder :border='false' safe-area-inset-top :title="$route.meta.title" left-text="" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="head_title">
      <p>
        截至{{this.rainConditionForm.day}}日{{this.rainConditionForm.hour}}时，全区汛情摘要如下：
      </p>
    </div>
    <div class="rain_condition">
      <wrap-title title="雨情" />
      <div class="rain_content">
        <div class="rain_item">
          <section>全区24小时降雨量为</section>
          <section></section>
          <section>{{this.rainConditionForm['24Hour']}}</section>mm
        </div>
        <div class="rain_item">
          <section>连续72小时面雨量为</section>
          <section></section>
          <section>{{this.rainConditionForm['72Hour']}}</section>mm
        </div>
        <div class="rain_item">
          <section>超过30毫米站点</section>
          <section style="font-size: 16px;">{{this.rainConditionForm.proprotion}}</section>
          <section>{{this.rainConditionForm.surpass30}}</section>座
        </div>
        <div class="rain_item">
          <section>降雨量最大的站点</section>
          <section>{{this.rainConditionForm.rainMaxStnm}}</section>
          <section>{{this.rainConditionForm.rainMax1}}</section>mm
        </div>
        <div class="rain_item">
          <section>最大降水的镇街村社</section>
          <section>{{this.rainConditionForm.rainMaxAdnm}}</section>
          <section>{{this.rainConditionForm.rainMax2}}</section>mm
        </div>
        <div class="rain_item">
          <section>连续72小时面雨量为</section>
          <section></section>
          <section>{{this.rainConditionForm['72Hour']}}</section>mm
        </div>
        <div class="rain_item">
          <section>超过30毫米站点</section>
          <section style="font-size: 16px;">{{this.rainConditionForm.proprotion}}</section>
          <section>{{this.rainConditionForm.surpass30}}</section>座
        </div>
        <div class="rain_item">
          <section>降雨量最大的站点</section>
          <section>{{this.rainConditionForm.rainMaxStnm}}</section>
          <section>{{this.rainConditionForm.rainMax1}}</section>mm
        </div>
        <div class="rain_item">
          <section>最大降水的镇街村社</section>
          <section>{{this.rainConditionForm.rainMaxAdnm}}</section>
          <section>{{this.rainConditionForm.rainMax2}}</section>mm
        </div>
      </div>
      <div class="water_reservoir">
        <wrap-title title="水库水情" />
        <div style="width:96%;margin: 0 auto;" class="water_content">
          <div class="rain_item">
            <section>全区超汛限水库</section>
            <section></section>
            <section>{{reservoirStationNum}}</section>座
          </div>
          <div class="table_box">
            <table-list :theadList="reservoirTheadList" :list="reservoirStationList" :finished="finished" v-if="reservoirStationList.length!==0">
            </table-list>
          </div>
        </div>

      </div>
      <div class="river_reservoir">
        <wrap-title title="江河水情" />
        <div style="width:96%;margin: 0 auto;" class="river_content">
          <div class="rain_item">
            <section>全区水位超警江河</section>
            <section></section>
            <section>{{riverStationNum}}</section>座
          </div>
          <div class="table_box">
            <table-list :theadList="riverStationTheadList" :list="riverStationList" :finished="finished" v-if="riverStationList.length!==0">
            </table-list>
          </div>
        </div>
      </div>
      <div class="sealevel_reservoir">
        <wrap-title title="潮位水情" />
        <div style="width:96%;margin: 0 auto;" class="sealevel_content">
          <div class="rain_item">
            <section>全区超警潮位站</section>
            <section></section>
            <section>{{waterStationNum}}</section>座
          </div>
        </div>
        <div class="table_box">
          <table-list :theadList="waterStationTheadList" :list="waterStationList" :finished="finished" v-if="waterStationList.length!==0">
            <template v-slot:stName='item'><span class="blue-color">{{item.data.stName}}</span></template>
          </table-list>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import wrapTitle from "@/components/wrapTitle";
import { floodPaper } from "@/api/appView";
import TableList from "@/components/tableList";
export default {
  name: "FloodPaper",
  prop: {},
  components: {
    TableList,
    wrapTitle
  },
  data() {
    return {
      rainConditionForm: {},
      hour24: 0,
      hour72: 0,

      waterStationList: [],
      waterStationNum: '',
      waterStationTheadList: [
        {
          label: '站名',
          prop: 'stName',
          slot: true
        }, {
          label: '水位',
          prop: 'tdz',
          slot: false
        }, {
          label: '警戒',
          prop: 'warningLevel',
          slot: false
        }, {
          label: '超警高度',
          prop: 'overWater',
          slot: false
        },

      ],
      finished: true,

      riverStationList: [],
      riverStationNum: '',
      riverStationTheadList: [
        {
          label: '站名',
          prop: 'stName',
          slot: true
        }, {
          label: '水位',
          prop: 'tdz',
          slot: false
        }, {
          label: '警戒',
          prop: 'warningLevel',
          slot: false
        }, {
          label: '超警高度',
          prop: 'overWater',
          slot: false
        },

      ],

      reservoirStationList: [],
      reservoirStationNum: '',
      reservoirTheadList: [
        {
          label: '站名',
          prop: 'stName',
          slot: true
        }, {
          label: '水位',
          prop: 'tdz',
          slot: false
        }, {
          label: '汛限',
          prop: 'warningLevel',
          slot: false
        }, {
          label: '超汛高度',
          prop: 'overWater',
          slot: false
        },
      ]



    }
  },
  mounted() {
    this.getRainCondition()
  },
  methods: {
    async getRainCondition() {
      let res = await floodPaper.rainCondition({

      })
      if (res.code == 0) {
        this.rainConditionForm = res.data
      }
      let res1 = await floodPaper.waterStation({
        stationType: '',
        warning: true
      })
      if (res1.code === 0) {
        for (var item of res1.data) {
          if (item.stType === 'TT') {
            this.waterStationList.push({
              stName: item.stName,
              tdz: item.tdz,
              warningLevel: item.warningLevel,
              overWater: item.tdz - item.warningLevel
            })
          }
          if (item.stType === 'ZZ') {
            this.riverStationList.push({
              stName: item.stName,
              tdz: item.tdz,
              warningLevel: item.warningLevel,
              overWater: item.tdz - item.warningLevel
            })
          }
          if (item.stType === 'RR') {
            this.reservoirStationList.push({
              stName: item.stName,
              tdz: item.tdz,
              warningLevel: item.warningLevel,
              overWater: item.tdz - item.warningLevel
            })
          }
        }
        this.waterStationNum = this.waterStationList.length
        this.riverStationNum = this.riverStationList.length
        this.reservoirStationNum = this.reservoirStationList.length

      }

    }

  }

}
</script>

<style scoped lang="scss">
.flood_wrap {
  background-color: #fff;
  .head_title {
    height: 50px;
    background: url("~@/assets/images/pageIndex/floodpaper_bg.png") no-repeat;
    background-size: 100% 100%;
    border-radius: 0px 0px 8px 8px;
    width: 375px;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 250;
    color: #ffffff;
    p {
      line-height: 50px;
      margin-left: 9px;
    }
  }
  .title {
    height: 32px;
    border-bottom: 1px solid #e9f0fd;
    padding-top: 20px;
    span {
      margin-left: 5px;
    }
    img {
      margin-left: 8px;
    }
  }
  .rain_content {
    width: 96%;
    margin: 0 auto;
  }
  .rain_item {
    width: 95%;
    height: 44px;
    background-color: #f6faff;
    border-radius: 4px;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    color: #666666;
    padding: 0 5px;
    margin: 8px auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    section {
      font-family: D-DINExp, D;
      font-weight: normal;
      position: relative;
      &:nth-child(1) {
        &::before {
          content: "";
        }
        width: 35%;
        padding-left: 8px;
        line-height: 16px;
        border-left: 3px solid #276af0;
      }
      &:nth-child(2) {
        text-align: right;
        width: 25%;
        
        color: #276af0;
      }
      &:nth-child(3) {
        width: 30%;
        font-size: 16px;
        text-align: right;
        color: #276af0;
      }
    }
  }
  .table_box {
    table {
      width: 97%;
      height: 100%;
      display: table;
      border: 0.5px solid #e6e6e6;
      box-sizing: border-box;
      border-collapse: collapse;
      border-spacing: 0px;
      margin: auto;
      td {
        text-align: center;
        line-height: 44px;
        border: 0.5px solid #e6e6e6;
      }
      tr {
        height: 44px;
      }
      thead {
        background: #ddecff;
        tr > th {
          font-size: 12px;
          font-weight: 350;
          line-height: 44px;
        }
      }
      tbody {
        tr > td {
          font-size: 12px;
          font-family: D-DINExp, D;
          font-weight: normal;
          color: #666666;
        }
        tr > td:nth-child(2) {
          color: #276af0;
        }
      }
    }
  }
}
</style>
<style>
.flood_wrap .van-nav-bar--fixed,
.flood_wrap .van-nav-bar__placeholder {
  background-color: #276af0;
}
.flood_wrap .van-nav-bar{
  padding-top: 30px;
  background-color: #276af0;
}
.flood_wrap .van-nav-bar__title,.flood_wrap .van-icon-arrow-left,.flood_wrap .van-nav-bar__content  {
  color: #fff;
}
</style>