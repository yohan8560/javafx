package com.itgroup.controller;

import com.itgroup.bean.Company;
import com.itgroup.dao.CompanyDao;
import com.itgroup.utility.Utility;
import com.sun.source.tree.UnaryTree;
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

public class CompanyInsertController implements Initializable {
    @FXML private TextField fxmlName;
    @FXML private TextField fxmlCeoName;
    @FXML private TextField fxmlImage;
    @FXML private ComboBox fxmlComSize;
    @FXML private TextField fxmlTalent;
    @FXML private DatePicker fxmlOpening;

    CompanyDao dao = null;
    Company bean = null; // 상품 1개


    public void onCompanyInsert(ActionEvent event) {
        System.out.println(event);

        boolean bool = validationCheck();
        if (bool == true) {
            int cnt = -1;
            cnt = insertDatabase();
            if (cnt == 1) {
                Node source = (Node) event.getSource(); // 이벤트가 발생한 UI 요소를 가져옴
                Stage stage = (Stage) source.getScene().getWindow(); //
                stage.close(); // 등록되면 창을 닫음
            }
        } else { // 유효성 검사 실패시
            System.out.println("등록 실패");
        }
    }

    private int insertDatabase() {
        int cnt = -1; // 작업 실패시 -1
        cnt = dao.insertData(this.bean);
        if (cnt == 1) {
            String[] message = new String[]{"회사 등록", "회사 등록 실패", "회사 등록을 실패했습니다."};
            Utility.showAlert(Alert.AlertType.ERROR, message);
        }
        return cnt;
    }

    private boolean validationCheck() {
        // 유효성 검사 통과시 true
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
        if (image.length() == 0) {
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

        this.bean = new Company();
        bean.setName(name);
        bean.setCeoName(ceoName);
        bean.setImage(image);
        bean.setComSize(comSize);
        bean.setTalent(talent);
        bean.setOpening(opening);


        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dao = new CompanyDao();

        fxmlComSize.getSelectionModel().select(0);
    }
}
