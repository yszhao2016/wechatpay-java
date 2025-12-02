package com.wechat.pay.java.service.transfer.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhaoyoushui
 * @date 2025/12/2 14:04
 * @Description
 * @Copyright 南京优通信息科技股份有限公司
 */
public class TransferSceneReportInfo {
  @SerializedName("info_type")
  private String infoType;

  @SerializedName("info_content")
  private String infoContent;

  public String getInfoType() {
    return infoType;
  }

  public void setInfoType(String infoType) {
    this.infoType = infoType;
  }

  public String getInfoContent() {
    return infoContent;
  }

  public void setInfoContent(String infoContent) {
    this.infoContent = infoContent;
  }
}
