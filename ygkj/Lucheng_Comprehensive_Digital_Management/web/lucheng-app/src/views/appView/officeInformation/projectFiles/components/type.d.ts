export interface Achieves {
  id: string;
  createId: string;
  createTime: string;
  deleteFlag: string;
  deleted: boolean;
  updateTime: string;
  uploadFlag: string;
  downloadFlag: string;
}

export interface AchievesFolder extends Achieves {
  achiPath: string;
  adminFlag: string;
  allowNull: string;
  fileSize: number;
  lookFlag: string;
  modify: boolean;
  name: string;
  parentId: string;
  projectCode: string;
  type: 'folder';
}

export interface AchievesFile extends Achieves {
  browseNum: number;
  fileName: string;
  fileSize: number;
  fileType: string;
  fileUrl: string;
  lookFlag: string;
  modify: boolean;
  type: 'file';
}

export interface AchievesFolderParams {
  id: string;
  name: string;
  sort?: number;
  parentId: string;
  projectCode?: string;
}

export interface AchievesFileParams {
  id: string;
  /**
   * @description `Pick<AchievesFile,'id'|'fileName'|'fileSize'|'fileType'>[]` 序列化后的字符串
   */
  fileStr: string;
  labelIds: string;
}

/**
 * /ums/ums/sysAchievement/achievementStructure 文件树节点
 */
export interface FileTreeNode {
  id: string;
  /** 父节点id */
  parentId: string;
  /** 档案路径 */
  achiPath?: null;
  /** 文件IDs `,`拼接 */
  fileIds?: null;
  /** 文件列表 */
  fileList: FileTreeFileItem[];
  fileSize?: null;
  fileStr?: null;
  /** 子节点列表 */
  children: FileTreeNode[];

  adminFlag: '0' | '1';
  adminId?: null;
  adminName?: null;
  allowNull: '0' | '1';
  createId?: null;
  createTime: '2022-03-21T06:19:08.000+0000';
  deleteFlag: '0' | '1';
  deleted: boolean;
  deportIds?: null;
  deptList?: null;
  downloadFlag: '0' | '1';
  jurisdictionListction?: null;
  labelIds?: null;
  lookFlag: '0' | '1';
  modify: true;
  name: string;
  projectCode?: null;
  /** 排序 */
  sort?: null;
  status?: null;
  updateId?: null;
  updateTime: '2022-03-21T06:19:08.000+0000';
  uploadFlag: '0' | '1';
  userIds?: null;
  userList?: null;
}

export interface FileTreeFileItem {
  id: string;
  /** 文件名 */
  fileName: string;
  /** 文件大小 */
  fileSize: number;
  /** 文件类型 (后缀名) */
  fileType: string;
  /** 文件url */
  fileUrl: string;

  achievementName?: null;
  browseNum: 0;
  collectionFlag?: null;
  collectionId?: null;
  createId: '61fe30e6ffb64e7288d9f2cdb79e6658';
  createTime: '2021-11-04 05:58:36';
  deleteFlag: '0' | '1';
  deleted: boolean;
  downloadFlag: '0' | '1';
  fileCode?: null;
  fileImage?: null;
  fileNo?: null;

  labelIds?: null;
  lookFlag: '0' | '1';
  modify: true;
  parentAchievementId?: null;
  updateId?: null;
  updateTime: '2021-11-04 05:58:40';
  uploadFlag: '0' | '1';
}
