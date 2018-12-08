<!DOCTYPE html>
<#--引入公共的css，js-->
<#include "common/base.ftl">
<html>
<head>


</head>
    <body>
        <section class="content">
            <div class="row">
                <!-- BEGIN SAMPLE TABLE PORTLET-->
                <div class="col-md-12">
                    <!-- BEGIN SAMPLE TABLE PORTLET-->
                    <div class="box-body" style="padding-bottom:0px;">
                        <div class="panel panel-default">
                            <div class="panel-heading">注册信息</div>
                            <div class="panel-body">
                                <form id="signup_form" class="form-horizontal">
                                    <div class="form-inline" style="margin-top:15px; width: 100%">
                                        <label class="control-label">用户昵称<span style="color: red">*</span></label>
                                        <input class="form-control required" type="text" style="width: 200px" name="username"/>
                                    </div>
                                    <div class="form-inline" style="margin-top:15px; width: 100%">
                                        <label class="control-label">邮箱<span style="color: red">*</span></label>
                                        <input class="form-control required" type="text" style="width: 200px" name="email"/>
                                    </div>
                                    <div class="form-inline" style="margin-top:15px; width: 100%">
                                        <label class="control-label">密码<span style="color: red">*</span></label>
                                        <input class="form-control required" type="password" style="width: 200px" name="pwd"/>
                                    </div>
                                    <div class="form-inline" style="margin-top:15px; width: 100%">
                                        <label class="control-label">确认密码<span style="color: red">*</span></label>
                                        <input class="form-control required" type="password" style="width: 200px" name="confirmpwd"/>
                                    </div>
                                </form>
                                <div class="form-actions">
                                    <div class="row">
                                        <div align="center">
                                            <button type="button" style="margin-left:50px" id="btn_submit" class="btn btn-success"">
                                            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;&nbsp;确定
                                            </button>
                                            <button type="button" style="margin-left:50px" id="btn_cancel" class="btn btn-warning">
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>&nbsp;&nbsp;取消
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </body>


<style>

</style>
<script>
    $(function () {
        $("#signup_form").bootstrapValidator({
            // live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
            // excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
            // submitButtons: '#btn_submit',//指定提交按钮，如果验证失败则变成disabled，但我没试成功，反而加了这句话非submit按钮也会提交到action指定页面
            // message: '通用的验证失败消息',//好像从来没出现过
            feedbackIcons: {//根据验证结果显示的各种图标
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            //校验字段
            fields: {
                username: {
                    message: '请输入用户名',
                    validators: {
                        notEmpty: {//检测非空,radio也可用
                            message: '请输入用户名'
                        },
                        stringLength: {
                            min: 4,
                            max: 8,
                            message: '用户名长度为4-8位'
                        },
                        regexp: {//正则验证
                            regexp: /^[a-zA-Z0-9_\.]+$/,
                            message: '用户名不符要求'
                        }
                        // emailAddress: {//验证email地址
                        //     message: '不是正确的email地址'
                        // },
                    }
                },


                pwd: {
                    message: '密码不能为空',
                    validators: {
                        notEmpty: {//检测非空,radio也可用
                            message: '密码不能为空'
                        },
                        stringLength: {
                            min: 4,
                            max: 20,
                            message: '密码长度不够'
                        },
                        different: {
                            field: 'username',
                            message: '密码不能与昵称相同'
                        }
                        // emailAddress: {//验证email地址
                        //     message: '不是正确的email地址'
                        // },
                    }
                },
                confirmpwd: {
                    message: '请输入确认密码',
                    validators: {
                        notEmpty: {
                            message: '请输入确认密码'
                        },
                        identical: {
                            field: 'pwd',
                            message: '两次输入的密码不同'
                        },
                        different: {
                            field: 'username',
                            message: '密码不能与昵称相同'
                        }
                    }
                },

                email: {
                    message: '邮箱不能为空',
                    validators: {
                        notEmpty: {//检测非空,radio也可用
                            message: '邮箱不能为空'
                        },

                        emailAddress: {//验证email地址
                            message: '请输入正确的email地址'
                        }
                    }
                }
            }
        });

        $("button[id='btn_submit']").click(function(){
            $("#signup_form").bootstrapValidator('validate');//提交验证
            if ($("#signup_form").data('bootstrapValidator').isValid()) {

            }
        });
    });
</script>

</html>

