<template>
  <div class="flood-detail-content map-dialog">
    <div class="base-table">
      <table width="100%">
        <tr>
          <td class="prop-td">总户数</td>
          <td>{{detailInfo.tho | EmptyFormat}}户</td>
          <td class="prop-td">总人口</td>
          <td>{{detailInfo.tpo | EmptyFormat}}人</td>
        </tr>
        <tr>
          <td class="prop-td">防汛责任人</td>
          <td>{{chargeManInfo}}</td>
          <td class="prop-td">代表站</td>
          <td>{{detailInfo.stationName | EmptyFormat}}</td>
        </tr>
      </table>
    </div>
    <div class="warn-cards">
      <div 
        class="warn-card-item" 
        v-for="(item,index) in detailInfo.list" 
        :key="index"
      >
        <table width="100%">
          <tr>
            <td class="prop-td" colspan="2">{{item.period}}小时报警</td>
          </tr>
          <tr>
            <td class="prop-td">实时雨量</td>
            <td>{{item.realTimeRainfall | EmptyFormat}}mm</td>
          </tr>
          <tr>
            <td class="prop-td">准备转移</td>
            <td style="color:#E89100">{{item.readyMove | EmptyFormat}}mm</td>
          </tr>
          <tr>
            <td class="prop-td">立即转移</td>
            <td style="color:#FF551F">{{item.immediateTransfer | EmptyFormat}}mm</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/RightSildePanel/index";
export default {
  name: "FloodDetailContent",
  props: {
    pointData:{
      type:Object
    }
  },
  data() {
    return {
      detailInfo:{}
    };
  },
  computed: {
    ...mapGetters([
      "previewModule",
      "formatterCurrentTime",
      "AffairId"
    ]),
    chargeManInfo(){
      let info = "";
      if(this.detailInfo.chargerName){
        info += this.detailInfo.chargerName + ' '; 
      }
      if(this.detailInfo.chargerPhone){
        info +=this.detailInfo.chargerPhone;
      }
      return info;
    },
  },
  created(){
    this.flashFloodDetails();
  },
  components: {
  },
  watch: {},
  methods: {
    async flashFloodDetails(){
      let opt = {
        nvi:this.pointData.nvi,
        avi:this.pointData.avi
      }
      if(this.previewModule){
        opt.endTime = new Date(this.formatterCurrentTime()).format("yyyy-MM-dd hh:mm:ss");
        opt.drillAffairId = this.AffairId;
      }
      let res = await disasterPreventionApi.flashFloodDetails(opt);
      if(res.code == 0){
        this.detailInfo = res.data;
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.flood-detail-content {
  height: 100%;
  width: 100%;
  height: 600px;
  overflow-y: auto;
  table {
    tr {
      td {
        font-size: 16px;
        padding: 12px;
        color: #fff;
        line-height: 20px;
        border: 1px solid rgba(0, 193, 255, 0.4);
        &.prop-td {
          text-align: center;
          width: 160px;
          color: #56fefe;
          background-image: linear-gradient(
            90deg,
            rgba(44, 158, 191, 0.3) 3%,
            rgba(41, 147, 180, 0.1) 100%
          );
          vertical-align: middle;
        }
        a{
          display: inline-block;
          vertical-align: middle;
          font-size: 16px;
          color: #56FEFE;
          margin-right: 10px;
        }
      }
    }
  }
  .warn-cards{
    @include flexbox;
    @include flexflow(row,wrap);
    @include flexAC;
    @include flexJC(space-between);
    margin-top: 16px;
    .warn-card-item{
      width: 31%;
      margin-bottom: 16px;
    }
  }
  
}
</style>
