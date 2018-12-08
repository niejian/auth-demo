<!DOCTYPE html>
<#assign ctx=request.contextPath />

<html>
<head>
    <script type="text/javascript" src="${ctx}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
    <script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.min.js"></script>
    <link href="${ctx}/plugin/bootstrap-table/bootstrap-table.css" rel="stylesheet"/>

    <script src="${ctx}/plugin/bootstrap-table/bootstrap-table.js"></script>
    <script src="${ctx}/plugin/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>



    <link rel="stylesheet" href="${ctx}/bootstrap/css/bootstrap.min.css">



</head>
    <button type="button" class="btn btn-primary">按钮</button><br/>
    <span id="time">当前时间：${date?string("yyyy-MM-dd HH:mm:ss")}</span>
    当前时间戳：${timestamp} inde/index

<section class="content">
    <div id="hiddenDiv">
        <input type="hidden" id="returnValue">
    </div>
    <div class="row">
        <!-- BEGIN SAMPLE TABLE PORTLET-->
        <div class="col-md-12">
            <!-- BEGIN SAMPLE TABLE PORTLET-->
            <div class="box-body" style="padding-bottom:0px;">
                <div class="panel panel-default">
                    <div class="panel-heading">模板消息群发</div>
                    <div class="panel-body">
                        <form id="formSearch" class="form-horizontal">
                            <div class="form-inline" style="margin-top:15px">
                                <label class="control-label">创建时间</label>
                                <input type="text" class="form-control" id="beginTime" autocomplete="off"
                                       style="width: 190px">
                                <label class="control-label">至</label>
                                <input type="text" class="form-control" id="endTime" autocomplete="off"
                                       style="width: 190px">

                                <label for="channelCode" style="margin-left: 14px">渠道</label>
                                <select class="selectpicker" id="channelCode" name="channelCode" style="width: 190px">
                                    <option value="">-----请选择-----</option>
                                </select>

                                <label for="msgStatus" style="margin-left: 14px">状态</label>
                                <select class="selectpicker" id="msgStatus" name="msgStatus" style="width: 190px">
                                    <option value="">-----请选择-----</option>
                                </select>
                            </div>
                            <div class="form-inline" style="margin-top: 15px">
                                <label class="control-label" for="title">标题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                <input type="text" class="form-control" id="title" style="width: 400px">
                                <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary" onclick="search()">查询
                                </button>
                                <button type="button" style="margin-left:50px" id="btn_reset" class="btn btn-danger">重置sdsd
                                </button>asas
                            </div>
                        </form>
                    </div>
                </div>

                <button type="button" class="btn btn-success">
                    <a href="#" onclick="viewMsg();">查看消息内容sd</a>
                </button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-success">
                    <a href="#" onclick="viewMsg();">查看消息内容sd</a>
                </button>
                <#--按钮-->
                <div class="btn-group" id="toolbar" style="margin-top: 15px">
                    <div class="btn-group" style="margin-left: 5px">
                        <button class="btn btn-primary" type="button">
                            <a href="#" onclick="createMsg();">创建群发
                            </a></button>

                        <button type="button" class="btn btn-success">
                            <a href="#" onclick="viewMsg();">查看消息内容</a>
                        </button>
                    </div>

                        <button type="button" class="btn btn-success">
                            <a href="#" onclick="viewMsg();">查看消息内容sd</a>
                        </button>

                    <div class="btn-group" style="margin-left: 5px">
                        <button type="button" class="btn btn-info">
                            <a href="#" onclick="view();">查看发送情况</a>
                        </button>
                        </button>
                    </div>
                    <div class="btn-group" style="margin-left: 5px">
                        <button type="button" class="btn btn-warning">
                            <a href="#" onclick="forceStop();">强制结束</a>
                        </button>
                    </div>
                    <div class="btn-group" style="margin-left: 5px">
                        <button type="button" class="btn btn-danger">
                            <a href="#" onclick="deleteMsg();">删除</a>
                        </button>
                    </div>

                </div>
                <#--数据展示-->
                <div class="table-scrollable" style="margin-top: 15px">
                    <table id="querylist" class="table table-striped"></table>
                </div>
            </div>

        </div>

    </div>

</section>

<style>
    a {
        color: white;
        cursor: pointer;

    }


</style>

<script>

    $(function(){
        var time = '${date?string("yyyy-MM-dd HH:mm:ss")}';
        var timestamp = '${timestamp}'

    });

    $('#querylist').bootstrapTable({
        columns: [{
            checkbox: true,
            align: "center"
        }, {
            title: '序号',//标题  可不加
            align: "center",
            formatter: function (value, row, index) {
                return index+1;
            }
        },{
            field: 'baseId',
            title: '主键Id',
            align: "center",
            visible: false
        },{
            field: 'msgBaseCode',
            title: '模板消息群发编号',
            align: "center"
        }, {
            field: 'channelName',
            title: '渠道',
            align: "center"
        },{
            field: 'channelCode',
            title: '渠道编号',
            align: "center",
            visible: false
        }, {
            field: 'title',
            title: '标题',
            align: "center"
        }, {
            field: 'msgSendAmount',
            title: '预计发送用户数',
            align: "center"
        }, {
            field: 'msgReceivedAmount',
            title: '已发送用户数',
            align: "center",
            formatter: ''
        }, {
            field: 'createTime',
            title: '创建时间',
            align: "center",
            formatter: ''
        },  {
            field: 'expectTime',
            title: '预估完成时间',
            align: "center",
            formatter: function (value, row, index) {


            }
        },{
            field: 'msgStatus',
            title: '状态',
            align: "center",
            formatter: function (value, row, index) {


            }
        }, {
            field: 'finishTime',
            title: '实际完成时间',
            align: "center",
            formatter: ''
        }, {
            field: 'lastUpdateBy',
            title: '操作人',
            align: "center"
        }, {
            field: 'lastUpdateTime',
            title: '操作时间',
            align: "center",
            formatter: ''
        }
        ],
        striped : true,
        clickToSelect: true,
        pagination: true,
        sidePagination: 'server',
        singleSelect: true,
        pageNumber: 1,
        pageSize: 15,
        pageList: [15, 20, 50, 100],
        cache: false,
        method: 'post',
        undefinedText: "",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                offset: params.offset,
                channelCode: $("#channelCode").val(),
                msgStatus: $("#msgStatus").val(),
                title: $("#title").val(),
                beginTime: $("#beginTime").val(),
                endTime: $("#endTime").val()
            }
        },
        url : '${root}/wxTemplateMsg/getWxTemplateMsgs'
        // onLoadSuccess: function () {
        //     var data = $('#querylist').bootstrapTable('getData', true);
        //     //console.log(data);
        //     var dataSize = data.length;
        //     for (i = 0; i < dataSize; i++) {
        //         //获取头像url
        //         var headImgUrl = data[i].headimgurl;
        //         var nickName = data[i].nickname;
        //         var headNickName = "";
        //         if (headImgUrl == "" || headImgUrl == null) {
        //             headNickName = (nickName == null) ? "" : nickName;
        //         } else {
        //             headNickName = '<div style="width:150px;"><img src=' + headImgUrl + ' width="50px" height="50px" style="margin-left: opx">' + '<span style="margin-left: opx">' + ((nickName == null) ? "" : nickName) + '</span></div>';
        //         }
        //         $('#querylist').bootstrapTable('updateRow', {
        //             index: i,
        //             row: {
        //                 nickname: headNickName
        //             }
        //         });
        //     }
        // }
    });


</script>
</html>