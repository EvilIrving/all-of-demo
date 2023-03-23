<template>
  <BasicLayout>
    <van-form @submit="onSubmit">
      <van-cell-group>
        <van-field
          v-model="projectName"
          is-link
          readonly
          required
          name="projectId"
          label="举报项目"
          placeholder="请选择项目"
          @click="showPicker = true"
          :rules="[{ required: true, message: '项目不能为空，请选择' }]"
        />
        <van-field
          v-model="informContent"
          name="informContent"
          label="举报内容"
          type="textarea"
          rows="4"
          required
          autosize
          placeholder="请输入举报内容"
          :rules="[{ required: true, message: '举报内容不能为空，请输入' }]"
        />
      </van-cell-group>
      <div style="margin: 16px">
        <van-button :disabled="submitType" block type="primary" native-type="submit"> 提交 </van-button>
      </div>
      <van-popup v-model:show="showPicker" position="bottom">
        <van-picker
          className="project-item"
          :columns="columns"
          :columns-field-names="customFieldName"
          @confirm="onConfirm"
          @cancel="showPicker = false"
        >
        </van-picker>
      </van-popup>
    </van-form>
  </BasicLayout>
</template>

<script lang="ts" setup>
import { ref, onMounted, toRaw } from 'vue';
import BasicLayout from './BasicLayout/index.vue';
import { projectListApi, saveOrUpdateApi } from '@/api/publicity';
import { login } from '@/api/test';
import { storage } from '@/utils/storage';
import { Toast } from 'vant';
import { useRouter } from 'vue-router';

//定义的数据
const router = useRouter();
const submitType = ref(false);
const showPicker = ref(false);
const projectName = ref('');
const projectId = ref('');
const informContent = ref('');
const columns = ref([]);
const customFieldName = {
  text: 'projectName'
};

//处理方法
const onConfirm = (currentValue: any) => {
  const data = toRaw(currentValue);
  projectName.value = data.projectName;
  projectId.value = data.id;
  showPicker.value = false;
};
const projectList = async () => {
  const res = await projectListApi();
  if (res.code == 0) {
    columns.value = res.data.list;
  }
};
console.log();
const onSubmit = () => {
  submitType.value = true;
  saveOrUpdateApi({
    projectId: projectId.value,
    informContent: informContent.value
  })
    .then((res: any) => {
      if (res.code == 0) {
        Toast.success('举报成功~');
        router.back();
      } else {
        Toast.fail(res.message);
        submitType.value = false;
      }
    })
    .catch(err => {
      Toast.fail(err);
      submitType.value = false;
    });
};

//生命周期
onMounted(async () => {
  if (process.env.NODE_ENV !== 'production') {
    const userInfo: any = storage.get('userInfo_obj');
    if (!userInfo) {
      await login({ username: '13858859582', password: 'ygkj0818' }).then(res => {
        if (res.code == 0) {
          storage.set('userInfo_obj', { ...res.data, timestamp: new Date().getTime() });
        }
      });
    }
  }
  await projectList();
});
</script>


<style lang="scss">
.project-item {
  .van-picker-column {
    .van-picker-column__wrapper {
      .van-picker-column__item {
        .van-ellipsis {
          font-size: 30px;
        }
      }
    }
  }
}
</style>