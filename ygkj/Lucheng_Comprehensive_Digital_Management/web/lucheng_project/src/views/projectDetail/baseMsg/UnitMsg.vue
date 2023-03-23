<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-14 13:54:18
 * @Description: 单位信息
 * @FilePath: \lucheng_project\src\views\projectDetail\baseMsg\UnitMsg.vue
-->
<template>
  <div class="wrap">
    <div class="wrap_top">
      <div class="item_box">
        <div class="item_box_title">
          <p>单位信息</p>
        </div>
        <div class="info">
          <span class="text" v-if="Introduction">{{Introduction}}</span>
          <span class="nodata" v-else>无</span>
        </div>
        <div class="image">
          <img src="" alt="">
        </div>
      </div>
      <div class="item_box">
        <div class="item_box_title">
          <p>组织结构</p>
        </div>
        <organizational-structure-chart @getInfo='getInfo' :chartData='chartData'></organizational-structure-chart>
      </div>
      <div class="item_box">
        <div class="item_box_title">
          <p>人员信息</p>
          <el-input size="mini" placeholder="请输入内容" clearable suffix-icon="el-icon-search" v-model="peopleName" @change="getListPeople">
          </el-input>
        </div>
        <div class="tb_wrapper">
          <div class="table">
            <el-table :data="personalList" border style="width: 100%;">
              <el-table-column prop="name" label="人员名称" align="center"></el-table-column>
              <el-table-column prop="telphone" label="联系电话" align="center"></el-table-column>
              <el-table-column :show-overflow-tooltip="true" label="职务" align="center">
                <template slot-scope="scope">
                  <div>
                    {{ scope.row.duty?scope.row.duty:scope.row.jobName }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div class="item_box">
        <div class="item_box_title">
          <p>年度培训</p>
          <el-date-picker size="mini" format="yyyy" value-format="yyyy" @change="getPersonTrain" v-model="personTrainYear" type="year" placeholder="选择年">
          </el-date-picker>
        </div>
        <div class="tb_wrapper">
          <div class="table">
            <el-table :show-overflow-tooltip="true" :data="personTrain" border style="width: 100%;height: 100%;">
              <el-table-column prop="year" label="年份" align="center"></el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="content" label="内容" align="center"></el-table-column>
              <!-- <el-table-column align="center" label="内容" width="150">
              <template slot-scope="scope">
                <div class="content_box">
                  <span>{{scope.row.content}}</span>
                </div>
              </template>
            </el-table-column> -->
              <el-table-column prop="fund" label="培训经费" align="center"></el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="time" label="时间" align="center"></el-table-column>
              <!-- <el-table-column align="center" label="时间" width="150">
              <template slot-scope="scope">
                <div class="content_box">
                  <span>{{scope.row.time}}</span>
                </div>
              </template>
            </el-table-column> -->
            </el-table>
          </div>
        </div>
      </div>
      <div class="item_box">
        <div class="item_box_title">
          <p>管护经费</p>
          <el-date-picker size="mini" format="yyyy" value-format="yyyy" @change="getManagefund" v-model="ManagefundYear" type="year" placeholder="选择年">
          </el-date-picker>
        </div>
        <double-bar-chart class="chart_wrap" :chartData="barData"></double-bar-chart>
      </div>
      <div class="item_box">
        <div class="item_box_title">
          <p>管理制度</p>
        </div>
        <div class="management_system" v-if="Manageinstitution.length">
          <div class="sys_box">
            <div class="top">
              <span>{{Manageinstitution[0].name}}</span>
            </div>
            <div class="bottom">
              <i class="el-icon-document"></i>
              <span>{{Manageinstitution[0].name}}</span>
            </div>
          </div>
          <div class="sys_box">
            <div class="top">
              <span>{{Manageinstitution[1].name}}</span>
            </div>
            <div class="bottom">
              <i class="el-icon-document"></i>
              <span>{{Manageinstitution[1].name}}</span>
            </div>
          </div>
          <div class="sys_box">
            <div class="top">
              <span>{{Manageinstitution[2].name}}</span>
            </div>
            <div class="bottom">
              <i class="el-icon-document"></i>
              <span>{{Manageinstitution[2].name}}</span>
            </div>
          </div>
        </div>
        <div class="management_system" v-else>
          <span class="nodata">无</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { maintenance } from "@/api/projectDetail";
import DoubleBarChart from '@/components/DoubleBarChart'
import OrganizationalStructureChart from '@/components/OrganizationalStructureChart.vue';
export default {
  name: 'UnitMsg',
  props: {},
  components: {
    OrganizationalStructureChart, DoubleBarChart
  },
  data() {
    return {
      barData: {
        id: 'manageBarChart',
        unit: '',
        xData: ['dddd', 'aaa'],
        yData: [[3, 25], [15, 2]]
      },
      barChartOption: {},
      chartData: {
        id: 'organizationalstructurechart',
        data: [
          {
            name: '',
            children: [

            ],
          },
        ],
      },
      Introduction: '',
      ManagefundYear: '2021',
      peopleName: '',
      personTrainYear: "2021",
      slectedYear: '',
      pageSize: 10,
      pageNum: 1,
      pageTotal: 0,
      personalList: [],
      managefund: [],
      Manageinstitution: [],
      personTrain: [],
      depInfo: [],
    };
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail
    }
  },
  mounted() {
    this.getOrganization()
    this.getIntroduction()
    this.getListPeople()
    this.getPersonTrain()
    this.getManagefund()
    this.getManageinstitution()
  },
  methods: {
    async getInfo(params) {
      let opt = {
        jobType: true,
        pjcd: true,
      }
      await maintenance.organizationDetail(opt).then((res) => {
        if (res.code === 0) {
          this.depInfo = res.data
        }
      });
    },
    async getIntroduction() {
      await maintenance.introduction(this.seawallDetail.seawallCode).then((res) => {
        if (res.code === 0) {
          this.Introduction = res.data
        }
      });
    },
    async getListPeople() {
      let opt = {
        seawallCode: this.seawallDetail.seawallCode,
        fetchAll: true,
        name: this.peopleName,
      }
      await maintenance.listPeople(opt).then((res) => {
        if (res.code === 0 && res.data.list.length > 0) {
          this.personalList = res.data.list
        } else {
          this.personalList = []
        }
      });
    },
    async getPersonTrain() {
      let opt = {
        seawallCode: this.seawallDetail.seawallCode,
        fetchAll: true,
        year: this.personTrainYear,
      }
      await maintenance.personTrain(opt).then((res) => {
        if (res.code === 0 && res.data.list.length > 0) {
          this.personTrain = res.data.list
        } else {
          this.personTrain = []
        }
      });
    },

    async getManagefund() {
      let opt = {
        seawallCode: this.seawallDetail.seawallCode,
        year: this.ManagefundYear,
      }
      await maintenance.managefund(opt).then((res) => {
        if (res.code === 0) {
          if (res.data && res.data.length > 0) {
            let xData = [res.data[0].quarter, res.data[1].quarter]
            let yData = [[res.data[0].foudTotal, res.data[0].foudFulfil], [res.data[1].foudTotal, res.data[1].foudFulfil]]
            this.barData.xData = xData
            this.barData.yData = yData
          } else {
            this.barData = {
              id: 'manageBarChart',
              unit: '',
              xData: ['', ''],
              yData: [[0, 0], [0, 0]]
            }
          }
        }
      });
    },
    async getManageinstitution() {
      await maintenance.manageinstitution(this.seawallDetail.seawallCode).then((res) => {
        if (res.code === 0 && res.data.length > 0) {
          this.Manageinstitution = res.data
        } else {
          this.Manageinstitution = []
        }
      });
    },
    async getOrganization() {
      await maintenance.organization(this.seawallDetail.seawallCode).then((res) => {
        if (res.code === 0 && res.data.length > 0) {
          res.data.map(i => {
            i.name = i.jobTypeName
          })
          this.chartData.data[0].name = this.seawallDetail.seawallName
          this.chartData.data[0].children = res.data;
        } else {
          this.chartData.data[0].name = '无'
          this.chartData.data[0].children = [];
        }
      });
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  padding-bottom: 16px;
  .wrap_top {
    @include flexbox();
    @include flexJC(space-between);
    flex-wrap: wrap;
    margin-bottom: 16px;
    .item_box {
      @include contentBox();
      width: calc(33% - 12px);
      min-height: 350px;
      .info {
        padding: 10px;
        .text {
          line-height: 1.5;
          text-indent: 10px;
        }
      }
      .chart_wrap {
        width: 100%;
        height: calc(100% - 56px);
      }
      &:nth-child(1),
      &:nth-child(2),
      &:nth-child(3) {
        margin-bottom: 30px;
      }
    }
  }
  .content_box {
    @include contentBox();
    box-sizing: border-box;
  }
}
.item_box_title {
  border: none;
  .el-input {
    width: 30%;
  }
}
.tb_wrapper {
  width: 100%;
  height: 300px;
  box-sizing: border-box;
  overflow: auto;
  .table {
    width: 100%;
    height: 100%;
    overflow: auto;
    .el-table {
      position: relative;
      overflow: hidden;
      box-sizing: border-box;
      flex: 1;
      width: 100%;
      max-width: 100%;
      background-color: #fff;
      font-size: 0.875rem;
      color: #606266;
    }
  }
}
.management_system {
  @include flexbox();
  flex-direction: column;
  .sys_box {
    width: calc(100% - 40px);
    height: 100%;
    @include flexbox();
    flex-direction: column;
    margin: 0px 20px 16px;
    .top {
      @include flexbox();
      @include flexJC(flex-start);
      @include flexAI(center);
      min-height: 30px;
      background: #dddddd;
      border: 1px solid #dddddd;
    }
    .bottom {
      @include flexbox();
      @include flexJC(flex-start);
      @include flexAI(center);
      min-height: 30px;
      border: 1px solid #fff;
      i,
      span {
        color: #1492ff;
        font-size: 14px;
        font-weight: 500;
      }
    }
  }
}
.nodata {
  font-size: 16px;
  padding-left: 10px;
  // left: 50%;
  // top: 50%;
  // transform: (-50%,-50%);
}
</style>