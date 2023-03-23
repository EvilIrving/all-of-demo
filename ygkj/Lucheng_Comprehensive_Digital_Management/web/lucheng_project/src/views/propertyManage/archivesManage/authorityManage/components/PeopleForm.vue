<!--
 * @Author: zhongg
 * @LastEditTime: 2021-10-25 10:43:59
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: /lucheng_project/src/views/propertyManage/archivesManage/authorityManage/components/PeopleForm.vue
-->
<template>
  <div>
    <el-form ref="peopleForm" :model="peopleForm">
      <!-- id -->
      <el-form-item label="id" prop="id" v-show="false"></el-form-item>

      <div class="warnConfig_box">
        <div class="notify_staff warn_common">
          <p>
            人员选择<i>已添加{{ chooseUser.length }}人</i>
          </p>
          <ul v-for="item in chooseUser" :key="item.id">
            <li>{{ item.username.smallName() }}</li>
          </ul>
          <span @click="selectPeople">+</span>
        </div>

        <div class="depart_select warn_common">
          <p>
            部门选择<i>已添加{{ chooseDepart.length }}个</i>
          </p>
          <ul v-for="item in chooseDepart" :key="item.id">
            <li>{{ item.title }}</li>
          </ul>
          <span @click="selectDepart">+</span>
        </div>
      </div>

      <!-- 按钮 -->
      <div class="el-dialog__footer">
        <el-row :gutter="20">
          <el-form-item>
            <div class="form-footer">
              <el-button
                class="form-btn cancel-btn"
                size="mini"
                @click="resetForm"
                >关闭</el-button
              >
              <el-button class="form-btn save-btn" size="mini" @click="onSubmit"
                >保存</el-button
              >
            </div>
          </el-form-item>
        </el-row>
      </div>
    </el-form>

    <el-dialog
      class="list-dialog"
      :visible.sync="peopleShow"
      :before-close="close"
      :close-on-click-modal="false"
      append-to-body
      width="50%"
    >
      <div slot="title" class="el-dialog__title">
        <strong>人员选择</strong>
      </div>
      <select-people
        ref="selectPeople"
        :visible="peopleShow"
        @close="close"
        @getPeoples="getPeoples"
      ></select-people>
    </el-dialog>

    <el-dialog
      class="list-dialog"
      :visible.sync="departShow"
      :before-close="departClose"
      :close-on-click-modal="false"
      append-to-body
      width="400px"
    >
      <div slot="title" class="el-dialog__title">
        <strong>部门选择</strong>
      </div>
      <select-depart
        ref="selectDepart"
        :visible="departShow"
        @close="departClose"
        @getDeparts="getDeparts"
      ></select-depart>
    </el-dialog>
  </div>
</template>

<script>
import SelectPeople from "./SelectPeople";
import SelectDepart from "./SelectDepart";
import { getDirectoryListApi, updateDirectoryApi } from "@/api/propertyManage";
export default {
  name: "PeopleForm",
  props: {},
  watch: {},
  components: {
    SelectPeople,
    SelectDepart,
  },
  data() {
    return {
      peopleShow: false,
      departShow: false,
      chooseUser: [],
      chooseUserIds: [],
      chooseDepart: [],
      chooseDepartIds: [],
      peopleForm: {
        id: "",
        userIds: "",
        deportIds: "",
      },
    };
  },
  mounted() {
    //this.getListData();
  },
  computed: {
    headers: function () {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
  },
  methods: {
    // 人员选择
    selectPeople() {
      this.peopleShow = true;
      this.$nextTick().then(() => {
        this.$refs.selectPeople.getDepartList(
          this.chooseDepartIds,
          this.chooseUserIds,
          this.chooseUser
        );
      });
    },
    // 部门选择
    selectDepart() {
      this.departShow = true;
      this.$nextTick().then(() => {
        this.$refs.selectDepart._getFileList(this.chooseDepartIds);
      });
    },
    close() {
      this.peopleShow = false;
    },
    departClose() {
      this.departShow = false;
    },
    async getPeoples(selectPeople, checkUsersIds) {
      this.chooseUser = selectPeople;
      this.chooseUserIds = checkUsersIds;
      this.$refs.selectPeople.clearPeople();
      this.close();
    },
    async getDeparts(checkDepart, checkDepartIds) {
      this.chooseDepart = checkDepart;
      this.chooseDepartIds = checkDepartIds;
      this.$refs.selectDepart.clearDepart();
      this.departClose();
    },
    async onSubmit() {
      console.log(11);
      this.peopleForm.userIds = this.chooseUserIds.join(",");
      this.peopleForm.deportIds = this.chooseDepartIds.join(",");
      await updateDirectoryApi(this.peopleForm).then((res) => {
        console.log(res);
        // if (res.code == 0) {
        //   this.$message({
        //     message: "保存成功",
        //     type: "success",
        //   });
        // } else {
        //   this.$message.error(res.msg);
        // }
      });
    },
    resetForm() {
      this.chooseUser = [];
      this.chooseUserIds = [];
      this.chooseDepart = [];
      this.chooseDepartIds = [];
      this.$emit("cancelDialog");
    },
    async getFormData(id) {
      await getDirectoryListApi({ id }).then((res) => {
        if (res.code == 0 && res.data.list && res.data.list.length > 0) {
          const { id, userIds, deportIds } = res.data.list[0];
          this.peopleForm = {
            id,
            userIds,
            deportIds,
          };
          if (
            res.data.list[0].userList &&
            res.data.list[0].userList.length > 0
          ) {
            this.chooseUser = res.data.list[0].userList;
          }
          if (res.data.list[0].userIds && res.data.list[0].userIds != "") {
            this.chooseUserIds = res.data.list[0].userIds.split(",");
          }
          if (
            res.data.list[0].deptList &&
            res.data.list[0].deptList.length > 0
          ) {
            this.chooseDepart = res.data.list[0].deptList;
          }
          if (res.data.list[0].deportIds && res.data.list[0].deportIds != "") {
            this.chooseDepartIds = res.data.list[0].deportIds.split(",");
          }
        }
      });
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
    padding: 30px 20px 15px 20px;
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

.el-dialog__footer {
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}
.warnConfig_box {
  width: 100%;
  max-height: 500px;
  overflow-y: auto;

  .warn_common {
    padding: 16px;
    p {
      font-size: 18px;
      font-weight: bold;
      color: #000;
      margin-bottom: 8px;
    }
  }
  .notify_staff {
    border-bottom: 1px solid #d8d8d8;
    p {
      i {
        padding: 8px 10px;
        font-size: 14px;
        font-style: normal;
        font-weight: normal;
        color: #868686;
        background: #f9f9f9;
        margin-left: 15px;
      }
    }
    ul {
      li {
        width: 40px;
        height: 40px;
        line-height: 40px;
        border-radius: 50%;
        color: #fff;
        text-align: center;
        float: left;
        margin-right: 15px;
        margin-top: 10px;
        background: #32c5ff;
      }
    }
    span {
      width: 40px;
      height: 40px;
      line-height: 40px;
      border-radius: 50%;
      color: #999;
      text-align: center;
      border: 1px solid #e8e8e8;
      background: #f8f8f8;
      float: left;
      margin-top: 10px;
      cursor: pointer;
    }
  }
  .depart_select {
    border-bottom: 1px solid #d8d8d8;
    p {
      i {
        padding: 8px 10px;
        font-size: 14px;
        font-style: normal;
        font-weight: normal;
        color: #868686;
        background: #f9f9f9;
        margin-left: 15px;
      }
    }
    ul {
      li {
        height: 32px;
        line-height: 32px;
        color: #fff;
        text-align: center;
        padding: 0 12px;
        float: left;
        margin-right: 15px;
        margin-top: 10px;
        background: #01a59c;
      }
    }
    span {
      width: 40px;
      height: 40px;
      line-height: 40px;
      border-radius: 50%;
      color: #999;
      text-align: center;
      border: 1px solid #e8e8e8;
      background: #f8f8f8;
      float: left;
      margin-top: 6px;
      cursor: pointer;
    }
  }
}
.el-form-item {
  margin-bottom: 0;
}
.notify_staff:after,
.depart_select:after {
  content: "";
  display: block;
  height: 0;
  clear: both;
  visibility: hidden;
}
.form-footer {
  width: 100%;
  padding: 12px 26px 12px 0;
}
</style>

