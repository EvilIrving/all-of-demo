export interface UserInfo {
  username: string;
  timestamp: number;
  id: string;
  phone?: string;
  accessToken?: string;
  refreshToken?: string;

  deptId: string;
  deptName: string;
  roleName: string;
  roleNum: number;
  roles: any[];
  sex: string;
}
