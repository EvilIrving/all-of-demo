<!--
 * @Date: 2022-01-12 20:50:02
 * @Author: dtb
 * @Description: 推演清单内容
 * @LastEditors: dtb
 * @LastEditTime: 2022-01-20 14:45:07
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\components\PlayList.vue
-->

<template>
  <div ref="wrapper" class="play_wrapper">
    <div ref="query" class="query_content">
      <!-- 查询区域 -->
      <template>
        <el-select popper-class="play_query_select" clearable v-model="selectAdcd" placeholder="请选择市县" @change="getTableList">
          <el-option v-for="item in adcdList" :key="item.adcd" :label="item.label" :value="item.adcd">
          </el-option>
        </el-select>
        <el-input v-model="inputName" placeholder="请输入名称" clearable @input="getTableList"></el-input>
        <el-button plain @click="getTableList">查询</el-button>
        <el-button plain @click="exportList">导出</el-button>
      </template>
    </div>
    <!-- 表格 -->
    <div ref="tb_wrappers" class="tb_wrappers">
      <!-- 纳蓄 -->
      <el-table v-loading='loading' :height='tableHeight' :data="siteList" style="width: 100%">
        <el-table-column prop="adcdctiy" label="所在市" align="center"></el-table-column>
        <el-table-column prop="adcdcountry" label="所在县" align="center"></el-table-column>
        <el-table-column prop="NAME" label="名称" align="center"></el-table-column>
        <el-table-column prop="scale" label="规模" align="center"></el-table-column>
        <el-table-column prop="" label="可纳雨量（万方）" align="center">
          <template slot-scope="scope">
            <div>
             -
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="currenty" label="当前库容（万方）" align="center"></el-table-column>
        <el-table-column prop="TATOL_STORAGE" label="总库容（万方）" align="center"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { listADCDtreeApi } from '@/api/api_utils'
import { generateProject } from "@/api/api_reservoir";
export default {
  props: {
    // checkedArr: {
    //   type: Array,
    //   required: false,
    //   default: []
    // },
    sgzgMapType: {
      type: String,
      required: true,
      default: ''
    },
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
  },
  data() {
    return {
      adcdList: [],
      selectAdcd: '',
      inputName: '',
      tableHeight: 0,
      loading: false,
      siteList: [],

    };
  },
  computed: {
    gpType() {
      return 6
    },
    ifUnder() {
      return this.$store.state.ifUnder
    },
    rain() {
      return this.$store.state.rain
    },
    // projectType() {
    //   let tempArr = []
    //   this.checkedArr.forEach(element => {
    //     switch (element) {
    //       case '大(1)型':
    //         tempArr.push(1)
    //         break;
    //       case '大(2)型':
    //         tempArr.push(2)
    //         break;
    //       case '中型':
    //         tempArr.push(3)
    //         break;
    //       case '小(1)型':
    //         tempArr.push(4)
    //         break;
    //       case '小(2)型':
    //         tempArr.push(5)
    //         break;
    //     }
    //   });
    //   return tempArr.join(',')
    // },
  },
  created() {
  },
  mounted() {
    this.resizeTable();
    this.selectAdcd = this.sgzgSelectAdcd ? this.sgzgSelectAdcd : this.$localData("get", "userInfo").adcd
    this.getAdcdOptions()
    this.getTableList()

  },
  watch: {

  },
  methods: {
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
        this.tableHeight = height;
      });
    },
    async getAdcdOptions() {
      let res = await listADCDtreeApi({
        parentadcd: this.$localData("get", "userInfo").adcd,
        fetchAll: true
      })
      if (res.success) {
        res.listTree.map((v, i) => {
          this.adcdList.push({
            label: v.ADNM,
            adcd: v.ADCD,
            value: i
          })
        })
      }
    },
    async getTableList() {
      this.loading = true
      let opt = {
        adcd: this.selectAdcd,
        basinCode: this.sgzgSelectBasin,
        name: this.inputName,
        gpType: this.gpType,
        ifUnder: this.ifUnder,
        rain: this.rain,
      }
      let res = await generateProject(opt)
      if (res.success) {
        this.loading = false
        this.siteList = res.data
      }
    },
    exportList() {
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
        `/mgt/bm/reservoirWT/generateProjectToExcel` +
        `?adcd=${this.selectAdcd}` +
        `&basinCode=${this.sgzgSelectBasin}` +
        `&projectScale=${this.projectType}` +
        `&ifUnder=${this.$store.state.ifUnder}` +
        `&rain=${this.$store.state.rain}` +
        `&gpType=${this.gpType}` +
        `&token=${token}`
      );
    },
  },
  components: {

  },
};
</script>
<style  scoped lang="scss">
.green {
  color: #58c056;
}
.red {
  color: #ff3153;
}
.yellow {
  color: #f7b500;
}
</style>
<style lang="scss">
.play_wrapper {
  // 查询样式
  font-size: 16px;
  width: 100%;
  height: 100%;
  .query_content {
    // 下拉样式
    margin: 0 1%;
    .el-input {
      width: 30%;
      margin: 0 2%;
    }
    .el-select {
      border-color: #448ef7;
      background: #03338d;
      width: 30%;
      & > .el-input {
        width: 100%;
      }
    }
    // 输入框样式
    .el-input .el-input__inner,
    .el-input.is-disabled .el-input__inner,
    .el-textarea .el-textarea__inner,
    .el-textarea.is-disabled .el-textarea__inner {
      background: #052176;
      border: 1px solid #1990ff;
    }

    .el-input__inner {
      color: #fff;
      font-size: 16px;
    }
    // 按钮样式
    .el-button {
      width: 100px;
      color: #fff;
      border-radius: 0;
      background: #063fb2;
      border: 2px solid #1990ff;
      font-size: 16px;
    }
    .el-button.is-plain:focus,
    .el-button.is-plain:hover {
      background: #063fb29c;
      border: 2px solid #1990ff9d;
      color: #fff;
    }
  }
  // 表格样式
  .tb_wrappers {
    width: 100%;
    height: 100%;
    margin: 1% 1% 0px;
    .el-loading-mask,
    .el-table__empty-block,
    .el-table__body-wrapper.is-scrolling-none,
    .el-table__body tr td {
      background-color: #052176;
      color: #ffffff;
      font-size: 16px;
    }
    .el-table {
      .el-table__header-wrapper th {
        background-color: #03338d;
        border: 1px solid #0051b5;
        color: #03e6f0;
        font-size: 16px;
      }
      th,
      tr {
        background-color: #03338d;
        font-size: 16px;
      }
      .el-table--border td,
      .el-table--border th,
      .el-table__body-wrapper
        .el-table--border.is-scrolling-left
        ~ .el-table__fixed {
        border-right: none;
      }
      td,
      th.is-leaf {
        border-bottom: 1px solid #3390e162;
      }
      .el-table__row {
        background-color: #03338d;
        &:hover {
          background-color: #fff;
        }
      }
    }
    .el-table--border td,
    .el-table--border th,
    .el-table__body-wrapper
      .el-table--border.is-scrolling-left
      ~ .el-table__fixed {
      border-right: none;
    }
    .el-table--border,
    .el-table--group {
      border: none;
    }
    .el-table--border::after,
    .el-table--group::after,
    .el-table::before {
      content: "";
      position: absolute;
      background-color: #03338d;
      z-index: 1;
    }
    .el-table--border::after,
    .el-table--group::after,
    .el-table::before {
      content: "";
      position: absolute;
      background-color: #03338d;
      z-index: 1;
    }
  }
}
.play_query_select {
  border-color: #448ef7;
  background: #03338d;
  .el-select-dropdown__list {
    padding: 0;
  }
  .el-select-dropdown__item {
    color: #fff;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    &.selected {
      color: #6ee4e5;
      background: #18407c;
    }
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background: #18407c;
  }
}
</style>
