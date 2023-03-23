<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-07 17:10:24
 * @Description: 专题简报
 * @FilePath: /rscp-big-screen/src/components/PreviewMessage.vue
-->
<template>
  <div
    class="preview_bg"
    element-loading-background="rgba(0, 0, 0, .4)"
    @click="closeLookMessagePopup()"
  >
    <div class="preview_box" @click.stop>
      <i class="el-icon-close" @click="closeLookMessagePopup()"></i>
      <div class="dialog_title">
        预警短信
      </div>
      <div class="dialog_box">
        <div class="fangan_select">
          <div
            class="planItem"
            v-for="(item, index) in fanganList"
            :key="index"
            :class="index == fanganIndex ? 'active' : ''"
            @click="fanganClick(index)"
          >
            {{ item.name }}
          </div>
        </div>
        <div class="person_select">
          <div class="person_list">
            <div
              class="person_item"
              v-for="(item, index) in personList"
              :key="index"
              :class="item.selected ? 'active' : ''"
              @click="personClick(index)"
            >
              <div>{{ item.job }}</div>
              <img src="../assets/images/person.png" alt="" />
              <p>
                <span v-html="item.title"></span>
              </p>
            </div>
          </div>
          <div class="send_btn">
            <el-button
              class="deepblue"
              type="primary"
              v-loading="btnLoading"
              icon="el-icon-s-promotion"
              @click="sendMsg()"
              >一键发送</el-button
            >
          </div>
        </div>
        <div class="table_list">
          <div class="list_search">
            <div class="scale_search">
              <div
                :class="['scale_item', index == scaleIndex ? 'active' : '']"
                v-for="(item, index) in scaleOptions"
                :key="index"
                @click="scaleClick(index)"
              >
                {{ item.name }}
              </div>
            </div>
            <el-input
              prefix-icon="el-icon-search"
              class="searchInput"
              v-model="name"
              clearable
              placeholder="请输入工程名称"
            ></el-input>
            <el-input
              prefix-icon="el-icon-search"
              class="searchInput"
              v-model="searchKeyward"
              clearable
              placeholder="请输入姓名、手机号"
            ></el-input>
            <el-button class="deepblue" type="primary" @click="getMsgList()"
              >搜索</el-button
            >
          </div>
          <div class="table_wrap" ref="tableContainer">
            <el-table
              :height="tableHeight"
              v-loading="tableLoading"
              :data="tableData"
              stripe
              class="table"
            >
              <template slot="empty">
                <div class="table_nodata">
                  <img src="../assets/images/noData.png" alt="" />
                </div>
              </template>
              <el-table-column type="index" label="序号"></el-table-column>
              <el-table-column label="水库名称" width="80">
                <template slot-scope="scope">
                  <div>{{ scope.row.NAME ? scope.row.NAME : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="行政区划" width="80">
                <template slot-scope="scope">
                  <div>{{ scope.row.cityADNM ? scope.row.cityADNM : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="水库规模" width="100">
                <template slot-scope="scope">
                  <div v-if="scope.row.project_scale == '1'">大(1)型</div>
                  <div v-if="scope.row.project_scale == '2'">大(2)型</div>
                  <div v-if="scope.row.project_scale == '3'">中型</div>
                  <div v-if="scope.row.project_scale == '4'">小(1)型</div>
                  <div v-if="scope.row.project_scale == '5'">小(2)型</div>
                </template>
              </el-table-column>
              <el-table-column label="最后一次短信发送时间" width="140">
                <template slot-scope="scope">
                  <div>{{ scope.row.sendTime ? scope.row.sendTime : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="发送状态" width="80">
                <template slot-scope="scope">
                  <div v-if="scope.row.sendStatus == '0'">发送成功</div>
                  <div v-if="scope.row.sendStatus == '1'">发送失败</div>
                </template>
              </el-table-column>

              <!-- <el-table-column label="人员类型">
                <template slot-scope="scope">
                  <div>{{scope.row.pstype ? scope.row.pstype : '-'}}</div>
                </template>
              </el-table-column>
              <el-table-column label="单位名称">
                <template slot-scope="scope">
                  <div>{{scope.row.psunit ? scope.row.psunit : '-'}}</div>
                </template>
              </el-table-column> -->
              <el-table-column label="姓名" width="80">
                <template slot-scope="scope">
                  <div>{{ scope.row.psnm ? scope.row.psnm : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="职务" width="80">
                <template slot-scope="scope">
                  <div>{{ scope.row.position ? scope.row.position : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="手机号" width="100">
                <template slot-scope="scope">
                  <div>{{ scope.row.phone ? scope.row.phone : "-" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="预警等级" width="80">
                <template slot-scope="scope">
                  <div v-if="scope.row.remark == 'waterLevel50'">50mm</div>
                  <div v-if="scope.row.remark == 'waterLevel100'">100mm</div>
                  <div v-if="scope.row.remark == 'waterLevel150'">150mm</div>
                  <div v-if="scope.row.remark == 'waterLevel200'">200mm</div>
                  <div v-if="scope.row.remark == 'waterLevel250'">250mm</div>
                  <div v-if="scope.row.remark == 'waterLevel300'">300mm</div>
                </template>
              </el-table-column>
              <el-table-column label="预警内容">
                <template slot-scope="scope">
                  <div>{{ scope.row.message ? scope.row.message : "-" }}</div>
                </template>
              </el-table-column>
              <!-- <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <div class="look_detail" @click="lookDetail(scope.row)">查看详情</div>
                </template>
              </el-table-column> -->
            </el-table>
          </div>
        </div>
        <!-- <el-pagination
          background
          layout="prev, pager, next"
          :total="">
        </el-pagination> -->
      </div>
    </div>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "PreviewBrief",
  props: {},
  components: {},
  computed: {},
  data() {
    return {
      fanganList: [
        {
          name: "50mm",
          value: "waterLevel50",
        },
        {
          name: "100mm",
          value: "waterLevel100",
        },
        // {
        //   name: '150mm',
        //   value: 'waterLevel150'
        // },
        {
          name: "200mm",
          value: "waterLevel200",
        },
        // {
        //   name: '250mm',
        //   value: 'waterLevel250'
        // },
        // {
        //   name: '300mm',
        //   value: 'waterLevel300'
        // }
      ],
      fanganIndex: 0,
      planId: "waterLevel50",
      personList: [
        {
          job: "副主任",
          title: "政府负责人",
          type: "executive_gov",
          selected: false,
        },
        {
          job: "副局长",
          title: "水行政主管<br>部门负责人",
          type: "executive_water",
          selected: false,
        },
        {
          job: "副总经理",
          title: "主管部门<br>（产权人）责任人",
          type: "executive_depart",
          selected: false,
        },
        {
          job: "副总经理",
          title: "管理单位责任人",
          type: "executive_manage",
          selected: false,
        },
        {
          job: "技术负责人",
          title: "综合管理部经理",
          type: "technical",
          selected: false,
        },
        {
          job: "巡查负责人",
          title: "现场负责",
          type: "patrol",
          selected: false,
        },
      ],
      personIndex: 0,
      personValue: [],
      tableData: [],
      tableHeight: 0,
      name: "",
      searchKeyward: "",
      tableLoading: true,
      btnLoading: false,
      scaleIndex: "",
      scaleValue: "",
      scaleOptions: [
        {
          name: "所有规模",
          value: "",
        },
        {
          name: "大(1)型水库",
          value: "1",
        },
        {
          name: "大(2)型水库",
          value: "2",
        },
        {
          name: "中型水库",
          value: "3",
        },
        {
          name: "小(1)型水库",
          value: "4",
        },
        {
          name: "小(2)型水库",
          value: "5",
        },
      ],
    };
  },
  computed: {
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {},
  mounted() {
    this.resizeTable();
    this.getMsgList();
  },
  methods: {
    fanganClick(index) {
      this.fanganIndex = index;
      this.planId = this.fanganList[index].value;
      this.getMsgList();
    },
    personClick(index) {
      let value = this.personList[index].type;
      if (this.checkPerson(value)) {
        this.deletePerson(value);
        this.personList[index].selected = false;
      } else {
        this.personList[index].selected = true;
        this.personValue.push(value);
      }
    },
    scaleClick(index) {
      this.scaleIndex = index;
      this.scaleValue = this.scaleOptions[index].value;
      this.getMsgList();
    },
    checkPerson(value) {
      if (this.personValue.length) {
        for (let i = 0; i < this.personValue.length; i++) {
          if (this.personValue[i] == value) {
            return true;
          }
        }
      }
      return false;
    },
    deletePerson(value) {
      for (let i = 0; i < this.personValue.length; i++) {
        if (this.personValue[i] == value) {
          this.personValue.splice(i, 1);
        }
      }
    },
    async sendMsg() {
      this.$alert(
        `短信预警成功！本次预警共涉及工程10座，涉及预警人员20位`,
        "提示",
        {
          confirmButtonText: "确定",
          callback: (action) => {},
        }
      );
      return;
      if (!this.personValue.length) {
        this.$message.warning("请选择短信发送对象！");
        return false;
      }
      this.btnLoading = true;
      let warnPerson = this.personValue.join("|");
      let res = await reservoirLeftStatisticApi({
        statistic: "sendSms",
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "forecast,warnPerson",
        itemValue: this.planId + "," + warnPerson,
      });
      if (res.success) {
        this.$alert(
          `短信预警成功！本次预警共涉及工程${res.data[0].projCnt}座，涉及预警人员${res.data[0].personCnt}位`,
          "提示",
          {
            confirmButtonText: "确定",
            callback: (action) => {},
          }
        );
        this.getMsgList();
      }
      this.btnLoading = false;
    },
    closeLookMessagePopup() {
      this.$emit("closeLookMessagePopup");
    },
    async getMsgList() {
      this.tableLoading = true;
      this.tableData = [];
      let opt = {
        name: this.name,
        statistic: "smsLog",
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "warnType",
        itemValue: this.planId,
      };
      if (this.scaleValue && this.searchKeyward) {
        opt = {
          name: this.name,
          statistic: "smsLog",
          adcd: this.$localData("get", "userInfo").adcd,
          itemCategory: "warnType,searchKeyward,scale",
          itemValue:
            this.planId + "," + this.searchKeyward + "," + this.scaleValue,
        };
      } else if (this.scaleValue) {
        opt = {
          name: this.name,
          statistic: "smsLog",
          adcd: this.$localData("get", "userInfo").adcd,
          itemCategory: "warnType,scale",
          itemValue: this.planId + "," + this.scaleValue,
        };
      } else if (this.searchKeyward) {
        opt = {
          name: this.name,
          statistic: "smsLog",
          adcd: this.$localData("get", "userInfo").adcd,
          itemCategory: "warnType,searchKeyward",
          itemValue: this.planId + "," + this.searchKeyward,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        if (res.data.length) {
          this.tableData = res.data;
        }
      }
      this.tableLoading = false;
    },
    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.preview_bg {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1001; //大于地图
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  .el-icon-close {
    position: absolute;
    top: 10px;
    right: 10px;
    color: #999;
    font-size: 20px;
    cursor: pointer;
    z-index: 10;
  }
  .preview_box {
    width: 70%;
    height: 95%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    .chart_title {
      font-weight: bold;
      font-size: 18px;
    }
    .words_box {
      padding: 4px 16px;
      h3 {
        font-weight: bold;
        font-size: 14px;
        color: #333;
        line-height: 36px;
        margin-top: 8px;
      }
      p {
        font-size: 14px;
        color: #666;
        line-height: 30px;
      }
    }
  }
  .dialog_title {
    background: #e7f3fb;
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    padding-left: 16px;
    font-weight: bold;
  }
  .dialog_box {
    background: #f9f9f9;
    height: calc(100% - 40px);
    .fangan_select {
      background: #ffffff;
      @include flexbox;
      @include flexAC();
      @include flexJC(center);
      padding: 14px 0;
      .planItem {
        text-align: center;
        height: 30px;
        line-height: 30px;
        color: #000;
        padding: 0 12px;
        background: rgba(152, 189, 250, 0.15);
        border: 1px solid #98bdfa;
        cursor: pointer;
        margin-right: 5px;
        &.active {
          border: 1px solid #193cc5;
          color: #fff;
          background: #193cc5;
        }
      }
    }
    .person_select {
      margin-top: 10px;
      background: #ffffff;
      padding: 20px 0;
      .person_list {
        @include flexbox;
        @include flexAC();
        @include flexJC(center);
        .person_item {
          width: 160px;
          height: 176px;
          background: #f8f8f8;
          border: 2px solid #d6d6d6;
          border-radius: 2px;
          margin-right: 8px;
          text-align: center;
          box-sizing: border-box;
          cursor: pointer;
          div {
            height: 32px;
            line-height: 32px;
            background: url("../assets/images/jobbg.png") no-repeat center;
            background-size: cover;
            color: #193cc5;
          }
          img {
            margin: 8px 0 4px 0;
          }
          p {
            height: 40px;
            @include flexbox;
            @include flexAC();
            @include flexJC(center);
            span {
              font-size: 16px;
              line-height: 20px;
              color: #000000;
              letter-spacing: 2px;
            }
          }
          &.active {
            background: rgba(152, 189, 250, 0.15);
            border: 2px solid #193cc5;
            border-radius: 2px;
          }
        }
      }

      .send_btn {
        width: 100%;
        text-align: center;
        padding-top: 20px;
      }
    }
    .table_list {
      margin-top: 10px;
      padding: 0;
      height: calc(100% - 380px);
      .list_search {
        width: 100%;
        height: 60px;
        background: #ffffff;
        margin-bottom: 10px;
        @include flexbox();
        @include flexAC();
        .scale_search {
          @include flexbox();
          @include flexAC();
          padding-left: 16px;
          margin-right: 30px;
          .scale_item {
            height: 40px;
            line-height: 40px;
            box-sizing: border-box;
            color: #666;
            font-size: 14px;
            padding: 0 16px;
            background: #f8f8f8;
            border: 1px solid #d6d6d6;
            border-left: none;
            cursor: pointer;
            &:first-child {
              border-left: 1px solid #d6d6d6;
            }
            &.active {
              background: #193cc5;
              border-radius: 2px 0 0 2px;
              color: #fff;
            }
          }
        }
        .searchInput {
          width: 220px;
          margin-right: 30px;
        }
      }
      .table_wrap {
        height: calc(100% - 60px);
        padding: 0 20px;
        background: #ffffff;
      }
      .table {
        width: 100%;
        // box-sizing: border-box;
        // padding: 0 20px;
      }
      .look_detail {
        cursor: pointer;
        color: rgb(59, 94, 233);
      }
    }
  }
}
</style>

<style lang="scss">
.deepblue.el-button--primary {
  color: #fff;
  background-color: #193cc5;
  border-color: #193cc5;
  &:hover {
    background-color: rgb(62, 95, 226);
    border-color: rgb(62, 95, 226);
  }
}
</style>
