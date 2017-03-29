package com.sutao.orderfood.bean;

/**
 * Created by Administrator on 2017/3/28.
 */
public class SellerInfo {

    //推送目标
    private String installId;
    //店铺名称
    private String sellerName;
    //菜单搜索的条件
    private String userNmae;

    public SellerInfo(String userNmae, String installId,String sellerName) {
        this.userNmae = userNmae;
        this.sellerName = sellerName;
        this.installId = installId;
    }

    public String getInstallId() {
        return installId;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

}
