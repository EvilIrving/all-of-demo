<template>
  <div class="collect-table">
    <div class="collect-table__content">
      <div class="main-search-box">
        名称:
        <el-input
          v-model="searchVal"
          style="width: 160px"
          @change="getMyCollectList"
        />
      </div>
      <el-table class="custom-table" v-loading="loading" :data="tableData">
        <el-table-column type="index" width="55" />
        <el-table-column label="名称" prop="name" />
        <el-table-column label="所属类别" prop="category" />
        <el-table-column
          label="上传人"
          prop="uploaderName"
          show-overflow-tooltip
        />
        <el-table-column label="收藏时间" prop="collectDate" />
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <vxe-button
              @click="previewPdf(scope.row.url)"
              type="text"
              status="primary"
              content="预览"
            />
            <!-- <span class="list-item-opearte" @click="previewPdf(scope.row.url)"
              >预览</span
            > -->
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="main-pagination-area">
      <el-pagination
        class="right"
        @current-change="getMyCollectList"
        :current-page.sync="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <!-- 预览pdf -->
    <preview-pdf
      ref="previewPdf"
      v-if="showFile"
      :visible="showFile"
      :fileUrl="fileUrl"
      @close="closePreviewPdf"
    ></preview-pdf>
  </div>
</template>

<script>
import previewPdf from "../components/lookFile.vue";
import { collectApi } from "@/api/collect";
export default {
  name: 'collect-table',
  components: {
    previewPdf
  },
  data() {
    return {
      tableData: [],
      loading: false,
      showFile: false,
      fileUrl: "",
      pageSize: 10,
      pageNum: 1,
      total: 0,
      searchVal: ""
    }
  },
  mounted() {
    this.getMyCollectList();
  },
  methods: {
    getMyCollectList() {
      let opt = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.searchVal
      }
      collectApi.getMyCollectList(opt).then(res => {
        if (res.code == 0) {
          this.tableData = res.data.list;
          this.total = res.data.totalRows;
        }
      })
    },
    // pdf预览
    previewPdf(fileUrl) {
      this.fileUrl = fileUrl;
      this.showFile = true;
    },
    closePreviewPdf() {
      this.showFile = false;
    }
  }
}
</script>

<style lang="scss" scoped>
.collect-table {
  height: 100%;
  .list-item-opearte {
    cursor: pointer;
  }
  .collect-table__content {
    .main-search-box {
      padding-bottom: 16px;
    }
  }
}
</style>