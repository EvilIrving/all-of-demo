<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>应急总结管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入名称"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="name"
        style="width: 240px; margin-right: 15px"
        clearable
      />

      <!-- <el-button type="primary" class="right" @click="exportTable('导出', '')"
        >导出</el-button
      > -->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="tm"
          label="时间"
          align="center"
        ></el-table-column>
        <el-table-column prop="type" label="类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.fileId">
              {{ scope.row.type == 1 ? '防汛防台' : '水雨情' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="文件名" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.fileId">
              {{ scope.row.file.fileName }}
            </span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="220">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-if="scope.row.fileId"
                @click="exporBriefing(scope.row.file)"
                v-permission="'EmergencySummaryManage:export'"
                >导出</span
              >
              <!-- <span
                class="deal_btn deal_btn_look"
                @click="lookFile(scope.row.file)"
                v-permission="'EmergencySummaryManage:look'"
                >查看</span
              > -->
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'EmergencySummaryManage:del'"
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
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <div class="wordDom" ref="file" v-if="fileDialog"></div>
    </el-dialog>
  </div>
</template>

<script>
import { emergencySummary } from "@/api/seawallPrevent";
import axios from "axios";
const docx = require("docx-preview");
export default {
  name: "EmergencySummaryManage",
  props: {},
  components: {},
  data() {
    return {
      tableData: [],
      showEditDialog: false,
      title: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      name: "",
      fileDialog: false,
      url: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await emergencySummary.floodPreventionBulletin({
        pageSize: this.pageSize,
        pageNum: this.pageNum,
      });
      console.log(res);
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    delTable(id) {
      this.$confirm("此操作会删除该条总结管理，是否继续", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        emergencySummary.del({ id: id }).then((res) => {
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--;
            }
            this.getList();
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          } else {
            this.$message({
              type: "error",
              message: "删除失败！",
            });
          }
        });
      });
    },
    exporBriefing(file) {
      axios({
        method: "get",
        responseType: "blob", // 因为是流文件，所以要指定blob类型
        url: file.fileUrl, // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        console.log(data); // 后端返回的是流文件
        const blob = new Blob([data]); // 把得到的结果用流对象转一下
        var a = document.createElement("a"); //创建一个<a></a>标签
        a.href = URL.createObjectURL(blob); // 将流文件写入a标签的href属性值
        a.download = file.fileName; //设置文件名
        a.style.display = "none"; // 障眼法藏起来a标签
        document.body.appendChild(a); // 将a标签追加到文档对象中
        a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove(); // 一次性的，用完就删除a标签
      });
    },
    lookFile(file) {
      this.fileDialog = true;
      axios({
        method: "get",
        responseType: "blob", // 设置响应文件格式
        url: file.fileUrl,
      }).then(async ({ data }) => {
        await docx.renderAsync(data, this.$refs.file); // 渲染到页面预览
      });
    },
  },
};
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
  .wordDom {
    height: 100%;
    //   overflow: auto;
    ::v-deep .docx-wrapper {
      height: 100%;
      padding: 10px 30px;
    }
    ::v-deep .docx {
      //   overflow: auto;
      min-height: 100% !important;
    }
    //   ::v-deep section.docx {
    //     overflow: auto;
    //   }
  }
  ::v-deep section.docx {
    overflow: auto;
  }
}
</style>