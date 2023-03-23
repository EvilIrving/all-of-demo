<template>

  <div class="brief-box" v-if="loading">
    <div class="export-btn" @click="exportList">导出</div>
    <h3 class="title-h3">
      <span style="font-weight:600;">
        <!-- <span>{{ bulletinDetail.year }}</span> -->
        {{ bulletinDetail.adnm }}
      </span>水库运管综合报告
    </h3>
    <p class="tip-p">
      <span>{{ bulletinDetail.year }}</span>
      年<span>{{ bulletinDetail.month }}</span>月<span>{{ bulletinDetail.day }}</span>日，据“智慧水库”动态监测显示，<span>{{ bulletinDetail.adnm }}</span>现有在册水库<span>{{ bulletinDetail.totalCount }}</span>座，其中：大型<span>{{ bulletinDetail.dx }}</span>座，中型<span>{{ bulletinDetail.zx }}</span>座，小(1)型<span>{{ bulletinDetail.xy }}</span>座，小(2)型<span>{{ bulletinDetail.xe }}</span>座，水库运行管理情况分析如下：
    </p>
    <h4 class="title-h4">一、除险保安</h4>
    <div class="title-div">
      风险研判
      <div class="line-title"></div>
    </div>
    <p class="text-p">
      根据水库“三色码”预警规则研判，<span class="red-span">{{
        bulletinDetail.redCount
      }}</span>座赋红码，<span class="yellow-span">{{
        bulletinDetail.yellowCount
      }}</span>座赋黄码，<span class="green-span">{{ bulletinDetail.greenCount }}</span>座赋绿码。
    </p>
    <div class="title-div">
      风险管控
      <div class="line-title"></div>
    </div>
    <h5 class="title-h5">1.安全鉴定与除险加固</h5>
    <p class="text-p">
      安全鉴定：目前，有三类坝水库<span>{{ bulletinDetail.aqjdSl }}</span>座，二类坝水库<span>{{ bulletinDetail.aqjdEl }}</span>座，有<span>{{ bulletinDetail.aqjdDq }}</span>座水库将在近一年内到期，需纳入安全鉴定工作计划。
    </p>
    <p class="text-p">
      除险加固：目前，<span>{{ bulletinDetail.aqjdSl }}</span>座三类坝水库中，已开工<span>{{ bulletinDetail.threeykg }}</span>座，未开工<span>{{ bulletinDetail.threewkg }}</span>座；<span>{{ bulletinDetail.aqjdEl }}</span>座二类坝水库，已开工<span>{{ bulletinDetail.twoykg }}</span>座，未开工<span>{{ bulletinDetail.twowkg }}</span>座。对尚未开工水库的需纳入除险加固计划，加快前期，争取早日开工。
    </p>
    <p class="text-p">
      存量清零：截至2021年6月底，有<span>{{ bulletinDetail.clqlTotal }}</span>座鉴定为三类坝水库纳入存量清零计划，要抓紧实施，目前，这批水库处于项目前期<span>{{ bulletinDetail.clqlXmqq }}</span>座，加固中<span>{{ bulletinDetail.clqlJgz }}</span>座，已完工<span>{{ bulletinDetail.clqlYwg }}</span>座，已竣工<span>{{ bulletinDetail.clqlYjg }}</span>座。
    </p>
    <h5 class="title-h5">2.超限制水位运行</h5>
    <p class="text-p">
      限制运用：目前，有<span>{{ bulletinDetail.xzyyCsj }}</span>座水库处于超设计水位运行，存在严重风险，需立即处置；有<span>{{
        bulletinDetail.xzyyCxz
      }}</span>座水库处于超限制水位运行，要密切关注，采取措施及时将水位降至限制水位以下。
    </p>
    <p class="text-p">
      三类坝：目前，<span>{{ bulletinDetail.aqjdSl }}</span>座三类坝水库，有<span>{{ bulletinDetail.kknum }}</span>座处于空库（限制）运行，<span>{{ bulletinDetail.xznum }}</span>座超限制水位运行，要采取措施立即将水位降至限制水位以下。
    </p>
    <h5 class="title-h5">3.隐患闭环处置</h5>
    <p class="text-p">
      今年以来，水库度汛检查、隐患排查已累计检查<span>{{
        bulletinDetail.checkNum
      }}</span>座次，共发现隐患问题<span>{{ bulletinDetail.problemNum }}</span>项，其中：已完成处置<span>{{ bulletinDetail.yczNum }}</span>项，未完成处置<span>{{ bulletinDetail.czzNum }}</span>项，对未完成处置问题，要抓紧落实措施闭环管理。
    </p>
    <h4 class="title-h4">二、提能增效</h4>
    <div class="title-div">
      纳蓄分析
      <div class="line-title"></div>
    </div>
    <p class="text-p">
      蓄水总量：全{{ bulletinDetail.adnm
      }}<span>{{ bulletinDetail.totalCount }}</span>座水库总库容<span>{{ bulletinDetail.totalStorage }}</span>亿方，正常库容<span>{{ bulletinDetail.cscnwl }}</span>亿方。目前，水库蓄水总量<span>{{ bulletinDetail.capableAll }}</span>亿方，总蓄水率<span>{{ bulletinDetail.storagePercAll }}</span>%，其中：大中型水库蓄水量<span>{{ bulletinDetail.capableDzx }}</span>亿方，蓄水率<span>{{ bulletinDetail.storagePercDzx }}</span>%，小型水库蓄水量<span>{{ bulletinDetail.capableXx }}</span>亿方，蓄水率<span>{{ bulletinDetail.storagePercXx }}</span>%。
    </p>
    <p class="text-p">
      可纳雨量：按照水库当前蓄水情况，结合库区集雨面积推算，至水库正常蓄水位，可纳雨量200mm以上的有<span>{{ bulletinDetail.twoHundredAbove }}</span>座，100～200 mm的有<span>{{ bulletinDetail.OneTwoHundred }}</span>座，50～100 mm的有<span>{{ bulletinDetail.fiftyOneHundred }}</span>座，50mm以下的有<span>{{ bulletinDetail.fiftyUnder }}</span>座，请密切关注未来降雨预报，结合防洪、供水需求及时做好水库调蓄。
    </p>
    <div class="title-div">
      防洪运用
      <div class="line-title"></div>
    </div>
    <p class="text-p">
      根据水库系统治理评估结论，{{
        bulletinDetail.adnm
      }}实际具有防洪功能的水库共<span>{{ bulletinDetail.fhskNum }}</span>座。
    </p>
    <p class="text-p">
      运行状态：当前，有<span>{{ bulletinDetail.gateflowNum }}</span>座水库正在泄洪，<span>{{ bulletinDetail.overflowNum }}</span>座水库正在溢洪。
    </p>
    <p class="text-p">
      防洪效益：今年以来，水库已拦洪<span>{{
        bulletinDetail.floodRetentionNum
      }}</span>座次，总拦洪水量<span>{{ bulletinDetail.floodRetention }}</span>亿方。
    </p>
    <div class="title-div">
      兴利效益
      <div class="line-title"></div>
    </div>
    <p class="text-p">
      在发挥防洪社会效益的同时，根据水库系统治理评估确定的实际功能，今年以来水库发挥了以下多种兴利效益。
    </p>
    <p class="text-p">
      供水效益：具有供水功能水库<span>{{ bulletinDetail.gsskNum }}</span>座，目前可供水量<span>{{ bulletinDetail.kgslTotal }}</span>亿方。根据月度供水量统计，今年以来已累计供水<span>{{
        bulletinDetail.gsslToral
      }}</span>万方。
    </p>
    <p class="text-p">
      灌溉效益：具有灌溉功能水库<span>{{ bulletinDetail.ggskNum }}</span>座，灌溉面积<span>{{ bulletinDetail.ggmjTotal }}</span>万亩。根据月度灌溉水量统计，今年以来已累计灌溉用水<span>{{
        bulletinDetail.ggyslTotal
      }}</span>万方。
    </p>
    <p class="text-p">
      发电效益：具有发电功能水库<span>{{ bulletinDetail.fdskNum }}</span>座，总装机容量<span>{{ bulletinDetail.installedTotal }}</span>万千瓦。根据月度发电量统计，今年以来已累计发电<span>{{
        bulletinDetail.powerGeneration
      }}</span>万千瓦时，相当于碳减排量<span>{{ bulletinDetail.tplTotal }}</span>万吨。
    </p>
    <p class="text-p">
      生态效益：具有生态功能水库<span>{{ bulletinDetail.stskNum }}</span>座。其中有<span>{{ bulletinDetail.scenicNum }}</span>座水库地处省级以上风景区内，是风景区的核心亮点。
    </p>
    <p class="tip-bottom-p">
      说明：以上分析报告的数据均来自于“智慧水库”综合计算成果，数据源均为“工程运管”中的每座水库。若分析结果与实际不符，可利用智慧水库之“一键生成、一键导出”功能深入分析原因，并在“工程运管”逐库核准。
    </p>
  </div>
</template>

<script>
import { bulletinApi } from "@/api/api_reservoir";
export default {
  data() {
    return {
      loading: false,
      bulletinDetail: {},
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
  },
  created() {
    this.bulletin();
  },
  methods: {
    async bulletin() {
      let res = await bulletinApi({
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.bulletinDetail = res;
        this.loading = true;
      }
      this.$emit("clearLoading");
    },
    exportList() {
      let adcd = this.sgzgSelectAdcd
        ? this.sgzgSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
        `/mgt/bm/reservoirWT/bulletinToWord` +
        `?adcd=${adcd}` +
        `&token=${token}`
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.brief-box {
  width: 96%;
  padding: 0 30px;
  margin: 0 auto;
  color: #333;
  font-family: "manrope";
  font-weight: 400;
  position: relative;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }
  .export-btn {
    width: 70px;
    height: 30px;
    line-height: 30px;
    border-radius: 4px;
    position: absolute;
    right: 12px;
    top: 0;
    border: 1px solid #ff9255;
    text-align: center;
    font-size: 16px;
    background-color: #ff9255;
    cursor: pointer;
  }
  span {
    color: #2952d0;
    &.red-span {
      color: #e4443d;
    }
    &.yellow-span {
      color: #ded052;
    }
    &.green-span {
      color: #6eec61;
    }
  }
  .title-h3 {
    font-size: 28px;
    text-align: center;
    font-weight: 600;
    line-height: 40px;
  }
  .title-h4 {
    font-size: 24px;
    padding-top: 14px;
    font-family: "youshe";
  }
  .title-h5 {
    padding-top: 8px;
    font-weight: 600;
    font-size: 20px;
  }
  .tip-p {
    padding-top: 16px;
    font-size: 18px;
  }
  .text-p {
    font-size: 18px;
    padding-top: 8px;
    line-height: 25px;
  }
  .tip-bottom-p {
    color: rgba(0, 0, 0, 0.6);
    font-size: 16px;
    padding-top: 40px;
  }
  .title-div {
    position: relative;
    font-size: 20px;
    font-family: "youshe";
    padding: 12px 0 0 22px;
    display: flex;
    &::after {
      position: absolute;
      left: 0;
      top: 13px;
      content: "";
      width: 16px;
      height: 16px;
      background: url("../../../assets/images/yuandian-p.png") no-repeat;
      background-size: 100% 100%;
    }
    &::before {
      position: absolute;
      right: 0;
      top: 14px;
      content: "";
      width: 28px;
      height: 15px;
      background: url("../../../assets/images/array-p.png") no-repeat;
      background-size: 100% 100%;
    }
    .line-title {
      height: 1px;
      width: calc(100% - 120px);
      background-color: #0170b0;
      margin: 9px 0 0 9px;
    }
  }
}

.el-loading-mask {
  background: #fff;
  color: #333;
  font-size: 16px;
}
</style>
