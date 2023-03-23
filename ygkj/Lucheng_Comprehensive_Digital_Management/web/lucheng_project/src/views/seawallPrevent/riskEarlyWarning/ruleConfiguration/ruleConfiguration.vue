<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 14:20:14
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\riskEarlyWarning\ruleConfiguration\ruleConfiguration.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <div class="item_box_title">
        <p>规则配置</p>
      </div>
      <div
        class="info_option"
        style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
        "
      >
        <span style="margin-right: 10px">消息提醒：</span>
        <el-select
          style="margin-right: 10px"
          v-model="ruletype"
          placeholder="请选择"
          @change="getList"
        >
          <el-option label="全部" value=""></el-option>
          <el-option label="报警" :value="1"></el-option>
          <el-option label="提醒" :value="2"></el-option>
        </el-select>
        <template v-if="ruletype === 1">
          <span style="margin: 0px 10px">报警规则：</span>
          <el-select
            v-model="warningRuleValue"
            placeholder="请选择"
            @change="getList"
          >
            <el-option
              v-for="item in options"
              :key="item.value + '-warningRule'"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
        <template v-if="ruletype === 2">
          <span style="margin: 0px 10px">提醒规则：</span>
          <el-select
            v-model="reminderRuleValue"
            placeholder="请选择"
            @change="getList"
          >
            <el-option
              v-for="item in reminderRuleOptions"
              :key="item.value + '-reminderRule'"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
        <el-button
          type="primary"
          class="right"
          @click="operateAdd"
          v-permission="'ruleConfiguration:add'"
          >新增</el-button
        >
        <el-button
          style="margin-right: 10px"
          class="right"
          v-permission="'ruleConfiguration:export'"
          @click="exportTable()"
          >导出</el-button
        >
      </div>
      <div class="tb_wrapper">
        <el-table :data="projectList" border style="width: 100%">
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="warningrulename"
            align="center"
            label="报警规则"
          ></el-table-column>
          <el-table-column label="站点数" align="center">
            <template slot-scope="scope">
              <div
                class="blue"
                @click="thresholdSet(scope.row.optionType, scope.row.id)"
              >
                {{ scope.row.bindcount }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发送人数 " align="center">
            <template slot-scope="scope">
              <div class="blue" @click="personSet(scope.row.id)">
                {{ scope.row.sendcount }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="启闭状态" align="center">
            <template slot-scope="scope">
              <el-switch
                size="mini"
                v-model="scope.row.isopen"
                :active-value="1"
                :inactive-value="2"
                active-color="#1492FF"
                inactive-color="rgba(0,0,0,25%)"
                @change="changeSwitch(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="operate('edit', scope.row.id)"
                  v-permission="'ruleConfiguration:edit'"
                  >编辑</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="del(scope.row.id)"
                  v-permission="'ruleConfiguration:del'"
                  >删除</span
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="pageTotal"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          @pagination="getList"
        />
      </div>
    </div>

    <!-- 关联工程 -->
    <threshold-dialog
      ref="thresholdDialog"
      :visible="thresholdShow"
      :type="optionType"
      :checkedStations="tableForm.bindcode"
      @close="thresholdClose"
      @setThresholdId="setThresholdId"
    />
    <!-- 关联人员 -->
    <person-dialog
      ref="personDialog"
      :visible="personShow"
      :checkedPersons="tableForm.sendcode"
      @setPersonIds="setPersonIds"
      @saveEmit="setPersonIds"
      @close="personClose"
    />
    <base-form
      v-if="dialogVisible"
      :visible="dialogVisible"
      :isLook="isLook"
      :isNew="isNew"
      :info="dialogInfo"
      @close="close"
      @getList="getList"
    ></base-form>
  </div>
</template>

<script>
import { exportRuleList } from "@/utils/export.js";
import { ruleConfiguration } from "@/api/seawallPrevent";
import BaseForm from "./BaseForm.vue";
import thresholdDialog from "./thresholdDialog.vue";
import personDialog from "./personDialog.vue";
export default {
  data() {
    return {
      optionType: "seawall", //option选择报警规则 做不同请求
      options: [
        {
          label: "全部",
          value: "",
        },
        //   <=7 绑定海塘  >7 测站
        {
          value: "1",
          label: "安全鉴定超期",
        },
        {
          value: "2",
          label: "汛前检查超期",
        },
        {
          value: "3",
          label: "年度总结超期",
        },
        {
          value: "4",
          label: "巡查信息异常",
        },
        {
          value: "5",
          label: "工程隐患",
        },
        {
          value: "6",
          label: "海塘实时报警",
        },
        {
          value: "7",
          label: "海塘预报预警",
        },
        {
          value: "8",
          label: "时段雨量报警",
        },
        {
          value: "9",
          label: "河道水情报警",
        },
        {
          value: "10",
          label: "水库水情报警",
        },
        {
          value: "11",
          label: "潮位报警",
        },
        {
          value: "12",
          label: "测站关联报警",
        },
        {
          value: "13",
          label: "测站限值报警",
        },
      ],
      reminderRuleOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          value: "15",
          label: "日常检查提醒",
        },
        {
          value: "18",
          label: "汛前检查提醒",
        },
        {
          value: "16",
          label: "汛后检查提醒",
        },
        {
          value: "17",
          label: "年度检查提醒",
        },
        {
          value: "14",
          label: "预案资料提醒",
        },
      ],
      ruletype: "",
      warningRuleValue: "",
      reminderRuleValue: "",
      messageState: "",
      messageStateOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "未处置",
          value: "0",
        },
        {
          label: "已转发",
          value: "1",
        },
        {
          label: "已下发",
          value: "2",
        },
        {
          label: "已完结",
          value: "3",
        },
      ],
      warningRuleName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      addRuleValue: "", //规则列表
      projectList: [], //规则列表
      dialogVisible: false,
      isLook: false, // 是否为查看操作
      isNew: false, //是否为新增操作
      dialogInfo: {},
      thresholdShow: false, //关联工程弹窗
      personShow: false, //发送人弹窗
      tableForm: {
        //新增规则 表格信息
        bindcode: "",
        isopen: "",
        bindcount: "",
        sendcount: "",
        sendcode: "",
        warningrule: "",
        id: "",
        usersName: "",
        projectName: "",
        type: "",
      },
      tableFormOne: {
        isopen: "",
        id: "",
      },
    };
  },
  props: {},
  components: { BaseForm, thresholdDialog, personDialog },
  watch: {},
  computed: {},
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let warningrule = "";
      if (this.ruletype === 1) {
        warningrule = this.warningRuleValue;
      } else if (this.ruletype === 2) {
        warningrule = this.reminderRuleValue;
      }
      let res = await ruleConfiguration.list({
        ruletype: this.ruletype,
        warningrule: warningrule,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code === 0) {
        let list = res.data.list.map((i) => {
          const option =
            this.options.find((k) => i.warningrule == k.value) ||
            this.reminderRuleOptions.find((k) => i.warningrule == k.value);
          i.warningrulename = option.label;
          switch (i.warningrule) {
            case 8:
              i.optionType = "PP";
              break;
            case 9:
              i.optionType = "ZZ";
              break;
            case 10:
              i.optionType = "RR";
              break;
            case 11:
              i.optionType = "TT";
              break;
            default:
              i.optionType = "seawall";
              break;
          }
          return i;
        });
        this.projectList = list;
        this.pageTotal = res.data.totalRows;
      }
    },
    //导出
    async exportTable() {
      let obj = {
        warningrule: this.warningRuleValue,
      };
      exportRuleList(obj);
    },
    //新增
    operateAdd() {
      this.isLook = false;
      this.isNew = true;
      this.dialogVisible = true;
      this.dialogInfo = {};
    },
    //修改
    operate(type, id) {
      this.isLook = type == "look";
      this.isNew = false;
      this.dialogVisible = true;
      this.dialogInfo = {
        id: id,
      };
    },
    //删除
    del(id) {
      this.$confirm("此操作将删除该条数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await ruleConfiguration
            .del({
              id: id,
            })
            .then((res) => {
              if (res.code == 0) {
                this.$message({
                  message: "删除成功",
                  type: "success",
                });
                this.getList();
              } else {
                this.$message.error(res.msg);
              }
            });
        })
        .catch(() => {});
    },
    // 切换开闭状态
    changeSwitch(row) {
      // this.getListById(row.id, row.isopen);
      this.tableFormOne = {
        id: row.id,
        isopen: row.isopen,
        warningrule: row.warningrule,
      };
      this.saveForm();
    },
    async saveForm() {
      await ruleConfiguration.update(this.tableFormOne).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "切换成功",
            type: "success",
          });
          this.dialogClose();
          this.$emit("getList");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 显示接收人
    async personSet(id) {
      await this.getListById(id);
      this.personShow = true;
    },
    // 关闭接收人
    personClose() {
      this.personShow = false;
    },
    // 接收人返回数据
    setPersonIds(checkArr) {
      console.log(checkArr, "checkArr");
      let arr = [];
      let str = [];
      checkArr.forEach((item) => {
        arr.push(item.pid);
        str.push(item.name);
      });
      this.tableForm.sendcode = arr.join(",");
      this.tableForm.sendcount = str.length;
      this.tableForm.usersName = str.join(",");
      //this.tableForm.peopleNames = str.join(",");
    },
    // 显示关联工程
    async thresholdSet(optionType, id) {
      await this.getListById(id);
      this.optionType = optionType;
      this.thresholdShow = true;
    },

    // 关闭关联工程
    thresholdClose() {
      this.thresholdShow = false;
    },
    // 关联工程返回数据
    setThresholdId(val) {
      let arr = [];
      let str = [];
      val.forEach((item) => {
        arr.push(item.seawallCode);
        str.push(item.seawallName);
      });
      //this.tableForm.stationNames = str.join(",");
      this.tableForm.bindcode = arr.join(",");
      this.tableForm.bindcount = arr.length;
      this.tableForm.projectName = str.join(",");
    },
    // 重置
    resetForm() {
      this.$refs.tableForm.resetFields();
    },
    //关闭弹窗
    close() {
      this.dialogVisible = false;
    },
    //获取详情
    async getListById(id) {
      await ruleConfiguration.getById({ id }).then((res) => {
        if (res.code == 0) {
          this.tableForm = res.data;
          const {
            bindcode,
            isopen,
            sendcode,
            warningrule,
            id,
            projectName,
            usersName,
            sendcount,
            bindcount,
          } = res.data;
          this.tableForm = {
            bindcode,
            isopen,
            sendcode,
            warningrule,
            id,
            projectName,
            usersName,
            sendcount,
            bindcount,
          };
          if (res.data.bindcount && res.data.bindcount > 0) {
            let tempArr = [];
            res.data.bindList.map((i) => {
              tempArr.push(i.seawall_name);
            });
            this.tableForm.projectName = tempArr.join(",");
          }
          if (res.data.sendcount && res.data.sendcount > 0) {
            let tempArr = [];
            res.data.users.map((i) => {
              tempArr.push(i.name);
            });
            this.tableForm.usersName = tempArr.join(",");
          }
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.people-box {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  .people-right {
    @include contentBox();
  }
  .form-box {
    .form-lable-item {
      display: flex;
      align-items: center;
      padding-bottom: 30px;
      .label {
        width: 80px;
      }
      .item {
        width: calc(100% - 80px);
      }
    }
    .btn-box {
      text-align: right;
    }
  }
}
.blue {
  color: #1492ff;
  cursor: pointer;
}
</style>
