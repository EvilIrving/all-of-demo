<template>
  <div class="operate-wrap">
    <!-- CAD智能对比 -->
    <div class="compare-detail-box" v-if="boxFlag">
      <!-- 开始比对 -->
      <div>
        <div class="title" style="margin-bottom: 10px">
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
        <div class="title" style="margin: 10px 0 5px 0">
          <p>项目名称</p>
        </div>
        <el-input
          v-model="compareName"
          clearable
          placeholder="请输入项目名称"
          style="height: 30px"
        />
        <div class="title" style="margin: 5px 0 10px 0">
          <p>对比项目</p>
        </div>
        <div class="checked-list">
          <el-row>
            <el-col
              :span="12"
              style="
                height: 45px;
                line-height: 45px;
                background: #267671;
                border-radius: 4px;
              "
            >
              <i class="icon-smart-compare" style="vertical-align: middle"></i>
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
          <p style="text-align: left; font-size: 14px; color: #c0c4cc">
            {{ fileName != "" ? fileName : "请上传.json文件进行智能比对" }}
          </p>
        </div>
      </div>
      <!-- 比对 返回按钮 -->
      <div class="button-area">
        <span @click="cancel">取消</span>
        <span class="compare-btn" @click="startedComparing">开始比对</span>
      </div>
    </div>
    <!-- 工具 -->
    <!-- 智能对比 -->
    <div>
      <el-dropdown trigger="click" @command="smartContrast">
        <span class="el-dropdown-link">
          <i class="icon-smart-compare" style="vertical-align: middle" />
          CAD智能对比<i class="el-icon-caret-top el-icon--right" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="智能对比">智能对比</el-dropdown-item>
          <!-- <el-dropdown-item command="比对历史">比对历史</el-dropdown-item> -->
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 流域网格 -->
    <div class="tool">
      <div>
        <i class="icon-lywg" style="vertical-align: middle"></i>
        <el-dropdown trigger="click" @command="basinCommand">
          <span class="el-dropdown-link">
            {{ basinName }}<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="飞云江流域">飞云江流域</el-dropdown-item>
            <el-dropdown-item command="鳌江流域">鳌江流域</el-dropdown-item>
            <el-dropdown-item command="瓯江流域">瓯江流域</el-dropdown-item>
            <el-dropdown-item command="全流域">全流域</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <!-- <div>
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            温州市<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>温州市</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div> -->
      <div>
        <i class="icon-operate-box" style="vertical-align: middle"></i>
        <el-dropdown trigger="click" @command="mapToolBar">
          <span class="el-dropdown-link">
            工具箱<i class="el-icon-caret-top el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="length">测距</el-dropdown-item>
            <el-dropdown-item command="area">测面</el-dropdown-item>
            <el-dropdown-item command="none">清除测量</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 缩放 -->
    <div class="enlarge zoom-change" @click="zoomChange('add')">+</div>
    <div class="narrow zoom-change" @click="zoomChange('reduce')">-</div>
    <div class="home" @click="zoomChange('home')" title="主视图"></div>

    <!-- 底图切换 -->
    <!-- <div class="switch">
      <i class="icon-change" style="vertical-align: middle"></i>
      底图切换
    </div> -->
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
// import Measure from "../../utils/measure";
import axios from "axios";
import mapDialog from "./dialog/mapDialog";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

export default {
  name: "PrimaryMapOperate",
  props: {},
  components: {
    mapDialog,
  },
  data() {
    return {
      gid: '',
      boxFlag: false,
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
      basinName: '全流域',
    };
  },
  mounted() {
    this.$nextTick(() => {
      // this.operatorName = this.$localData("get", "userInfo").username;
      // this.operatorId = this.$localData("get", "userInfo").id;
      this.operateTime = new Date().format("yyyy-MM-dd hh:mm:ss");
    })
  },
  methods: {
    /**
     * 智能对比方法开始
     */
    // 智能对比
    smartContrast(val) {
      if (val == "智能对比") {
        this.boxFlag = true;
      } else {
        this.boxFlag = false;
        this.$router.push("/comparativeHistory");
      }
    },
    // 开始比对
    startedComparing() {
      this.addMultiCompareRecord(this.comparisonList);
    },
    // 关闭历史对比弹框
    closeFormDialog() {
      this.formDialogShow = false;
    },
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
        if (res.code == 0) {
          this.detailsList = res.data;
          this.formDialogShow = true;
        }
      })
    },
    // 取消
    cancel() {
      this.comparisonList = [];
      this.compareName = '';
      this.boxFlag = false;
    },
    /**
     * 只能比对方法结束
     */
    mapToolBar(command) {
      // this.$message('click on item ' + command);
      // alert(command)
      this.$emit("measure", command);
    },
    zoomChange(type) {
      this.$emit("zoomChange", type);
    },
    basinCommand(command) {
      this.basinName = command;
      this.$emit("basinCommand", command);
    },
  },
}
</script>
<style lang="scss">
.operate-wrap {
  display: flex;
  > div {
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    margin-left: 16px;
    cursor: pointer;
    text-align: center;
    i {
      margin-right: 6px;
    }
  }
  .compare-detail-box {
    position: absolute;
    bottom: 52px;
    border-radius: 4px;
    width: 300px;
    height: 300px;
    border: 1px solid #00b673;
    background: rgba(0, 0, 0, 0.4);
    padding: 10px 16px 0 16px;
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
      p {
        border-left: 4px solid #32bcb4;
        line-height: 18px;
        padding-left: 12px;
      }
    }
    .title-sty {
      background: #267671;
      border-radius: 2px;
      font-size: 14px;
      height: 30px;
      line-height: 30px;
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
      @include flexbox;
      justify-content: flex-end;
      span {
        margin-left: 12px;
        &.compare-btn {
          color: #fff;
          background: #278372;
          padding: 0 12px;
        }
      }
    }
  }
  .tool {
    display: flex;
    padding: 8px 24px;
    line-height: 24px;
    margin: 0;
    > div:first-child {
      border-right: 1px solid #fff;
      padding-right: 12px;
      // margin-right: 18px;
    }
    > div:last-child {
      border-left: 1px solid #fff;
      padding-left: 18px;
      // margin-left: 12px;
    }
  }
  .zoom-change {
    width: 40px;
    font-size: 28px;
  }
  .switch {
    padding: 0 22px;
  }
  .home {
    height: 40px;
    width: 40px;
    font-size: 28px;
    background: url(~assets/images/Home01.png) no-repeat center center;
    background-color: rgba(0, 0, 0, 0.4);
  }
}
</style>
<style lang="scss">
.el-dropdown {
  font-size: 16px;
  color: #fff;
  vertical-align: middle;
}
.el-dropdown-menu__item:focus,
.el-dropdown-menu__item:not(.is-disabled):hover {
  background-color: rgba(38, 118, 113, 0.26);
  color: #267671;
}
</style>