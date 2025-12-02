package com.wechat.pay.java.service.transfer.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhaoyoushui
 * @date 2025/12/2 14:53
 * @Description
 * @Copyright 南京优通信息科技股份有限公司
 */
public class TransferQueryResponse {

  @SerializedName("mch_id")
  private String mchId;

  @SerializedName("out_bill_no")
  private String outBillNo;

  @SerializedName("transfer_bill_no")
  private String transferBillNo;

  @SerializedName("appid")
  private String appid;

  @SerializedName("state")
  private State state;
}
