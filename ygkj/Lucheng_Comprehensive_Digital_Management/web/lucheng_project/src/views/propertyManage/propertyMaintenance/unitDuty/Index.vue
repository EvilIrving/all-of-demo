<!--
 * @Date: 2022-04-13 10:55:40
 * @Author: 
 * @Description: 单位职责
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-16 15:10:52
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyMaintenance\unitDuty\Index.vue
-->

<template>
  <div class="warp-page">
    <div class="warp-unit-list" v-if="userInfo.userType != 3">
      <unit-list @checkItem="checkItem" />
    </div>
    <div class="wrap" :class="{'wrap-type': userInfo.userType == 3}">
      <div class="item_box_title">
        <p>单位职责</p>
      </div>
      <!-- 编辑状态下，编辑完成后点击确认后再保存 -->
      <div
        class="info_option"
        style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
        "
      >
        <h3>基础信息1</h3>
        <el-button
          type="primary"
          size="mini"
          v-permission="'proUnitDuty:edit'"
          style="margin: 10px"
          class="right"
          @click="editTable"
          >{{ handleName }}</el-button
        >
        <table class="t_content">
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
        <textarea
          disabled
          style="width: 100%"
          name=""
          id=""
          cols="30"
          rows="20"
          v-html="this.detail.unitResponsibilities"
        ></textarea>
        <!-- <textarea :disabled='disableInfo' style="width:100%;" name="" id="" cols="30" rows="20"></textarea> -->
      </div>
      <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
        <unit-duty-form
          v-if="editDialog"
          :id="id"
          :deptId="deptId"
          @closeEditDialog="closeEditDialog"
        ></unit-duty-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { operationUnit } from "@/api/propertyManage";
import UnitDutyForm from "./UnitDutyForm.vue";
import UnitList from "@/components/UnitList.vue";
export default {
  name: "threeChargePeopleListPro",
  props: {},
  components: {
    UnitDutyForm,
    UnitList,
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
      deptId: "",
      userInfo: {}
    };
  },
  mounted() {
    this.userInfo = this.$sessionData("get", "userInfo");
    if(this.userInfo.userType == 3){
      this.deptId = this.userInfo.deptId
      this.getList()
    }
  },
  methods: {
    checkItem(item) {
      this.deptId = item.id;
      this.getList();
    },
    async getList() {
      let res = await operationUnit.queryUnit({
        unitType: 2, // 单位类型 1-运行单位 2-物业单位
        deptId: this.deptId,
      });
      if (res.code == 0) {
        this.detail = res.data || {};
        this.id = res.data ? res.data.id : "";
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
.warp-page {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}
.warp-unit-list {
  @include contentBox();
  height: 100%;
  width: 255px;
}
.wrap {
  @include contentBox();
  height: 100%;
  width: calc(100% - 275px);
  .look {
    cursor: pointer;
  }
}
.wrap-type{
  width: 100%;
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
</style>