/**
/**
 * Created by A on 2017/3/13.
 */
//var api_link="/api";//yyx
//var api_link="http://192.168.2.178:8085";//yyx
var api_link="http://121.199.23.67:8085";//线上
//var api_link="http://192.168.2.201:8085";//林通
//var api_link="http://192.168.2.223:8085"  //宝强
//var api_link="http://192.168.2.250:8085";  //yyz
//var api_link="http://192.168.2.224:8085"; //赵建彪
//var api_link="http://192.168.2.225:8085";  //巫地禄
//var api_link="http://192.168.2.122:8085";//鲍明
//var api_link="http://192.168.2.168:8085";//胡友伟
//var api_link="http://192.168.2.222:8085";//张书恒

 //检测是否为中文，true表示是中文，false表示非中文
function isChinese(str){
    if(/^[\u3220-\uFA29]+$/.test(str)){
         return true;
    }else{
    	return false;
    }
}
var ct_projectCode=$.cookie('projectCode')
var ct_orgId=$.cookie('orgId')
function queryParams(params){
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        rows: params.limit,   //页面大小
        page: params.offset/params.limit+1
    };
    return temp;
}
function getUrl(url){
	if(url.indexOf("?")==-1){
        var urls=url+"?projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&code="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }else{
        var urls=url+"&projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&code="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }
	 if(isChinese(urls)){
    	urls=encodeURI(urls)
    }
    return urls
}
function getUrl_nocode(url){
	if(url.indexOf("?")==-1){
        var urls=url+"?projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }else{
        var urls=url+"&projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }
    if(isChinese(urls)){
    	urls=encodeURI(urls)
    }
    return urls
}
function loadTable(name,url,columns,json){
    var urls=getUrl(url);
    $("#"+name).bootstrapTable({
        url:api_link+urls,
        ajaxOptions:{
            headers: {"authorization": $.cookie('token')||getQueryString("token")}
        },
        dataType:'json',
        method: 'get',      //����ʽ��*��
        striped: false,      //�Ƿ���ʾ�м��ɫ
        showExport: false,//��ʾ������ť
        exportDataType: "all",//��������
        pagination: true,     //�Ƿ���ʾ��ҳ��*��
        sortable: true,      //�Ƿ���������
        sortOrder: "asc",     //����ʽ
        queryParams:json?(json.queryParams?json.queryParams:queryParams):queryParams,
        sidePagination: "server",   //��ҳ��ʽ��client�ͻ��˷�ҳ��server����˷�ҳ��*��
        pageNumber:1,      //��ʼ�����ص�һҳ��Ĭ�ϵ�һҳ
        pageSize: 10,      //ÿҳ�ļ�¼������*��
        pageList: [10, 25, 50, 100],  //�ɹ�ѡ���ÿҳ��������*��
        search:false,    //�Ƿ���ʾ����������������ǿͻ������������������ˣ����ԣ����˸о����岻��
        searchOnEnterKey:true,
        strictSearch: false,//�Ƿ�����ȫ��ƥ��
        showHeader:true,
        showColumns: false,     //�Ƿ���ʾ���е���
        showRefresh: false,     //�Ƿ���ʾˢ�°�ť
        minimumCountColumns: 2,    //�������������
        //height: 500,      //�иߣ����û������height���ԣ�����Զ����ݼ�¼�������ñ��߶�
        showToggle:false,     //�Ƿ���ʾ��ϸ��ͼ���б���ͼ���л���ť
        cardView: false,     //�Ƿ���ʾ��ϸ��ͼ
        clickToSelect:true, //����true ���ڵ����ʱ���Զ�ѡ��rediobox �� checkbox
        singleSelect:json&&json.singleSelect,//��ѡ
        // detailView: false,     //
        columns:columns,
        onLoadSuccess: function(data){
            if(data.rows&&data.rows.length==0){
                $('.no-records-found>td').html("<div class='center fff'><img style='width:250px' src='/images/no-data.png' alt=''/></div>")
            }
            json&&json.onLoadSuccess&&json.onLoadSuccess(data);
        },
        onClickRow:json&&json.onClickRow,
        onLoadError: function(data){  //
        }

    });
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function getHttp_nocode(url,success,data){//��ȡ�б�
   var urls=getUrl_nocode(url);
   getHttp_no(urls,success,data)
}
function getHttp(url,success,data){//基本ajax请求
    var urls=getUrl(url);
   	getHttp_no(urls,success,data)
}
function getHttp_no(url,success,data){//基本ajax请求
	data=data?data:"";
    $.ajax({
        type: "post",
        url: api_link+url,
        data:data,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'json',
        success: function(msg){
            success(msg);
            //赵建彪写的页面是动态加载页面的  所以综合信息查看界面按钮是否要在这里判定
            
        }
    })
}
function getHttp_no_cover(url,success,data){//基本ajax请求
	data=data?data:"";
    $.ajax({
        type: "post",
        url: api_link+url,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'json',
        data:data,
        beforeSend:function(XMLHttpRequest){
            //alert('远程调用开始...');
            $(".cover_layer").removeClass("none");
        },
        success: function(msg){
            success(msg);
            //赵建彪写的页面是动态加载页面的  所以综合信息查看界面按钮是否要在这里判定
            projectLook&&$(".btn_").css("display","none");
        }
    });
}


function getHttp_no_xml(url,success){//基本ajax请求
    $.ajax({
        type: "post",
        url: api_link+url,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'text',
        success: function(xml){
            success(xml);
            //赵建彪写的页面是动态加载页面的  所以综合信息查看界面按钮是否要在这里判定
            projectLook&&$(".btn_").css("display","none");
        }
    });
}

function getHttp_haveData(url,success,data){//基本ajax请求
    $.ajax({
        type: "post",
        data:data,
        url: api_link+url,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'json',
        success: function(xml){
            success(xml);
            //赵建彪写的页面是动态加载页面的  所以综合信息查看界面按钮是否要在这里判定
            projectLook&&$(".btn_").css("display","none");
        }
    });
}

function getHttp_no_async(url,success){//基本ajax请求
    $.ajax({
        type: "post",
        url: api_link+url,
        async: false,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'json',
        success: function(msg){
            success(msg);
            //赵建彪写的页面是动态加载页面的  所以综合信息查看界面按钮是否要在这里判定
            projectLook&&$(".btn_").css("display","none");
        }
    });
}
//select框选择
function asyncAjax(url){
    var selects=[];
    $.ajax({
        type: "post",
        url: api_link+url,
        async:false,
        headers: { "authorization":$.cookie('token')||getQueryString("token")},
        dataType:'json',
        success: function(msg){
            selects=msg
        }
    })
    return selects
}

function sendHttp_nocode(url,success,data){//ajax操作时的封装
    var urls=getUrl_nocode(url);
    data=data?data:""
        $.ajax({
            type: "POST",
            url:api_link+ urls,
            data:data,
            headers: { "authorization":$.cookie('token')||getQueryString("token")},
            dataType:'json',
            success: function(msg){
                if(msg.status=="1"||msg.status=="success"){//�����ɹ�
                    layer.alert("操作成功！",{icon: 3, title:'提示'},function(){
                        success();
                        layer.close(layer.index)
                    });

                }else{
                    alert("操作失败("+msg.msg+")")
                }
            },
            error: function(error){
                alert("服务器响应失败!")

            }
        });
   
}
function sendHttp(url,success,data){//ajax操作时的封装
    var urls=getUrl(url);
    data=data?data:""
        $.ajax({
            type: "POST",
            url:api_link+ urls,
            data:data,
            headers: { "authorization":$.cookie('token')||getQueryString("token")},
            dataType:'json',
            async: false,
            success: function(msg){
                if(msg.status=="1"||msg.status=="success"){//�����ɹ
                   layer.alert("操作成功！",{icon: 3, title:'提示'},function(){
                        success();
                       layer.close(layer.index)
                    });

                }else{
                    alert("操作失败("+msg.msg+")")
                }
            },
            error: function(error){
                alert("服务器响应失败!")
            }
        });
    
}
function sendHttp_no(url,success,data){//ajax操作时的封装
    data=data?data:""
        $.ajax({
            type: "POST",
            url:api_link+ url,
            data:data,
            headers: { "authorization":$.cookie('token')||getQueryString("token")},
            dataType:'json',
            async: false,
            success: function(msg){
                if(msg.status=="1"||msg.status=="success"){//�����ɹ
                    layer.alert("操作成功！",{icon: 3, title:'提示'},function(){
                        success();
                        layer.close(layer.index);
                    });

                }else{
                    alert("操作失败("+msg.msg+")")
                }
            },
            error: function(error){
                alert("服务器响应失败!")

            }
        });
    
}

function yyzUrl(url){
    var url_yyz
    if(url.indexOf("?")==-1){
        url_yyz=api_link+url+"?projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&code="+ct_projectCode+"&projectID="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }else{
        url_yyz=api_link+url+"&projectCode="+ct_projectCode+"&projectcode="+ct_projectCode+"&code="+ct_projectCode+"&projectID="+ct_projectCode+"&projectId="+ct_projectCode+"&orgid="+ct_orgId+"&orgId="+ct_orgId+"&userid="+$.cookie('userId');
    }
    return url_yyz;
}

function getNowFormatDate() {  //获取当前时间。格式为yyyy-MM-dd hh:mm:ss
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}























function mapSelect(lng,lat){
    top.layer.open({
        type: 2 //此处以iframe举例
        ,title: '经纬度选择'
        ,area: ['1000px', '500px']
        ,shadeClose: false //点击遮罩关闭
        ,maxmin: true
        ,content: common_link+'map_select.html?lng='+$("#"+lng).val()+"&lat="+$("#"+lat).val()
        ,btn: ['确定', '关闭'],
        yes: function(index, layero){
            var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();

            top.layer.close(index);//关闭对话框。
            //if(flag){
            $("#"+lng).val(iframeWin.returnLongitude())
            $("#"+lat).val(iframeWin.returnLatitude())
            //}else{
            //$("#moreUser").val(iframeWin.returnUserName())
            //$(".moreUser").val(iframeWin.returnUserId())
            //}
        },
        cancel: function(index){
        }
    })
}

function alert_map(latId,lngId){
   	top.layer.open({
            title:"选择经纬度",
            type:2,
            content:common_link+"commonPages/mapBack.html?lat="+lat+"&lng="+lng,
            shade:0.2,
            area:["950px","500px"],
            btn: ['确定', '关闭'],
	        yes: function(index, layero){
	            var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
	
	            top.layer.close(index);//关闭对话框。
	            //if(flag){
	            $("[dataId='"+lngId+"']").val(iframeWin.lng)
	            $("[dataId='"+latId+"']").val(iframeWin.lat)
	            //}else{
	            //$("#moreUser").val(iframeWin.returnUserName())
	            //$(".moreUser").val(iframeWin.returnUserId())
	            //}
	        },
	        cancel: function(index){
	        }
        });
   }

//placehoder

