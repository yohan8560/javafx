package com.itgroup.controller;

import com.itgroup.bean.Company;
import com.itgroup.dao.CompanyDao;
import com.itgroup.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CompanyUpdateController implements Initializable {
    @FXML private TextField fxmlPnum;
    @FXML private TextField fxmlName;
    @FXML private TextField fxmlCeoName;
    @FXML private TextField fxmlImage;
    @FXML private ComboBox<String> fxmlComSize;
    @FXML private TextField fxmlTalent;
    @FXML private DatePicker fxmlOpening;

    private Company oldBean = null;
    private Company newBean = null;
    private CompanyDao dao = new CompanyDao();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("수정이벤트");
    }

    public void onCompanyInsert(ActionEvent event) {
        boolean bool =  validationCheck();

        if (bool == true) {
            int cnt = -1;
            cnt = dao.updateData(this.newBean);
            System.out.println(cnt);
            if (cnt == 1) {
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("수정 실패");
            }
        } else {
            System.out.println("유효성 검사 실패");
        }
    }

    private boolean validationCheck() {
        int pnum = Integer.valueOf(fxmlPnum.getText().trim());

        String[] message = null;

        String name = fxmlName.getText().trim();
        if (name.length() <= 1 || name.length() >= 16) {
            message = new String[]{"유효성 검사 : 회사 이름", "길이 제한 위배", "이름은 2글자 이상 15글자 미만이여야합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        String ceoName = fxmlCeoName.getText().trim();
        if (ceoName.length() <= 1 || ceoName.length() >= 11) {
            message = new String[]{"유효성 검사 : 대표이사", "길이 제한 위배", "이름은 2글자 이상 10글자 미만이여야합니다."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }

        boolean bool = false;
        String image = fxmlImage.getText().trim();
        if (image.length() == 0 || image == null) {
            image = "noimage.png";
        }

        if(image != null){
            bool = image.endsWith(".jpg") || image.endsWith(".png") ;
            if(!bool){
                message = new String[]{"유효성 검사 : 이미지02", "확장자 점검", "이미지의 확장자는 '.jpg' 또는 '.png' 이하이어야 합니다."} ;
                Utility.showAlert(Alert.AlertType.WARNING, message);
                return false;
            }
        }

        int selectedIndex = fxmlComSize.getSelectionModel().getSelectedIndex();
        String comSize = null;

        if (selectedIndex == 0) {
            message = new String[]{"유효성 검사 : 회사규모", "회사규모 미선택", "회사에 적합한 회사 규모를 반드시 선택해주세여."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        } else {
            comSize = fxmlComSize.getSelectionModel().getSelectedItem().toString();
            System.out.println("선택된 항목");
            System.out.println(comSize);
        }

        String talent = fxmlTalent.getText().trim();
        if (talent.length() <= 6 || talent.length() >= 301) {
            message = new String[]{"유효성 검사 : 인재상", "길이 제한 위배", "인재상 설명은 5글자 이상, 300글자 이하로 적어주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        }


        LocalDate _opening = fxmlOpening.getValue();
        String opening = null;
        if (_opening == null) {
            message = new String[]{"유효성 검사 : 설립일", "무효한 날짜 형식", "올바른 설립 일자 형식을 입력해 주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
            return false;
        } else {
            opening = _opening.toString();
            // "-"를 "/"로 대체
            opening = opening.replace("-", "/");
        }

        this.newBean = new Company();
        newBean.setPnum(pnum);
        newBean.setName(name);
        newBean.setCeoName(ceoName);
        newBean.setImage(image);
        newBean.setComSize(comSize);
        newBean.setTalent(talent);
        newBean.setOpening(opening);


        return true;
    }

    public void setBean(Company bean) {
        this.oldBean = bean;
        fillPreviousData(); // 하드코딩한 메소드 가로안에
        // 데이터 베이스의 primary key에 해당하는 상품 번호를 숨겨줍니다.
        fxmlPnum.setVisible(false);
    }

    private void fillPreviousData() {
        fxmlPnum.setText(String.valueOf(this.oldBean.getPnum())); // String.valueOf() : Pnum은 정수인데 TextField는 무조건 String이라 변환 시켜주는 메소드
        fxmlName.setText(this.oldBean.getName());
        fxmlCeoName.setText(this.oldBean.getCeoName());
        fxmlImage.setText(this.oldBean.getImage());

        String comSize = this.oldBean.getComSize();
        fxmlComSize.setValue(dao.getComSizeName(comSize));

        fxmlTalent.setText(this.oldBean.getTalent());
        String opening = this.oldBean.getOpening();
        if (opening == null || opening.equals("null")) {
        } else {
            fxmlOpening.setValue(Utility.getDatePicker(opening));
        }
    }
}
