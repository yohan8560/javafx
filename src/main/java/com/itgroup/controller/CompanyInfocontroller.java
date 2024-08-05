package com.itgroup.controller;

import com.itgroup.bean.Company;
import com.itgroup.dao.CompanyDao;
import com.itgroup.utility.Paging;
import com.itgroup.utility.Utility;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CompanyInfocontroller implements Initializable {
    private CompanyDao dao = null;
    @FXML private ImageView imageView;
    @FXML private Pagination pagination;
    @FXML private Label pageStatus;
    @FXML private TableView<Company> companyTable;
    @FXML private ComboBox<String> fieldSearch;
    @FXML private Label fxLabelTalent;

    private String mode = null;

    ObservableList<Company> dataList = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dao = new CompanyDao();

        setTableColumns();
        setPagination(0);

        ChangeListener<Company> tableListener = new ChangeListener<Company>() {
            @Override
            public void changed(ObservableValue<? extends Company> observableValue, Company oldValue, Company newValue) {
                if (newValue != null) {
                    System.out.println("회사 정보");
                    System.out.println(newValue);

                    String imageFile;
                    if (newValue.getImage() != null) {
                        imageFile = Utility.IMAGE_PATH + newValue.getImage().trim();
                    } else {
                        imageFile = Utility.IMAGE_PATH + "noimage.png";
                    }

                    Image someImage;
                    // 이미지 파일이 있는지 확인
                    URL imageUrl = getClass().getResource(imageFile);
                    if (imageUrl == null) {
                        imageUrl = getClass().getResource(Utility.IMAGE_PATH + "noimage.png");
                    }

                    if (imageUrl != null) {
                        someImage = new Image(imageUrl.toString());
                        imageView.setImage(someImage);

                        // 이미지의 원래 크기를 가져와서 ImageView 크기 설정
                        double originalWidth = someImage.getWidth();
                        double originalHeight = someImage.getHeight();
                        imageView.setFitWidth(originalWidth);
                        imageView.setFitHeight(originalHeight);
                        imageView.setPreserveRatio(true);  // 원본 비율 유지
                        imageView.setSmooth(true);        // 부드럽게 스케일링
                        imageView.setCache(true);         // 캐싱 사용
                    } else {
                        imageView.setImage(null);
                    }
                }
                if (newValue.getTalent() != null) {
                    fxLabelTalent.setText(newValue.getTalent());
                } else {
                    fxLabelTalent.setText("정보 없음");
                }
                
            }
        };
        companyTable.getSelectionModel().selectedItemProperty().addListener(tableListener);
    }

    private void setPagination(int pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        pagination.setPageFactory(this::createPage);

        imageView.setImage(null);
    }


    private Node createPage(int pageIndex) {
//        String mode = null;

        int totalCount = 0;
        totalCount = dao.getTotalCount(mode);

        Paging pageInfo = new Paging(String.valueOf(pageIndex + 1), "10", totalCount, null, this.mode, null); // 나중에
        // mode의 null을 바꿔줘야함

        pagination.setPageCount(pageInfo.getTotalPage());

        fillTableData(pageInfo);

        VBox vbox = new VBox(companyTable);
        return vbox;
    }

    private void fillTableData(Paging pageInfo) {

        List<Company> companyList = dao.getPaginationData(pageInfo);
        System.out.println(companyList.size());

        dataList = FXCollections.observableArrayList(companyList);

        companyTable.setItems(dataList);
        pageStatus.setText(pageInfo.getPagingStatus());
    }

    private void setTableColumns() {
        String[] fields = {"pnum", "name", "ceoName", "comSize", "opening"};
        String[] colNames = {"번호", "회사이름", "대표이사", "회사규모", "설립일"};

        TableColumn tableColumn = null;

        for (int i = 0; i < fields.length; i++) {
            tableColumn = companyTable.getColumns().get(i);
            tableColumn.setText(colNames[i]); // 컬럼을 한글 이름으로 변경 (Column: 테이블의 세로줄)

            tableColumn.setCellValueFactory(new PropertyValueFactory<>(fields[i]));

            tableColumn.setStyle("-fx-alignment:center;");
            // CSS이용해서 모든 셀 데이터를 가운데 정렬하기 (CSS: 스타일을 정의하는 언어)
        }
    }

    public void onInsert(ActionEvent event) throws Exception {
        String fxmFile = Utility.FXML_PATH + "CompanyInsert.fxml";

        URL url = getClass().getResource(fxmFile);

        FXMLLoader fxmlLoader = new FXMLLoader(url);

        Parent container = fxmlLoader.load();

        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("회사 등록하기");
        stage.showAndWait();

        setPagination(0);
    }

    public void onUpdate(ActionEvent event) throws Exception {
        int idx = companyTable.getSelectionModel().getSelectedIndex();

        if (idx >= 0) {
            System.out.println("번호 : " + idx);

            String fxmFile = Utility.FXML_PATH + "CompanyUpdate.fxml";
            URL url = getClass().getResource(fxmFile);
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent container = fxmlLoader.load();

            Company bean = companyTable.getSelectionModel().getSelectedItem();

            CompanyUpdateController controller = fxmlLoader.getController();
            controller.setBean(bean);

            Scene scene = new Scene(container);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("회사 수정하기");
            stage.showAndWait();

            setPagination(0);
        }
    }

    public void onDelete(ActionEvent event) {
        // 특정 항목 삭제
        int idx = companyTable.getSelectionModel().getSelectedIndex();

        if (idx >= 0) {
            String[] message = new String[]{"삭제 확인", "삭제 항목 선택 대화 상자", "이거 증말로 삭제할것에요? 진짜?."};

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("삭제 확인");
            alert.setHeaderText("삭제 항목 선택 대화 상자");
            alert.setContentText("삭제하시겠습니까?");

            Optional<ButtonType> response = alert.showAndWait();


            if (response.get() == ButtonType.OK) {
                Company bean = companyTable.getSelectionModel().getSelectedItem();
                int pnum = bean.getPnum();
                int cnt = -1;
                cnt = dao.deleteData(pnum); // dao에 delete SQL문 실행
                if (cnt != -1) {
                    System.out.println("삭제 성공");
                    setPagination(0); // 초기 페이지 0으로 설정
                } else {
                    System.out.println("삭제 실패");
                }
            } else {
                System.out.println("삭제 취소합니다.");
            }
        } else {
            String[] message = new String[]{"삭제할 목록 확인", "삭제할 대상 미선택", "삭제할 행을 선택해주세요."};
            Utility.showAlert(Alert.AlertType.WARNING, message);
        }
    }

    public void onClosing(ActionEvent event) {
        // 종료 버튼 클릭시 종료 여부를 확인 받고 종료합니다.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("종료");
        alert.setHeaderText("프로그램을 종료 합니다");
        alert.setContentText("프로그램 종료");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("OK 버튼을 눌러서 종료하도록 합니다.");
            Platform.exit();
        } else {
            System.out.println("Cancel 버튼을 누르셨군요");
        }
    }
    public void choiceSelect(ActionEvent event) {
        String comSize = fieldSearch.getSelectionModel().getSelectedItem();
        this.mode = dao.getComSizeName(comSize);
        setPagination(0);
        System.out.println(comSize);
    }

    public void onSaveFile(ActionEvent event) {
        // 현재 페이지에 보이는 테이블 뷰 목록을 텍스트 형식의 파일로 저장합니다.
        FileChooser chooser = new FileChooser();
        Button myBtn = (Button) event.getSource();
        Window window = myBtn.getScene().getWindow();
        File savedFile = chooser.showSaveDialog(window);

        if (savedFile != null) {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(savedFile);
                bw = new BufferedWriter(fw); // 승급
                if (dataList != null) {
                    for (Company bean : dataList) {
                        bw.write(bean.toString());
                        bw.newLine();
                    }
                } else {
                    System.out.println("dataList가 null입니다.");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (fw != null) {
                        fw.close();
                    }

                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }

            }
            System.out.println("파일이 저장되었습니다.");
        } else {
            System.out.println("파일 저장 실패");
        }
    }
}


//            System.out.println("파일이 저장되었습니다.");
//        } else {
//                System.out.println("파일 저장 실패");
//        }
//                }
