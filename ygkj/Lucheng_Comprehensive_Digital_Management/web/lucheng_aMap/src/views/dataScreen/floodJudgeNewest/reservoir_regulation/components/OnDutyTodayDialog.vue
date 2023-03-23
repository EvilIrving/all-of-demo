<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    top="25vh"
    title="今日值班"
    class="dispatch-dialog"
    :close-on-click-modal="false"
  >
    <div class="dialog-content" style="height: 310px">
      <div style="padding: 10px 40px">
        <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="formLabelAlign"
        >
          <el-form-item label="排版时间">
            <el-input v-model="formLabelAlign.date" disabled></el-input>
          </el-form-item>
          <el-form-item label="带班领导">
            <el-input v-model="formLabelAlign.dald" disabled></el-input>
          </el-form-item>
          <el-form-item label="值班人员">
            <el-input v-model="formLabelAlign.zbry" disabled></el-input>
          </el-form-item>
          <el-form-item label="副班人员">
            <el-input v-model="formLabelAlign.fbry" disabled></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- 尾部取消 -->
    <div slot="footer">
      <div class="form-footer">
        <el-button class="form-btn btn-save" @click="handleClose"
          >取消</el-button
        >
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
export default {
  name: "JudgedDialog",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      formLabelAlign: {
        date: "",
        dald: "",
        zbry: "",
        fbry: "",
      },
    };
  },
  mounted() {
    this.waterRainSimpleReport(this.getDates());
  },
  methods: {
    // 获取当前时间，并处理格式为“yyyy-MM-dd hh:mm:ss”
    getDates() {
      //获取当前时间
      var date = new Date();
      var year = date.getFullYear(); //月份从0~11，所以加一
      var dateArr = [date.getMonth() + 1, date.getDate()];
      for (var i = 0; i < dateArr.length; i++) {
        if (dateArr[i] >= 1 && dateArr[i] <= 9) {
          dateArr[i] = "0" + dateArr[i];
        }
      }
      var strDate = year + "-" + dateArr[0] + "-" + dateArr[1];
      return strDate;
    },
    //取消按钮
    handleClose() {
      this.$emit("handleClose");
    },
    //标题事件
    async waterRainSimpleReport(val) {
      let obj = {
        startDate: val,
        endDate: val,
      };
      await dispatchApi.getMonitorInfoByDate(obj).then((res) => {
        if (res.code == 0) {
          this.formLabelAlign.date = this.getDates();
          this.formLabelAlign.dald = res.data[0].dbldList[0].username;
          this.formLabelAlign.zbry = res.data[0].fbryList[0].username;
          this.formLabelAlign.fbry = res.data[0].fbryList[1].username;
        }
      });
    },
  },
};
</script>

<style lang="scss">
.dispatch-dialog {
  user-select: none;
  .el-dialog {
    .dialog-content {
      overflow: auto;
    }
  }
}
.form-btn {
  background: rgba(0, 0, 0, 0);
  color: #2c72be;
  border: none;
  border-radius: 0;
  &.btn-save {
    color: #fff;
    background: #239def;
  }
}
.el-form-item__label {
  color: #fff;
}
</style>