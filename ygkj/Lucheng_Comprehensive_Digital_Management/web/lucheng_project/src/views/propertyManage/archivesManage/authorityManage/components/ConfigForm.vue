<!--
 * @Author: zhongg
 * @LastEditTime: 2021-10-25 10:43:51
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: /lucheng_project/src/views/propertyManage/archivesManage/authorityManage/components/ConfigForm.vue
-->
<template>
  <div class="config_wrap">
    <div class="left">
      <ul>
        <li
          v-for="(item, index) in typeList"
          :key="index"
          :class="typeIndex == index ? 'active' : ''"
          @click="typeClick(index)"
        >
          <i></i>{{ item.title }}
        </li>
      </ul>
    </div>
    <div class="right">
      <div class="warnConfig_box">
        <div class="common_select warn_common">
          <p>
            人员选择<i>已添加{{ checkUsersIds.length }}人</i>
          </p>
          <ul>
            <li
              v-for="(people, index) in userList"
              @click="peopleChanged(index, people, people.id)"
              :key="people.id"
              :class="checkUsersIds.includes(people.id) ? 'active' : ''"
            >
              <i></i>{{ people.username }}
            </li>
          </ul>
        </div>

        <div class="common_select warn_common">
          <p>
            部门选择<i>已添加{{ checkDepartIds.length }}个</i>
          </p>
          <ul>
            <li
              v-for="(depart, index) in departList"
              @click="departChanged(index, depart, depart.id)"
              :key="depart.id"
              :class="checkDepartIds.includes(depart.id) ? 'active' : ''"
            >
              <i></i>{{ depart.name }}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 按钮 -->
    <div class="el-dialog__footer">
      <el-button class="form-btn cancel-btn" size="mini" @click="resetForm"
        >关闭</el-button
      >
      <el-button class="form-btn save-btn" size="mini" @click="onSubmit"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import { getDirectoryListApi, addJurisdictionApi } from "@/api/propertyManage";
export default {
  name: "ConfigForm",
  props: {},
  watch: {},
  components: {},
  data() {
    return {
      achievementId: "",
      typeIndex: 0,
      type: "look",
      typeList: [
        { title: "查看权限", value: "look" },
        { title: "上传权限", value: "upload" },
        { title: "删除权限", value: "delete" },
        { title: "下载权限", value: "download" },
      ],
      userList: [],
      departList: [],
      checkUsersIds: [],
      checkDepartIds: [],
      lookUserIds: [],
      lookDepartIds: [],
      uploadUserIds: [],
      uploadDepartIds: [],
      deleteUserIds: [],
      deleteDepartIds: [],
      downloadUserIds: [],
      downloadDepartIds: [],
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
    async onSubmit() {
      let opt = [
        {
          achievementId: this.achievementId,
          deportIds: this.lookDepartIds.join(","),
          userIds: this.lookUserIds.join(","),
          type: 1,
        },
        {
          achievementId: this.achievementId,
          deportIds: this.uploadDepartIds.join(","),
          userIds: this.uploadUserIds.join(","),
          type: 2,
        },
        {
          achievementId: this.achievementId,
          deportIds: this.deleteDepartIds.join(","),
          userIds: this.deleteUserIds.join(","),
          type: 3,
        },
        {
          achievementId: this.achievementId,
          deportIds: this.downloadDepartIds.join(","),
          userIds: this.downloadUserIds.join(","),
          type: 4,
        },
      ];
      //console.log(JSON.stringify(opt));
      await addJurisdictionApi({ data: JSON.stringify(opt) }).then((res) => {
        if (res.code == 0) {
          this.$message({
            message: "保存成功",
            type: "success",
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    resetForm() {
      this.$emit("cancelDialog");
    },

    typeClick(index) {
      if (this.type == "look") {
        this.lookUserIds = this.checkUsersIds;
        this.lookDepartIds = this.checkDepartIds;
      } else if (this.type == "upload") {
        this.uploadUserIds = this.checkUsersIds;
        this.uploadDepartIds = this.checkDepartIds;
      } else if (this.type == "delete") {
        this.deleteUserIds = this.checkUsersIds;
        this.deleteDepartIds = this.checkDepartIds;
      } else if (this.type == "download") {
        this.downloadUserIds = this.checkUsersIds;
        this.downloadDepartIds = this.checkDepartIds;
      }
      this.typeIndex = index;
      this.type = this.typeList[index].value;
      this.$nextTick().then(() => {
        if (this.type == "look") {
          this.checkUsersIds = this.lookUserIds;
          this.checkDepartIds = this.lookDepartIds;
        } else if (this.type == "upload") {
          this.checkUsersIds = this.uploadUserIds;
          this.checkDepartIds = this.uploadDepartIds;
        } else if (this.type == "delete") {
          this.checkUsersIds = this.deleteUserIds;
          this.checkDepartIds = this.deleteDepartIds;
        } else if (this.type == "download") {
          this.checkUsersIds = this.downloadUserIds;
          this.checkDepartIds = this.downloadDepartIds;
        }
      });
    },
    peopleChanged(index, user, id) {
      if (this.checkUsersIds.includes(id)) {
        for (let i = 0; i < this.checkUsersIds.length; i++) {
          if (this.checkUsersIds[i] == id) {
            this.checkUsersIds.splice(i, 1);
          }
        }
      } else {
        this.checkUsersIds.push(id);
      }
    },

    departChanged(index, depart, id) {
      if (this.checkDepartIds.includes(id)) {
        for (let i = 0; i < this.checkDepartIds.length; i++) {
          if (this.checkDepartIds[i] == id) {
            this.checkDepartIds.splice(i, 1);
          }
        }
      } else {
        this.checkDepartIds.push(id);
      }
    },

    async getFormData(id) {
      this.achievementId = id;
      await getDirectoryListApi({ id }).then((res) => {
        if (res.code == 0 && res.data.list && res.data.list.length > 0) {
          if (
            res.data.list[0].userList &&
            res.data.list[0].userList.length > 0
          ) {
            this.userList = res.data.list[0].userList;
          }
          if (
            res.data.list[0].deptList &&
            res.data.list[0].deptList.length > 0
          ) {
            this.departList = res.data.list[0].deptList;
          }
          if (
            res.data.list[0].jurisdictionListction &&
            res.data.list[0].jurisdictionListction.length > 0
          ) {
            for (
              var i = 0;
              i < res.data.list[0].jurisdictionListction.length;
              i++
            ) {
              if (res.data.list[0].jurisdictionListction[i].type == "1") {
                //查看权限
                this.lookUserIds =
                  res.data.list[0].jurisdictionListction[i].userIds.split(",");
                this.lookDepartIds =
                  res.data.list[0].jurisdictionListction[i].deportIds.split(
                    ","
                  );
              } else if (
                res.data.list[0].jurisdictionListction[i].type == "2"
              ) {
                //上传权限
                this.uploadUserIds =
                  res.data.list[0].jurisdictionListction[i].userIds.split(",");
                this.uploadDepartIds =
                  res.data.list[0].jurisdictionListction[i].deportIds.split(
                    ","
                  );
              } else if (
                res.data.list[0].jurisdictionListction[i].type == "3"
              ) {
                //删除权限
                this.deleteUserIds =
                  res.data.list[0].jurisdictionListction[i].userIds.split(",");
                this.deleteDepartIds =
                  res.data.list[0].jurisdictionListction[i].deportIds.split(
                    ","
                  );
              } else if (
                res.data.list[0].jurisdictionListction[i].type == "4"
              ) {
                //下载权限
                this.downloadUserIds =
                  res.data.list[0].jurisdictionListction[i].userIds.split(",");
                this.downloadDepartIds =
                  res.data.list[0].jurisdictionListction[i].deportIds.split(
                    ","
                  );
              }
            }
            this.checkUsersIds = this.lookUserIds;
            this.checkDepartIds = this.lookDepartIds;
          }
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.el-dialog__footer {
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}
.config_wrap {
  width: 100%;
  .left {
    width: 160px;
    height: 400px;
    border: 1px solid #e5e5e5;
    padding: 25px;
    background: #fff;
    float: left;
    ul {
      li {
        line-height: 40px;
        cursor: pointer;
        i {
          display: inline-block;
          width: 16px;
          height: 16px;
          vertical-align: middle;
          margin-top: -2px;
          margin-right: 5px;
          background: url("../../../../../assets/images/userSelect_icon1.png")
            no-repeat center;
          background-size: 100% 100%;
        }
      }
      li.active {
        color: #09a8a0;
        i {
          background: url("../../../../../assets/images/userSelect_icon2.png")
            no-repeat center;
        }
      }
    }
  }
  .right {
    width: calc(100% - 160px);
    .warnConfig_box {
      width: 100%;
      .warn_common {
        padding: 16px;
        p {
          font-size: 18px;
          font-weight: bold;
          color: #000;
          margin-bottom: 8px;
        }
      }
      .common_select {
        width: 100%;
        height: 200px;
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
          width: 100%;
          height: calc(100% - 30px);
          overflow-y: auto;
          li {
            padding: 8px 10px 8px 10px;
            margin-right: 10px;
            margin-top: 10px;
            border: 1px solid #dcdfe6;
            float: left;
            cursor: pointer;
            i {
              display: inline-block;
              width: 16px;
              height: 16px;
              vertical-align: middle;
              margin-top: -2px;
              margin-right: 5px;
              background: url("../../../../../assets/images/userSelect_icon1.png")
                no-repeat center;
              background-size: 100% 100%;
            }
          }
          li.active {
            color: #09a8a0;
            border: 1px solid #09a8a0;
            i {
              background: url("../../../../../assets/images/userSelect_icon2.png")
                no-repeat center;
            }
          }
        }
      }
    }
  }
}

.el-form-item {
  margin-bottom: 0;
}
.notify_staff:after,
.depart_select:after,
.config_wrap:after {
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

