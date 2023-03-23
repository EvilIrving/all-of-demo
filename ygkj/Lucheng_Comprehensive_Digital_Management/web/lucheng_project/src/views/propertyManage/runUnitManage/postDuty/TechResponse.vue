<!--
 * @Date: 2022-04-13 19:16:21
 * @Author: 
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:36:01
 * @FilePath: \lucheng_project\src\views\propertyManage\runUnitManage\postDuty\TechResponse.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="item_box_title">
      <p>单位职责</p>
    </div> -->
    <!-- 编辑状态下，编辑完成后点击确认后再保存 -->
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <!-- <h3>基础信息</h3> -->
      <el-button
        type="primary"
        size="mini"
        style="margin: 10px"
        class="right"
        @click="editTable"
        v-permission="'runTechResponse:edit'"
        >{{ handleName }}</el-button
      >
      <!-- <table class="t_content">
        <tbody>
          <tr>
            <td>机构全称</td>
            <td class="edit">{{this.detail.unitName}}</td>
            <td>单位地址</td>
            <td class="edit">{{this.detail.unitAddress}}</td>
          </tr>
          <tr>
            <td>单位性质</td>
            <td class="edit">{{this.detail.unitNature}}</td>
            <td>主管部门</td>
            <td class="edit">{{this.detail.mainDepartment}}</td>
          </tr>
          <tr>
            <td>单位联系电话</td>
            <td class="edit">{{this.detail.unitPhone}}</td>
            <td>单位汛期值班电话</td>
            <td class="edit">{{this.detail.unitPhoneInFloodReason}}</td>
          </tr>
          <tr>
            <td>单位传真</td>
            <td class="edit">{{this.detail.unitFax}}</td>
            <td>单位批文</td>
            <td class="edit"></td>
          </tr>
        </tbody>
      </table> -->
      <h3>技术负责人</h3>
      <!-- <div class="text_border">{{ this.detail.jobResponsibilities }}</div> -->
      <textarea
        disabled
        class="text_border"
        style="width: 100%"
        name=""
        id=""
        cols="30"
        rows="20"
        v-html="detail.jobResponsibilities"
      ></textarea>
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
      <tech-response-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></tech-response-form>
    </el-dialog>
  </div>
</template>

<script>
import { operationUnit } from "@/api/propertyManage";
import TechResponseForm from "./TechResponseForm.vue";
export default {
  name: "TechResponse",
  props: {},
  components: {
    TechResponseForm,
  },
  data() {
    return {
      handleName: "编辑",
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      detail: {},
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await operationUnit.queryJob({
        unitType: 1,
        jobType: 2,
      });
      if (res.code == 0) {
        this.detail = res.data || {};
        this.id = res.data.id || "";
      }
    },
    editTable() {
      this.title = "编辑";
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
    // edit() {
    //   if (this.handleName === '编辑') {
    //     this.disableInfo = true
    //     this.$nextTick(() => {
    //       this.disableInfo = false
    //       this.handleName = '保存'
    //     })
    //   } else {
    //     this.$confirm('确认保存?', '提示', {
    //       confirmButtonText: '确定',
    //       cancelButtonText: '取消',
    //       type: 'warning',
    //     }).then(() => {
    //       rescueWarehouseApi.del({
    //         ids: this.textInfo
    //       }).then((res) => {
    //         if (res.code == 0) {
    //           this.$message({
    //             type: 'success',
    //             message: '保存成功!',
    //           })
    //           this.disableInfo = false
    //           this.$nextTick(() => {
    //             this.disableInfo = true
    //             this.handleName = '编辑'
    //           })
    //           this.getList()
    //         } else {
    //           this.$message({
    //             type: 'error',
    //             message: '保存失败！',
    //           })
    //         }
    //       })
    //     })
    //   }
    // },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
h3 {
  display: inline-block;
  font-size: 18px;
  font-weight: 600;
  color: #333333;
  padding: 15px 0;
  width: 50%;
}
.t_content {
  width: 100%;
  tr {
    width: 100%;
    td {
      border: 1px solid #e9e9e9;
      width: 15%;
      text-align: center;
      padding: 5px 0;
      font-size: 14px;
      padding-right: 10px;
      position: relative;
      vertical-align: middle;
      word-break: break-all;
      background-clip: padding-box;
      background: #f9fafc;
      &.edit {
        background-color: #fff;
        width: 30%;
        text-align: left;
        padding: 0;
        text-indent: 10px;
        font-weight: normal;
      }
    }
  }
}
// textarea:disabled {
//   background-color: #fbfbfb;
// }
.text_border {
  line-height: 22px;
}
</style>