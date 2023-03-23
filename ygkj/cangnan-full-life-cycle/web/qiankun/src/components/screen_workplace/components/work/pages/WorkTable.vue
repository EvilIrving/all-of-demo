<template>
  <div class="collect-table">
    <div class="collect-table__content">
      <div class="main-search-box">
        名称:
        <el-input
          v-model="searchVal"
          style="width: 160px; margin-right: 20px"
          @change="getMyWorkList"
        />
        工作描述:
        <el-input
          v-model="description"
          style="width: 260px"
          @change="getMyWorkList"
        />
      </div>
      <el-table class="custom-table" v-loading="loading" :data="tableData">
        <el-table-column type="index" width="55" label="序号" />
        <el-table-column label="工作名称" prop="name" />
        <el-table-column width="150" label="所属应用" prop="system" />
        <el-table-column label="描述" prop="description" />
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <vxe-button
              type="text"
              status="primary"
              content="进入办理"
              @click="
                doEnterAction(scope.row.url, scope.row.name, scope.row.id,scope.row.system)
              "
            />
            <!-- <span class="list-item-opearte">进入办理</span> -->
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="main-pagination-area">
      <el-pagination
        class="right"
        @current-change="getMyWorkList"
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
import { collectApi } from "@/api/collect";
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'collect-table',
  components: {
  },
  data() {
    return {
      tableData: [],
      loading: false,
      pageSize: 10,
      pageNum: 1,
      total: 0,
      searchVal: "",
      description: "",
    }
  },
  mounted() {
    this.getMyWorkList();
  },
  methods: {
    ...mapActions(["SetWorkPlaceVisible", "SetMenuName", "SetSelectMenu", "SetWorkName", "SetWorkId","SetWorkFlag","SetSystemType"]),
    getMyWorkList() {
      let opt = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.searchVal,
        description: this.description
      }
      collectApi.getMyJobs(opt).then(res => {
        if (res.code == 0) {
          this.tableData = res.data.list;
          this.total = res.data.totalRows;
        }
      })
    },
    doEnterAction(url, workName, id,system) {
      this.SetWorkPlaceVisible(false);
      this.SetSelectMenu(2);
      this.SetMenuName("日常工作")
      this.SetSystemType(system);
      this.SetWorkName(workName);
      this.SetWorkFlag(true);
      this.$router.push(url);
      this.$nextTick(()=>{
        this.SetWorkId(id);
      })
    },
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