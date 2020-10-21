package top.lrshuai.encrypt.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.StringUtils;
import top.lrshuai.encrypt.config.KeyConfig;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encryption.AesUtils;
import top.lrshuai.encryption.RsaUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 解码处理
 * @author rstyro
 */
@Slf4j
public class DecodeInputMessage implements HttpInputMessage {

    private HttpHeaders headers;

    private InputStream body;

    public DecodeInputMessage(HttpInputMessage httpInputMessage, KeyConfig keyConfig) {
        // 这里是body 读取之前的处理
        this.headers = httpInputMessage.getHeaders();
        String encodeAesKey = "";
        List<String> keys = this.headers.get(Result.KEY);
        if (keys != null && keys.size() > 0) {
            encodeAesKey = keys.get(0);
        }
        try {
            // 1、解码得到aes 密钥
            String decodeAesKey = RsaUtils.decodeBase64ByPrivate(keyConfig.getRsaPrivateKey(), encodeAesKey);
            // 2、从inputStreamReader 得到aes 加密的内容
            String encodeAesContent = new BufferedReader(new InputStreamReader(httpInputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
            // 3、AES通过密钥CBC解码
            String aesDecode = AesUtils.decodeBase64(encodeAesContent, decodeAesKey, keyConfig.getAesIv().getBytes(), AesUtils.CIPHER_MODE_CBC_PKCS5PADDING);
            if (!StringUtils.isEmpty(aesDecode)) {
                // 4、重新写入到controller
                this.body = new ByteArrayInputStream(aesDecode.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
