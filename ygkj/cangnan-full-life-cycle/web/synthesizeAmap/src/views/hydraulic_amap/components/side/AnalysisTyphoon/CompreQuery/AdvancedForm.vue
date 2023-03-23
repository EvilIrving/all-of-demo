<template>
  <div class="query-form">
    <form-item label="时间选择">
      <form-item-inline label="年份" :label-width="47">
        <el-select style="width: 140px" v-model="startYear">
          <el-option
            v-for="(item, index) in yearList"
            :value="item.value"
            :label="item.value"
            :key="index"
          />
        </el-select>
        <span class="divider" />
        <el-select style="width: 140px" v-model="endYear">
          <el-option
            v-for="(item, index) in yearList"
            :value="item.value"
            :label="item.value"
            :key="index"
          />
        </el-select>
      </form-item-inline>
      <form-item-inline label="月份" :label-width="47">
        <el-select style="width: 140px" v-model="startMonth">
          <el-option
            v-for="(item, index) in monthList"
            :value="item.value"
            :label="item.value"
            :key="index"
          />
        </el-select>
        <span class="divider" />
        <el-select style="width: 140px" v-model="endMonth">
          <el-option
            v-for="(item, index) in monthList"
            :value="item.value"
            :label="item.value"
            :key="index"
          />
        </el-select>
      </form-item-inline>
    </form-item>
    <form-item label="登陆强度">
      <form-item-inline label="等级" unit="级" custom-label>
        <el-input v-model="minLandingLevel" />
        <span class="divider" />
        <el-input v-model="maxLandingLevel" />
      </form-item-inline>
      <!-- <form-item-inline label="中心气压" unit="级">
        <el-input />
        <span class="divider" />
        <el-input />
      </form-item-inline> -->
    </form-item>
    <form-item label="风力极值">
      <form-item-inline label="极值范围" unit="米/秒">
        <el-input v-model="minWindExtremum" />
        <span class="divider" />
        <el-input v-model="maxWindExtremum" />
      </form-item-inline>
    </form-item>
    <div class="action-slot">
      <div class="query-btn" @click="onAdvancedButton">
        <span>查询</span>
      </div>
    </div>
  </div>
</template>
<script>
import { FormItem, FormItemInline } from "../../components.js";
import moment from "moment";
export default {
  name: "AdvancedForm",
  components: { FormItem, FormItemInline },
  data() {
    return {
      startYear: "",
      endYear: "",
      yearList: new Array(100).fill(0).map((item, index) => {
        let curYear = new Date().getFullYear();
        return { value: curYear - index + "" };
      }),

      startMonth: "",
      endMonth: "",
      monthList: new Array(12).fill(0).map((item, index) => ({ value: `${index + 1}` })),

      minLandingLevel: "",
      maxLandingLevel: "",
      minWindExtremum: "",
      maxWindExtremum: "",
    };
  },
  methods: {
    getFormatYear(year, month) {
      return moment(`${year}/${month}/01 01:00:00`).format("yyyy-MM");
    },
    onAdvancedButton() {
      let { startYear, startMonth } = this;
      let { endYear, endMonth } = this;
      if ([startYear, startMonth, endYear, endMonth].includes("")) {
        this.$message.warning("请选择详细年份或月份。");
        return;
      }
      let { minLandingLevel, maxLandingLevel } = this;
      let { minWindExtremum, maxWindExtremum } = this;
      let begin = this.getFormatYear(startYear, startMonth);
      let end = this.getFormatYear(endYear, endMonth);

      let opt = {
        minLandingLevel,
        maxLandingLevel,
        minWindExtremum,
        maxWindExtremum,
        begin,
        end,
      };
      this.$emit("query", opt);
    },
  },
};
</script>
<style lang="scss" scoped>
.query-form {
  display: flex;
  flex-direction: column;
  margin-top: 12px;
  .divider {
    width: 20px;
    display: inline-block;
    position: relative;
    &::before {
      content: "";
      width: 80%;
      position: absolute;
      left: 10%;
      top: -5px;
      border-top: 1px solid #fff;
    }
  }
  .action-slot {
    width: 100%;
    margin-top: 16px;
    margin-bottom: 18px;
  }
  .query-btn {
    margin: 0 auto;
    cursor: pointer;
    width: 84px;
    height: 34px;
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border-radius: 4px;
    text-align: center;
    line-height: 34px;
  }
}
</style>
<style lang="scss">
.form-item {
  margin-left: 14px;
  .form-item__label {
    position: relative;
    &::before {
      position: absolute;
      content: "";
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #56fefe;
      top: 50%;
      left: -12px;
      transform: translateY(-50%);
    }
  }
  .form-item__content {
  }
}
</style>
<style lang="scss">
.form-item-inline {
  margin: 6px 0;
  > span.form-item-inline__label {
    display: inline-block;
    position: relative;
    > span {
      letter-spacing: 28px;
      white-space: nowrap;
    }
    &::after {
      content: "：";
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
    }
  }
  > div.form-item-inline__wrapper {
    display: inline-block;
    position: relative;
    .form-item-inline__content {
    }
    .form-item-inline__unit {
      margin-left: 8px;
    }
  }
}
</style>
