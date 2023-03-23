<template>
  <div class="tab-content">
    <div class="send-main">
      预警类型:
      <el-select
        :loading="typeOptionsLoading"
        v-model="type"
        style="width: 132px"
        @change="getMySend"
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
      <el-select v-model="handleStatus" style="width: 132px" @change="getMySend">
        <el-option label="全部" value="" />
        <el-option label="已处理" value="已处理" />
        <el-option label="待处理" value="待处理" />
        <el-option label="超时未处理" value="超时未处理" />
      </el-select>
      关键字:
      <el-input v-model="keyWord" style="width: 160px" @change="getMySend" />
      <el-table v-loading="loading" :data="tableData" :height="375">
        <el-table-column type="index" width="55" />
        <el-table-column label="类别" prop="sendType" width="60" />
        <el-table-column label="预警类型" prop="type" width="180" />
        <el-table-column label="预警内容" prop="content" show-overflow-tooltip />
        <el-table-column label="发送时间" prop="sendTime" width="160" />
        <el-table-column label="接收人" prop="destinationUserName" width="80" />
        <el-table-column label="追踪" width="80">
          <template slot-scope="{ row }">
            <template v-if="row.needHandle">
              <el-popover
                popper-class="screen-popover"
                placement="top"
                trigger="click"
                @show="beforePopoverShow(row)"
              >
                <span>
                  {{ popoverMsg }}
                </span>
                <el-link slot="reference" :underline="false" style="color: #56fefe"
                  >追踪</el-link
                >
              </el-popover>
            </template>
            <template v-else>
              <el-link :underline="false" style="color: #56fefe" disabled>追踪</el-link>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-area" style="margin-top:16px">
      <el-pagination
        class="right"
        @current-change="getMySend"
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

      popoverMsg: "",
    };
  },
  mounted() {
    this.getMessageType();
    this.getMySend();
  },
  methods: {
    beforePopoverShow(row) {
      messageApi.getMessageById(row.id).then((res) => {
        if (res.code == 0) {
          let history = res.data.histories[0];
          let handleUserName = history?.handleUserName || null,
            actionTime = history?.actionTime || null,
            action = history?.action || null;
          let popoverMsg = "暂无追踪信息";
          if (handleUserName && actionTime && action) {
            popoverMsg = `${handleUserName}于${actionTime}${action}`;
          }
          this.popoverMsg = popoverMsg;
        }
      });
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
    getMySend() {
      this.loading = true;
      let opt = {
        content: this.keyWord,
        handleStatus: this.handleStatus,
        type: this.type,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      messageApi.getMySend(opt).then((res) => {
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
.screen-popover {
  background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
  border: 2px solid #00c1ff;
  box-shadow: inset 0 4px 30px 0 #2351ef40;
  color: #fff;
  .popper__arrow {
    border-top-color: #00c1ff !important;
  }

  .popper__arrow::after {
    bottom: 2px !important;
    border-top-color: #103567 !important;
  }
}
</style>
