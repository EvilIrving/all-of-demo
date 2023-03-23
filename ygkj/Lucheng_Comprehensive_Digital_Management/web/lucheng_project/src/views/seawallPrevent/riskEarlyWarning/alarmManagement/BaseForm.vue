<template>
  <div class="base_form">
    <el-dialog
      :visible.sync="visible"
      width="1000px"
      :before-close="dialogClose"
      top="15vh"
      :title="title"
      class="list-dialog"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        ref="tableForm"
        :model="tableForm"
        :rules="rules"
        size="mini"
        label-width="130px"
        label-position="right"
      >
        <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="报警规则：" prop="warningrulename">
              <el-input
                disabled
                type="textarea"
                v-model="tableForm.warningrulename"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="报警内容：" prop="warningcontent">
              <el-input
                disabled
                type="textarea"
                v-model="tableForm.warningcontent"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="报警时间：" prop="warningtime">
              <el-input disabled v-model="tableForm.warningtime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!isNew" type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="接收人员：" prop="usersName">
              <el-input disabled v-model="tableForm.usersName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!isNew" type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="消息状态：" prop="statusname">
              <el-input disabled v-model="tableForm.statusname"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="isNew" type="flex" justify="left">
          <el-col :span="24">
            <el-form-item label="接收人：" prop="usersName">
              <el-input
                placeholder="请选择接收人"
                type="textarea"
                @focus="personSet"
                v-model="tableForm.usersName"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-row>
          <div class="form-footer">
            <!-- 查看 -->
            <el-button
              v-if="!isNew"
              class="form-btn btn-cancel"
              @click="dialogClose"
              size="mini"
              >确定</el-button
            >
            <!-- 转发 -->
            <el-button
              v-if="isNew"
              class="form-btn btn-cancel"
              size="mini"
              @click="dialogClose"
              >取消</el-button
            >
            <el-button
              v-if="isNew"
              class="form-btn"
              size="mini"
              @click="saveData"
              >确定</el-button
            >
          </div>
        </el-row>
      </div>
    </el-dialog>
    <!-- 关联人员 -->
    <person-dialog
      ref="personDialog"
      :visible="personShow"
      :checkedPersons="tableForm.sendcode"
      @setPersonIds="setPersonIds"
      @saveEmit="setPersonIds"
      @close="personClose"
    />
  </div>
</template>
<script>
import { alarmManagement } from "@/api/seawallPrevent";
import personDialog from "../ruleConfiguration/personDialog.vue";

export default {
  name: "baseForm",
  props: {
    visible: Boolean,
    info: Object,
    isLook: Boolean,
    isNew: Boolean,
  },
  watch: {
    visible(val) {
      this.show = val;
    },
  },
  components: { personDialog },
  data() {
    return {
      rules: {
        users: [
          {
            required: true,
            message: "请选择接收人",
          },
        ],
      },
      show: false,

      title: "详情",
      tableForm: {
        warningrule: "",
        warningrulename: "",
        warningcontent: "",
        warningtime: "",
        usersName: "",
        sendcode: "",
        status: "",
        statusname: "",
        sendcount: "",
      },
      provinceArr: [],
      cityArr: [],
      countyArr: [],
      personShow: false,
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
      ],
      messageStateOptions: [
        {
          label: "已转发",
          value: "1",
        },
        {
          label: "未转发",
          value: "2",
        },
      ],
    };
  },
  mounted() {
    this.$nextTick(() => {
      if (this.isNew) {
        this.title = "转发";
      } else {
        if (this.isLook) {
          this.title = "查看";
        }
      }
      //调用获取详情数据方法
      this.getListById();
    });
  },
  methods: {
    //获取详情
    async getListById() {
      await alarmManagement.getById({ id: this.info.id }).then((res) => {
        if (res.code == 0) {
          this.tableForm = res.data;
          const {
            warningrule,
            warningrulename,
            warningcontent,
            warningtime,
            usersName,
            sendcode,
            status,
            statusname,
            sendcount,
          } = res.data;
          this.tableForm = {
            warningrule,
            warningrulename,
            warningcontent,
            warningtime,
            usersName,
            sendcode,
            status,
            statusname,
            sendcount,
          };
          this.tableForm.sendcode = res.data.sendcode;
          this.tableForm.id = res.data.id;
          if (res.data.sendcount && res.data.sendcount > 0) {
            let tempArr = [];
            res.data.users.map((i) => {
              tempArr.push(i.name);
            });
            this.tableForm.usersName = tempArr.join(",");
          }
          this.tableForm.warningrulename = res.data.warningrulename = this.warningRuleOptions.find(
            (k) => res.data.warningrule == k.value
          ).label;
          this.tableForm.statusname = res.data.statusname = this.messageStateOptions.find(
            (k) => res.data.status == k.value
          ).label;
        }
      });
    },
    // 显示接收人
    personSet() {
      this.personShow = true;
    },
    // 关闭接收人
    personClose() {
      this.personShow = false;
    },
    // 接收人返回数据
    setPersonIds(checkArr) {
      let arr = [];
      let str = [];
      checkArr.forEach((item) => {
        arr.push(item.id);
        str.push(item.name);
      });
      this.tableForm.sendcode = arr.join(",");
      this.tableForm.sendcount = arr.length;
      this.tableForm.usersName = str.join(",");
    },
    // 重置
    resetForm() {
      this.$refs.baseForm.resetFields();
    },
    // 验证表单
    saveData() {
      this.$refs.tableForm.validate((valid) => {
        if (valid) {
          this.saveForm();
        } else {
          this.$message.error("请填写必填内容！");
          return false;
        }
      });
    },
    async saveForm() {
      await alarmManagement.forward(this.tableForm).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "转发成功",
            type: "success",
          });
          this.dialogClose();
          this.$emit("getList");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 关闭
    dialogClose() {
      this.$emit("close");
    },
  },
};
</script>

<style lang="scss" scoped>
.list-dialog {
  ::v-deep .el-dialog__header {
    background: #1492ff;
    padding: 15px 20px;
    .el-dialog__title {
      color: #fff;
    }
    .el-dialog__headerbtn {
      top: 5px;
      right: 5px;
      padding-top: 10px;
      .el-icon-close {
        color: #fff;
        height: 30px;
        width: 35px;
      }
    }
  }
  ::v-deep .el-dialog__body {
    padding: 15px 20px;
    .el-input__inner {
      background-color: #fff;
      border: 0.0625rem solid #dcdfe6;
      color: #606266;
    }
  }
  ::v-deep .el-dialog__footer {
    border-top: 2px solid #dcdfe6;
    padding: 15px;
  }
  .el-select {
    width: 100%;
  }
  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 100%;
  }
}
</style>
