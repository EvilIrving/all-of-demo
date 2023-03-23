<!--
 * @Date: 2022-06-14 10:58:28
 * @Author: 
 * @Description: 转发
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-16 17:11:08
 * @FilePath: \lucheng-app\src\views\appView\workbench\TransInfo.vue
-->
<template>
  <div class="wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <van-search v-model="input" show-action label="" placeholder="请输入名字" @search="getUserList">
      <template #action>
        <div @click="getUserList">搜索</div>
      </template>
    </van-search>
    <div class="name_list">
      <van-checkbox-group v-model="result" @change='changeGroup'>
        <van-cell-group>
          <van-cell v-for="(item, index) in list" clickable :key="index" :title="`${item.name} ${item.phone}`" :label="item.deptName" @click="toggle(item,index)">
            <template #right-icon>
              <van-checkbox :name="item" ref="checkboxes" />
            </template>
            <!-- {{item.deptName}} -->
          </van-cell>
        </van-cell-group>
      </van-checkbox-group>
    </div>
    <div class="bottom_btn" v-if="status == 2" @click="checkTrans">确认转发</div>
    <div class="bottom_btn white" v-else @click="checkTrans">已转发</div>
    <!-- </van-list> -->
  </div>
</template>

<script>
import { policeMessage } from "@/api/projectDetail";
export default {
  name: 'TransInfo',
  props: {

  },
  components: {

  },
  data() {
    return {
      list: [],
      result: [],
      input: '',
      finished: false,
      loading: false,
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
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
      status: '',//是否已转发
    };
  },
  computed: {

  },
  watch: {

  },
  created() {

  },
  mounted() {
    this.addZwlog(this)
    this.id = this.$route.query.id
    this.getById(this.id)
    this.getUserList()
  },
  methods: {
    async getById(id) {
      let res = await policeMessage.getById({ id });
      if (res.code === 0) {
        const {
          warningrule,
          warningrulename,
          warningcontent,
          warningtime,
          sendcode,
          status,
          sendcount,
        } = res.data;
        this.tableForm = {
          warningrule,
          warningrulename,
          warningcontent,
          warningtime,
          sendcode,
          status,
          sendcount,
        };
        this.status = res.data.status
      }
    },
    async getUserList() {
      let res = await policeMessage.userList({ name: this.input });
      if (res.code === 0) {
        this.list = res.data;
      }
    },
    toggle(item, index) {
      this.$refs.checkboxes[index].toggle();
    },
    changeGroup(row) {
      let sendcode = []
      row.forEach(i => sendcode.push(i.id))
      this.tableForm.sendcount = row.length;
      this.tableForm.sendcode = sendcode.join(',');
    },
    async checkTrans() {
      this.tableForm.id = this.id
      let res = await policeMessage.save(this.tableForm);
      if (res.code === 0) {
        this.$toast.success({ message: '转发成功', duration: 2000, onOpened: () => { this.$router.go(-1) } });
      }
    },
  },
};
</script>

<style scoped lang="scss">
.wrap {
  width: 100%;
  height: 100%;
  .name_list {
    padding: 10px 16px;
    height: 80%;
    overflow: auto;
  }
}
</style>
