<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-03-08 14:56:06
 * @Description: 
 * @FilePath: /lucheng/src/views/water_disaster_custom/dialog/DetailsCommonDialog/components/cardListDetail/seawallCardList/components/components/PageDanger.vue
-->
<template>
  <div class="page-patrol">
    <div class="water-from-box">
      <span style="color:white">发现方式：</span>
      <el-select size="mini" v-model="foundmethod" placeholder="请选择" clearable @change="getPagePatrol">
        <el-option v-for="item in options" :key="item.val" :label="item.val" :value="item.code" placeholder="请选择">
        </el-option>
      </el-select>
      <span style="color:white;margin-left: 10px;">时间：</span>
      <el-date-picker size="mini" v-model="timeChangeValue" type="datetimerange" :picker-options="pickerOptions" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" align="right" clearable @change="getPagePatrol">
      </el-date-picker>
      <data-detail-button class="data-detail-button" @click="exportExcel" style="margin-left: auto">导 出</data-detail-button>
    </div>
    <v-table :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
      <template v-slot:picaid="row">
        <span v-if="row.data.picaid" class="look">有</span>
        <span v-else>-</span>
      </template>
      <template v-slot:afteraid="row">
        <span v-if="row.data.afteraid" class="look">有</span>
        <span v-else>-</span>
      </template>
    </v-table>
    <div class="bottom__pagination">
      <el-pagination :current-page.sync="pageNum" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next" :total="totalPage" @size-change="onSizeChange" @current-change="onCurrentChange" />
    </div>
    <lookFiles v-if="visible" @close="visible = false" :appendType="true" :visible="visible" :id="fileId" />
  </div>
</template>

<script>
import { getSeawallPageDanger, listDict } from '@/api/seawall.js';
import VTable from '../../../../table/VTable.vue';
import lookFiles from '@/components/look_pdf/lookFile.vue';
import DataDetailButton from "../../../../DataDetailButton";
import { mapGetters } from 'vuex';
import { exportPageDanger } from "@/api/micro-service/project-manage/export";
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dynamicColumnSetting: [
        {
          prop: 'abbreviation',
          label: '简称'
        },
        {
          prop: 'personliable',
          label: '责任人'
        },
        {
          prop: 'position',
          label: '部位'
        },
        {
          prop: 'description',
          label: '描述及说明'
        },
        {
          prop: 'phone',
          label: '治理负责人电话'
        },
        {
          prop: 'foundmethod',
          label: '发现方式'
        },
        {
          prop: 'disposalmethod',
          label: '处置方式'
        },
        {
          prop: 'foundtime',
          label: '发现时间'
        },
        {
          prop: 'planCompTm',
          label: '计划治理完成时间'
        },
        {
          prop: 'actlCompTm',
          label: '实际完成治理时间'
        },
        {
          prop: 'manageDesc',
          label: '治理说明'
        },
        {
          prop: 'picaid',
          label: '隐患照片',
          slot: true
        },
        {
          prop: 'afteraid',
          label: '治理后照片',
          slot: true
        }
      ],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      visible: false,
      timeChangeValue: [],
      fileId: '',
      options: [],
      foundmethod: ''
    };
  },
  components: {
    VTable,
    lookFiles,
    DataDetailButton
  },
  computed: {
    ...mapGetters(['SeawallActive']),
  },
  async mounted() {
    await this.getlistDict()
    this.getPagePatrol();
  },
  methods: {
    async getlistDict() {
      let res = await listDict();
      if (res.code == 0 && res.data) {
        this.options = res.data;
      }
    },
    async getPagePatrol() {
      let res = await getSeawallPageDanger({
        projectCode: this.code,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        startTime: this.timeChangeValue ? this.timeChangeValue[0] : '',
        endTime: this.timeChangeValue ? this.timeChangeValue[1] : '',
        seawallName: this.SeawallActive.name,
        foundmethod: this.foundmethod
      });
      if (res.code == 0 && res.data) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    lookFiles(data) {
      this.fileId = data;
      this.visible = true;
    },
    onSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getPagePatrol();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getPagePatrol();
    },
    // 导出
		async exportExcel() {
			const obj = {
				projectCode: this.code,
        startTime: this.timeChangeValue ? this.timeChangeValue[0] : '',
        endTime: this.timeChangeValue ? this.timeChangeValue[1] : '',
        seawallName: this.SeawallActive.name,
        foundmethod: this.foundmethod
			};
			exportPageDanger(obj);
		},
  }
};
</script>

<style lang="scss" scoped>
.page-patrol {
  .look {
    cursor: pointer;
    color: #00baff;
  }
  .bottom__pagination {
    padding-top: 10px;
    text-align: right;
  }
}
</style>

<style lang="scss">
.el-pager li {
  background: transparent;
  color: #fff;
}
.el-pager li.active {
  background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
  border-radius: 2px;
  color: #fff;
}
.el-pagination__total {
  color: #fff;
}
.el-pagination .el-select .el-input .el-input__inner {
  border: 1px solid #fff;
}
.el-input__suffix {
  .el-input__suffix-inner {
    .el-select__caret {
      color: #fff;
    }
  }
}
</style>
