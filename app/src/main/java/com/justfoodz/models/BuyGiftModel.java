package com.justfoodz.models;

public class BuyGiftModel {

    public String id,giftname,giftprice, giftimage,giftdescrption,gifttermscondition,giftredeemption ;

    public BuyGiftModel(String id,String giftname, String giftprice,String giftimage,String giftdescrption,String gifttermscondition,String giftredeemption) {
        this.id=id;
        this.giftname=giftname;
        this.giftprice=giftprice;
        this.giftimage=giftimage;
        this.giftdescrption=giftdescrption;
        this.gifttermscondition=gifttermscondition;
        this.giftredeemption=giftredeemption;

    }

    public String getGiftdescrption() {
        return giftdescrption;
    }

    public void setGiftdescrption(String giftdescrption) {
        this.giftdescrption = giftdescrption;
    }

    public String getGifttermscondition() {
        return gifttermscondition;
    }

    public void setGifttermscondition(String gifttermscondition) {
        this.gifttermscondition = gifttermscondition;
    }

    public String getGiftredeemption() {
        return giftredeemption;
    }

    public void setGiftredeemption(String giftredeemption) {
        this.giftredeemption = giftredeemption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getGiftprice() {
        return giftprice;
    }

    public void setGiftprice(String giftprice) {
        this.giftprice = giftprice;
    }

    public String getGiftimage() {
        return giftimage;
    }

    public void setGiftimage(String giftimage) {
        this.giftimage = giftimage;
    }

    public String GiftVoucherTitle;
    public String GiftVoucherPurchaseAmount;

    public String getGiftVoucherTitle() {
        return GiftVoucherTitle;
    }

    public void setGiftVoucherTitle(String giftVoucherTitle) {
        GiftVoucherTitle = giftVoucherTitle;
    }

    public String getGiftVoucherPurchaseAmount() {
        return GiftVoucherPurchaseAmount;
    }

    public void setGiftVoucherPurchaseAmount(String giftVoucherPurchaseAmount) {
        GiftVoucherPurchaseAmount = giftVoucherPurchaseAmount;
    }

    public String getVoucher_long_description() {
        return voucher_long_description;
    }

    public void setVoucher_long_description(String voucher_long_description) {
        this.voucher_long_description = voucher_long_description;
    }

    public String getGiftVoucherImage() {
        return GiftVoucherImage;
    }

    public void setGiftVoucherImage(String giftVoucherImage) {
        GiftVoucherImage = giftVoucherImage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getVoucher_terms_description() {
        return voucher_terms_description;
    }

    public void setVoucher_terms_description(String voucher_terms_description) {
        this.voucher_terms_description = voucher_terms_description;
    }

    public String getVoucher_redeem_instructions_description() {
        return voucher_redeem_instructions_description;
    }

    public void setVoucher_redeem_instructions_description(String voucher_redeem_instructions_description) {
        this.voucher_redeem_instructions_description = voucher_redeem_instructions_description;
    }

    public String voucher_long_description;
    public String GiftVoucherImage;
    public String error;
    public String voucher_terms_description;
    public String voucher_redeem_instructions_description;


}
