<!--
 * @Date: 2022-01-14 17:08:59
 * @Author: dtb
 * @Description: 推演
 * @LastEditors: dtb
 * @LastEditTime: 2022-08-22 17:26:30
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\Deduction.vue
-->
<template>
  <div ref="deduction" class="deduction_box">
    <div class="play_box">
      <span class="text">可纳雨量</span>
      <!-- <input type="text" v-model="inputValue"> -->
      <el-input autofocus class="input_text" placeholder="请输入" v-model="inputValue">
        <template slot="append">mm</template>
      </el-input>
      <div class="dedu_switch_select">
        <el-select popper-class="deduce_select" size="mini" v-model="interval" placeholder="请选择">
          <el-option v-for="item in intervalOption" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="map_switch_btn">
        <div v-loading='loading' class="switch_item active" @click="deducing">研判</div>
        <div class="switch_item active" @click="handleClose">取消</div>
        <div class="switch_item active" @click="exportList">导出</div>
      </div>
    </div>
    <!-- 表格 -->
    <div v-if="siteList.length !== 0" ref="tb_wrappers" class="sk_table_wrapper whitestyle">
      <!-- 纳蓄 -->
      <el-table v-loading='loading' :height='tableHeight' :data="siteList" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" align="center" width="50px"></el-table-column>
        <el-table-column prop="adcdctiy" label="所在市" align="center"></el-table-column>
        <el-table-column prop="adcdcountry" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column align="center">
          <template slot="header">
            <div>可纳雨量</div>
            <div>（mm）</div>
          </template>
          <template slot-scope="scope">
            <div v-show="scope.row.knRain >= 0">
              {{scope.row.knRain }}
            </div>
            <div v-show="scope.row.knRain < 0">0</div>
          </template>
        </el-table-column>
        <el-table-column prop="currenty" align="center">
          <template slot="header">
            <div>当前库容</div>
            <div>（万方）</div>
          </template>
        </el-table-column>
        <el-table-column prop="TATOL_STORAGE" align="center">
          <template slot="header">
            <div>总库容</div>
            <div>（万方）</div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { mutations } from 'vuex'
import { generateProject } from "@/api/api_reservoir";
export default {
  props: {
    sgzgSelectBasin: {
      type: String,
      required: true,
      default: ''
    },
    sgzgSelectAdcd: {
      type: String,
      required: true,
      default: ''
    },
    fullscreen: {
      type: String,
      default: false,
    }
  },
  data() {
    return {
      inputName: '',
      tableHeight: 0,
      loading: false,
      siteList: [],
      interval: 2,
      inputValue: '',
      intervalOption: [{ label: '以上', value: 2 }, { label: '以下', value: 1 },]
    };
  },
  computed: {

  },
  created() {

  },
  mounted() {
    this.$emit('startDeducing', false)
    this.selectAdcd = this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd
  },
  watch: {
    fullscreen(val) {
      this.resizeTable()
    }
  },
  methods: {
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.deduction.clientHeight - 60;
        this.tableHeight = height;
      });
    },
    async getTableList() {
      this.loading = true
      let opt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        gpType: 6,
        rain: this.inputValue,
        ifUnder: this.interval,
      }
      let res = await generateProject(opt)
      if (res.success) {
        this.loading = false
        this.$emit('startDeducing', res.success)
        this.siteList = res.data
      }
      this.resizeTable()
    },
    deducing() {
      if (this.inputValue === '') {
        this.$emit('startDeducing', false)
        this.$message.warning('请填写可纳雨量')
        this.siteList = []
        return;
      }
      this.getTableList()
      // let showPlayList = true
      this.$store.commit('SET_RAIN', this.inputValue)
      this.$store.commit('SET_IFUNDER', this.interval)
      // this.$emit('handleClose')
    },
    handleClose() {
      this.$emit('handleClose')
    },
    exportList() {
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
        `/mgt/bm/reservoirWT/generateProjectToExcel` +
        `?adcd=${this.selectAdcd}` +
        `&basinCode=${this.sgzgSelectBasin}` +
        `&ifUnder=${this.interval}` +
        `&rain=${this.inputValue}` +
        `&gpType=6` +
        `&token=${token}`
      );
    },
  },
};
</script>

<style scoped lang="scss">
.deduction_box {
  width: 100%;
  height: 100%;
  // @include flexbox();
  // @include flexAI(center);
  // @include flexJC(space-around);
  // @include flexflow(column nowrap);
  .play_box {
    height: 40px;
    @include flexbox();
    @include flexAI(center);
    @include flexJC(space-around);
    .text {
      width: 80px;
      height: 40px;
      font-size: 18px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      line-height: 40px;
    }
  }
}
.map_switch_btn {
  width: 30%;
  @include flexbox();
}
.switch_item {
  color: #fff;
  @include flexbox();
  @include flexAI(center);
  @include flexJC(center);
  cursor: pointer;
  box-sizing: border-box;
  margin-right: 5px;
  width: 100px;
  height: 40px;
  line-height: 44px;
  text-align: center;
  font-size: 18px;
  border-width: 1px;
  border-style: solid;
  border-color: #fff;
  border-radius: 4px;
  background: #ff9255;
  &.active {
    background: #ff92559e;
  }
}
</style>

<style lang="scss">
.deduction_box {
  // 输入框
  .input_text {
    width: 20%;
    .el-input__inner {
      font-size: 16px;
    }
  }
  .el-input-group--append .el-input__inner,
  .el-input-group__prepend,
  .el-input-group__append {
    border-color: #ccc;
    border-radius: 0px;
    font-size: 16px;
  }
  // 下拉框
  .dedu_switch_select {
    width: 15%;
    .el-select {
      .el-input__inner {
        height: 40px;
        font-size: 16px;
        border-color: #ccc;
      }
    }
    .deduce_select {
      width: 100%;
      .el-input {
        width: 100%;
        height: 40px;
      }
      .el-select-el-select-dropdown__item {
        height: 50px;
        line-height: 50px;
        font-size: 16px;
        &.selected {
          color: #6ee4e5;
        }
      }
    }
  }
}
</style>
