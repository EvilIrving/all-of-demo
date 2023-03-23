<!--
 * @Author: ly
 * @Date: 2021-08-11 10:18:02
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2021-08-15 15:18:20
 * @Descripttion: 
 * @FilePath: \hydrology\src\views\daily_business\project_supervision\dialog\newContentDialog.vue
-->
<template>
  <el-dialog top="10vh" :visible.sync="visible" width="400px" center :before-close="close"
    :title="newDialogData + '配置信息'" class="list-dialog scroll-dialog" append-to-body>
    <div>
      <p class="item-label">条目信息</p>
      <el-input v-model="entryName" placeholder="请输入" type="textarea" :rows="3" :disabled="newDialogData == '查看'" />
    </div>
    <!-- 保存按钮 -->
    <div slot="footer" v-if="newDialogData != '查看'">
      <div class="form-footer">
        <el-button class="form-btn btn-cancel" @click="close">取消</el-button>
        <el-button class="form-btn btn-save" @click="saveForm">保存</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { projectSupervisionApi } from "@/api/projectSupervision";
export default {
  props: {
    configurationObj: Object,
    newDialogData: String,
    visible: Boolean,
    stageData: String
  },
  data() {
    return {
      entryName: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      if (this.configurationObj) {
        this.entryName = this.configurationObj.entryName;
      }
    })
  },
  methods: {
    // 更新异常信息配置条目
    async updateExInfo() {
      let opt = {
        projectStage: this.configurationObj.projectStage,
        id: this.configurationObj.id,
        entryName: this.entryName
      }
      let res = await projectSupervisionApi.updateExInfo(opt);
      if (res.code == 0) {
        this.$message({ message: "修改成功!", type: "success" });
        this.$emit("close");
        this.$emit("exInfoList");
      } else {
        this.$message({ message: "修改异常!", type: "error" });
      }
    },
    // 新增异常信息配置条目
    async addExInfo() {
      let str = this.stageData == '开工阶段' ? '1' : this.stageData == '施工阶段' ? '2' : '3';
      let opt = {
        projectStage: str,
        entryName: this.entryName
      }
      let res = await projectSupervisionApi.addExInfo(opt);
      if (res.code == 0) {
        this.$message({ message: "新增成功!", type: "success" });
        this.$emit("close");
        this.$emit("exInfoList");
      } else {
        this.$message({ message: "新增异常!", type: "error" });
      }
    },
    // 保存
    saveForm() {
      if (this.newDialogData == '新增') {
        this.addExInfo();
      } else {
        this.updateExInfo();
      }
    },
    // 关闭
    close() {
      this.$emit("close");
    }
  }
}
</script>

<style lang="scss">

</style>