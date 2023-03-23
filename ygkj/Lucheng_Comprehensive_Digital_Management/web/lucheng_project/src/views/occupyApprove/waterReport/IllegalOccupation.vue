<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 15:03:22
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\waterReport\IllegalOccupation.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>违法占用报表</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="name"
          @change="getList"
          clearable
          style="width: 180px; margin: 0 20px 0 0"
        />
        <span style="font-size: 14px">行政区划：</span>
        <el-select
          v-model="areaValue"
          placeholder="请选择"
          style="width: 180px; margin: 0 20px 0 0"
          clearable
          @change="getList"
        >
          <el-option
            v-for="item in areaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'illegalOccupation:export'"
          @click="exportTable"
          >导出</el-button
        >
        <el-button
          type="primary"
          v-permission="'illegalOccupation:add'"
          @click="editTable('新增')"
          >新增</el-button
        >
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <!-- <el-table-column type="selection" width="50"> </el-table-column> -->
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="水域名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="adcdNames"
          label="行政区划"
          align="center"
        ></el-table-column>
        <el-table-column label="坐标" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.longitude && scope.row.latitude"
              >{{ scope.row.longitude }},{{ scope.row.latitude }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="发现时间" prop="findTime" align="center">
        </el-table-column>
        <el-table-column
          prop="illegalOccupy"
          label="违法占用行为"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="unitsInvolved"
          label="涉事单位（个人）信息"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="uploadfile" label="图片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.picturesIds"
              @click="lookPic(scope.row.files)"
              src="../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="190">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'illegalOccupation:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'illegalOccupation:del'"
                >删除</span
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="getList"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <IllegalOccupationAddFrom
        @close="closeDialog"
        :id="id"
        :areaOptions="areaOptions"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog title="图片预览" :visible.sync="pictureDialog">
      <div
        v-for="(item, index) in picList"
        :key="index"
        style="margin: 5px"
        class="lookPic"
      >
        <!-- <span
            style="font-size: 16px"
            @click="lookFile(item.fileUrl)"
        >{{item.fileName}}</span> -->
        <el-image
          style="width: 100px; height: 100px"
          :src="item.fileUrl"
        ></el-image>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IllegalOccupationAddFrom from "./components/IllegalOccupationAddFrom.vue";
import { watersReportApi } from "@/api/occupyApprove.js";
import { projectSupervisionApi } from "@/api/projectSupervision";
import LookFile from "@/components/LookFile";
import { watersReportInfoExport } from "@/utils/export.js";
export default {
  data() {
    return {
      waterName: "",
      name: "",
      areaOptions: [],
      areaValue: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: "",
      title: "新增",
      pictureDialog: false,
      picList: [],
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    IllegalOccupationAddFrom,
  },
  mounted() {
    this.getAdcdList(); // 行政区划接口
    this.getList();
  },
  methods: {
    // 行政区划
    async getAdcdList() {
      let res = await projectSupervisionApi.getAdcdList({
        areaCode: "330302",
        level: 4,
      });
      if (res.code == 0) {
        console.log(res.data);
        // this.areaOptions = [
        //   {
        //     value: null,
        //     label: '全部',
        //   },
        // ]
        res.data.forEach((item) => {
          this.areaOptions.push({
            value: item.adcd,
            label: item.adnm,
          });
        });
      }
    },
    exportTable() {
      let obj = {
        name: this.name,
        adcds: this.areaValue,
      };
      watersReportInfoExport(obj);
    },
    async getList() {
      await watersReportApi
        .occupationList({
          name: this.name,
          adcds: this.areaValue,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res.data.list);
            this.tableData = res.data.list;
            this.pageTotal = res.data.totalRows;
          }
        });
    },
    handleClose() {
      this.visibleDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.getList();
    },
    editTable(type, id) {
      // console.log(id);
      this.id = id;
      this.title = type;
      this.visibleDialog = true;
    },
    lookPic(images) {
      this.pictureDialog = true;
      this.picList = images;
      console.log(this.picList);
    },
    del(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        watersReportApi
          .occupationDel({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
  .point {
    display: inline-block;
    border-radius: 50%;
    width: 6px;
    height: 6px;
    background: #01a59c;
    vertical-align: middle;
    margin-right: 7px;
    &.blue {
      background: #1492ff;
    }
    &.red {
      background: #ff0000;
    }
  }
  .lookPic {
    display: inline-block;
  }
  .search-right {
    text-align: right;
    padding: 25px 16px 16px;
  }
}
.popover_title {
  span {
    float: right;
  }
}
.popover_content {
  background: rgba(216, 216, 216, 0.19);
  border-radius: 2px;
}
::v-deep .el-dialog__body {
  //   padding: 0;
}
</style>