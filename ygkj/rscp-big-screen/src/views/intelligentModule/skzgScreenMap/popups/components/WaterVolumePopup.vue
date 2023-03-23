<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.name }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="el-dialog__close" @click="closePopup()"></i>
    </div>
    <div class="sk_table_wrapper" ref="wrapper">
      <!-- 工程名称、工程规模、水库功能、开始时间、结束时间、 -->
      <el-table v-loading='loading' :height='tableHeight'  stripe border :data="siteList" style="width: 100%">
        <el-table-column prop="name" min-width="100" label="工程名称" align="center"></el-table-column>
        <el-table-column prop="project_scale_str" label="工程规模" align="center"></el-table-column>
        <el-table-column prop="feature" label="水库功能" align="center"></el-table-column>
        <el-table-column label="供水年月" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.tm ? scope.row.tm.slice(0, 7) : '-'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="supply_total" label="供水水量(万m³)" align="center"></el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import { generateProject } from "@/api/api_reservoir";
export default {
  name: 'WaterVolumePopup',
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
      tableHeight: 0,
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'waterVolumePopup') {
        this.getData()
        setTimeout(()=>{
          this.resizeTable();
        },1000)
      }
    }
  },
  mounted() {
    
  },
  methods: {
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      debugger
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
    resizeTable() {
      this.$nextTick(()=>{
        const height = this.$refs.wrapper.clientHeight;
        this.tableHeight = height;
        console.log(this.tableHeight);
      });
    },
    async getData() {
      this.loading = true
      let opt = {
        prcd: this.popupData.prcd,
        gpType: 8,
        extractPram: '供水',
        interType: 2
      }
      let res = await generateProject(opt)
      if (res.success) {
        this.loading = false
        this.siteList = res.data
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
  height: 550px;
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
  .sk_table_wrapper{
    width: 100%;
    height: calc(100% - 40px);
    margin: 0;
  }
  
  .dialog_body {
    width: 100%;
    height: calc(100% - 40px);
    @include flexbox();
    .body_content {
      width: 100%;
      .small_title {
        font-size: 20px;
        font-weight: 500;
        color: #333;
        //  background: url($imageUrl + "small_title.png")
        //   no-repeat left 25px;
        // background-size: 8px 8px;
        padding: 17px 0 10px 20px;
        position: relative;
        &:before {
          top: 22px;
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