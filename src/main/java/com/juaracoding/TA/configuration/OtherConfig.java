package com.juaracoding.TA.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:other.properties")
public class OtherConfig {
    private static String flagLogging;//additionForLogging


    private static String flagTestDebug;

    private static String flagBcrypt;

    private static String flagMaxCounter;

    private static String flagSMTPActive;

    private static String flagSessionValidation;

    private static String urlEndPointVerify;

    private static String urlPathVerifyEmail;

    private static String pathThymeleafTemplateReport;
    private static String pathSeparatorReport;
    private static String howToDownloadReport;
    private static String pathTemplateDownload;

    public static String getPathTemplateDownload() {
        return pathTemplateDownload;
    }

    @Value("${path.template.download}")
    private void setPathTemplateDownload(String pathTemplateDownload) {
        OtherConfig.pathTemplateDownload = pathTemplateDownload;
    }

    public static String getHowToDownloadReport() {
        return howToDownloadReport;
    }
    @Value("${how.to.download.report}")
    private void setHowToDownloadReport(String howToDownloadReport) {
        OtherConfig.howToDownloadReport = howToDownloadReport;
    }

    @Value("${path.separator.report}")
    public static String getPathSeparatorReport() {
        return pathSeparatorReport;
    }

    @Value("${path.separator.report}")
    private void setPathSeparatorReport(String pathSeparatorReport) {
        OtherConfig.pathSeparatorReport = pathSeparatorReport;
    }

    public static String getPathThymeleafTemplateReport() {
        return pathThymeleafTemplateReport;
    }

    @Value("${path.thymeleaf.template.report}")
    private void setPathThymeleafTemplateReport(String pathThymeleafTemplateReport) {
        OtherConfig.pathThymeleafTemplateReport = pathThymeleafTemplateReport;
    }

    public static String getUrlPathVerifyEmail() {
        return urlPathVerifyEmail;
    }

    @Value("${url.path.verify.email}")
    private void setUrlPathVerifyEmail(String urlPathVerifyEmail) {
        OtherConfig.urlPathVerifyEmail = urlPathVerifyEmail;
    }

    public static String getUrlEndPointVerify() {
        return urlEndPointVerify;
    }

    @Value("${url.end.point.verify}")
    private void setUrlEndPointVerify(String urlEndPointVerify) {
        OtherConfig.urlEndPointVerify = urlEndPointVerify;
    }

    public static String getFlagSessionValidation() {
        return flagSessionValidation;
    }

    @Value("${flag.session.validation}")
    private void setFlagSessionValidation(String flagSessionValidation) {
        OtherConfig.flagSessionValidation = flagSessionValidation;
    }

    public static String getFlagSMTPActive() {
        return flagSMTPActive;
    }

    @Value("${flag.smtp.active}")
    private void setFlagSMTPActive(String flagSMTPActive) {
        OtherConfig.flagSMTPActive = flagSMTPActive;
    }

    public static String getFlagMaxCounter() {
        return flagMaxCounter;
    }

    @Value("${flag.max.counter.login}")
    private void setFlagMaxCounter(String flagMaxCounter) {
        OtherConfig.flagMaxCounter = flagMaxCounter;
    }

    public static String getFlagBcrypt() {
        return flagBcrypt;
    }

    @Value("${flag.bcrypts}")
    private void setFlagBcrypt(String flagBcrypt) {
        OtherConfig.flagBcrypt = flagBcrypt;
    }

    public static String getFlagTestDebug() {
        return flagTestDebug;
    }

    @Value("${flag.test.debug}")
    private void setFlagTestDebug(String flagTestDebug) {
        OtherConfig.flagTestDebug = flagTestDebug;
    }

    public static String getFlagLogging() {
        return flagLogging;
    }

    @Value("${flag.logging}")
    private void setFlagLogging(String flagLogging) {
        this.flagLogging = flagLogging;
    }

}