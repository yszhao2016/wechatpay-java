package com.wechat.pay.java.service.transfer.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhaoyoushui
 * @date 2025/12/2 14:56
 * @Description
 * @Copyright 南京优通信息科技股份有限公司
 */

public enum State {

  @SerializedName("ACCEPTED")
  ACCEPTED,

  @SerializedName("PROCESSING")
  PROCESSING,

  @SerializedName("WAITING_FOR_RECEIVING")
  WAITING_FOR_RECEIVING,

  @SerializedName("TRANSFERRED")
  TRANSFERRED,

  @SerializedName("SUCCESS")
  SUCCESS,

  @SerializedName("FAIL")
  FAIL,

  @SerializedName("CANCELING")
  CANCELING,

  @SerializedName("CANCELED")
  CANCELED,

}
