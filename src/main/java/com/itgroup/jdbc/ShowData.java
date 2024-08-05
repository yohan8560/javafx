package com.itgroup.jdbc;

import com.itgroup.bean.Company;

public class ShowData {
    public static void printBean(Company bean) {
        int pnum = bean.getPnum();
        String name = bean.getName();
        String ceoName = bean.getCeoName();
        String image = bean.getImage();
        String comSize = bean.getComSize();
        String talent = bean.getTalent();
        String opening = bean.getOpening();

        System.out.println(pnum);
        System.out.println(name);
        System.out.println(ceoName);
        System.out.println(image);
        System.out.println(comSize);
        System.out.println(talent);
        System.out.println(opening);
    }
}
