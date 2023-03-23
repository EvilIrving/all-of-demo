/**
 * Created by A on 2016/7/30.
 */
///////////////////////按钮组切换//////////////////////////////////////////////////////////////////////
function btnsSwitch(me) {
    $(".btns>li.active").next("li").children("span").addClass("bordernone");
    $(me).siblings("li").removeClass("active");
    $(me).addClass("active");
    $(".btns>li>span").removeClass("bordernone");
    $(".btns>li.active").next("li").children("span").addClass("bordernone");
};

//取消冒泡
function cancelBubble(e) {
    e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();
    } else {
        e.cancelBubble = true;
    }
}

$(document).ready(function () {
    for (var i = 0; i < $(".container>.aside>ul>li").length; i++) {
        if ($($(".container>.aside>ul>li")[i]).children("ul").length > 0) {
            $($(".container>.aside>ul>li")[i]).children("a").append("<i></i>");
        }
    }
    ///////////////////////侧边栏伸缩//////////////////////////////////////////////////////////////////////
    $("button.show_hide").click(function () {
        $(this).siblings().toggleClass("none");
        $(this).toggleClass("hide");
        if ($(this).siblings("ul").hasClass("none")) {
            $("#threeLevelPage").width($(".container").width() - 15);
        } else {
            $("#threeLevelPage").width($(".container").width() - $(".aside").width() - 10)
        }
    });
    ///////////////////////侧边栏下拉//////////////////////////////////////////////////////////////////////
    $(".container>.aside>ul>li>a").click(function (event) {
        event ? event.preventDefault() : window.event.returnValue = false;
    });
    $(".container>.aside>ul>li>ul>li>a").click(function (event) {
        event ? event.preventDefault() : window.event.returnValue = false;
    })
    if ((".container>.aside>ul>li.active>ul").length > 0) {
        $(".container>.aside>ul>li.active>ul").slideDown('fast');
        $(".container>.aside>ul>li.active>a>i").addClass("gt");
    }
    $(".container>.aside>ul>li").click(function () {
        /* for(var i=0;i<$(".container>.aside>ul>li").length;i++){
                $( $(".container>.aside>ul>li")[i]).children('img').attr("src",  $( $(".container>.aside>ul>li")[i]).children('.ico').attr("src").replace("d.png",".png"));
            }
            $(this).children('img').attr("src", $(this).children('img').attr("src").replace(".png","d.png"));*/
        $(this).siblings("li").removeClass("active");
        $(this).siblings("li").children("ul").slideUp('fast');
        $(this).addClass("active");
        $(this).siblings("li").children("a").children("i").removeClass("gt");
        if ($(this).children("ul").length > 0) {
            if ($(this).children("ul").css("display") != "none") {
                $(this).children("a").children("i").removeClass("gt");
            } else {
                $(this).children("a").children("i").addClass("gt");
            }
            $(this).children("ul").slideToggle("fast");
        } else {
            $("#threeLevelPage").attr('src', $(this).children("a").attr("href"))
        }
    });
    $(".container>.aside>ul>li>ul>li").click(function (event) {
        event ? event.stopPropagation() : window.event.cancelBubble = true;
        $(this).siblings("li").removeClass("active");
        $(this).addClass("active");
        $("#threeLevelPage").attr('src', $(this).children("a").attr("href"));
    })
    ///////////////////////下拉框设定/////////////////////////////////////////////////////////////////////////////////////////
    $(".select>p").click(function () {
        $(this).siblings(".s_content").slideToggle("fast");
        $(this).children("b").toggleClass("bottom")
    })
    $(".select>.s_content>li").click(function () {
        $(this).parent().slideUp("fast").siblings("p").children("span").html($(this).html());
        $(this).parent().parent().children("p").children("b").removeClass("bottom")
    })
    /////////////////////////////////工程类型选择效果///////////////////////////////////////////////////////
    $(".project_type.t_content td>ul>li").click(function () {
        $(this).toggleClass("active").siblings("li").removeClass("active")
    })
    /////////////////////////////////pdf列表切换//////////////////////////////////////////////////////
    $(".pdf_aside>ul>li").click(function () {
        $(this).addClass("active").siblings(".active").removeClass("active");
    });
    // /////////////////////////organi中岗位培训的导航栏切换和数据刷新 /////////////////////////
    $(".fHeader>li").click(function () {
        $(".fHeader>li>span").removeClass("transparent")
        $(this).addClass("active").siblings(".active").removeClass("active")
        $(this).children("span").addClass("transparent")
        if ($(this).prev() != '') {
            $(this).prev().children("span").addClass("transparent")
        }
        $($(this).attr("data-statistics")).removeClass("none").siblings("div").addClass("none")
        if ($(this).attr("data-statistics") == "#yearStatistics") {
            com_datagrid_deail("ftable1");
        }
        $("[text-anchor='end']").html("")
        $(".footer .highcharts-title").css("font-weight", "bold")
    });
    //////////////////////////流程表单////////////////////////////////////////////
    $(".process_content>p>b").click(function () {
        $(this).parent().parent().toggleClass("active");
        $(this).parent().siblings("div").slideToggle();
        $(this).toggleClass("open");
        if ($(this).hasClass('open')) {
            $(this).html('收起')
        } else {
            $(this).html('展开')
        }
    })
    //////////////////////////自适应///////////////////////////////////////////////////////////////////////////////////
    $(window).resize(function () {
        if (!!window.ActiveXObject || "ActiveXObject" in window) {
            self_ie()
        } else {
            self();
        }
    });
    if (!!window.ActiveXObject || "ActiveXObject" in window) {
        self_ie()
    } else {
        self();
    }


    // ajax全局错误事件处理
    $.ajaxSetup({
        type: "POST",
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.status);
            switch (jqXHR.status) {
                case(500):
                    showMsg("服务器系统内部错误");
                    break;
                case(401):
                    showMsg("请先登录");
                    break;
                case(301):
                    window.location.href="/";
                    break;
                case(403):
                    showMsg("无权限执行此操作");
                    break;
                case(408):
                    showMsg("请求超时");
                    break;
                case(404):
                    showMsg("找不到该请求地址");
                    break;
                default:
                    showMsg("未知错误");
            }
        }
    });

});

function showMsg(msg) {
    if ($.messager) {
        $.messager.alert('消息', msg, 'error');
    } else {
        alert(msg);
    }
}

function self() {
    //$("body").width($(window).width());
    if ($(".ztree .button.chk").length > 0) {
        $(".ztree li a").css("padding-left", "20px")
    }
    if ($(".aside_top").css("display") == "none") {
        $(".aside>ul").css("height", "100%");
    } else {
        $(".aside>ul").css("height", $(".aside").height() - $(".aside_top").height() - 20 + "px");
    }
    ;
    $("#threeLevelPage").width($(".container").width() - $(".aside").width() - 10);//三级页面宽度

    for (var i = 0, num = 0; i < $(".ztree_header").length; i++) {
        if ($($(".ztree_header")[i]).css("display") != "none") {
            num++;
        }
    }
    $(".ztree_main").css("width", $("body").width() - num * ($(".ztree_header").width() + 15) + "px");////三级页面中ztree主页面宽度
    $(".ztree_main>iframe").height($(".ztree_main").height() - 5)
    if ($(".test").parent().hasClass("ztree_header")) {
        $(".test").height($(".ztree_header").height() - 30)
    }
    if ($(".top").length == 0) {
        $(".pdf_body").height($("body").height() - 15);
        $(".main").css("height", $("body").height() + "px");//三级页面中主页面高度
    } else {
        if ($(".pdf_body").siblings(".top").css("display") == "none") {
            $(".pdf_body").height($("body").height() - 10);
        } else {
            $(".pdf_body").height($("body").height() - 81);
        }
        $(".main").css("height", $("body").height() - parseInt($(".top").css("height")) - 10 + "px");//三级页面中主页面高度
        $(".main>#or_main").height('98%')
    }
    $(".pdf").width($(".pdf_body").width() - 160 - 11);
    /*$(".pdf").width($(".pdf_body").width() - $(".pdf_aside").width()-11);*/
    $(".edit>Wdate").css("width", "100%");
    for (var i = 0; i < $(".isUnsafe").length; i++) {
        $($(".isUnsafe>.line_qiu")[i]).height($($(".isUnsafe")[i]).height());
    }
}

function self_ie() {
    //$("body").width($(window).width());
    if ($(".ztree .button.chk").length > 0) {
        $(".ztree li a").css("padding-left", "20px")
    }
    if ($(".aside_top").css("display") == "none") {
        $(".aside>ul").css("height", "100%");
    } else {
        $(".aside>ul").css("height", $(".aside").height() - $(".aside_top").height() - 20 + "px");
    }
    ;
    $("#threeLevelPage").width($(".container").width() - $(".aside").width() - 10);//三级页面宽度

    for (var i = 0, num = 0; i < $(".ztree_header").length; i++) {
        if ($($(".ztree_header")[i]).css("display") != "none") {
            num++;
        }
    }
    $(".ztree_main").css("width", $("body").width() - num * ($(".ztree_header").width() + 10) + "px");////三级页面中ztree主页面宽度
    $(".ztree_main>iframe").height($(".ztree_main").height() - 5)
    if ($(".test").parent().hasClass("ztree_header")) {
        $(".test").height($(".ztree_header").height() - 30)
    }
    if ($(".top").length == 0) {
        $(".pdf_body").height($("body").height() - 15);
        $(".main").css("height", $("body").height() - 10 + "px");//三级页面中主页面高度
    } else {
        if ($(".pdf_body").siblings(".top").css("display") == "none") {
            $(".pdf_body").height($("body").height() - 10);
        } else {
            $(".pdf_body").height($("body").height() - 81);
        }
        $(".main").css("height", ($("body").height() - parseInt($(".top").css("height")) - 10) - 10 + "px");//三级页面中主页面高度
        $(".main>#or_main").height('98%')
    }
    $(".pdf").width($(".pdf_body").width() - 160);
    /*$(".pdf").width($(".pdf_body").width() - $(".pdf_aside").width()-21);*/
    $(".edit>Wdate").css("width", "98%");
}

function img_click() {
    $(".group").click(function (e) {
        var hrefs = []
        e.preventDefault();
        $(".group").each(function (i, v) {
            hrefs.push($(v).attr("href"));
        })
        top.alert_group(hrefs, $(this).index());
    })
}

//搜索框添加回车事件
key_search()

function key_search() {
    $("input.search").keydown(function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e.keyCode == 13) {
            $(this).attr("changeFun") && eval($(this).attr("changeFun"))
        }
    })
}


////////////////////////////////////表格数据显示//////////////////////////////////////////////////
function DatagridSet(obj, dataCount) {
    var p = obj.datagrid('getPager');
    if (p.data("pagination")) {
        var pagesize = p.data("pagination").options.pageSize;
        var pagelist = p.data("pagination").options.pageList;
        if (pagelist && pagelist != []) {

        } else {
            pagelist = [5, 10, 20, 50];
        }
        $(p).pagination({
            pageList: pagelist,
            showPageList: true,
            links: 5,
            pageSize: pagesize,//每页显示的记录条数，默认为10
            layout: ['list', 'sep', 'first', 'prev', 'links', 'next', 'last'],
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示 {from} - {to} 条   共 {total} 条'
        });
    }

//    var pagesize = obj.datagrid('getPager').data("pagination").options.pageSize;
    var datagrid = obj.closest(".datagrid");
    var view = datagrid.find(".datagrid-view");
    if (dataCount == 0) {
        if (datagrid.find(".nodata-box").length == 0)
            view.after("<div class='nodata-box'><p class='nodata'></p></div>");
    } else {
        datagrid.find(".nodata-box").remove();
    }
    if (dataCount <= pagesize) {
        datagrid.find(".datagrid-pager table").hide();
        $(".datagrid-pager.pagination .pagination-info").css("top", "0px")
    } else {
        datagrid.find(".datagrid-pager table").show();
        $(".datagrid-pager.pagination .pagination-info").css("top", "5px")
    }
    datagrid.find(".ico_noread").parents(".datagrid-row").addClass('bold');
    setTimeout(function () {
        obj.datagrid("resize", function () {
        });
    }, 50);
}

$(document).ready(function () {
    $(".datagrid-header-check").append("<b onclick='$(this).siblings(\"input\").click()' style=\"font-size:12px\">全选</b>");
});

function autoWidth(id) {
    var iNumArr = [];
    var oUl = document.getElementById(id);
    var aLi = oUl.getElementsByTagName("li");
    for (var i = 0; i < aLi.length; i++) {
        var str = aLi[i].innerHTML;
        var iNum = str.split("").length;
        iNumArr.push(iNum);
    }
    //alert(iNumArr);
    iNumArr.sort(function (n1, n2) {
        return n2 - n1
    });
    //alert(iNumArr)
    oUl.style.width = iNumArr[0] * 16 + 20 + "px";
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "H+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

function getLength(val, long) {
    var len = 0;
    var v = val;
    for (i = 0; i < v.length; i++) {
        if (v.charCodeAt(i) > 256) {
            len += 2;
        } else {
            len++;
        }
        if (len > long) {
            v = v.slice(0, i - 1) + "...";
            return v
        }
    }
    return v
}

function getLength2(val, long) {
    var len = 0;
    var v = val;
    for (i = 0; i < v.length; i++) {
        if (v.charCodeAt(i) > 256) {
            len += 2;
        } else {
            len++;
        }
        if (len > long) {
            v = v.slice(0, i - 1);
            return v
        }
    }
    return v
}

$.fn.serializeJson = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (this.value) {
            if (o[this.name]) {
                o[this.name] = o[this.name] + ',' + this.value || '';
            } else {
                o[this.name] = this.value || '';
            }
        }
    });
    return o;
}

// 新版用户选择带回控件
function userLookUpNew(_name, _id, _singleSelect) {
    $.dialog({
        id: "userLookUpNew",
        title: '用户选择',
        //content : "url:<c:url value='/management/lookup/orgUserTree'/>",
        content: "url:management/lookup/orgUserTree",
        max: false,
        min: false,
        width: 800,
        height: 350,
        icon: 'succeed.gif',
        lock: true,
        data: {
            nameId: _name, // 名字对应的input id
            valueId: _id, // 值对应的input id
            nameField: "nickname",		// 名字对应的dataGrid field
            valueField: "id",			// 值对应的dataGrid field
            checkedIds: $("#" + _id).val(), // 选择值
            checkedNames: $("#" + _name).val(),
            singleSelect: _singleSelect,			// 是否多选
            callBack: function (names, values, datas) {			// 回调函数
                //console.log(names);
            }
        }
    });
}

// 岗位选择带回控件
function jobLookUp(_id, _name, orgId, code) {
    $.dialog({
        id: "jobLookUp",
        title: '岗位选择',
        //content : "url:<c:url value='/management/lookup'/>",
        content: "url:management/lookup",
        max: false,
        min: false,
        width: 800,
        height: 350,
        icon: 'succeed.gif',
        lock: true,
        data: {
            nameId: _name, // 名字对应的input id
            valueId: _id, // 值对应的input id
            nameField: "workName", // 名字对应的dataGrid field
            valueField: "id", // 值对应的dataGrid field
            checkedIds: $("#" + _id).val(), // 选择值
            checkedNames: $("#" + _name).val(),
            singleSelect: false, // 是否多选
            callBack: function (names, values, datas) { // 回调函数
                //console.log(datas);
            },
            dataUrl: "job/listJson?projectId=" + code + "&orgId=" + orgId, // dataGrid数据地址
            columns: [ // dataGrid列配置
                [
                    {field: 'id', checkbox: true},
                    {field: 'workName', title: '岗位名称', width: "90%"}
                ]]
        }
    });
}

//创建下拉框map
function createSelectMap(selectId) {
    var selectDom = document.getElementById(selectId);
    var selectMap = {};
    var options = selectDom.options;
    for (var i = 0, len = options.length; i < len; i++) {
        var option = options[i];
        selectMap[option.value] = option.text;
    }
    return selectMap;
}

//设置日期为当前日期
function initDate(type) {
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
    if (type == "year") return date.getFullYear();
    if (type == "time") return date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
    return date.getFullYear() + seperator1 + month + seperator1 + strDate;

}

function isEmpty(val) {
    if ((val == null || typeof (val) == "undefined" || val == "undefined" || val == "null") || (typeof (val) == "string" && val == "" && val != "undefined" && val != "null")) {
        return "-";
    } else {
        return val;
    }
}


//上传文件
function loadUpload() {
    $(".upload_").each(function (i, v) {
        var id = $(this).attr('data-id');
        var dataId = id + "File";
        var acceptMime = $(this).attr('acceptMime') || "";
        //var fieldName=$(this).attr('fieldName')
        var type = $(this).attr('type');
        var multiple = $(this).attr('multiple') ? true : false;
        upload.render({ //允许上传的文件后缀
            elem: '#' + dataId,
            url: _ctx + '/upload',
            accept: 'file', //普通文件
            field: 'uploadFile',
            multiple: multiple,
            acceptMime: acceptMime,
            size: (acceptMime.indexOf("video") == -1 ? 102400 : 1024000),//kb
            exts: type,
            before: function (res) {
                $("#" + id).after("<img src='" + _ctx + "/styles/images/images/loading.gif' style='margin-left: 20px;width: 25px;height: 25px;'>");
            },
            done: function (res) {
                $("#" + id + "+img").remove();
                var icon = "";
                if (res.ext == "pdf") {
                    icon = "pdfIcon";
                } else if (res.ext == "docx" || res.ext == "doc") {
                    icon = "wordIcon";
                } else if (res.ext == "xlsx" || res.ext == "xls") {
                    icon = "excelIcon";
                } else {
                    icon = "";
                }

                // excel和word不预览
                var nameHtml = ''
                if(res.ext == "docx" || res.ext == "doc" || res.ext == "xlsx" || res.ext == "xls"){
                    nameHtml = '<i>' + res.originalName + '</i>'
                }else{
                    nameHtml = '<i onclick="lookFile(\'' + res.dataId + '\')">' + res.originalName + '</i>'
                }
                if (multiple) {
                    $("#" + id + "List").append(
                        '<li class="' + icon + '" dataId="' + res.dataId + '" >'
                        + nameHtml
                        + '<span><b class="upload_det" onclick="deleteFile(this)"></b></span></li>');
                } else {
                    $("#" + id + "List").html(
                        '<li class="' + icon + '" dataId="' + res.dataId + '" >'
                        + nameHtml
                        + '<span><b class="upload_det" onclick="deleteFile(this)"></b></span></li>');
                }

                setFilePath(id);
                if (id == "msgfile")///messagenew_form.jsp多了个msgfilename附件名称 在此赋值
                    $("." + id + "name").val(res.originalName);
                //$("#" + id).attr("value", res.dataId);  //给form框fileId赋值
            },
            error: function (index, upload) {
                $("#" + id + "+img").remove();
            }
        });
    })
}

function deleteFile(obj) {
    var dataId = $(obj).parent().parent().parent().attr("data-id");
    $(obj).parent().parent().remove();
    setFilePath(dataId);
    var e = window.event || arguments.callee.caller.arguments[0];
    e.preventDefault ? e.preventDefault() : e.returnValue = false;
    cancelBubble(e);
}

function setFilePath(id) {
    var path = [];
    $("#" + id + "List li").each(function (index, item) {
        path.push($(item).attr("dataId"));
    });
    $("." + id + "").val(path.join(","));
    $("#" + id + "").val(path.join(","));
}