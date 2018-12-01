var wul_uploader;	//上传控件
var wul_fileMd5 = "";	//文件md5
var wul_size = 0;	//文件大小
var wul_fileName = "";	//文件名称
var wul_chunk = 0;	//切片上传数
WebUploader.Uploader.register({
    "before-send-file": "beforeSendFile",
    "before-send": "beforeSend",
    "after-send-file": "afterSendFile",
}, {
    beforeSendFile: function (file) {
        deferred = WebUploader.Deferred();
        wul_uploader.md5File(file, 0, 1 * 1024 * 1024)
            .then(function (val) {
                wul_fileMd5 = val;
                wul_size = file.size;
                wul_fileName = file.name;
                timestamp = Date.parse(new Date()) / 1000;
                signParam = "{chunkSize=" + chunkSize + ", fileMd5=" + wul_fileMd5 + ", size=" + wul_size + ", timestamp=" + timestamp + "}";
                sign = Base64.encode(CryptoJS.HmacSHA1(signParam, securityKey));
                // 获取断点续传位置
                jQuery.ajax({
                    type: "POST",
                    // 测试
                    url: checkUrl,
                    data: {
                        // 文件大小
                        size: wul_size,
                        // 文件唯一标记
                        fileMd5: wul_fileMd5,
                        // 切片大小
                        chunkSize: chunkSize,
                        // 签名
                        sign: sign,
                        // 应用分配id
                        appId: appId,
                        // 当前时间戳
                        timestamp: timestamp

                    },
                    dataType: "json",
                    // 上传失败
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        jQuery('#tip').find("p").text(wul_fileName + "上传失败...");
                    },
                    success: function (response) {
                        if (response.responseCode == 0) { // 切片获取成功
                            wul_chunk = response.chunk;
                            deferred.resolve();
                        } else { // 切片获取失败,请求成功
                            wul_uploader.cancelFile(file);	//取消文件上传
                            jQuery("#tip").find("p").text("切片检查失败,请联系管理员");
                            deferred.resolve();
                        }
                    }

                });
                return deferred.promise();
            });
        return deferred.promise();
    },
    beforeSend: function (block) {
        var deferred = WebUploader.Deferred();
        if (block.chunk < wul_chunk) {
            return deferred.reject();
        }
        wul_uploader.md5File(block.blob)
            .then(function (chunkMd5) {
                timestamp = Date.parse(new Date()) / 1000;
                signParam = '{chunk=' + block.chunk + ', chunkMd5=' + chunkMd5 + ', chunkSize=' + chunkSize + ', fileMd5=' + wul_fileMd5 + ', size=' + wul_size + ', timestamp=' + timestamp + '}';
                console.log(signParam);
                signTemp = CryptoJS.HmacSHA1(signParam, securityKey);
                sign = Base64.encode(signTemp);	//获取sign值
                wul_uploader.options.formData = {
                    'timestamp': timestamp,
                    'appId': appId,
                    'chunk': block.chunk,
                    'chunkSize': chunkSize,
                    'fileMd5': wul_fileMd5,
                    'chunkMd5': chunkMd5,
                    'size': wul_size,
                    'sign': sign
                };
                // console.log(wul_uploader.options.formData);
                deferred.resolve();
            })
        return deferred.promise();
    },
    afterSendFile: function (file) {
        timestamp = Date.parse(new Date()) / 1000;
        signParam = "{chunkSize=" + chunkSize + ", fileMd5=" + wul_fileMd5 + ", fileName=" + file.name + ", size=" + wul_size + ", timestamp=" + timestamp + "}";
        sign = Base64.encode(CryptoJS.HmacSHA1(signParam, securityKey));
        // 如果分块上传成功，则通知后台合并分块
        jQuery.ajax({
            type: "POST",
            url: mergeUrl,
            data: {
                appId: appId,
                fileMd5: wul_fileMd5,
                fileName: file.name,
                chunkSize: chunkSize,
                sign: sign,
                size: wul_size,
                timestamp: timestamp
            },
            success: function (response) {
                if (response.responseCode == 0) {
                    jQuery("#uri").find("p").text("uri:" + response.filePath);
                    jQuery('#tip').find("p").text("文件:" + wul_fileName + "上传成功");
                } else {
                    jQuery('#tip').find("p").text("文件:" + wul_fileName + "上传失败,失败原因:" + response.responseMsg);
                }

            }
        });
    }
});
//绑定事件
function wul_init() {
    //提示只能选择一个文件
    wul_uploader.on('filesQueued', function (files) {
        if (files.length > 1) {
            jQuery("#tip").find("p").text("请选择一张图片");
            for (i = 0; i < files.length; i++) {
                wul_uploader.cancelFile(files[i]);
            }
            wul_uploader.reset();
            wul_fileMd5 = "";
            wul_size = 0;
            wul_fileName = "";
            wul_chunk = 0;	//当前切片数
        }
    });

    //文件校验格式和大小
    wul_uploader.on('error', function (type) {
        if (type == 'Q_EXCEED_SIZE_LIMIT') {
            jQuery("#tip").find("p").text("文件超过128M");
        }
        if (type == 'Q_TYPE_DENIED') {
            jQuery("#tip").find("p").text("文件格式错误，请选择文件");
        }
    });

    //上传进度
    wul_uploader.on('uploadProgress', function (file, percentage) {
        jQuery("#progress").find("p").text("上传进度:" + percentage * 100 + "%");
    });

    //每次切片上传完成之后的判断
    wul_uploader.on('uploadAccept', function (object, ret) {
        if (ret.responseCode != 0) {
            wul_uploader.cancelFile(wul_uploader.getFiles()[0].id);
            jQuery("#tip").find("p").text(ret.responseMsg);
        }
    });

    wul_uploader.on('uploadBeforeSend', function (object, data, headers) {
        // console.log(object);
        console.log(data);
        // console.log(headers);
    });

}
// //初始化断点续传工具
jQuery(function () {
    console.log($("#basePath").val())
    wul_uploader = WebUploader.create({
        // swf文件路径
        swf:$("#basePath").val() + '/js/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: uploadUrl,
        // 定义选择按钮
        // pick: "#filePicker",
        pick: {
            "id": preview,
            "innerHTML": preveiwName
        },
        // 自动上传
        auto: auto, 
        // 禁止浏览器打开文件
        disableGlobalDnd: true,
        // 添加截图功能
        paste: '#wrapper',
        // 定义拖动面板
        dnd: '#wrapper',
        // 分片上传
        chunked: true,
        // 分片大小为2M
        chunkSize: chunkSize,
        // 分片上传失败重试次数
        chunkRetry: chunkRetry,
        // 图片不做压缩
        compress: false,
        // 队列设置10个,为了选择多个文件的时候能提示
        fileNumLimit: 10,
        // 提前准备好下一个文件
        prepareNextFile: true,
        // 限制单个文件大小
        fileSingleSizeLimit: sizeLimit,
        //线程数
        threads: 1,
        // 限制格式
        accept: {
            title: "access",
            extensions: ext
        }
    });
    wul_init();
    console.log(wul_uploader);
});

//初始化之后,动态修改webuploader限制
var option = function options(key, val) {
    wul_uploader.option(key, val);
    var options = wul_uploader.options;
    wul_uploader.destroy();	//注销uploader
    wul_uploader = WebUploader.create(options);
    wul_init();
    console.log(wul_uploader);
}

//文件开始上传
var start = function uploadFile() {
    if (wul_uploader.getFiles()[0] != null) {
        wul_uploader.upload(wul_uploader.getFiles()[0].id);
        jQuery("#tip").find("p").text("");
    } else {
        jQuery("#tip").find("p").text("请选择上传文件");
    }
}

//暂停文件上传
var stop = function stopUpload() {
    wul_uploader.cancelFile(wul_uploader.getFiles()[0].id);
};