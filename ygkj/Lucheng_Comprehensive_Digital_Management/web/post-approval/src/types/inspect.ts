export interface InspectProject {
  address: string;
  appraisalResult: string;
  coordinator: string;
  coordinatorId: string;
  createId: string;
  createTime: string;
  delFlag: number;
  distributor: string;
  distributorPhone: string;
  hiddenDanger: number;
  id: string;
  inspector: string;
  inspectorId: string;
  legalRepresent: string;
  patrolBeginTime: string;
  patrolEndTime: string;
  patrolResult: number;
  phone: string;
  projectId: string;
  projectName: string;
  rectifyBeginTime: string;
  rectifyEndTime: string;
  rectifyName: string;
  rectifyNoticeTime: string;
  rectifyPeople: string;
  resultStatus: number;
  reviewResult: string;
  reviewer: string;
  reviewerEndTime: string;
  reviewerId: string;
  /** 当前项目阶段 1-开工 2-施工 3-完工 */
  stage: number;
  updateId: string;
  updateTime: string;
}
