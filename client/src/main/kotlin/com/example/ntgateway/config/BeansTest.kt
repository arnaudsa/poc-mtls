package com.example.ntgateway.config

class BeansTest {
}


//    @Bean
//    fun restTemplate(): RestTemplate {
//        val keyStore = KeyStore.getInstance("pkcs12")
//        val classPathResource = ClassPathResource("keystore/keystore-client.p12")
//
//        val inputStream = classPathResource.inputStream
//        keyStore.load(inputStream, "1234".toCharArray())
//
//        val socketFactory = SSLConnectionSocketFactory(
//            SSLContextBuilder.create()
//                .loadTrustMaterial(null, TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, "1234".toCharArray())
//                .build(),
//            NoopHostnameVerifier.INSTANCE
//        )
//
//        val httpClient = HttpClients.custom()
//            .setSSLSocketFactory(socketFactory)
//            .setMaxConnTotal(5)
//            .setMaxConnPerRoute(5)
//            .build()
//
//        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
//        requestFactory.setReadTimeout(10000)
//        requestFactory.setConnectTimeout(10000)
//
//        val restTemplate = RestTemplate()
//        restTemplate.requestFactory = requestFactory
//
//        return restTemplate
//    }


//    @Bean
//    fun restTemplate(certificateConfig: CertificateConfig): RestTemplate {
//        val certificate: X509Certificate = CertificateFactory
//            .getInstance("X.509")
//            .generateCertificate(certificateConfig.certificateStream()) as X509Certificate
//
//        val normalizedPrivateKey = privateKeyStr3
//            .replace(Regex(PEM_PRIVATE_START), "")
//            .replace(Regex(PEM_PRIVATE_END), "")
//            .replace(Regex("\\s"), "")
////
//        val decode = Base64.getDecoder().decode(normalizedPrivateKey)
//        println(decode.size)
//
//        val pbeKeySpec = PBEKeySpec("1234".toCharArray())
//        val pkInfo = EncryptedPrivateKeyInfo(decode)
//        val skf = SecretKeyFactory.getInstance("PBEWithHmacSHA1AndAES_256")
//        val pbeKey = skf.generateSecret(pbeKeySpec)
//
//        val cipher = Cipher.getInstance("PBEWithHmacSHA1AndAES_256")
//        cipher.init(Cipher.DECRYPT_MODE, pbeKey, pkInfo.algParameters)
//        val pkcs8KeySpec = pkInfo.getKeySpec(cipher)
//
//        val keyFactory = KeyFactory.getInstance("RSA")
//        val privateKey = keyFactory.generatePrivate(pkcs8KeySpec)
//
//        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//        keyStore.load(null)
//        keyStore.setKeyEntry("client", privateKey,null, arrayOf(certificate))
//
//        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//        trustManagerFactory.init(keyStore)
//
//        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
//        keyManagerFactory.init(keyStore, null)
//
//        val socketFactory = SSLConnectionSocketFactory(
//            SSLContextBuilder.create()
//                .loadTrustMaterial(keyStore, TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, null)
//                .build(),
//            NoopHostnameVerifier.INSTANCE
//        )
//
//        val httpClient = HttpClients.custom()
//            .setSSLSocketFactory(socketFactory)
//            .setMaxConnTotal(5)
//            .setMaxConnPerRoute(5)
//            .build()
//
//        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
//        requestFactory.setReadTimeout(10000)
//        requestFactory.setConnectTimeout(10000)
//
//        val restTemplate = RestTemplate()
//        restTemplate.requestFactory = requestFactory
//
//        return restTemplate
//    }

//    @Bean
//    fun restTemplate(): RestTemplate {
//        val keyStore = KeyStore.getInstance("pkcs12")
//        val classPathResource = ClassPathResource("keystore/keystore-client.p12")
//
//        val inputStream = classPathResource.inputStream
//        keyStore.load(inputStream, "1234".toCharArray())
//
//        val socketFactory = SSLConnectionSocketFactory(
//            SSLContextBuilder.create()
//                .loadTrustMaterial(null, TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, "1234".toCharArray())
//                .build(),
//            NoopHostnameVerifier.INSTANCE
//        )
//
//        val httpClient = HttpClients.custom()
//            .setSSLSocketFactory(socketFactory)
//            .setMaxConnTotal(5)
//            .setMaxConnPerRoute(5)
//            .build()
//
//        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
//        requestFactory.setReadTimeout(10000)
//        requestFactory.setConnectTimeout(10000)
//
//        val restTemplate = RestTemplate()
//        restTemplate.requestFactory = requestFactory
//
//        return restTemplate
//    }



//        println(certificate)
//        val pemParser = PEMParser(StringReader(privateKey2))
//        val enckeyPair = pemParser.readObject() as PEMEncryptedKeyPair
//        val keyPair = enckeyPair.decryptKeyPair(BcPEMDecryptorProvider("".toCharArray()))
//        val privateKeyInfo = keyPair.privateKeyInfo
//
//        val converter = JcaPEMKeyConverter()
//        val privateKey1 = converter.getPrivateKey(privateKeyInfo)
//        println(privateKey1)


//val privateKeyStr ="-----BEGIN PRIVATE KEY-----\n" +
//        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDg3ee1OpvVN1M5\n" +
//        "xD0fNz/4mi6Cdj5IAb5TquXfbsvx+uRbfiUdcZ/A0DnobKdu7Qy3Zz4oni8aw8O6\n" +
//        "RVxV4UgJmajFlMrs1KjEyPfeuZz6OugLwOSbs/zmEtpXYfief9CJxJXdJ1z4VI43\n" +
//        "+pLHzMt1Oegbs8i+TAOCsVH+bjnJQzU8QcD+CNvUpS7ckAuGziFuAew/MNCNFR7T\n" +
//        "466HazQ3N8698QI3BL6suTzjrvik9+oY9+GfxYZViZbUbvlGb9miH54itlgctc0x\n" +
//        "DTy86Ki+FqTCge1SJ7gPOaRGJf3Yqgk0857szshx/+3To8sM5zqeOc+OloAYXfRk\n" +
//        "JtqUfKG9AgMBAAECggEAUoFY+OfOMYchJDraak+tSxvXIMsdwvHQ0YYM3dQbRGfX\n" +
//        "zbIVEB6WjPpZpOvo6wxS9CAPcHvJsLpPYFL+tAv6kHsur4AOjKLiYYGTYMcdz7o5\n" +
//        "Vcaqmm6Jxij4r8Mko5MmAsZ8bi55MCxmGif4HxMHO7XJchCfASOGzjcdbIso7Kzq\n" +
//        "A7XzOEuE4oRQzudOHEDhGF43ZuS5BE8kCvieV9VMakueoB02EF5Hwav5ivnqCt1N\n" +
//        "eDfJLU5bv0dhXDiVB+TtKT4sgTcJ2oV9wKEEKDp/BkS7kYqV6LKyF8O6MZ49L0+L\n" +
//        "BXiiyVOF3edc8+sUIQO7uZeDeQIuer69njcRDNXpgQKBgQD2lJsc7Jc4pcbICMRp\n" +
//        "ZciXvk8BK5o7YpJPK+jy0FZHnwhUyO+tG/eYTJyLkhJCV+hck1hXaseCw0F/AFAB\n" +
//        "udIiVgyoUq9YgKmKZlw39nwn8Gott7zJ5LaO4i0vYiiEX/d9rDgQaSsusOjg5jc4\n" +
//        "pX/LGryfRMEcX5uFnKAyGvTHDQKBgQDpdPQtqGZnWcgdluBXE9oxdUeQVlbFGxfw\n" +
//        "s3L1SQu5IoBV6WGYEzu8ksqIfawTEHKqfCJ5Wl2VuiaHfacTpnyopWc9G0I5D7y1\n" +
//        "iS//tyvjZqRKWJ0oKX28fnkiUz0sVV2KsWI7WJUQ/5zzNrZvZeTMUEniNAMshYm6\n" +
//        "FLRA0uKZcQKBgHsPERZvUMz5luoPzg4XuPNaC822uAM5H3GZpWlA5j5aNOszWMIY\n" +
//        "+gks+teA79HiU1mYQ3dlF+q/jXlXa5Zg6WkvbqO2mEHf/DzQ9ucLAU1hY2wrjXFo\n" +
//        "zl8iq5BBOZ1O05IJl/VAt0dXDPHoktiXZ96yGj6WLpX9dP1KfHxlDZIlAoGBALW0\n" +
//        "es0CslmkrrYhFZGQ+RXfo4i7OqQFd3e4JDS32vEI4nx7vqM/7RHT6uGbdxALkEQT\n" +
//        "hD2R0R9VmOGCMkWAIqycuKL+EWgaUJW3Jm3Q4s8Gt77KA+AbkyrgAWZvBUD5XGoz\n" +
//        "sDJiuvtVCJLgtdQ0qYVAxJDUTNgdQ9pafQWrXgVxAoGAGBBmRfTpvaXV9Q9hgcGy\n" +
//        "UY2DXj/KhvDTtfWvgFb9yHyYTDKLTEtvH2/QMwxZcLLEQsBd/hAsCYSzs8SjK1Rb\n" +
//        "f1G47EpipTcFREOJu7xW3l2GdlAzGdA3HS9o2EOiu5k9Z4wYPMTU42izGNZkj8q4\n" +
//        "xukYfQM46Rh/A0yeN+0AvGA=\n" +
//        "-----END PRIVATE KEY-----\n"

//val privateKeyStr2 = "-----BEGIN PRIVATE KEY-----\n" +
//        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEBkBFZiPxX64N\n" +
//        "FGc6PPC/P1TkscyJoMH/XslsaELkwsHbLEbVIbBAhbfjXaBev3B22d2Ph2LRmMV1\n" +
//        "TkrB3TWUb80n0n7/uRUhGWmi1nWnMBqiO3pqsaLeVcWbTYBu3g5Dm4wVWHNqTizu\n" +
//        "F3v3EOdIIhT9dgooHjTCGbF3mQPx05doHE2tF1z/d78cDBj+HnhtNJt4TlVcPlCN\n" +
//        "efdB7cyDEOk6oovLCpl8RkBMLnzekT69AEoFlzWCF4YND+OrdJFOWptw8Guf0anO\n" +
//        "NzPZNs+gQN4Wi4qsUO6LaTdnKjB3xQnMkvyaRujO3CcBF6iRwsNyhzPI69cd8PLb\n" +
//        "2yVsFvBLAgMBAAECggEAGKKl+Y0upcqpyq0E+hwG/FjP+YCn3wKNUJ6aTU1DNjaV\n" +
//        "P9WKPhj0L4YEIzwEQcnNLZBHxJGJwfyU0+fREU26wsD98jFYG1Wo+K0qRgVonNLg\n" +
//        "FWet1xzgfEZbAji6/3UWCk2LHSyNFMgToDXnntmbnp0ld4pAKO5B//oVYHDACK6g\n" +
//        "xJb8I0ILKAa5mCkCLNFcm3AWsTC1Ngu3APegP5Qd7bxxoWLJUfb1Eu/J6Aqin4Rk\n" +
//        "pk5bzsl53sRQ3eQxiBPChxzuD8WJFVJURbIDp5DjgiSslVVaBPWy3Vz/fas+rL2n\n" +
//        "/X9U6dHinRd+MO5PMRNZ/Phc62ci+5l5AeEwbggk6QKBgQDxbt4cUNeuVUWCJud9\n" +
//        "Xui1DtXH0vcm59Zn+BFSe/F2qaVTlq1i25EPZScwsxXDMC1/ZHbCYvBVRFWoGUuh\n" +
//        "GAb7PqmotihCOuAIDOt9FHbY0CvNoMwh5VJ5gH//w0yph7163tkB+HF5gl9rZk7d\n" +
//        "BeIqzp6WIkLkW2jGedOKGiKF1wKBgQCL/XoMYtbQ4+R0tPJ8OYtE3p4WCsxsbI6n\n" +
//        "8lIn+4GBf9uCmB8N/7FTykaryfyY5UcgnFzayBNcwCvXgBBjOh5BUuEXpGl7H39n\n" +
//        "hD8ldF+ao/MtmYZmJ3EtqCs+6T7iGtUSTmRp7yrmCTl8xfXc9I5d5KZSFbEgFlIR\n" +
//        "Tt8Cb4WyrQKBgG62fYoUlGMwXTiK6Jq4PBRTIjuJBadEpzB0NFVT2u2ka9kB/q6X\n" +
//        "q1SY+Ti+1tr0K9kAU+ld/4HKXVFBkEnmP8VU/e4M4jdE0lk+u3pXOQzBoWShORrO\n" +
//        "E1z+TTQW9f/D7GL88ByUNk0XOPSCLOXz/bvNljGYsn4PYOOrGBN7WptvAoGAFIcE\n" +
//        "wJA1w8fHFj36WLDvdRfXwo+uzi9U89eaqHY+1Uh0XcAEvb7Ct/CvUD18JsdoGKYV\n" +
//        "D5fLDwy24RDMVmCNUzLeAuN+/CjwO7OutsVLx5dG72bVyGD8PZ+tR7pYGDurBtYr\n" +
//        "70S826J6pYs5tD7AKZB/uwWnRXdAbX1NQkQsLLkCgYEA2me//XI3c5av6116y/ZK\n" +
//        "0vcxEEv826xiMpfFCNzY25JshOCIRqGc2rgDuGouFmujKhlbRsYoQ0mZnc8Cd3Kt\n" +
//        "V8+ZUA2glK65Crsy5zgcFV7DfKdftaXV1sPv5Cfh+u2qXB95prVtlUKK54wImcED\n" +
//        "pSHsIz3ZjNYoFfgppAMbb8U=\n" +
//        "-----END PRIVATE KEY-----"