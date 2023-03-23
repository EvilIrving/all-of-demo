<template>
  <div class="tab-content">
    <div class="send-main">
      预警类型:
      <el-select
        :loading="typeOptionsLoading"
        v-model="type"
        style="width: 132px"
        @change="getMyHandled"
      >
        <el-option label="全部" value="" />
        <el-option
          v-for="(item, index) in typeOptions"
          :key="index"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      状态:
      <el-select v-model="handleStatus" style="width: 132px" @change="getMyHandled">
        <el-option label="全部" value="" />
        <el-option label="已处理" value="已处理" />
        <el-option label="待处理" value="待处理" />
        <el-option label="超时未处理" value="超时未处理" />
      </el-select>
      关键字:
      <el-input v-model="keyWord" style="width: 160px" @change="getMyHandled" />
      <el-table v-loading="loading" :data="tableData" :height="390">
        <el-table-column type="index" width="55" />
        <el-table-column label="类别" prop="sendType" width="60" />
        <el-table-column label="预警类型" prop="type" width="180" />
        <el-table-column label="预警内容" prop="content" show-overflow-tooltip />
        <el-table-column label="发送时间" prop="sendTime" width="160" />
        <el-table-column label="发送人" prop="fromUserName" width="80" />
        <el-table-column label="操作" width="80">
          <template slot-scope="{ row }">
            <el-link
              :underline="false"
              style="color: #56fefe"
              :href="handleUrl(row)"
              :disabled="row.handleUrl == ''"
              target="_blank"
              >详情</el-link
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-area">
      <el-pagination
        class="right"
        @current-change="getMyHandled"
        :current-page.sync="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </div>
</template>
<script>
import { messageApi } from "@/api/message.js";
export default {
  name: "SendTab",
  data() {
    return {
      loading: false,
      tableData: [],

      type: "",
      typeOptionsLoading: false,
      typeOptions: [],
      handleStatus: "",
      keyWord: "",

      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  mounted() {
    this.getMessageType();
    this.getMyHandled();
  },
  methods: {
    handleUrl(row) {
      const url = `${row.handleUrl}&batchId=${row.batchId}&id=${row.id}`;
      return url;
    },
    getMessageType() {
      this.typeOptionsLoading = true;
      messageApi.getMessageType().then((res) => {
        this.typeOptionsLoading = false;
        if (res.code == 0) {
          this.typeOptions = res.data.map((item) => ({ label: item, value: item }));
        }
      });
    },
    getMyHandled() {
      this.loading = true;
      let opt = {
        content: this.keyWord,
        handleStatus: this.handleStatus,
        type: this.type,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      messageApi.getMyHandled(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.tableData = res.data.list;
          this.total = res.data.totalRows;
        }
      });
    },
  },
};
</script>
<style lang="scss">
.tab-content {
  .send-main {
    .el-input {
      .el-input__inner {
        border: 1px solid #fff;
        height: 32px;
        line-height: 32px;
      }
      .el-select__caret {
        line-height: 32px;
      }
    }
    .el-table {
      margin-top: 18px;
    }
  }
}
</style>
