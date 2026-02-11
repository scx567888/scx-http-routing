package dev.scx.http.routing.handler;

import dev.scx.http.routing.RoutingContext;
import dev.scx.http.exception.NotFoundException;
import dev.scx.http.media_type.FileFormat;
import dev.scx.http.media_type.ScxMediaType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static dev.scx.http.headers.HttpHeaderName.ACCEPT_RANGES;
import static dev.scx.http.headers.HttpHeaderName.CONTENT_RANGE;
import static dev.scx.http.media_type.MediaType.*;
import static dev.scx.http.status_code.HttpStatusCode.PARTIAL_CONTENT;

/// StaticHelper
///
/// @author scx567888
/// @version 0.0.1
public class StaticHelper {

    public static void sendStatic(File file, RoutingContext context) throws IOException {
        var request = context.request();
        var response = context.response();

        //参数校验
        var notExists = !file.exists();
        if (notExists) {
            throw new NotFoundException();
        }
        //获取文件长度
        var fileLength = file.length();

        //1, 通知客户端我们支持 分段加载
        response.headers().set(ACCEPT_RANGES, "bytes");

        //2, 尝试解析 Range
        var range = request.headers().range();

        //3, 设置 contentType (只有在未设置的时候才设置)
        if (response.contentType() == null) {
            var contentType = getMediaTypeByFile(file);
            response.contentType(contentType);
        }

        //3, 如果为空 则发送全量数据
        if (range == null) {
            response.send(file);
            return;
        }

        //4, 尝试解析
        //目前我们只支持单个的部分请求
        //获取第一个分段请求
        var start = range.getStart();
        var end = range.getEnd(fileLength);

        //计算需要发送的长度
        var length = end - start + 1;

        //我们需要构建如下的结构
        // status: 206 Partial Content
        response.statusCode(PARTIAL_CONTENT);
        // Content-Range: bytes 0-1023/146515
        response.setHeader(CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);
        // Content-Length: 1024
        response.contentLength(length);
        //发送
        response.send(file, start, length);

    }

    public static ScxMediaType getMediaTypeByFile(File file) {
        var fileFormat = FileFormat.findByFileName(file.getName());
        if (fileFormat == null) {
            fileFormat = FileFormat.BIN;
        }
        var mediaType = fileFormat.mediaType();
        var contentType = ScxMediaType.of(mediaType);
        if (mediaType == TEXT_PLAIN || mediaType == TEXT_HTML || mediaType == APPLICATION_XML || mediaType == APPLICATION_JSON) {
            contentType.charset(StandardCharsets.UTF_8);
        }
        return contentType;
    }

}
