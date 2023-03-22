<template>
  <div class="tab-untreated">
    <van-tabs v-model:active="stage" type="card" title-active-color="#fff" title-inactive-color="#0079ff"
      color="#0079ff" @change="onStageChange">
      <van-tab :title="'全部' + tabStatic['全部']" name=""></van-tab>
      <van-tab :title="'前期阶段' + tabStatic['前期阶段']" name="1"></van-tab>
      <van-tab :title="'施工阶段' + tabStatic['施工阶段']" name="2"></van-tab>
      <van-tab :title="'完工阶段' + tabStatic['完工阶段']" name="3"></van-tab>
    </van-tabs>
    <van-list v-model:loading="data.loading" v-model:error="data.error" :finished="data.finished"
      finished-text="没有更多记录了" class="untreated-list" @load="getList">
      <div v-for="(item, index) in data.list" :key="index" class="untreated-item" @click="onItemClick(item)">
        <div class="item-title">{{ item.projectName }}</div>
        <div class="item-label">
          项目地址:<span>{{ item.address }}</span>
        </div>
        <van-row class="item-label inline">
          <van-col :span="10">
            项目负责人:<span>{{ item.inspector }}</span>
          </van-col>
          <van-col :span="14">
            联系电话:<span>{{ item.phone }}</span>
          </van-col>
        </van-row>

        <div class="van-hairline--top">
          <van-row class="item-label inline">
            <van-col :span="10">
              派发人:<span>{{ item.distributor }}</span>
            </van-col>
            <van-col :span="14">
              派发时间:<span>{{ item.createTime }}</span>
            </van-col>
          </van-row>
          <div class="item-label">
            联系电话:<span>{{ item.distributorPhone }}</span>
          </div>
        </div>
      </div>
    </van-list>
  </div>
</template>
<script lang="ts">
import { useList, usePagination } from '@/use';
import { defineComponent, onMounted, ref, reactive } from 'vue';
import { getBlueInspectList, getStatusCount } from '@/api/blueInspect';
import { InspectProject } from '@/types/inspect';
import { useRouter } from 'vue-router';

export default defineComponent({
  setup() {
    const router = useRouter();
    const { pageNum, pageAdd, resetPage } = usePagination();
    const { data, resetData } = useList<Partial<InspectProject>>();
    const tabStatic = ref({
      全部: 0,
      前期阶段: 0,
      施工阶段: 0,
      完工阶段: 0
    });
    getStatusCount().then(res => {
      if (res.code === 0) {
        tabStatic.value = res.data;
      }
    });
    const stage = ref<'1' | '2' | '3'>();
    const getList = () => {
      data.loading = true;
      getBlueInspectList({ pageNum: pageNum.value, pageSize: 10, type: '1', stage: stage.value }).then(res => {
        if (res.code == 0) {
          if (res.data.length > 0) {
            if (res.data.length < 10) data.finished = true;
            pageAdd();
            data.list = data.list.concat(res.data);
            data.loading = false;
          } else data.finished = true;
        } else data.error = true;
      });
    };
    const onItemClick = (item: Partial<InspectProject>) => {
      if (item.id)
        router.push({
          name: 'Project',
          params: { id: ['deal', item.id] }
        });
    };
    const onStageChange = () => {
      resetData();
      resetPage();
      getList();
    };
    onMounted(() => {
      // getList();
    });
    return { data, pageNum, stage, onStageChange, getList, onItemClick, tabStatic };
  }
});
</script>
<style lang="scss" scoped>
.untreated-list {
  .untreated-item {
    padding: 40px;

    .item-title {
      font-size: 48px;
      font-weight: 500;
      padding: 8px 0;
    }

    .item-label {
      padding: 6px 0;
      font-size: 24px;
      color: #a8a8a8;

      span {
        margin-left: 12px;
      }

      &.inline {
        margin-top: 16px;
        padding-top: 16px;

        >div {
          display: inline-block;
        }
      }
    }
  }
}
</style>
