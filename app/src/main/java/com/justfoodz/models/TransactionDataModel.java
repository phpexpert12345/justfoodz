package com.justfoodz.models;

public class TransactionDataModel {

    private String transactionheading, transaction_id, Billed_to_name, Billed_to_wallet_number, Billed_to_email_address,
            Billed_to_mobile_number, Site_Company_Name, Site_Company_Address, wallet_payment_date, wallet_payment_time,
            wallet_payment_status, wallet_recieve_amount, wallet_money_display,wallet_payment_status_icon,Billed_Heading;


    public TransactionDataModel(String transaction_id, String transactionheading, String Billed_to_name, String Billed_to_wallet_number, String Billed_to_email_address, String Billed_to_mobile_number, String Site_Company_Name,
                                String Site_Company_Address, String wallet_payment_date, String wallet_payment_time,
                                String wallet_payment_status, String wallet_recieve_amount, String wallet_money_display,String wallet_payment_status_icon,
                                String Billed_Heading) {


        this.transaction_id = transaction_id;
        this.transactionheading = transactionheading;
        this.Billed_to_name = Billed_to_name;
        this.Billed_to_wallet_number = Billed_to_wallet_number;
        this.Billed_to_email_address = Billed_to_email_address;
        this.Billed_to_mobile_number = Billed_to_mobile_number;
        this.Site_Company_Name = Site_Company_Name;
        this.Site_Company_Address = Site_Company_Address;
        this.wallet_payment_date = wallet_payment_date;
        this.wallet_payment_time = wallet_payment_time;
        this.wallet_payment_status = wallet_payment_status;
        this.wallet_recieve_amount = wallet_recieve_amount;
        this.wallet_money_display = wallet_money_display;
        this.wallet_payment_status_icon=wallet_payment_status_icon;
        this.Billed_Heading=Billed_Heading;
    }

    public String getWallet_payment_status_icon() {
        return wallet_payment_status_icon;
    }

    public void setWallet_payment_status_icon(String wallet_payment_status_icon) {
        this.wallet_payment_status_icon = wallet_payment_status_icon;
    }

    public String getTransactionheading() {
        return transactionheading;
    }

    public void setTransactionheading(String transactionheading) {
        this.transactionheading = transactionheading;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getBilled_to_name() {
        return Billed_to_name;
    }

    public void setBilled_to_name(String billed_to_name) {
        Billed_to_name = billed_to_name;
    }

    public String getBilled_to_wallet_number() {
        return Billed_to_wallet_number;
    }

    public void setBilled_to_wallet_number(String billed_to_wallet_number) {
        Billed_to_wallet_number = billed_to_wallet_number;
    }

    public String getBilled_to_email_address() {
        return Billed_to_email_address;
    }

    public void setBilled_to_email_address(String billed_to_email_address) {
        Billed_to_email_address = billed_to_email_address;
    }

    public String getBilled_to_mobile_number() {
        return Billed_to_mobile_number;
    }

    public void setBilled_to_mobile_number(String billed_to_mobile_number) {
        Billed_to_mobile_number = billed_to_mobile_number;
    }

    public String getSite_Company_Name() {
        return Site_Company_Name;
    }

    public void setSite_Company_Name(String site_Company_Name) {
        Site_Company_Name = site_Company_Name;
    }

    public String getSite_Company_Address() {
        return Site_Company_Address;
    }

    public void setSite_Company_Address(String site_Company_Address) {
        Site_Company_Address = site_Company_Address;
    }

    public String getWallet_payment_date() {
        return wallet_payment_date;
    }

    public void setWallet_payment_date(String wallet_payment_date) {
        this.wallet_payment_date = wallet_payment_date;
    }

    public String getWallet_payment_time() {
        return wallet_payment_time;
    }

    public void setWallet_payment_time(String wallet_payment_time) {
        this.wallet_payment_time = wallet_payment_time;
    }

    public String getWallet_payment_status() {
        return wallet_payment_status;
    }

    public void setWallet_payment_status(String wallet_payment_status) {
        this.wallet_payment_status = wallet_payment_status;
    }

    public String getWallet_recieve_amount() {
        return wallet_recieve_amount;
    }

    public void setWallet_recieve_amount(String wallet_recieve_amount) {
        this.wallet_recieve_amount = wallet_recieve_amount;
    }

    public String getWallet_money_display() {
        return wallet_money_display;
    }

    public void setWallet_money_display(String wallet_money_display) {
        this.wallet_money_display = wallet_money_display;
    }

    public String getBilled_Heading() {
        return Billed_Heading;
    }

    public void setBilled_Heading(String Billed_Heading) {
        this.Billed_Heading = Billed_Heading;
    }
}
