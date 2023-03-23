<template>
  <div>
    <div class="fab-container">
      <img class="fab" src="@/assets/images/file-fab.svg" @close="newDirName = ''" @click="show = true" />
    </div>
    <van-popup v-model="show" round position="bottom" class="shortcut-popup">
      <div class="title">快捷操作</div>
      <div class="content">
        <div class="actions">
          <div v-if="directory !== '-1'" @click="mkdirShow = true">
            <img src="@/assets/images/new-dir.svg" alt="" />
            <div>新建文件夹</div>
          </div>
          <van-uploader :after-read="uploadFile" accept="image/*">
            <img src="@/assets/images/upload-img.svg" alt="" />
            <div>上传图片</div>
          </van-uploader>
          <van-uploader :after-read="uploadFile">
            <img src="@/assets/images/upload-file.svg" alt="" />
            <div>上传文件</div>
          </van-uploader>
        </div>
      </div>
    </van-popup>
    <van-popup v-model="mkdirShow" round position="bottom" class="mkdir-popup">
      <van-nav-bar
        title="新建文件夹"
        left-text="返回"
        right-text="完成"
        :left-arrow="$store.state.viewType !== 'irs'"
        @click-left="mkdirShow = false"
        @click-right="onMkdirSubmit()"
      />
      <div class="content">
        <img src="@/assets/images/new-dir.svg" alt="" />
        <div class="mkdir-input">
          <input v-model="newDirName" type="text" />
          <div class="input-clear" @click="newDirName = ''">x</div>
        </div>
      </div>
    </van-popup>
  </div>
</template>
<script>
  import { fileUploadApi } from '@/api/rainfall.js';
  import axios from '@/config/httpRequset';

  /**
   * @typedef {import('./type').AchievesFileParams} AchievesFileParams
   * @typedef {import('./type').AchievesFolderParams} AchievesFolderParams
   */

  /**
   * 新增文件(夹)
   * @param {AchievesFileParams|AchievesFolderParams} data
   */
  function insertAchieves(data) {
    return axios.post('/ums/ums/sysAchievement/insert', data);
  }
  export default {
    name: 'ArchivesShortcut',
    emits: ['change'],
    props: {
      directory: { type: String }
    },
    data() {
      return {
        show: false,
        mkdirShow: false,
        newDirName: ''
      };
    },
    methods: {
      async uploadFile(file, detail) {
        const formData = new FormData();
        formData.append('uploadFile', file.file);
        const uploadRes = await fileUploadApi(formData);
        if (uploadRes.code === 0) {
          const file = {
            fileName: uploadRes.data.originalName,
            id: uploadRes.data.id,
            fileNo: uploadRes.data.fileNo,
            fileSize: uploadRes.data.fileSize,
            fileType: uploadRes.data.ext
          };
          const res = await insertAchieves({ id: this.directory, fileStr: JSON.stringify([file]) });
          if (res.code === 0) {
            this.$toast(res.message);
            this.show = false;
            this.$emit('change');
            this.onClose();
          }
        }
      },
      onClose() {
        this.mkdirShow = false;
        this.show = false;
        this.newDirName = '';
      },
      async onMkdirSubmit() {
        const res = await insertAchieves({ name: this.newDirName, parentId: this.directory });
        if (res.code === 0) {
        }
        this.$emit('change');
        this.onClose();
      }
    }
  };
</script>
<style lang="scss" scoped>
  .fab-container {
    position: fixed;
    bottom: 0.75rem;
    left: 0;
    right: 0;
    text-align: center;
    pointer-events: none;
    .fab {
      pointer-events: auto;
      height: 50px;
      width: 50px;
    }
  }
  .shortcut-popup {
    .title {
      padding: 20px 24px;
      background-image: linear-gradient(270deg, #ecf4fe, #fdfeff);
    }
    .content {
      padding: 32px 52px 52px 52px;
    }
    .actions {
      display: flex;
      justify-content: space-between;
      > div {
        text-align: center;
      }
      img {
        height: 72px;
        width: 72px;
        margin-bottom: 8px;
      }
    }
  }
  .mkdir-popup {
    height: 80vh;
    width: 100vw;
    .content {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-top: 18vh;
      img {
        margin-bottom: 1rem;
      }
    }
  }
  .mkdir-input {
    display: inline-block;
    min-width: 320px;
    position: relative;
    .input-clear {
      position: absolute;
      transform: translateY(-50%);
      right: 16px;
      top: 50%;
      color: #fff;
      line-height: 16px;
      border-radius: 100%;
      text-align: center;
      height: 16px;
      width: 16px;
      background-color: #c0c3c7;
    }
    input {
      box-sizing: border-box;
      width: 100%;
      padding: 14px;
      background-color: #f0f4f7;
      border-radius: 0.12rem;
    }
  }
</style>
