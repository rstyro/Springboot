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
            String decodeAesKey = RsaUtils.decodeBase64ByPrivate(keyConfig.getRsaPrivateKey(), encodeAesKey);
            System.out.println("decodeKey=" + decodeAesKey);
            InputStream body = httpInputMessage.getBody();
            String encodeAesContent = new BufferedReader(new InputStreamReader(httpInputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println("encodeAesContent=" + encodeAesContent);
            String aesDecode = AesUtils.decodeBase64(encodeAesContent, decodeAesKey);
            System.out.println("aesDecode=" + aesDecode);
            if (!StringUtils.isEmpty(aesDecode)) {
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
