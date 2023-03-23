<template>
  <div :class="['project-detail', isProcessed && 'processed']">
    <div class="project-title">
      <span class="title-label">{{ inspection.data.projectName }}</span>
      <span class="title-project-stage">{{ stageMap[inspection.data.stage as number] }}</span>
      <van-icon class="title-icon" name="idcard" :size="24" />
    </div>
    <div class="out-title">巡查项</div>
    <van-form class="styled-form">
      <van-cell-group v-for="(problem, index) in problemList" :key="index" :title="`${index + 1}、${problem.entryName}`">
        <van-field label="状态">
          <template #input>
            <van-radio-group v-model="problem.status" direction="horizontal" :disabled="isProcessed">
              <van-radio :name="1">正常</van-radio>
              <van-radio :name="2">异常</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field v-model="problem.problemDescrip" label="问题描述" :required="problem.status == 2" rows="2" autosize
          type="textarea" :placeholder="isProcessed ? '' : '请输入问题描述'" :readonly="isProcessed" />
        <van-field label="上传照片">
          <template #input>
            <div v-if="isProcessed" class="uploaded-images">
              <van-image v-for="(image, index) in problem.fileIds?.split(',') || []" :key="index" :src="image" />
            </div>
            <van-uploader v-else v-model="filesList[index]" :after-read="afterRead" :name="index">
              <div class="van-uploader__border-box">
                <img src="@/assets/images/camera.png" />
              </div>
              <template #preview-cover="{ file }">
                <div class="preview-cover van-ellipsis">{{ file.name }}</div>
              </template>
            </van-uploader>
          </template>
        </van-field>
      </van-cell-group>
      <div v-if="!isProcessed" style="margin: 32px">
        <van-button :loading="buttonLoading" round block type="primary" @click="onSubmit()"> 提交 </van-button>
      </div>
    </van-form>
  </div>
</template>
<script lang="ts" setup>
import { getBlueInspectDetail, feedbackBlueInspectDetail } from '@/api/blueInspect';
import { UploaderFileListItem } from 'vant';
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { InspectProject } from '@/types/inspect';
import { useToggle } from '@vant/use';
import { Toast } from 'vant';
import { useFileUpload } from '@/use/files';
import { UploaderAfterRead } from 'vant/lib/uploader/types';

interface FormProblemItem {
  createId: string;
  createTime: string;
  delFlag: number;
  /** 本次巡查异常条目 */
  entryName: string;
  /** 图片url `%2C`隔开 */
  fileIds: string;
  id: string;
  inspectionId: string;
  /** 问题描述 */
  problemDescrip: string;
  rectify: number;
  /** 状态 1正常 2异常 */
  status: 1 | 2;
  updateId: string;
  updateTime: string;
}


const stageMap = ['', '前期阶段', '施工阶段', '完工阶段'];

const route = useRoute();
const router = useRouter();
const [method, id] = route.params.id as ["deal" | "view", string];
const isProcessed = ref<boolean>(method == 'view');

const [buttonLoading, toggle] = useToggle(false);

const problemList = ref<Partial<FormProblemItem>[]>([]);
const inspection = reactive<{ data: Partial<InspectProject> }>({ data: {} });

const filesList = ref<UploaderFileListItem[][]>([]);
const { afterRead: afterReadPure } = useFileUpload();
const afterRead: UploaderAfterRead = (data, options) => {
  if (Array.isArray(data)) return
  afterReadPure(data).then(res => {
    if (problemList.value[options.name as number].fileIds) problemList.value[options.name as number].fileIds += `,${res.data.filePath}`;
    else problemList.value[options.name as number].fileIds = `${res.data.filePath}`;
  });
};

const onSubmit = () => {
  toggle(true);
  feedbackBlueInspectDetail({
    problems: problemList.value,
    type: inspection.data.patrolEndTime ? '2' : '1'
  }).then(res => {
    if (res.code == 0) {
      toggle(false);
      Toast.success('提交成功');
      router.back();
    }
  });
};
onMounted(() => {
  getBlueInspectDetail({ inspectionId: id }).then(res => {
    if (res.code == 0) {
      problemList.value = res.data.list;
      filesList.value = new Array(res.data.list.length).fill([]);
      inspection.data = res.data.inspection;
    }
  });
}); 
</script>
<style lang="scss">
.project-detail {
  --van-radio-disabled-label-color: var(--van-radio-label-color);
  --van-radio-disabled-icon-color: var(--van-white);
  --van-picker-title-font-size: 32px;

  .project-title {
    padding: 32px;
    font-size: 32px;

    .title-project-stage {
      margin-left: 8px;
      color: #037fff;
      background-color: #d5eaff;
      padding: 4px 8px;
      font-size: 24px;
      border: 1px #91c6ff solid;
      border-radius: 4px;
    }

    .title-icon {
      float: right;
    }
  }

  .out-title {
    font-size: 24px;
    color: #909194;
    background-color: #eff0f4;
    padding: 8px;
    width: 100%;
  }

  .van-field__control--custom {
    align-items: flex-start;
    --van-uploader-size: 120px;

    .van-picker__title {
      font-size: --van-picker-title-font-size;
    }

    .van-uploader {
      .van-uploader__border-box {
        height: var(--van-uploader-size);
        width: var(--van-uploader-size);
      }
    }

    .uploaded-images {
      .van-image {
        height: var(--van-uploader-size);
        width: var(--van-uploader-size);
        margin-right: 16px;
      }
    }
  }

  &.processed {
    .van-field__control {
      border: none !important;
    }

    .van-radio__icon--disabled.van-radio__icon--checked .van-icon {
      color: var(--van-white);
      background-color: var(--van-radio-checked-icon-color);
      border-color: var(--van-radio-checked-icon-color);
    }

    .van-radio__icon--disabled.van-radio__icon--checked .van-icon {
      color: var(--van-white);
      background-color: var(--van-radio-checked-icon-color);
      border-color: var(--van-radio-checked-icon-color);
    }
  }
}
</style>
