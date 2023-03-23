<template>
  <!-- CAD智能对比 -->
  <div class="compare-detail-box">
    <!-- 开始比对 -->
    <div>
      <div class="title">
        <p>对比对象</p>
      </div>
      <div class="checked-list">
        <el-row>
          <el-col :span="12" class="title-sty"> 河道管理范围线 </el-col>
          <el-col :span="11" class="title-sty" style="margin-left: 10px">
            河道水域面
          </el-col>
        </el-row>
      </div>
      <div class="title">
        <p>项目名称</p>
      </div>
      <el-input v-model="compareName" clearable placeholder="请输入项目名称" />
      <div class="title">
        <p>对比项目</p>
      </div>
      <div class="checked-list">
        <el-row>
          <el-col
            :span="12"
            style="
              height: 40px;
              line-height: 40px;
              background: #2D86C6;
              border-radius: 4px;
              text-align:center;
            "
          >
            <a style="color: #fff" @click="chooseFile">上传文件</a>
            <!-- 使用 accept 属性可以限定 文件选择的格式 -->
            <input
              type="file"
              id="file"
              style="display: none"
              accept=".json, .txt"
              @change="fileInfo(getFileContent)"
            />
          </el-col>
        </el-row>
        <p style="text-align: left; font-size: 14px; color: #c0c4cc;padding:8px 0">
          {{ fileName != "" ? fileName : "请上传.json文件进行智能比对" }}
        </p>
      </div>
    </div>

    <!-- 比对 返回按钮 -->
    <div class="button-area">
      <span @click="cancel">取消</span>
      <span class="compare-btn" @click="startedComparing">开始比对</span>
    </div>

    <!-- 智能比对查看弹窗 -->
    <mapDialog
      :visible="formDialogShow"
      :mapShowsList="mapShowsList"
      v-if="formDialogShow"
      :gid="gid"
      :detailsList="detailsList"
      @close="closeFormDialog"
    />
  </div>
</template>

<script>
import axios from "axios";
import mapDialog from "./dialog/mapDialog";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import {mapActions} from "vuex"
export default {
  name: 'SmartCompare',
  data() {
    return {
      gid: '',
      compareName: "",
      operatorName: "李昶",
      operatorId: "61fe30e6ffb64e7288d9f2cdb79e6658",
      operateTime: '',
      file: {},
      fileName: "",
      fileContent: "",
      mapShowsList: [],
      comparisonList: [],
      detailsList: {},
      formDialogShow: false,
    }
  },
  components: {
    mapDialog,
  },
  mounted() {
    this.$nextTick(() => {
      this.operateTime = new Date().format("yyyy-MM-dd hh:mm:ss");
    })
  },
  methods: {
    ...mapActions(["SetComponentLoading"]),
    chooseFile() {
      // 弹出文件选择框
      if (this.compareName) {
        let input = document.getElementById("file");
        input.click();
      } else {
        this.$message.error("请先填写项目名称！");
      }
    },
    fileInfo(callback) {
      // 获取input标签选择的文件,并选择第一条
      let resultFile = document.getElementById("file").files[0];
      // 如果文件存在
      if (resultFile) {
        // 获取文件信息
        this.file = resultFile;
        // 获取文件名
        this.fileName = resultFile.name;
        // 使用 FileReader 来读取文件
        let reader = new FileReader();
        // 读取纯文本文件,且编码格式为 utf-8
        reader.readAsText(resultFile, "UTF-8");
        // 读取文件,会触发 onload 异步事件,可使用回调函数 来获取最终的值.
        reader.onload = function (e) {
          let fileContent = e.target.result;
          // 若为回调函数,则触发回调事件
          if (callback && typeof callback === "function") {
            callback(fileContent);
          }
        }
      }
    },
    getFileContent(fileContent) {
      let str = fileContent.slice(0, 7) == "POLYGON";
      this.fileContent = fileContent;
      if (str) {
        this.test(fileContent);
      } else {
        this.$message.error("格式错误, 内容必须为wkt格式的字符串!");
      }
    },
    async test(val) {
      await axios({
        url: `http://117.149.228.207:8081/giscad/gissde/river/intersectAndAreas?projectAreaWKTString=${val}`,
        method: "post",
      }).then((res) => {
        let arr = [];
        res.data.datas.forEach((item) => {
          this.mapShowsList.push(item.proj_surf_geom, item.water_surf_geom, item.relevancy_geom);
          let obj = {
            operatorName: this.operatorName,
            operatorId: this.operatorId,
            operateTime: this.operateTime,
            projName: this.compareName,
            relevancyGeom: item.relevancy_geom,
            compareName: this.compareName + this.operateTime,
            compareResultParam: item.compare_result,
            waterOccupyAcreage: item.water_occupy_acreage,
            projSurfGeom: item.proj_surf_geom,
            riverCode: item.river_code,
            riverName: item.river_name,
            waterSurfGeom: item.water_surf_geom,
          };
          arr.push(obj);
        });
        this.comparisonList = arr;
      });
    },
    cancel() {
      this.comparisonList = [];
      this.compareName = '';
      this.$emit("closeSmart");
    },
    // 关闭历史对比弹框
    closeFormDialog() {
      this.formDialogShow = false;
    },
    // 开始比对
    startedComparing() {
      this.SetComponentLoading(true);
      this.addMultiCompareRecord(this.comparisonList);
    },
    // 添加多个对比记录
    async addMultiCompareRecord(obj) {
      await hydraulicApi.addMultiCompareRecord(obj).then((res) => {
        if (res.code == 0) {
          this.getCompareOverview(res.data);
          this.gid = res.data;
        }
      })
    },
    // 查看一组对比结果
    async getCompareOverview(obj) {
      await hydraulicApi.getCompareOverview({ gid: obj }).then((res) => {
        this.SetComponentLoading(false);
        if (res.code == 0) {
          this.detailsList = res.data;
          this.formDialogShow = true;
        }
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.compare-detail-box {
  width: 100%;
  height: 100%;
  color: #fff;
  .el-select {
    width: 100%;
  }
  .title {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 1rem;
    height: 15px;
    margin: 12px 0;
    p {
      border-left: 4px solid #2D86C6;
      line-height: 18px;
      padding-left: 12px;
      
    }
  }
  .title-sty {
    background: #2D86C6;
    border-radius: 2px;
    font-size: 14px;
    height: 30px;
    line-height: 30px;
    text-align: center;
  }
  .contrast-sty {
    text-align: left;
    > p {
      height: 30px;
      padding: 5px 0;
      color: #c0c4cc;
      > span {
        color: #fff;
      }
    }
    > p:nth-child(1) {
      color: #fff;
    }
  }
  .checked-list {
    border-radius: 4px;
  }
  .button-area {
    width: 100%;
    height: 40px;
    margin-top: 8px;
    @include flexbox;
    justify-content: flex-end;
    span {
      margin-left: 12px;
      @include flexbox;
      @include flexAC;
      @include flexJC(center);
      cursor: pointer;
      &.compare-btn {
        color: #fff;
        background: #2D86C6;
        padding: 0 12px;
      }
    }
  }
}
</style>