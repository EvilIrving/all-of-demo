<!--
 * @Author: hanyu
 * @LastEditTime: 2021-07-10 22:43:33
 * @Description: 地图右侧-台风路径
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawall/seawallRight/SeawallRightTflj.vue
-->
<template>
  <div class='wrap'>
    <div class="top_two_tabs">
      <div :class="['left_tab', tabFlag ? 'active' : '']" @click="tabClick(true)">当前台风</div>
      <div :class="['right_tab', !tabFlag ? 'active' : '']" @click="tabClick(false)">历史台风</div>
    </div>
    <section v-if="tabFlag" class="typhoon_now_list">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="">
        台风信息
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" :data="tableData" stripe style="width: 100%">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="时间" align="center"></el-table-column>
          <el-table-column prop="NAME" label="位置" align="center"></el-table-column>
          <el-table-column prop="NAME" label="风速(m/s)" align="center"></el-table-column>
          <el-table-column prop="NAME" label="气压" align="center"></el-table-column>
        </el-table> 
      </div>
    </section>
    <section v-if="!tabFlag" class="typhoon_history_list">
      <div class="typhoon_history_list_top">
        <el-date-picker
          style="width: 100%"
          v-model="year"
          type="year"
          placeholder="选择年">
        </el-date-picker>
        <div class="table_list">
          <el-table v-loading="tableTopLoading" :data="tableTopData" stripe style="width: 100%" @selection-change="handleTyphoonChange">
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column type="selection"></el-table-column>
            <el-table-column prop="NAME" label="台风编号" align="center"></el-table-column>
            <el-table-column prop="NAME" label="中文名" align="center"></el-table-column>
            <el-table-column prop="NAME" label="英文名" align="center"></el-table-column>
          </el-table> 
        </div>
      </div>
      <div class="typhoon_history_list_bottom">
        <div class="chart_title">
          <img src="../../../../assets/images/chart_item_icon.png" alt="">
          台风信息
        </div>
        <div class="table_list">
          <el-table v-loading="tableLoading" :data="tableData" stripe style="width: 100%">
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column prop="NAME" label="时间" align="center"></el-table-column>
            <el-table-column prop="NAME" label="位置" align="center"></el-table-column>
            <el-table-column prop="NAME" label="风速(m/s)" align="center"></el-table-column>
            <el-table-column prop="NAME" label="气压" align="center"></el-table-column>
          </el-table> 
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      tabFlag: true,
      tableData: [],
      nowTableData: [],
      year: new Date().format('yyyy'),
      tableTopLoading: false,
      tableTopData: [],
      tableLoading: false,
      tableHeight: 0
    };
  },
  mounted() {
    this.$parent.$refs.seawallMap.removeAllLayer();
    this.$parent.$refs.seawallMap.closePopup();
    this.$parent.$refs.seawallMap.initView();
    this.$nextTick(function () {
      const height = this.$refs.tableContainer.clientHeight;
      this.tableHeight = height - 20
      let _this = this;
      window.onresize = function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20
      }
    })
  },
  methods: {
    tabClick(flag){
      this.tabFlag = flag
    },
    handleTyphoonChange(){

    }
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    height: 100%;
    .top_two_tabs{
      padding: 16px;
      width: 100%;
      box-sizing: border-box;
      @include flexbox();
      @include flexAC();
      @include flexJC(center);
      &>div{
        height: 66px;
        width: 50%;
        text-align: center;
        color: #000;
        font-size: 22px;
        font-weight: 500;
        line-height: 66px;
        background: #F8F8F8;
        border: 1px solid #F8F8F8;
      }
      .active{
        background: #dbe2f8;
        border: 1px solid #193BC4;
        color: #193BC4;
      }
      .left_tab{
        border-top-left-radius: 33px;
        border-bottom-left-radius: 33px;
      }
      .right_tab{
        border-top-right-radius: 33px;
        border-bottom-right-radius: 33px;
      }
    }
    .typhoon_now_list{
      height: calc(100% - 100px);
      .table_list{
        height: calc(100% - 42px);
        box-sizing: border-box;
        padding-bottom: 16px;
        // height: 500px;
        overflow: auto;
        
      }
    }
    .typhoon_history_list{
      height: calc(100% - 100px);
      .typhoon_history_list_top{
        padding: 16px;
        .table_list{
          padding: 16px 0;
        }
      }
    }
  }
</style>