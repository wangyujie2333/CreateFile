package com.idea.plugin.translator.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idea.plugin.utils.HttpUtil;
import com.idea.plugin.utils.JsonUtil;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 百度翻译
 *
 * @author wangyuj
 * @date 2022/04/18
 */
public class BaiduTranslator extends AbstractTranslator {
    private static final Logger LOGGER = Logger.getInstance(BaiduTranslator.class);

    private static final String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate?from=auto&to=auto&appid=%s&salt=%s&sign=%s&q=%s";
    private static final String appId = "20180530000169357";
    private static final String token = "VSm4LcHQoVxpfYAcL4nK";

    @Override
    public String translateEn2Ch(String text) {
        try {
            return get(text);
        } catch (Exception e) {
            LOGGER.error("请求百度翻译接口异常", e);
            return StringUtils.EMPTY;
        }
    }

    @Override
    public String translateCh2En(String text) {
        try {
            return get(text);
        } catch (Exception e) {
            LOGGER.error("请求百度翻译接口异常", e);
            return StringUtils.EMPTY;
        }
    }

    private String get(String text) throws InterruptedException {
        String result = "";
        for (int i = 0; i < 5; i++) {
            String salt = RandomStringUtils.randomNumeric(16);
            String sign = DigestUtils.md5Hex(appId + text + salt + token);
            String eText = HttpUtil.encode(text);
            BaiduResponse response = JsonUtil.fromJson(HttpUtil.get(String.format(URL, appId, salt, sign, eText)), BaiduResponse.class);
            if (response == null || "54003".equals(response.getErrorCode())) {
                Thread.sleep(500);
            } else {
                result = Objects.requireNonNull(response).getTransResult().get(0).getDst();
                break;
            }
        }
        return result;
    }

    public static class BaiduResponse {
        @JsonProperty("error_code")
        private String errorCode;
        @JsonProperty("error_msg")
        private String errorMsg;
        private String from;
        private String to;
        @JsonProperty("trans_result")
        private List<TransResult> transResult;

        public void setFrom(String from) {
            this.from = from;
        }

        public String getFrom() {
            return from;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTo() {
            return to;
        }

        public void setTransResult(List<TransResult> transResult) {
            this.transResult = transResult;
        }

        public List<TransResult> getTransResult() {
            return transResult;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

    public static class TransResult {

        private String src;
        private String dst;

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }

        public String getDst() {
            return dst;
        }

    }
}
