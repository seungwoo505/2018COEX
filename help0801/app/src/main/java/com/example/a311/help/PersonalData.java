package com.example.a311.help;

public class PersonalData {
    private String member_PM10;
    private String member_address;
    private String member_PM2_5;

    public String getMember_PM10() {
        return member_PM10;
    }

    public String getMember_PM2_5() {
        return member_PM2_5;
    }

    public String getMember_address() {
        return member_address;
    }


    public void setMember_PM10(String member_PM10) {
        this.member_PM10 = member_PM10;
    }

    public void setMember_PM2_5(String member_PM2_5){
        this.member_PM2_5 = member_PM2_5;
    }

    public void setMember_address(String member_address) {
        this.member_address = member_address;
    }

}
