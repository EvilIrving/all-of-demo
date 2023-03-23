<!--
 * @Author: zhongg
 * @LastEditTime: 2021-10-25 10:44:04
 * @Descripttion: 
 * @FilePath: /lucheng_project/src/views/propertyManage/archivesManage/authorityManage/components/SelectPeople.vue
-->
<template>
  <div class="depart_warp">
    <div class="depart-wrap">
      <div class="user_tree" v-loading="loading">
        <!-- 部门树 -->
        <el-tree
          :data="fileList"
          node-key="id"
          ref="fileTree"
          :props="defaultProps"
          :highlight-current="true"
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick"
        ></el-tree>
      </div>

      <div class="right">
        <ul>
          <li
            v-for="(people, index) in userList"
            @click="stateChanged(index, people, people.id)"
            :key="people.id"
            :class="checkUsersIds.includes(people.id) ? 'active' : ''"
          >
            <i></i>{{ people.username }}
          </li>
        </ul>
      </div>
    </div>
    <div class="form-footer">
      <el-button class="form-btn save-btn" size="mini" @click="sure"
        >确定</el-button
      >
      <!-- <el-button class="form-btn cancel-btn" @click="close">取消</el-button> -->
    </div>
  </div>
</template>

<script>
import { getDepartTreeApi, getUserList } from "@/api/propertyManage";
export default {
  name: "FileTree",
  props: {},
  data() {
    return {
      fileList: [],
      defaultProps: {
        children: "children",
        label: "title",
      },
      selectFileId: "",
      selectFileName: "",
      expandedKeys: [],
      loading: true,
      userList: [],
      checkUsersIds: [],
      selectPeople: [], //存储被选择的人员
    };
  },
  components: {},
  mounted() {
    //this._getUserList();
  },
  methods: {
    async getDepartList(departIds, userIds, user) {
      this.fileList = [];
      this.checkUsersIds = userIds;
      this.selectPeople = user;
      await getDepartTreeApi({ deptIds: departIds.join(",") }).then((res) => {
        this.loading = false;
        if (res.data) {
          if (this.selectFileId == "") {
            this.selectFileId = res.data[0].id;
            this.selectFileName = res.data[0].name;
          }
          this.fileList = res.data;
          //选中默认节点
          this.$nextTick().then(() => {
            this.expandedKeys.push(res.data[0].id);
            this.$refs.fileTree.setCurrentKey(this.selectFileId);
          });
          //请求人员数据
          this._getUserList();
        }
      });
    },
    async _getUserList() {
      this.userList = [];
      await getUserList({ deptId: this.selectFileId, fetchAll: true }).then(
        (res) => {
          this.userList = res.list;
        }
      );
    },
    handleNodeClick(data) {
      this.selectFileId = data.id;
      this._getUserList();
    },
    stateChanged(index, user, id) {
      if (this.checkUsersIds.includes(id)) {
        for (let i = 0; i < this.checkUsersIds.length; i++) {
          if (this.checkUsersIds[i] == id) {
            this.checkUsersIds.splice(i, 1);
            this.selectPeople.splice(i, 1);
          }
        }
      } else {
        this.selectPeople.push(user);
        this.checkUsersIds.push(id);
      }
    },
    sure() {
      //console.log(this.selectPeople)
      this.$emit("getPeoples", this.selectPeople, this.checkUsersIds);
    },
    clearPeople() {
      (this.selectFileId = ""),
        (this.selectFileName = ""),
        (this.expandedKeys = []),
        (this.userList = []),
        (this.checkUsersIds = []),
        (this.selectPeople = []);
    },
  },
};
</script>

<style lang="scss" scoped>
.depart-wrap {
  width: 100%;
  .user_tree {
    width: 256px;
    height: 460px;
    border: 1px solid #e5e5e5;
    background: #fff;
    float: left;
    overflow-y: auto;
    .el-tree {
      padding: 8px;
      > .el-tree-node {
        border-radius: 4px;
        border: 1px solid #e5e5e5;
        background: #fff;
        .el-tree-node__content {
          height: 40px;
          .parent-node {
            width: 100%;
            position: relative;
            .add-round-btn {
              width: 18px;
              height: 18px;
              position: absolute;
              right: 3px;
              top: -1px;
            }
          }
          .tree-none1 {
            font-weight: bold;
          }
          .second-node {
            width: 100%;
            position: relative;
            .tree-node2 {
              display: inline-block;
              max-width: 180px;
              @include text-ellipsis;
            }
          }
        }
      }
    }
  }
  .right {
    width: calc(100% - 264px);
    height: 460px;
    border: 1px solid #e5e5e5;
    background: #fff;
    padding: 8px 0 0 8px;
    overflow-y: auto;
    ul {
      li {
        padding: 8px 10px 8px 10px;
        margin-right: 8px;
        margin-bottom: 8px;
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
.depart-wrap:after {
  content: "";
  display: block;
  height: 0;
  clear: both;
  visibility: hidden;
}
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  color: #189bfe;
}
.form-footer {
  padding: 16px 0 0 0;
  text-align: right;
}
</style>