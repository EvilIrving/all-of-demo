import { InjectionKey } from 'vue';
import { createStore, Store, useStore as baseUserStore } from 'vuex';
import { RootStateTypes } from '@/types/store';

export interface AllStateTypes extends RootStateTypes {
  [keys: string]: any;
}

export default createStore<RootStateTypes>({
  state: {},
  mutations: {},
  actions: {},
  modules: {}
});

export const key: InjectionKey<Store<RootStateTypes>> = Symbol('vue-store');

export function useStore<T = AllStateTypes>() {
  return baseUserStore<T>(key);
}
