<!--
 * @Date: 2022-01-05 16:13:26
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-07 09:09:52
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\codeForm\CodeSetting.vue
-->
<template>
  <div class="code-setting">
    <div class="explain-box">请输入不同状态健康码对应的得分范围</div>
    <div class="score-box">
      <el-row>
        <el-col :span="8" v-for="(item, index) in codeList" :key="index">
          <el-row>
            <el-col :span="3">{{item.category}}:</el-col>
            <el-col :span="19">
              <el-row>
                <el-col :span="7">
                  <el-input style="border-radius: 2px; border: 1px solid #239DEF;" :min="0" :max="100" size="small" v-model="item.minVal" placeholder="请输入"></el-input>
                </el-col>
                <el-col :span="2">-</el-col>
                <el-col :span="7">
                  <el-input style="border-radius: 2px;border: 1px solid #239DEF;" :min="0" :max="100" size="small" v-model="item.maxVal" placeholder="请输入"></el-input>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
    <div class="child-btn mt16">
      <span class="btn derive-sty " @click="sure">确定</span>
      <span class="btn" @click="close">取消</span>
    </div>
  </div>
</template>

<script>
import { setCode, getCode } from '@/api/seawall.js';
export default {
  name: "CodeSetting",
  props: {
    info: Object,
  },
  data() {
    return {
      input: "",
      codeList: [],
      times: 0,
    };
  },
  components: {},
  created() { },
  mounted() {
    this._getAchievementCode();
  },
  methods: {
    // 获取健康码配置信息
    async _getAchievementCode() {
      let res = await getCode(this.info.code);
      if (res.code == 0) {
        if (res.data && res.data.length > 0) {
          this.codeList = res.data;
        }
      }
    },
    // 配置档案码信息
    async sure() {
      console.log(this.codeList);
      let res = await setCode(this.codeList);
      if (res.code == 0) {
        this.$message.success("保存成功！");
      } else {
        this.$message.error("保存失败，请稍后再试！");
      }
    },
    close() {
      this.$emit("close");
    },
  },
};
</script>
<style lang="scss" scoped>
@import './style.scss';
.score-box {
  width: 100%;
  height: 404px;
  padding: 0 22px;
  .el-row {
    line-height: 32px;
    .el-col.el-col-2 {
      text-align: center;
    }
  }
}
.btn {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  float: right;
  color: #00baff;
  border: 1px solid #00baff;
  background: rgba(35, 157, 239, 0.12);
}
.derive-sty {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  float: right;
  color: #fff;
  border-color: #0639ff;
  background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
}
.explain-box {
  width: 1256px;
  height: 44px;
  background: rgba(35, 157, 239, 0.12);
  @include flexbox();
  align-items: center;
  margin: 10px 0px 10px;
  color: #23e3fb;
  .black {
    width: 111px;
    height: 22px;
    font-size: 16px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #23e3fb;
    line-height: 22px;
    margin-left: 10px;
  }
}
.el-col-3 {
  width: 48px;
  color: #ffffff;
}
</style>

<style>
.el-input__inner {
  text-align: center;
}
</style>