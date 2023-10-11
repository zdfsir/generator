package com.greedystar.generator.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSAUtil {

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCulH31QLov8QUJjPHqrpxT5Uj+LEnrXV9UG4I1NNsVFA+U9FD5jOTWrNz06ofq5qzihkT6dQK6H/22BCpBGJamqmyiq3s7ApzM2Wy4NzjHMDf3Hn8BEl9XxWmwsNFBv+JIbm6oA1HFVh8gt+shKjYAshXAkvfXXWVhE1x8huov4wIDAQAB";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK6UffVAui/xBQmM8equnFPlSP4sSetdX1QbgjU02xUUD5T0UPmM5Nas3PTqh+rmrOKGRPp1Arof/bYEKkEYlqaqbKKrezsCnMzZbLg3OMcwN/cefwESX1fFabCw0UG/4khubqgDUcVWHyC36yEqNgCyFcCS99ddZWETXHyG6i/jAgMBAAECgYAYNtxpofqxH5r1QiljxahFMq43Iul1zdOlFNOwkY45lDi9Dk4U7ufmpr9W8Ri9ChW8rabvnj8slbdZAabitIhghrmyReor1KPD8EnMwwD++Ui9KO+NtggVd6MZA+oLygAcgYBO27korc5kQvIMPZVQLzvwNilC0PI9ynYtUbraYQJBAOUCUosBClcGcWPT27FfeM9qrj1rqPB19jnJ8pcc3vcfsj4GirJ0zyOnlN4QMeVBrz9Il2KTnghCXGewnwt1l3kCQQDDJ+7rdl39U8OanESInZoD/D9s9+U9Z4IcBumq/rAPyh67u795+vjROAJSjWWtHO97lbIdJJrTjtxvLbOw9b87AkEAiq/qXyYlJejP+J3P2U4xMqOvm6C8cctubzbexCcG3HAEmM2LZ2GYJEaTwBvcq7Mot1F15IvzU5skrOLonp77OQJAddQZg98OtpIpiqMJGfcV84wMe21c4mspemSOFqj5gj8FuVGNNUfbjO6lMFSg7BmJWnOye7gLJuqdmx7b3F8z2wJBAMD7Z1F8wKfTrfGCaU5NK368dT0fVSZylo9RJAHK3WtL7qjFA1R/4x4Szo2lIvK3qWr6d55MecMgtuXNVE37hgI=";

    public static void main(String[] args) {
        // 生成一对公私钥
//        RSAUtil.genRSAKey();
        RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);
        String content = "{\"name\": \"张三\", \"id\": 123}";

        String encryptStr = RSAUtil.encrypt(rsa, content, KeyType.PublicKey);

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        String decryptStr = RSAUtil.decrypt(rsa, encryptStr, KeyType.PrivateKey);
    }

    /**
     * 加密
     *
     * @param rsa
     * @param content 加密的内容
     * @param keyType 解密key
     * @return String
     */
    public static String encrypt(RSA rsa, String content, KeyType keyType) {
        if (log.isDebugEnabled()) {
            log.debug(">>> [RSAUtil.encrypt] >>> key: {}, content: {}", keyType, content);
        }

        byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), keyType);
        String result = Base64.encode(encrypt);

        if (log.isDebugEnabled()) {
            log.debug(">>> [RSAUtil.encrypt] >>> key: {}, content: {}, \n加密结果: {}", keyType, content, result);
        }
        return result;
    }

    /**
     * 解密
     *
     * @param rsa
     * @param content 解密的内容
     * @param keyType 解密key
     * @return String
     */
    public static String decrypt(RSA rsa, String content, KeyType keyType) {
        if (log.isDebugEnabled()) {
            log.debug(">>> [RSAUtil.decrypt] >>> key: {}, content: {}", keyType, content);
        }

        byte[] decrypt = rsa.decrypt(Base64.decode(content), keyType);
        String result = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);

        if (log.isDebugEnabled()) {
            log.debug(">>> [RSAUtil.decrypt] >>> key: {}, content: {}, \n解密结果: {}", keyType, content, result);
        }
        return result;
    }

    /**
     * 生成一对公私钥
     *
     * @return Key
     */
    public static Key genRSAKey() {
        RSA rsa = new RSA();
        return new Key().setPublicKey(rsa.getPublicKeyBase64()).setPrivateKey(rsa.getPrivateKeyBase64());
    }



    @Data
    @Accessors(chain = true)
    @ApiModel(value = "RSAUtil.Key", description = "")
    public static class Key {
        private String publicKey;
        private String privateKey;
    }
}
