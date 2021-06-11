package com.lornwolf.home.bean;

public class People {
    // 身份证号
    private String number;

    // 手机号码
    private String phone;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        if (number != null && number.length() > 0) {
            int year = Integer.parseInt(number.substring(6, 10));
            return 2021- year;
        }
        return 0;
    }
}
