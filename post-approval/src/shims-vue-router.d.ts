/* eslint-disable */
import { RouteLocationRaw } from 'vue-router';
export interface RouteMeta extends Record<string | number | symbol, any> {}
declare module 'vue-router' {
  export interface RouteMeta {
    title?: string;
    showNav?: boolean;
  }
}
