package com.itgroup.bean;

public class Company {
    private int pnum; // 회사 등록 번호
    private String name; // 회사 이름
    private String ceoName; // 대표이사 이름
    private String image; // 로고 이미지
    private String comSize; // 회사 규모
    private String talent; // 인재상
    private String opening; // 설립일


    @Override
    public String toString() {
        final String SEPARATOR = "/";

        String temp = "";
        temp += pnum + SEPARATOR;
        temp += (name == null ? "" : name) + SEPARATOR;
        temp += (ceoName == null ? "" : ceoName) + SEPARATOR;
        temp += (image == null ? "" : image) + SEPARATOR;
        temp += (comSize == null ? "" : comSize) + SEPARATOR;
        temp += (talent == null ? "" : talent) + SEPARATOR;
        temp += (opening == null ? "" : opening) + SEPARATOR;

        return temp;
    }

    public Company() {
    }

    public Company(int pnum, String name, String ceoName, String image, String comSize, String talent, String opening) {
        this.pnum = pnum;
        this.name = name;
        this.ceoName = ceoName;
        this.image = image;
        this.comSize = comSize;
        this.talent = talent;
        this.opening = opening;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComSize() {
        return comSize;
    }

    public void setComSize(String comSize) {
        this.comSize = comSize;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }
}
