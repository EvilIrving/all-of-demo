
import { createElement, useState } from "rax";
import View from 'rax-view';
import Text from 'rax-text';
import TextInput from "rax-textinput";
import Mgop from "./mgop";
import { Button } from "@alifd/next";
import Vconsole from "vconsole";
import "./css/index.css";


export default function jsBridgeDemo() {

   let vconsole = new Vconsole();
   
  return (
    <View>
       <Mgop /> 
    </View>
  );
}