import { uploadFile, UploadResponse } from '@/api/files';
import { UploaderFileListItem } from 'vant';
import { ref } from 'vue';

export function useFileUpload() {
  const fileList = ref<UploaderFileListItem[]>([]);
  const afterRead = (data: UploaderFileListItem) => {
    return new Promise<UploadResponse>((resolve, reject) => {
      const formData = new FormData();
      formData.append('uploadFile', data.file as Blob);
      uploadFile(formData)
        .then(res => {
          if (res.code == 0) {
            resolve(res);
          } else {
            reject(res);
          }
        })
        .catch(err => {
          reject(err);
        });
    });
  };
  return { fileList, afterRead };
}
