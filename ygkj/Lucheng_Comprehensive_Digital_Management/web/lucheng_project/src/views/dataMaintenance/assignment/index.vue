<template>
  <div class="wrap">
    <div class="card">
      <div class="item_box_title">
        <p>维护任务</p>
      </div>
      <div class="container">
        <el-form inline>
          <el-form-item label="状态">
            <el-select v-model="status" @change="getList">
              <el-option label="全部" :value="null" />
              <el-option label="已处理" value="1" />
              <el-option label="未处理" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间">
            <el-date-picker
              v-model="sendTime"
              :value="1"
              type="date"
              placeholder="选择日期"
              @change="getList"
            ></el-date-picker>
          </el-form-item>
        </el-form>
        <div class="table-wrapper">
          <el-table height="100%" :data="tableData" border>
            <el-table-column
              label="序号"
              align="center"
              type="index"
              width="55"
            />
            <el-table-column label="目录名称" prop="directoryName" />
            <el-table-column label="数据项" prop="dataItem" />
            <el-table-column label="详细描述" prop="description" />
            <el-table-column label="当前数据来源" prop="source" />
            <el-table-column label="任务内容" prop="content" />
            <el-table-column label="图片" width="55">
              <template #default="{ row, $index }">
                <el-button
                  icon="el-icon-picture"
                  size="small"
                  style="padding: 0"
                  type="text"
                  circle
                  @click="onImagePreviewClick(row, `image-${$index}`)"
                >
                  <el-image
                    :ref="`image-${$index}`"
                    style="display: none"
                    src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
                    :preview-src-list="processPictureUrls(row.pictures)"
                  />
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="发送时间" prop="sendTime" />
            <el-table-column label="任务状态" width="80">
              <template #default="{ row }">
                {{ row.status === "0" ? "未处理" : "已处理" }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <span class="button" @click="onViewClick(row)">查看</span>
                <el-divider direction="vertical"></el-divider>
                <span
                  v-permission="'dataMaintenance:delete'"
                  @click="onDeleteClick(row.id)"
                  class="button danger"
                  >删除</span
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination
            :total="total"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            @pagination="getList"
          />
        </div>
      </div>
    </div>
    <DetailDialog
      ref="detailDialog"
      :visible.sync="detailVisible"
      @change="getList"
    />
  </div>
</template>
<script>
import { querySendInfo, deleteSendInfo } from "@/api/dataMaintenance";
import moment from "moment";
import DetailDialog from "./components/DetailDialog.vue";
export default {
  name: "Assignment",
  components: { DetailDialog },
  data() {
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      detailVisible: false,
      status: null,
      sendTime: null,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let startTime = "";
      let endTime = "";
      if (this.sendTime) {
        startTime = moment(this.sendTime).format("yyyy-MM-DD HH:mm:ss");
        endTime = moment(this.sendTime)
          .add(1, "day")
          .format("yyyy-MM-DD HH:mm:ss");
      }
      const res = await querySendInfo({
        status: this.status,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        startTime,
        endTime,
      });
      if (res.code === 0) {
        this.tableData = res.data.list;
        this.total = res.data.totalRows;
      }
    },
    processPictureUrls(urls) {
      return urls.split(",");
    },
    onViewClick(data) {
      this.detailVisible = true;
      this.$nextTick(() => {
        this.$refs.detailDialog.form = Object.assign({}, data);
      });
    },
    onDeleteClick(id) {
      this.$confirm("此操作将永久删除该条数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteSendInfo({ id }).then((res) => {
          if (res.code === 0) {
            this.$message.success("删除成功!");
            this.getList();
          }
        });
      });
    },
    onImagePreviewClick(row, ref) {
      if (row.pictures !== "") {
        this.$refs[ref].clickHandler();
      } else {
        this.$message.info("暂无图片!");
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  padding: 16px;
  height: calc(100% - 100px);
  .table-wrapper {
    height: calc(100% - 150px);
    .button {
      cursor: pointer;
      &:hover {
        color: #111;
      }
      &.danger {
        color: #ee2233;
        &:hover {
          color: #ee0000;
        }
      }
    }
  }
}
.wrap {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .card {
    @include contentBox();
    height: 100%;
    box-sizing: border-box;
  }
}
</style>
