<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-31 16:46:44
 * @Description: 
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\dialog\components\ReservoirDialog\reservoirPerson.vue
-->
<template>
  <div class="person">
    <div class="person-title">
      <StatisticsBox title="总人数" icon="icon-reservoir-person">
        <span>{{ totalPage }}</span> 人
      </StatisticsBox>
    </div>
    <div class="person-box">
      <div class="person-search">
        <LabelFormItem label="名称:">
          <el-input
            size="small"
            placeholder="请输入"
            @change="getTeamPersons()"
            v-model="name"
            style="width: 180px"
            clearable
          />
        </LabelFormItem>
      </div>
      <div class="person-table-box" ref="tableDom">
        <primary-map-table :columnList="dynamicColumnSetting" :dataList="tableData"></primary-map-table>
        <!-- <div class="bottom__pagination">
          <el-pagination
            :current-page.sync="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="totalPage"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
          />
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { StatisticsBox, LabelFormItem } from '../DetailsCommonDialog/components';
import { PrimaryMapTable } from '@/components';
import { getTeamPersonsList } from '@/api/flow';
export default {
  components: {
    StatisticsBox,
    LabelFormItem,
    PrimaryMapTable
  },
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      name: '',
      dynamicColumnSetting: [
        {
          prop: 'personName',
          label: '人员姓名'
        },
        {
          prop: 'personJob',
          label: '职务'
        },
        {
          prop: 'officePhone',
          label: '办公电话'
        },
        {
          prop: 'personPhone',
          label: '移动电话'
        }
      ],
      tableData: [],
      tableHeight: '',
      totalPage: 0
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = this.$refs.tableDom.offsetHeight;
      console.log(this.tableHeight);
    });
    this.getTeamPersons(1);
  },
  methods: {
    async getTeamPersons(type) {
      let res = await getTeamPersonsList({
        teamId: this.code,
        personName: this.name,
        pageNum: 1,
        pageSize: 9999
      });
      console.log(res);
      if (res.code == 0) {
        if (type) {
          this.totalPage = res.data.totalRows;
        }
        this.tableData = res.data.list;
      }
    },
    onSizeChange() {},
    onCurrentChange() {}
  }
};
</script>

<style lang="scss" scoped>
.person {
  height: 100%;
}
.person-title {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid rgba(0, 193, 255, 0.2);
}
.person-box {
  padding-top: 16px;
  height: calc(100% - 90px);
  // background-color: red;
  .person-table-box {
    margin-top: 24px;
    height: calc(100% - 90px);
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
.person-table-box {
  .table-wrap {
    .el-table {
      .table-header {
        background: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
        th {
          background: transparent;
          color: #00baff;
        }
      }
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
              background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
              border-radius: 0.125rem;
            }
          }
        }
      }
    }
  }
}
</style>