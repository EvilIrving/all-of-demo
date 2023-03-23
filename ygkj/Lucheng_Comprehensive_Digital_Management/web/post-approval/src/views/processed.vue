<template>
  <div class="tab-processed">
    <van-dropdown-menu active-color="#0079ff">
      <van-dropdown-item v-model="result" :options="[
        { text: '全部状态', value: undefined },
        { text: '无异常', value: '0' },
        { text: '异常', value: '1' }
      ]" @change="onFilterChange" />
      <van-dropdown-item v-model="stage" :options="[
        { text: '前期阶段', value: '1' },
        { text: '施工阶段', value: '2' },
        { text: '完工阶段', value: '3' }
      ]" @change="onFilterChange" />
    </van-dropdown-menu>
    <van-list v-model:loading="data.loading" v-model:error="data.error" :finished="data.finished"
      finished-text="没有更多记录了" class="processed-list">
      <div v-for="(item, index) in data.list" :key="index" class="processed-item" @click="onItemClick(item)">
        <div class="item-title">{{ item.projectName }}</div>
        <div class="item-person">
          巡查人员:<span>{{ item.inspector }}</span>
        </div>
        <div class="item-start-time">
          开始时间:<span>{{ item.patrolBeginTime }}</span>
        </div>
        <div class="item-end-time">
          结束时间:<span>{{ item.patrolEndTime }}</span>
        </div>
        <div class="item-check-stamp">
          <img v-if="item.patrolResult == 0" src="@/assets/images/normal-stamp.png" alt="" />
          <img v-else src="@/assets/images/abnormal-stamp.png" alt="" />
        </div>
      </div>
    </van-list>
  </div>
</template>
<script lang="ts">
import { getBlueInspectList } from '@/api/blueInspect';
import { InspectProject } from '@/types/inspect';
import { useList, usePagination } from '@/use';
import { defineComponent, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  setup() {
    const router = useRouter();
    const { pageNum, pageAdd, resetPage } = usePagination();
    const { data, resetData } = useList<Partial<InspectProject>>();
    const result = ref<'0' | '1' | undefined>();
    const stage = ref<'1' | '2' | '3' | undefined>('1');

    const getList = () => {
      getBlueInspectList({
        pageNum: pageNum.value,
        pageSize: 10,
        type: '2',
        stage: stage.value,
        result: result.value
      }).then(res => {
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
    const onFilterChange = () => {
      resetData();
      resetPage();
      getList();
    };
    const onItemClick = (item: Partial<InspectProject>) => {
      if (item.id)
        router.push({
          name: 'Project',
          params: { id: ['view', item.id] },
        });
    };
    onMounted(() => {
      getList();
    });
    return { data, stage, result, onItemClick, onFilterChange };
  }
});
</script>
<style lang="scss">
.processed-list {
  .processed-item {
    padding: 40px;
    position: relative;

    .item-title {
      font-size: 48px;
      font-weight: 500;
      padding: 8px 0;
    }

    .item-person,
    .item-start-time,
    .item-end-time {
      padding: 6px 0;
      font-size: 24px;
      color: #a8a8a8;

      >span {
        margin-left: 12px;
      }
    }

    .item-check-stamp {
      position: absolute;
      top: 36px;
      right: 50px;
    }
  }
}
</style>
