package com.wechat.pay.java.service.transfer;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.*;
import com.wechat.pay.java.service.transfer.model.InitiateTransferRequest;
import com.wechat.pay.java.service.transfer.model.InitiateTransferResponse;
import com.wechat.pay.java.service.transfer.model.TransferQueryResponse;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static com.wechat.pay.java.core.util.GsonUtil.toJson;
import static java.util.Objects.requireNonNull;

/**
 * @author zhaoyoushui
 * @date 2025/12/2 11:28
 * @Description
 * @Copyright 南京优通信息科技股份有限公司
 */
public class TransferService {

  private final HttpClient httpClient;
  private final HostName hostName;
  private final PrivacyEncryptor encryptor;
  private final PrivacyDecryptor decryptor;

  private TransferService(
      HttpClient httpClient,
      HostName hostName,
      PrivacyEncryptor encryptor,
      PrivacyDecryptor decryptor) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
    this.encryptor = requireNonNull(encryptor);
    this.decryptor = requireNonNull(decryptor);
  }

  /**
   * TransferBatchService构造器
   */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;
    private PrivacyEncryptor encryptor;
    private PrivacyDecryptor decryptor;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      this.encryptor = config.createEncryptor();
      this.decryptor = config.createDecryptor();
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder encryptor(PrivacyEncryptor encryptor) {
      this.encryptor = encryptor;
      return this;
    }

    public Builder decryptor(PrivacyDecryptor decryptor) {
      this.decryptor = decryptor;
      return this;
    }

    public TransferService build() {
      return new TransferService(httpClient, hostName, encryptor, decryptor);
    }
  }

  /**
   * 发起商家转账
   *
   * @param request 请求参数
   * @return InitiateTransferResponse
   * @throws HttpException             发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException       发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException          发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public InitiateTransferResponse initiateBatchTransfer(InitiateTransferRequest request) {

    String requestPath = "https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills";
    // 加密敏感信息
    InitiateTransferRequest realRequest = request.cloneWithCipher(encryptor::encrypt);
    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.WECHAT_PAY_SERIAL, encryptor.getWechatpaySerial());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.POST)
            .url(requestPath)
            .headers(headers)
            .body(createRequestBody(realRequest))
            .build();
    HttpResponse<InitiateTransferResponse> httpResponse =
        httpClient.execute(httpRequest, InitiateTransferResponse.class);
    return httpResponse.getServiceResponse();
  }


  /**
   * 通过微信单号查询转账
   *
   * @param transferBillNo 请求参数
   * @return TransferEntity
   * @throws HttpException             发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException       发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException          发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public TransferQueryResponse getTransferByBillNo(String transferBillNo) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills/transfer-bill-no" +
            "/{transfer_bill_no}";

    // 添加 path param
    requestPath =
        requestPath.replace("{" + "transfer_bill_no" + "}", urlEncode(transferBillNo));

    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    HttpResponse<TransferQueryResponse> httpResponse = httpClient.execute(httpRequest,
        TransferQueryResponse.class);
    return httpResponse.getServiceResponse();
  }

  /**
   * 通过商家单号查询订单
   *
   * @param outBillNo 请求参数
   * @return TransferEntity
   * @throws HttpException             发送HTTP请求失败。例如构建请求参数失败、发送请求失败、I/O错误等。包含请求信息。
   * @throws ValidationException       发送HTTP请求成功，验证微信支付返回签名失败。
   * @throws ServiceException          发送HTTP请求成功，服务返回异常。例如返回状态码小于200或大于等于300。
   * @throws MalformedMessageException 服务返回成功，content-type不为application/json、解析返回体失败。
   */
  public TransferQueryResponse getTransferByOutNo(String outBillNo) {
    String requestPath =
        "https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills/out-bill-no" +
            "/{out_bill_no}";

    // 添加 path param
    requestPath =
        requestPath.replace("{" + "out_bill_no" + "}", urlEncode(outBillNo));

    if (this.hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.addHeader(Constant.ACCEPT, MediaType.APPLICATION_JSON.getValue());
    headers.addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .headers(headers)
            .build();
    HttpResponse<TransferQueryResponse> httpResponse = httpClient.execute(httpRequest,
        TransferQueryResponse.class);
    return httpResponse.getServiceResponse();
  }

  private RequestBody createRequestBody(Object request) {
    return new JsonRequestBody.Builder().body(toJson(request)).build();
  }
}
