<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 14:20:23
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\riskEarlyWarning\alarmManagement\alarmManagement.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <div class="item_box_title">
        <p>报警管理</p>
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
            style="margin-right: 10px"
            v-model="warningRuleValue"
            placeholder="请选择"
            @change="getList"
          >
            <el-option
              v-for="item in warningRuleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </template>
        <template v-if="ruletype === 2">
          <span style="margin: 0px 10px">提醒规则：</span>
          <el-select
            style="margin-right: 10px"
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
        <span style="margin: 0px 10px">消息状态：</span>
        <el-select
          v-model="messageState"
          placeholder="请选择"
          @change="getList"
        >
          <el-option
            v-for="item in messageStateOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="tb_wrapper">
        <el-table v-loading="loading" :data="projectList" stripe border>
          <el-table-column
            type="index"
            label=""
            width="50"
            :index="indexMethod"
            align="center"
          />
          <el-table-column
            prop="warningrulename"
            label="报警规则"
            min-width="120"
            align="center"
          />
          <el-table-column
            prop="warningcontent"
            label="报警内容"
            min-width="120"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="warningtime"
            label="报警时间"
            min-width="120"
            align="center"
          ></el-table-column>
          <el-table-column prop="status" label="消息状态" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.status == '1'">已转发</span>
              <span v-if="scope.row.status == '2'">未转发</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="operate('look', scope.row.id)"
                  v-permission="'alarmManagement:look'"
                >
                  详情
                </span>
                <span v-if="scope.row.status == '2'">
                  <i class="divide"></i>
                  <span
                    class="deal_btn deal_btn_look"
                    v-permission="'alarmManagement:transpond'"
                    @click="operate('forward', scope.row.id)"
                  >
                    转发
                  </span>
                </span>
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
import BaseForm from "./BaseForm.vue";
import { alarmManagement } from "@/api/seawallPrevent";
export default {
  data() {
    return {
      loading: false,
      showEditDialog: false,
      departmentValue: "",
      warningRuleOptions: [
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
          label: "已转发",
          value: "1",
        },
        {
          label: "未转发",
          value: "2",
        },
      ],
      projectList: [], //规则列表
      dialogVisible: false,
      isLook: false, // 是否为查看操作
      isNew: false, //是否为转发操作
      dialogInfo: {},
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
    };
  },
  props: {},
  components: { BaseForm },
  watch: {},
  computed: {},
  methods: {
    async getList() {
      this.loading = true;
      let warningrule = "";
      if (this.ruletype === 1) {
        warningrule = this.warningRuleValue;
      } else if (this.ruletype === 2) {
        warningrule = this.reminderRuleValue;
      }
      let res = await alarmManagement.list({
        ruletype: this.ruletype,
        warningrule: warningrule,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        status: this.messageState,
      });
      if (res.code === 0) {
        this.loading = false;
        res.data.list.map((i) => {
          const option =
            this.warningRuleOptions.find((k) => i.warningrule == k.value) ||
            this.reminderRuleOptions.find((k) => i.warningrule == k.value);
          i.warningrulename = option.label;
        });
        this.projectList = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    //详情 或 转发
    operate(type, id) {
      this.isLook = type == "look";
      this.isNew = type == "forward";
      this.dialogVisible = true;
      this.dialogInfo = {
        id: id,
      };
    },
    //关闭弹窗
    close() {
      this.dialogVisible = false;
    },
    indexMethod(index) {
      return (this.pageNum - 1) * this.pageSize + index + 1;
    },
  },
  created() {},
  mounted() {
    this.getList();
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
}
</style>

<style lang="scss">
.tree-list .el-tree .custom-tree-node {
  width: 100%;
}
.tree-list .el-tree .el-tree-node__content {
  height: 48px;
  padding-right: 18px;
}
.tree-list
  .el-tree.el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background: url("../../../../assets/images/tree-active-icon.png") no-repeat;
  background-size: 100% 100%;
}
</style>
