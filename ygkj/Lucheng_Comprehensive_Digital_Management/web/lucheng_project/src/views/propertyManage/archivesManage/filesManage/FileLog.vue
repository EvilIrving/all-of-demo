<template>
  <div class="wrap log_query">
    <div class="logQuery_top">
      <span class="return_fileManage" @click="$emit('cancelFileLog')"><i></i>返回</span>
      <span>{{fileName}}</span>
      <div class="right">
        <i class="demonstration">操作类型：</i>
        <el-select v-model="typeSelect" placeholder="选择操作类型" @change="handleFilter" class="edSelect">
          <el-option
              v-for="item in typeSelectOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
    </div>
    <div class="logQuery_main">
      <div class="main_left">
        <div class="left_top">
          <el-date-picker
            v-model="selectMonth"
            type="month"
            format="yyyy-MM"
            value-format="yyyy-MM"
            @change="changeMonth"
            >
          </el-date-picker>
        </div>
        <div class="left_list">
          <ul>
            <li v-for="(item, index) in dateList" :key="index" :class="dateIndex == index ? 'active' : ''" @click="dateClick(index)">{{item}}</li>
          </ul>
        </div>
      </div>
      <div class="main_right">
        <div class="table_input">
          
          <span>
            {{dateSelect}}
            <b>（访问记录：{{total}}次）</b>
          </span>
          
          
        </div>
        <div class="table_wrap">
          <el-table :data="tableData" style="width: 100%;margin-bottom:16px;" stripe>
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="createTime" label="操作时间"></el-table-column>
            <el-table-column prop="ip" label="IP地址"></el-table-column>
            <el-table-column prop="createName" label="操作人员"></el-table-column>
            <el-table-column prop="fileName" label="档案名称"></el-table-column>
            <el-table-column prop="archiveName" label="目录归属"></el-table-column>
            <el-table-column prop="status" label="操作类型">
              <template slot-scope="scope">
                <span v-if="scope.row.status == 1"><i></i>查看</span>
                <span v-if="scope.row.status == 2"><i></i>下载</span>
                <span v-if="scope.row.status == 3"><i></i>删除</span>
                <span v-if="scope.row.status == 4"><i></i>更新</span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="right"
            background
            layout="total,prev, pager, next"
            :total="total"
            :page-sizes="[10, 20, 30, 40]"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script>
import {getTimeListApi, getFileListApi} from "@/api/propertyManage";
export default {
  name: 'LogQuery',
  props: {
    
  },
  components: {
  },
  data () {
    return {
      selectMonth: new Date().format("yyyy-MM"),
      typeSelect: '',
      typeSelectOptions: [
        { label: "全部", value: '' },
        { label: "查看", value: '1' },
        { label: "下载", value: '2' },
        { label: "删除", value: '3' },
        { label: "更新", value: '4' }
      ],
      dateIndex: 0,
      dateSelect: '',
      dateList: [],
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      fileLogId: '',
      fileName: ''
    };
  },
  mounted() {
    
  },
  methods: {
    //左侧时间列表
    async getTimeList() {
      let res = await getTimeListApi({
        time: this.selectMonth
      });
      if(res.code == 0){
        this.dateSelect = '';
        this.dateList = res.data;
        if(res.data.length>0){
          this.dateSelect = res.data[0];
          this.getFileList();
        }
      }
    },
    //日志访问表格数据
    async getFileList() {
      let res = await getFileListApi({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        queryTime: this.dateSelect,
        status: this.typeSelect,
        id: this.fileLogId
      });
      if(res.code == 0){
        this.tableData = res.data.list;
        this.total = res.data.total;
      }
    },
    changeMonth(val) {
      this.selectMonth = val;
      this.getTimeList();
    },
    dateClick(index){
      this.dateIndex = index;
      this.dateSelect = this.dateList[index];
      this.pageNum = 1;
      this.total = 0;
      this.getFileList();
    },
    handleFilter() {
      this.getFileList();
    },
    doSearch() {
      this.getFileList();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getFileList();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getFileList();
    },
    getFileLogId(id,name) {
      this.fileLogId = id;
      this.fileName = name;
      this.getTimeList();
    }
  }
}
</script>

<style lang='scss' scoped>
  .log_query{
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    padding: 16px;
  }
  .logQuery_top{
    width: 100%;
    height: 64px;
    background: #fff;
    border: 1px solid #E6E6E6;
    padding: 12px 16px;
    box-sizing: border-box;
    span{
      font-size: 18px;
      color: #000;
      line-height: 40px;
      b{
        font-size: 14px;
        font-weight: normal;
      }
    }
    i{
      font-style: normal;
      margin-left: 25px;
    }
    span.return_fileManage{
      font-size: 14px;
      line-height: 24px;
      padding: 6px 12px;
      margin-right: 15px;
      background: #f5f5f5;
      border-radius: 4px;
      float: left;
      cursor: pointer;
        i{
          width: 20px;
          display: inline-block;
          height: 20px;
          margin-top: -2px;
          margin-right: 8px;
          margin-left: 0;
          vertical-align: middle;
        }
      }
      span.return_fileManage i{
        background: url("../../../../assets/images/icon-ycs.png")no-repeat center;
        background-size: 100% 100%;
      }
  }
  .logQuery_main{
    width: 100%;
    height: calc(100% - 72px);
    margin-top: 8px;
    @include flexbox();
    @include flexJC(space-between);
    >.main_left{
      width: 256px;
      height: auto;
      border: 1px solid #e6e6e6;
      background: #fff;
      padding: 15px;
      .left_list{
        width: 100%;
        height: calc(100% - 50px);
        margin-top: 10px;
        overflow-y: auto;
        ul{
          li{
            font-size: 14px;
            color: #95969b;
            line-height: 32px;
            letter-spacing: 0.8px;
            padding: 0 32px;
            cursor: pointer;
          }
          li.active{
            color: #000
          }
        }
      }
    }
    >.main_right{
      width: calc(100% - 264px);
      height: 100%;
      .table_input{
        height: 64px;
        background: #fff;
        border: 1px solid #E6E6E6;
        padding: 12px 16px;
        span{
          font-size: 18px;
          color: #000;
          line-height: 40px;
          b{
            font-size: 14px;
            font-weight: normal;
          }
        }
        i{
          font-style: normal;
          margin-left: 25px;
        }
        .wrap{
          padding: 0;
        }
      }
      .table_wrap{
        width: 100%;
        height: calc(100% - 72px);
        background: #fff;
        border: 1px solid #E6E6E6;
        margin-top: 8px;
        padding: 12px;
        overflow-y: auto;
        box-sizing: border-box;
      }
    }
  }
</style>
<style lang='scss'>
  .log_query .table_input .el-button{
    margin-right: -20px;
  }
  .log_query .table_input .el-input{
    margin-right: 0;
  }
  .log_query .table_input .wrap .table-view-head{
    height: 38px;
  }
</style>

