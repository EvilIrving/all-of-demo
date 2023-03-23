<template>
  <div class="primary-dialog">
    <DataDetailDialog
      :visible.sync="visible"
      :loading="exporting"
      :fullscreen="fullscreen"
      :before-close="handleClose"
      :title="type == 'norm' ? '节水指标' : '取水监管'"
      @changeFullScreen="changeFullScreen"
    >
      <template #stat>
        <StatisticsBox v-if="type == 'norm'" icon="icon-reach-norm-years" title="达到控制指标年份">
          <span>{{ statistic.data.reachNormYears || 0 }}/{{ statistic.data.totalYears || 0 }}</span>
        </StatisticsBox>
        <StatisticsBox v-if="type != 'norm'" icon="icon-license-total" title="取水许可证数量">
          <span>{{ statistic.data.totalNum || 0 }}</span
          >个
        </StatisticsBox>
        <StatisticsBox v-if="type != 'norm'" icon="icon-license-max-area" title="最多地区">
          <span>{{ statistic.data.numMaxArea || '-' }}</span
          >{{ statistic.data.numMaxAreaNum || 0 }}个
        </StatisticsBox>
      </template>
      <template #query>
        <LabelFormItem v-if="type == 'norm'" label="起止年份:">
          <el-date-picker
            v-model="startYear"
            format="yyyy"
            value-format="yyyy"
            type="year"
            placeholder="开始"
            clearable
            :picker-options="{
              disabledDate: (time) => (endYear ? time.getFullYear() > endYear : null)
            }"
            @change="getList"
          />
          至
          <el-date-picker
            v-model="endYear"
            format="yyyy"
            value-format="yyyy"
            type="year"
            placeholder="结束"
            clearable
            :picker-options="{
              disabledDate: (time) => (startYear ? time.getFullYear() < startYear : null)
            }"
            @change="getList"
          />
        </LabelFormItem>
        <LabelFormItem v-if="type != 'norm'" label="行政区划">
          <el-select v-model="areaCode" size="small" clearable @change="getList">
            <el-option v-for="item in areaList" :key="item.value" :label="item.adnm" :value="item.adcd" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem v-if="type != 'norm'" label="是否过期">
          <el-select v-model="outOfDate" size="small" clearable @change="getList">
            <el-option label="是" :value="false" />
            <el-option label="否" :value="true" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem v-if="type != 'norm'" label="许可证编号">
          <el-input v-model="applSnKey" @change="getList" />
        </LabelFormItem>
        <DataDetailButton style="margin-left: auto" @click="exportTable">导 出</DataDetailButton>
      </template>
      <el-table
        ref="tableRef"
        :data="tableData"
        stripe
        height="100%"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column label="行政区划" prop="adnm" />
        <template v-if="type == 'norm'">
          <el-table-column label="年份" prop="year" />
          <el-table-column label="用水量(亿m³)" prop="totalWatCsm" width="120" />
          <el-table-column label="十三五控制指标(亿m³)" prop="totalWatCsm5Norm" width="170" />
          <el-table-column label="较省平均(%)" prop="twcCompareZjRate" width="120" />
          <el-table-column label="万元GDP用水量(m³/万元)" prop="wpg" width="200" />
          <el-table-column label="控制指标(m³/万元)" prop="wpg5Norm" width="140" />
          <el-table-column label="十三五较省平均(%)" prop="wpgCompareZjRate" width="180" />
          <el-table-column label="万元工业增加值(m³/万元)" prop="induWpg" width="200" />
          <el-table-column label="控制指标(m³/万元)" prop="induWpg5Norm" width="200" />
          <el-table-column label="十三五较省平均(%)" prop="wpgidCompareZjRate" width="200" />
          <el-table-column label="农业灌溉有效利用系数" prop="agIrrEffectFactor" width="200" />
          <el-table-column label="节水型社会创建达标情况" prop="watSavNum" width="200" />
        </template>
        <template v-else>
          <el-table-column label="取水口名称" prop="wainName" />
          <el-table-column label="取水证编码" prop="applSn" />
          <el-table-column label="取水用途" prop="typeQslx" />
          <el-table-column label="批准取水量" prop="wpermitsize" />
          <el-table-column label="许可有效期限" prop="intBdDl" />
          <el-table-column label="是否过期" prop="ouOfDate" />
          <el-table-column label="是否有证无点" prop="withoutWain" />
        </template>
      </el-table>
    </DataDetailDialog>
  </div>
</template>
<script>
//@ts-check
import { exportWaterSavingAction, exportWsaSuperviseList } from '@/api/micro-service/project-manage/export';
import { getWaterSavingActionList, getWaterSavingActionNormStats } from '@/api/micro-service/wra';
import { getWaterSavingSuperviseList, getWaterSavingSuperviseStat } from '@/api/micro-service/water-saving';
import { computed, defineComponent, reactive, watch, ref } from '@vue/composition-api';
import {
  DataDetailDialog,
  StatisticsBox,
  DataDetailButton,
  LabelFormItem
} from '../../dialog/DetailsCommonDialog/components';
import useAsyncTable from './composables/useAsyncTable';
import useFullScreen from './composables/useFullScreen';
import useFilterTable from './composables/useFilterTable';

export default defineComponent({
  name: 'WaterSavingActionDetail',
  props: {
    visible: { type: Boolean, default: false },
    type: /** @type {import('@vue/composition-api').PropType<'norm'|'supervision'>} */ (String)
  },
  components: { DataDetailDialog, StatisticsBox, DataDetailButton, LabelFormItem },
  setup(props, { emit }) {
    const outOfDate = ref(false);
    const applSnKey = ref('');
    const computedOpt = computed(() => {
      return props.type == 'norm'
        ? {
            startYear: startYear.value || 2015,
            endYear: endYear.value || new Date().getFullYear()
          }
        : { adcd: areaCode.value, applSnKey: applSnKey.value, outOfDate: outOfDate.value };
    });
    async function fetchCallback() {
      const opt = computedOpt.value;
      const res = await (props.type == 'norm' ? getWaterSavingActionList : getWaterSavingSuperviseList)(opt);
      return /** @type {any[]} */ (res.data);
    }
    async function exportCallback() {
      const opt = computedOpt.value;
      await (props.type == 'norm' ? exportWaterSavingAction : exportWsaSuperviseList)(opt);
    }
    async function statCallback() {
      const opt = computedOpt.value;
      const res = await (props.type == 'norm' ? getWaterSavingActionNormStats : getWaterSavingSuperviseStat)(opt);
      if (res.code == 0) {
        statistic.data = res.data;
      }
    }
    const { tableData, loading, exporting, getList, exportTable } = useAsyncTable(fetchCallback, exportCallback);
    const { tableRef, fullscreen, changeFullScreen } = useFullScreen();
    const { startYear, endYear, areaCode, areaList } = useFilterTable();
    const statistic = reactive({
      data: { reachNormYears: 0, totalYears: 0 }
    });

    watch(
      () => props.visible,
      (val) => {
        if (val) getList(statCallback);
      }
    );

    const handleClose = () => {
      emit('update:visible', false);
    };

    return {
      statistic,
      handleClose,
      fullscreen,
      changeFullScreen,
      startYear,
      endYear,
      tableRef,
      tableData,
      loading,
      exporting,
      areaCode,
      areaList,
      outOfDate,
      applSnKey,
      getList,
      exportTable
    };
  }
});
</script>
