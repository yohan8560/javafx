package com.itgroup.jdbc;

import com.itgroup.bean.Company;
import com.itgroup.dao.CompanyDao;

import java.util.List;

public class SelectMain {
    public static void main(String[] args) {
        CompanyDao dao = new CompanyDao();
        List<Company> alldata = dao.selectAll();
        System.out.println("등록된 기업 수 : " + alldata.size());

        for (Company bean : alldata) {
            ShowData.printBean(bean);
            System.out.println("------------------------------------");
        }
    }
}
