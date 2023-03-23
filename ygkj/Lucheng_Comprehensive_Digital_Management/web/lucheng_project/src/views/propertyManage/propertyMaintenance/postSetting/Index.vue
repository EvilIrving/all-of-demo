<!--
 * @Date: 2022-04-13 11:06:34
 * @Author: 
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-16 15:16:31
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyMaintenance\postSetting\Index.vue
-->
<template>
  <div class="warp-page">
    <div class="warp-unit-list" v-if="userInfo.userType != 3">
      <unit-list @checkItem="checkItem" />
    </div>
    <div class="wrap" :class="{'wrap-type': userInfo.userType == 3}">
      <div class="item_box_title">
        <p>岗位设置</p>
      </div>
      <div class="list_wrap">
        <div class="list_left">
          <el-input
            style="width: 230px; margin: 25px"
            size="mini"
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            @change="getList"
            v-model="searchText"
            clearable
          />
          <div class="tree-list">
            <el-tree
              ref="popularTree"
              class="setting-tree-list"
              :data="deplList"
              node-key="id"
              @node-click="handleNodeClick"
              :current-node-key="currentLivingId"
              :default-checked-keys="[1]"
              :props="defaultProps"
              default-expand-all
              :expand-on-click-node="false"
              :highlight-current="true"
            >
              <span class="custom-tree-node" slot-scope="{ node }">
                <div class="tree-box">
                  <div class="tree-name" :title="node.label">
                    {{ node.label }}
                  </div>
                </div>
              </span>
            </el-tree>
          </div>
        </div>
        <div class="list_right">
          <div
            class="info_option"
            style="
              width: 100%;
              padding: 25px 16px 16px 16px;
              box-sizing: border-box;
              margin-bottom: 20px;
            "
          >
            <!-- 工程名称、工程规模、所属街道、工程类别 -->
            <span style="font-size: 14px; margin-left: 15px">工程规模：</span>
            <el-select
              size="mini"
              v-model="scale"
              placeholder="请选择"
              @change="getList"
              clearable
              style="width: 180px; margin: 0 20px 0 15px"
            >
              <el-option
                v-for="item in scaleOptions"
                :key="item.label"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
            <span style="font-size: 14px; margin-left: 15px">工程名称：</span>
            <el-input
              size="mini"
              placeholder="请输入..."
              prefix-icon="el-icon-search"
              @change="getList"
              v-model="name"
              style="width: 180px; margin-right: 15px"
              clearable
            />
            <span style="font-size: 14px; margin-left: 15px">所属街道：</span>
            <el-select
              size="mini"
              v-model="street"
              placeholder="请选择"
              @change="getList"
              clearable
              style="width: 180px; margin: 0 20px 0 15px"
            >
              <el-option
                v-for="item in streetOptions"
                :key="item.adnm"
                :label="item.adnm"
                :value="item.adnm"
              >
              </el-option>
            </el-select>
            <!-- <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList" v-model="street" style="width: 180px; margin-right: 15px" clearable /> -->
            <!-- <el-button size="mini" type="primary" class="right" @click="editTable('新增')">新增</el-button> -->
            <!-- <el-button size="mini" style="margin-right: 10px" class="right" @click="exportTable()">导出</el-button> -->
          </div>
          <div class="tb_wrapper">
            <el-table
              v-loading="tableLoading"
              :data="postList"
              border
              style="width: 100%"
            >
              <el-table-column
                type="index"
                align="center"
                width="50"
                label="序号"
              ></el-table-column>
              <el-table-column
                prop="projectName"
                min-width="100"
                label="工程名称"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="projectScale"
                label="规模"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="townAdnm"
                label="所属乡镇（街道）"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="unitLeader"
                label="单位负责人"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="unitLeaderPhone"
                label="联系方式"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="technicalLeader"
                label="技术负责人"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="technicalLeaderPhone"
                label="联系方式"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="patrolLeader"
                label="巡查负责人"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="patrolLeaderPhone"
                label="联系方式"
                align="center"
              ></el-table-column>
              <el-table-column align="center" label="操作" width="150">
                <template slot-scope="scope">
                  <div class="deal_box">
                    <span
                      class="deal_btn deal_btn_look"
                      v-permission="'proPostSetting:edit'"
                      @click="editTable('编辑', scope.row.id)"
                      >编辑</span
                    >
                    <span
                      class="deal_btn deal_btn_del"
                      v-permission="'proPostSetting:del'"
                      @click="del(scope.row.id)"
                      >删除</span
                    >
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <Pagination
              :total="pageTotal"
              :page.sync="pageNum"
              :limit.sync="pageSize"
              :page-sizes="[5, 10, 15, 20]"
              @pagination="getList"
            />
          </div>
        </div>
      </div>

      <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
        <post-setting-form
          v-if="editDialog"
          :id="id"
          :deptId='deptId'
          :formData="insideFormData"
          @closeEditDialog="closeEditDialog"
        ></post-setting-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { operationUnit } from "@/api/propertyManage";
import PostSettingForm from "./PostSettingForm.vue";
import { exportEquipList } from "@/utils/export.js";
import UnitList from "@/components/UnitList.vue";
export default {
  name: "postSetting",
  props: {},
  components: {
    PostSettingForm,
    UnitList,
  },
  data() {
    return {
      searchText: "",
      currentLivingId: 11,
      defaultProps: {
        children: "list",
        label: "deptname",
      },
      deplList: [
        {
          deptname: "水利工程",
          id: 1,
          list: [
            {
              deptname: "海塘",
              id: 11,
              pid: "1",
            },
            {
              deptname: "水库",
              id: 2,
              pid: "1",
            },
            {
              deptname: "堤防",
              id: 3,
              pid: "1",
            },
            {
              deptname: "水闸",
              id: 4,
              pid: "1",
            },
            {
              deptname: "堤防",
              id: 5,
              pid: "1",
            },
            {
              deptname: "泵站",
              id: 6,
              pid: "1",
            },
            // {
            //   deptname: "山塘",
            //   id: "7",
            // },
            {
              deptname: "水电站",
              id: 8,
              pid: "1",
            },
            {
              deptname: "农饮水",
              id: 9,
              pid: "1",
            },
          ],
        },
      ],
      insideFormData: {},
      postList: [],
      name: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      name: "",
      scale: "",
      proType: "海塘",
      scaleOptions: [
        { label: "全部", value: "" },
        { label: "大（1）型", value: 1 },
        { label: "大（2）型", value: 2 },
        { label: "中型", value: 3 },
        { label: "小（1）型", value: 4 },
        { label: "小（2）型", value: 5 },
      ],
      street: "",
      streetOptions: [],
      tableLoading: false,
      headerList: [],
      deptId: "",
      userInfo: {},
    };
  },
  mounted() {
    this.userInfo = this.$sessionData("get", "userInfo");
    if (this.userInfo.userType == 3) {
      this.deptId = this.userInfo.deptId;
      this.getList();
    }
    this.getStreetList();
  },
  methods: {
    test(row) {
      console.log(row, "row");
    },
    checkItem(item) {
      this.deptId = item.id;
      this.getList();
    },
    handleNodeClick(row) {
      this.currentLivingId = row.id;
      this.proType = row.deptname;
      this.getList();
    },
    async getList() {
      const opt = {
        projectCategory: this.proType,
        projectName: this.name,
        projectScale: this.scale,
        townAdnm: this.street,
        type: 2,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        deptId: this.deptId,
      };
      this.tableLoading = true;
      await operationUnit.queryPositions(opt).then((res) => {
        if (res.code == 0) {
          // res.data.list.forEach((item) => {
          //   let arr = []
          //   // console.log((item.array && item.array.length) || 0, 'legnth');
          //   let length = 3 - (item.array && item.array.length) || 0
          //   for (let index = 0; index < length; index++) {
          //     arr.push({
          //       leader: '',
          //       leaderCertificatefile: '',
          //       leaderCertificatefileUrl: '',
          //       leaderCertificatefileName: '',
          //       leaderPhone: '',
          //       leaderPost: '',
          //     })
          //   }
          //   // debugger
          //   length !== 3 ? item.array = [...item.array, ...arr] : item.array = arr
          // })
          this.postList = res.data.list;
          // console.log(this.postList)
          this.pageTotal = res.data.totalRows;
          this.tableLoading = false;
        }
      });
    },
    async getStreetList() {
      let res = await operationUnit.lcArea();
      if (res.code === 0) {
        this.streetOptions = res.data;
      }
    },
    editTable(title, id) {
      this.id = id;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    del(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        operationUnit
          .delPosition({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
    exportTable() {
      let obj = {
        name: this.name,
      };
      exportEquipList(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
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
.wrap {
  // @include flexbox();
  .item_box_title {
    border: none;
  }
  .list_wrap {
    border-top: 1px solid #eee;
    @include flexbox();
  }
  .list_left {
    width: 280px;
  }
  .list_right {
    width: calc(100% - 280px);
    border-left: 1px solid #eee;
  }
}

.el-dialog__body {
  padding: 10px 20px 30px;
}
</style>

<style lang="scss">
.setting-tree-list {
  .el-tree-node__content {
    height: 50px;
  }
}
</style>