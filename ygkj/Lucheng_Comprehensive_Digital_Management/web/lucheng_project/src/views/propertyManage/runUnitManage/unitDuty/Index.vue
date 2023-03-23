<!--
 * @Date: 2022-04-13 10:55:40
 * @Author: 
 * @Description: 单位职责
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:34:34
 * @FilePath: \lucheng_project\src\views\propertyManage\runUnitManage\unitDuty\Index.vue
-->

<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>单位职责</p>
    </div>
    <!-- 编辑状态下，编辑完成后点击确认后再保存 -->

    <div
      class="info_option"
      style="width: 100%; padding: 5px 16px 16px 16px; box-sizing: border-box"
    >
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane
          v-for="(item, index) in tableData"
          :key="item.id"
          :label="item.unitName"
          :name="`${index}`"
        ></el-tab-pane>
      </el-tabs>
      <h3>基础信息</h3>
      <el-button
        type="primary"
        size="mini"
        style="margin: 10px"
        class="right"
        @click="editTable"
        v-permission="'runUnitDuty:edit'"
        >{{ handleName }}</el-button
      >
      <table class="t_content">
        <!-- "id": "238381960304570368",
    "unitName": "1",
    "unitAddress": "1",
    "unitNature": "1",
    "mainDepartment": "1",
    "unitPhone": "1",
    "unitPhoneInFloodReason": "1",
    "unitFax": "1",
    "unitResponsibilities": "1",
    "unitType": "2",
    "creatorId": "c7ef46fab91149e9a3ff2aadadd8a685",
    "createTime": "2022-04-12 09:45:01",
    "updaterId": "c7ef46fab91149e9a3ff2aadadd8a685",
    "updateTime": "2022-04-12 09:45:23",
    "delFlag": "0" -->
        <tbody>
          <tr>
            <td>机构全称</td>
            <td class="edit">{{ this.detail.unitName }}</td>
            <td>单位地址</td>
            <td class="edit">{{ this.detail.unitAddress }}</td>
          </tr>
          <tr>
            <td>单位性质</td>
            <td class="edit">{{ this.detail.unitNature }}</td>
            <td>主管部门</td>
            <td class="edit">{{ this.detail.mainDepartment }}</td>
          </tr>
          <tr>
            <td>单位联系电话</td>
            <td class="edit">{{ this.detail.unitPhone }}</td>
            <td>单位汛期值班电话</td>
            <td class="edit">{{ this.detail.unitPhoneInFloodReason }}</td>
          </tr>
          <tr>
            <td>单位传真</td>
            <td class="edit">{{ this.detail.unitFax }}</td>
            <td>单位批文</td>
            <td class="edit"></td>
          </tr>
        </tbody>
      </table>
      <h3>单位职责</h3>
      <!-- <div class="text_border">{{this.detail.unitResponsibilities}}</div> -->
      <!-- <el-input type="textarea" class="textarea-box" readonly resize="none" v-model="detail.unitResponsibilities" :autosize="{ minRows: 2}"></el-input> -->
      <textarea
        disabled
        class="textarea-box"
        style="width: 100%"
        name=""
        id=""
        cols="30"
        rows="18"
        v-html="detail.unitResponsibilities"
      ></textarea>
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
      <unit-duty-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></unit-duty-form>
    </el-dialog>
  </div>
</template>

<script>
import { operationUnit } from "@/api/propertyManage";
import UnitDutyForm from "./UnitDutyForm.vue";
export default {
  name: "threeChargePeopleList",
  props: {},
  components: {
    UnitDutyForm,
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
      activeName: "0",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await operationUnit.queryUnit({
        unitType: 1,
      });
      if (res.code == 0) {
        if (res.data.length > 0) {
          this.tableData = res.data;
          if (Number(this.activeName) !== 0) {
            this.detail = this.tableData[Number(this.activeName)];
          } else {
            this.detail = this.tableData[0];
          }

          this.id = this.detail.id;
        }
      }
    },
    handleClick() {
      this.detail = this.tableData[Number(this.activeName)];
      this.id = this.detail.id;
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
  width: calc(100% - 20px);
  min-height: 160px;
  padding: 10px;
  border: 1px solid #e6e6e6;
}
.textarea-box {
  line-height: 22px;
}
</style>