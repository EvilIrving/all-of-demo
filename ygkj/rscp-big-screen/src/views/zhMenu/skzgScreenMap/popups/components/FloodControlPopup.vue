<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:10:28
 * @Description: 防洪弹窗 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\FloodControlPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.prnm }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.projectScale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.projectScale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.projectScale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.projectScale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.projectScale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="el-dialog__close" @click="closePopup()"></i>
    </div>
    <div class="sk_table_wrapper whitestyle">
      <el-table v-loading='loading' :height='tableHeight' stripe border :data="siteList" style="width: 100%">
        <el-table-column sortable prop="dispatchtime" min-width="150" label="时间" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.dispatchtime ? scope.row.dispatchtime.slice(0,13) + '时' : '-'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gateflow" label="闸门泄洪(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="overflow" label="溢流(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="electricityflow" label="发电流量(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="watersupplyflow" label="供水流量(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="ecoflow" label="生态流量(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="totalflow" show-overflow-tooltip label="合计出库流量(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="inputflow" label="入库流量(立方米/秒)" align="center"></el-table-column>
        <el-table-column prop="remark" show-overflow-tooltip label="调度事项记录" align="center"></el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import { floodListJson } from "@/api/api_reservoir";
export default {
  name: 'FloodControlPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components: {

  },
  data() {
    return {
      loading: false,
      siteList: [],
      tableHeight: 500,
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'floodControlPopup') {
        this.getData()
      }
    }
  },
  mounted() {
    // this.resizeTable();
  },
  methods: {
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
    // resizeTable() {
    //   this.$nextTick(function () {
    //     const height = this.$refs.wrapper.clientHeight - this.$refs.query.clientHeight;
    //     this.tableHeight = height;
    //   });
    // },
    async getData() {
      this.loading = true
      let opt = {
        prcd: this.popupData.prcd,
        year: 2022,
        fetchAll: true
      }
      let res = await floodListJson(opt)
      if (res.success) {
        this.loading = false
        this.siteList = res.rows
      }
    },

  }
}
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  background: #fff;
  padding: 30px 20px 40px 20px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl +"sk-popup-bg.png") no-repeat
  //   center;
  // background-size: 100% 100%;
  width: 900px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    // background: url($imageUrl + "sk-popup-title.png")
    //   no-repeat bottom;
    // background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    padding-left: 16px;
    font-weight: bold;
    width: 100%;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-weight: bold;
      // font-family: youshe;
      color: #333;
    }
    & > i {
      position: absolute;
      top: 20px;
      right: 40px;
      width: 20px;
      height: 20px;
      color: #999;
      font-size: 20px;
      cursor: pointer;
      background: url($imageUrl + "closeImg.png") no-repeat center;
      background-size: 100%;
      z-index: 10;
    }
  }
  .dialog_body {
    width: 100%;
    height: calc(100% - 40px);
    @include flexbox();
    .body_content {
      width: 100%;
      .small_title {
        font-size: 18px;
        font-weight: 500;
        color: #333;
        //  background: url($imageUrl + "small_title.png")
        //   no-repeat left 25px;
        // background-size: 8px 8px;
        padding: 17px 0 10px 20px;
        position: relative;
        &:before {
          top: 8px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
      }
      .left_item {
        height: 38px;
        line-height: 38px;
        @include flexbox;
        padding-left: 5px;
        box-sizing: border-box;
        p {
          min-width: 70px;
          color: #333;
          font-size: 14px;
        }
        span {
          color: #333;
          font-size: 14px;
        }
        img {
          width: 80px;
          height: 71px;
          margin-top: 13px;
        }
        &.bg {
          // background: #03338d;
          box-sizing: border-box;
          background: #f4f5f8;
        }
      }
    }
  }
}
</style>