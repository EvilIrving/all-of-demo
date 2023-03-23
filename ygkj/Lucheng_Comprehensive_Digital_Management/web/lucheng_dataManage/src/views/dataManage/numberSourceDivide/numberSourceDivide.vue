<!--
 * @Author: zg
 * @Date: 2022-03-23 16:17:48
 * @LastEditTime: 2022-03-23 17:13:45
 * @LastEditors: zg
 * @Description: 数据表管理
 * @FilePath: \web-data-center\src\views\dataManage\resourceDirectory\resourceDirectory.vue
-->
<template>
  <div class="module_box">
    <div class="module_content" v-show="!columnDetailsDialogShow">
      <div class="mc_left">
        <div class="left_tit">
          <p>数据源</p>
        </div>
        <div class="left_main">
          <div class="left_search">
            <el-input
              v-model="searchDataSource"
              placeholder="请输入关键字"
              @change="getDataSourceListData"
            />
          </div>
          <div class="left_tree">
            <ul>
              <li
                v-for="(item, index) in dataSourceList"
                :key="item.id"
                :class="activeIndex === index ? 'active' : ''"
                @click="dataSourceClick(item.id, index)"
              >
                <span class="name">
                  {{ item.dataBaseName }}
                </span>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="mc_right">
        <primary-table
          title="数据表列表"
          searchText="名称"
          @searchAction="searchAction"
          :hideAdd="true"
        >
          <!-- 操作按钮 -->
          <div ref="tableBox" class="table-box">
            <!-- 表格 -->
            <table-page
              ref="tablePage"
              :manualLoad="true"
              :url="tableUrl"
              :columns="columns"
              :query-params="queryParams"
              :pageIngType="false"
              :checkBox="false"
              :height="tableHeight"
            >
              <template slot-scope="{ index }" slot="order">{{
                index + 1
              }}</template>
              <template slot-scope="{ row }" slot="action">
                <el-button @click="columnDetails(row)" type="text"
                  >详情</el-button
                >
              </template>
            </table-page>
          </div>
        </primary-table>
      </div>

      <!-- 详情弹窗 -->
      <!-- <el-dialog
        v-if="columnDetailsDialogShow"
        title="详情"
        :visible.sync="columnDetailsDialogShow"
        :close-on-click-modal="false"
        width="80%"
        top="10vh"
        class="columnDetailsDialog"
        @close="closeColumnDetailsDialog"
      >
        <ColumnDetails
          ref="columnDetails"
          @close="closeColumnDetailsDialog"
        ></ColumnDetails>
      </el-dialog> -->
    </div>
    <!-- 数据列 -->

    <ColumnDetails
      v-if="columnDetailsDialogShow"
      ref="columnDetails"
      @close="closeColumnDetailsDialog"
    ></ColumnDetails>
  </div>
</template>

<script>
import PrimaryTable from "_c/primary_table/index";
import { getDataSourceList } from "_a/dataManage/dataSheetManage/dataSheetManage";
import { ConfirmCustom, MessageError, MessageSuccess } from "_l/message";
import TablePage from "_c/CommonForm/table-page";
import ColumnDetails from "./components/columnDetails";
export default {
  name: "dataSheetManage",
  components: {
    PrimaryTable,
    TablePage,
    ColumnDetails,
  },
  data() {
    return {
      tableUrl: "/data-manage/dataTable/page",
      columns: [
        { label: "序号", slot: "order", width: 50 },
        { label: "表名称", prop: "tableName" },
        { label: "表别名", prop: "customTableName" },
        { label: "资源目录名称", prop: "resourceName" },
        { label: "注释信息", prop: "remarks" },
        { label: "操作", slot: "action", width: 120 },
      ],
      queryParams: {},
      tableHeight: 0,

      form: {},
      activeIndex: 0,
      dataSourceId: "",
      directoryDialogShow: false,
      directoryDialogTitle: "新增",
      searchDataSource: "",
      dataSourceList: [],

      columnDetailsDialogShow: false,
    };
  },
  watch: {},
  methods: {
    // 获取左侧数据源数据
    getDataSourceListData() {
      getDataSourceList({
        page: false,
      }).then((res) => {
        if (res.data) {
          this.dataSourceList = res.data.records;
          if (this.dataSourceList.length > 0) {
            this.dataSourceId = this.dataSourceList[0].id;
            this.queryParams.dataSourceId = this.dataSourceId;
            this.activeIndex = 0;
            this.refresh();
          }
        }
      });
    },

    searchAction(name) {
      this.queryParams.tableName = name;
      this.refresh();
    },
    /**
     * 刷新
     */
    refresh() {
      this.$refs.tablePage.refresh();
    },
    /**
     * 重置搜索条件
     */
    reset() {
      this.queryParams = {};
      this.$nextTick(function () {
        this.refresh();
      });
    },

    // 左侧目录树点击查询目录资源列表
    dataSourceClick(id, index) {
      this.dataSourceId = id;
      this.queryParams.dataSourceId = this.dataSourceId;
      this.activeIndex = index;
      this.refresh();
    },

    /**
     * 打开列详情弹窗
     */
    columnDetails(row) {
      this.columnDetailsDialogShow = true;
      this.$nextTick(() => {
        this.$refs.columnDetails.getColumnData(row);
      });
    },
    closeColumnDetailsDialog() {
      this.columnDetailsDialogShow = false;
    },
  },
  mounted() {
    this.getDataSourceListData();
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableBox.offsetHeight;
    });
  },
};
</script>

<style scoped lang="scss">
@import "@/styles/customizeHandle.scss";
.module_box {
  width: 100%;
  height: 100%;
}
.module_content {
  width: 100%;
  height: 100%;
  padding: 16px;
  @include themeify {
    background-color: themed("background_color1");
  }
  .mc_left {
    width: 260px;
    height: 100%;
    @include themeify {
      background-color: themed("background_color2");
    }
    @include themeify {
      box-shadow: 0px 1px 4px 0px themed("box_shadow_color1"),
        0px 0px 2px 0px themed("box_shadow_color2");
    }
    border-radius: 2px;
    float: left;
    .left_tit {
      width: 100%;
      height: 56px;
      padding: 16px 10px;
      @include themeify {
        border-bottom: 1px solid themed("border_color1");
      }
      p {
        font-size: 18px;
        font-weight: 600;
        @include themeify {
          color: themed("font_color2");
        }
        line-height: 20px;
        padding-left: 10px;
        @include themeify {
          border-left: 4px solid themed("border_color2");
        }
      }
    }
    .left_main {
      width: 100%;
      height: calc(100% - 56px);
      padding: 16px 10px;
      .left_add {
        width: 100%;
        span {
          display: inline-block;
          width: 100%;
          height: 32px;
          @include themeify {
            background-color: themed("background_color3");
          }
          @include themeify {
            color: themed("font_color1");
          }
          border-radius: 0.25rem;
          text-align: center;
          line-height: 32px;
          cursor: pointer;
          margin-bottom: 16px;
          @include themeify {
            border: 0.0625rem solid themed("border_color2");
          }
        }
      }
      .left_tree {
        width: 100%;
        height: calc(100% - 46px);
        margin-top: 10px;
        overflow-y: auto;
        ul {
          li {
            width: 100%;
            display: flex;
            align-items: center;
            height: 32px;
            padding: 0 16px;
            cursor: pointer;
            span.name {
              display: inline-block;
              width: calc(100% - 16px);
              padding-right: 10px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            &.active {
              // @include themeify {
              //   color: themed("font_color1");
              //   background-color: themed("background_color3");
              // }
              background-color: #edf6ff;
            }
          }
        }
      }
    }
  }
  .mc_right {
    width: calc(100% - 276px);
    height: 100%;
    margin-left: 16px;
    @include themeify {
      background-color: themed("background_color2");
    }
    @include themeify {
      box-shadow: 0px 1px 4px 0px themed("box_shadow_color1"),
        0px 0px 2px 0px themed("box_shadow_color2");
    }
    border-radius: 2px;
    float: left;
    .wrap {
      width: 100%;
      height: 100%;
      ::v-deep .table_content {
        width: 100%;
        height: calc(100% - 120px);
        padding-bottom: 16px;
        .table-box {
          height: 100%;
        }
      }
    }
  }
}

.tree_btn {
  i {
    display: block;
    font-size: 14px;
    font-style: normal;
    @include font_color("font_color2");
    line-height: 28px;
    text-align: center;
    cursor: pointer;
  }
  i:hover {
    @include themeify {
      background-color: themed("background_color4");
    }
  }
}
.columnDetailsDialog {
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}
</style>
<style lang="scss">
.el-popover {
  min-width: 88px !important;
  padding: 12px 0;
}
</style>
