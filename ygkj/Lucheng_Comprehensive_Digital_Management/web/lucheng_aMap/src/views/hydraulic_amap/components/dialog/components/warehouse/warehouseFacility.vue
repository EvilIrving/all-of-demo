<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-31 16:33:37
 * @Description: 
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\dialog\components\warehouse\warehouseFacility.vue
-->
<template>
  <div>
    <div class="person-box">
      <!-- <div class="person-search">
        <LabelFormItem label="名称:">
          <el-input size="small" @change="search" placeholder="请输入" v-model="name" style="width: 180px" clearable />
        </LabelFormItem>
        <DataDetailButton style="margin-left: auto">导 出</DataDetailButton>
      </div> -->
      <div class="table-box">
        <!-- <v-table :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
          <template v-slot:name="row">
            <span>{{ row.data.name }}</span>
          </template>
          <template slot="operation">
            <span>123456</span>
          </template>
        </v-table> -->
        <div class="table-item">
          <PrimaryMapTable
            :columnList="dynamicColumnSetting"
            :dataList="tableData"
          />
        </div>
        <div class="bottom__pagination">
          <el-pagination
            :current-page.sync="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="totalPage"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSelectDevice } from "@/api/flow";
import { PrimaryMapTable } from "@/components";
export default {
  components: {
    PrimaryMapTable,
  },
  props: {
    code: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      name: "",
      dynamicColumnSetting: [
        {
          prop: "name",
          label: "物资名称",
        },
        {
          prop: "num",
          label: "数量",
        },
        {
          prop: "standard",
          label: "物资规格",
        },
        {
          prop: "amount",
          label: "物资价值",
        },
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
    };
  },
  mounted() {
    this.getWaterLoggingList();
  },
  methods: {
    onSizeChange(size) {
      this.pageNum = 1;
      this.pageSize = size;
      this.getWaterLoggingList();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getWaterLoggingList();
    },
    async getWaterLoggingList() {
      let res = await getSelectDevice({
        warehouseId: this.code,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        name: this.name,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    search() {
      this.pageNum = 1;
      this.getWaterLoggingList();
    },
  },
};
</script>

<style lang="scss" scoped>
.person-title {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid rgba(0, 193, 255, 0.2);
}
.person-box {
  height: 370px;
  padding-top: 16px;
  .table-box {
    height: 100%;
    .table-item{
      height: calc(100% - 40px);
    }
    .bottom__pagination {
      padding-top: 10px;
      text-align: right;
    }
  }
}
</style>

<style lang="scss">
.person-search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .label-form-item {
    .label-form-item__Label {
      color: #fff;
    }
    .el-input__inner {
      border: 1px solid #fff;
    }
  }
}
.person-box {
  .table-box {
    .bottom__pagination {
      .el-pagination__total {
        color: #fff;
      }
      .el-pagination {
        .el-pagination__sizes {
          .el-input__inner {
            border: 1px solid #fff;
          }
          .el-input__suffix {
            .el-input__icon {
              color: #fff;
            }
          }
        }
        .el-pager {
          li {
            color: #fff;
            &.active {
              background-image: linear-gradient(
                180deg,
                #36c0e4 20%,
                #0639ff 100%
              );
              border-radius: 0.125rem;
            }
          }
        }
      }
    }
  }
}
</style>