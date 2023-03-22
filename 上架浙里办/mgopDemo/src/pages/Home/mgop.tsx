import { createElement, useState } from 'rax';
import View from 'rax-view';
import Text from 'rax-text';
import { mgop } from '@aligov/jssdk-mgop';
import TextInput from 'rax-textinput';

function Mgop() {

    const [apiName,setApiName] = useState('');
    const [type,setType] = useState('');
    const [appKey,setAppkey] = useState('');
    const [param,setParam] = useState('');
    const [result,setResult] = useState('');
    const [headerParam,setHeaderParam] = useState('');

    const RPCApiTest = () => {

        var paramData;
        var headerData ;
        if (null !== headerParam && "" !== headerParam && undefined !== headerParam) {
            headerData = JSON.parse(headerParam);
        } else {
            headerData = {};
        }

        if (null !== param && "" !== param && undefined !== param) {
            paramData = JSON.parse(param);
        } else {
            paramData = {};
        }

        console.log("调试入参为:" + param);
        console.log("header为:" + headerParam);
        console.log("请求方法为:" + type);
        
        mgop({ 
                api: apiName, // 必须   
                dataType: 'JSON',  
                type: type, 
                data: paramData,
                header: headerData,
                appKey: appKey, // 必须  
        onSuccess: data => {   
            setResult(JSON.stringify(data));   
            console.log("mgop run success. data :  " + JSON.stringify(data)); 
        },  
        onFail: err => {     
            setResult(JSON.stringify(err));  
            console.log("mgop run error. error :  " + JSON.stringify(err)); 
            }})
    }

  return (
    <View>
        <div>
            <h1 style={{textAlign:'center',color:'white'}}>mgop调用demo</h1>
            <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>请输入调试的API名称:</h5>
                   <TextInput value={apiName}
                              onChangeText={text => setApiName(text)}
                              style={{
                                width: 600,
                                height: 200,
                                borderWidth: 1,
                                borderColor: '#dddddd',
                                borderStyle: 'solid'
                            }}
                   ></TextInput>
                </div>
                <div>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                </div>
                <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>请选择方法类型:</h5>
                   <span>
                        <button onClick={() => setType('GET')}
                        >GET</button>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <button onClick={() => setType('POST')}>POST</button>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <button onClick={() => setType('PUT')}>PUT</button>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <button onClick={() => setType('DELETE')}>DELETE</button>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    </span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                </div>
                <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>请输入appKey:</h5>
                   <TextInput value={appKey}
                              onChangeText={text => setAppkey(text)}
                              style={{
                                width: 600,
                                height: 200,
                                borderWidth: 1,
                                borderColor: '#dddddd',
                                borderStyle: 'solid'
                            }}
                   ></TextInput>
                </div>
                <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>请输入请求头(如无自定义参数则不填):</h5>
                   <TextInput value={headerParam}
                              onChangeText={text => setHeaderParam(text)}
                              style={{
                                width: 600,
                                height: 200,
                                borderWidth: 1,
                                borderColor: '#dddddd',
                                borderStyle: 'solid'
                            }}
                   ></TextInput>
                </div>
                <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>请输入参数(JSON字符串):</h5>
                   <TextInput value={param}
                              onChangeText={text => setParam(text)}
                              style={{
                                width: 600,
                                height: 200,
                                borderWidth: 1,
                                borderColor: '#dddddd',
                                borderStyle: 'solid'
                            }}
                   ></TextInput>
                </div>
                <div>
                   <h5 style={{textAlign:'justify',color:'blue'}}>响应结果为:</h5>
                   <Text>{result}</Text>
                </div>
                <div>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                </div>
                <button onClick={() => RPCApiTest()}
                            style={{
                                width: 160,
                                height: 40,
                                backgroundColor: 'grey',
                                color: 'white',
                                fontSize: 8
                            }}  
                  >发送请求</button>
                  <div>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                </div>

        </div>
    </View>
  );
}

export default Mgop;
