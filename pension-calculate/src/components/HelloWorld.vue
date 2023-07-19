<template>
  <div class="wrapper">
    <h2>
      Pension Calculator
      <icon class="" name="info-circle" size="medium" />
    </h2>
    <info-tips type="warning" :isDot="false" :value="0"></info-tips>
    <t-form :data="formData" :rules="rules" ref="form" resetType='initial' :labelWidth="240" @reset="onReset" @submit="onSubmit">
      <t-form-item label="年龄" name="age">
        <t-input-number theme="normal" align="center" suffix="岁" style="width: 70%" :max="65" :min="1" v-model="formData.age"></t-input-number>
      </t-form-item>
      <t-form-item label="预计退休年龄" name="retirementAge">
        <t-input-number theme="normal" align="center" suffix="岁" style="width: 70%" :max="70" :min="40" v-model="formData.retirementAge"></t-input-number>
      </t-form-item>

      <t-form-item label="公司为你申报的缴纳基数" name="contributoryBaseWage">
        <t-input-number theme="normal" align="center" suffix="元" style="width: 70%" v-model="formData.contributoryBaseWage"></t-input-number>
      </t-form-item>
      <t-form-item label="企业缴费比例" name="enterpriseRate">
        <t-input-number theme="normal" align="center" suffix="%" style="width: 70%" v-model="formData.enterpriseRate"></t-input-number>
      </t-form-item>
      <t-form-item label="个人缴费比例" name="individualRate">
        <t-input-number align="center" suffix="%" theme="normal" style="width: 70%" v-model="formData.individualRate"></t-input-number>
      </t-form-item>
      <t-form-item label="月缴费额(元)(自动)" name="monthlyContribution">
        <t-input-number theme="normal" align="center" suffix="元" style="width: 70%" v-model="formData.monthlyContribution"></t-input-number>
      </t-form-item>
      <t-form-item label="参保地上一年社会月平均工资" name="averageSocialWage">
        <t-input-number theme="normal" align="center" suffix="元" style="width: 70%" v-model="formData.averageSocialWage"></t-input-number>
      </t-form-item>
      <t-form-item label="社会月平均工资增长率" name="averageSocialWageGrowth">
        <t-input-number theme="normal" align="center" suffix="%" style="width: 70%" v-model="formData.averageSocialWageGrowth"></t-input-number>
      </t-form-item>
      <t-form-item label="缴纳基数增长率" name="wageGrowth">
        <t-input-number theme="normal" align="center" suffix="%" style="width: 70%" v-model="formData.wageGrowth"></t-input-number>
      </t-form-item>

      <t-form-item label="个人账户余额" name="balance">
        <t-input-number theme="normal" align="center" suffix="元" style="width: 70%" v-model="formData.balance"></t-input-number>
      </t-form-item>
      <t-form-item label="个人账户记账利率" name="personalRate">
        <t-input-number theme="normal" align="center" suffix="元" style="width: 70%" v-model="formData.personalRate"></t-input-number>
      </t-form-item>
      <t-form-item class="center-btn" style="margin-left: 100px">
        <t-space size="10px">
          <t-button theme="primary" type="submit">Calc</t-button>
          <t-button theme="default" variant="base" type="reset">Reset</t-button>
        </t-space>
      </t-form-item>
    </t-form>

    <h5 v-show="calcResData.pensionMoney !== 0">{{calcResData.pensionMoney}}</h5>
    <section class="notes">
      <!-- <h3>说明：</h3> -->
      <h3>计算公式：</h3>
      <h4>退休后每个月个人账户领到的钱：</h4>
      <p>（缴纳基数 * 8% * 缴纳年数 + 这些钱在这些年的利息）/ 计发月份</p>

      <h4>退休后每个月统筹账户领到的钱：</h4>
      <p>计发基数 * （1 + 缴纳工资指数）/ 2 * 缴纳年数 * 1%</p>
      <p>其中缴纳工资指数是‘每月缴纳基数/当年社会平均工资的累月平均值’</p>
      <!-- <h4>注意：上述数据均为预期账面数值，并未考虑通货膨胀因素。</h4> -->
    </section>

    <!-- <t-button>计算</t-button> -->
    <!-- <td-icon></td-icon> -->

  </div>
</template>

<script>
import InfoTips from './info/InfoTips.vue';
import { calcPension } from "./util";
const INITIAL_DATA = {
  age: 25, // 年龄
  retirementAge: 60, // 预计退休年龄
  contributoryBaseWage: 4494, // 公司为你申报的缴纳基数
  wageGrowth: 0.01,// 预计工资增长率
  averageSocialWage: 8309, // 社会平均工资
  averageSocialWageGrowth: 0.01, // 社会平均工资增长率

  enterpriseRate: 16, // 企业缴费比例
  individualRate: 8, // 个人缴费比例
  monthlyContribution: 100, // 月缴费额（自动）

  balance: 10000, // 当前账户余额
  personalRate: 0.02, // 个人账户利率

};
const MonthSheat = {
  40: 233,
  41: 230,
  42: 226,
  43: 223,
  44: 220,
  45: 216,
  46: 230,
  47: 230,
  48: 230,
  49: 230,
  50: 195,
  51: 230,
  52: 230,
  53: 230,
  54: 230,
  55: 170,
  56: 230,
  57: 230,
  58: 230,
  59: 230,
  60: 139,
  61: 230,
  62: 230,
  63: 230,
  64: 109,
  65: 101,
  66: 93,
  67: 84,
  68: 75,
  69: 65,
  70: 56,
}
export default {
  components: { InfoTips },
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
    return {
      formData: { ...INITIAL_DATA },
      rules: {
        age: [{ required: true, message: '此项必填' }],
        retirementAge: [{ required: true, message: '此项必填' }],
        contributoryBaseWage: [{ required: true, message: '缴费基数工资必填', type: 'error' }],
        averageSocialWage: [{ required: true, message: '此项必填' }],
        enterpriseRate: [{ required: true, message: '此项必填' }],
        individualRate: [{ required: true, message: '此项必填' }],
        monthlyContribution: [{ required: true, message: '此项必填' }],
      },
      calcResData: {
        pensionMoney: 0
      },
    }
  },
  methods: {
    onReset() {
      this.$message.success('重置成功');
      console.log('formData', this.formData);
    },
    onSubmit({ validateResult, firstError }) {
      if (validateResult === true) {
        this.$message.success('提交成功');
        this.calcResData = calcPension(this.formData)
      } else {
        console.log('Errors: ', validateResult);
        this.$message.warning(firstError);
      }
    },
    handleClear() {
      this.$refs.form.clearValidate();
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.wrapper {
  position: relative;
  width: 50%;
  height: 100%;
  margin: 0 auto;
}
.notes {
  font-size: 14px;
  text-align: left;
}
.center-btn ::v-deep .t-form__controls-content {
  justify-content: center;
}
h2 {
  margin: 20px 10px;
}
</style>
