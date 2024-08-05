package com.itgroup.utility;


import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    public static final String FXML_PATH ="/com/itgroup/fxml/"; // fxml파일이 있는 경로
    public static final String IMAGE_PATH = "/com/itgroup/images/"; // 이미지 파일이 있는 경로
    public static final String CSS_PATH = "/com/itgroup/css/";
    public static final String DATA_PATH = "\\src\\main\\java\\com\\itgroup\\data\\";

    private static Map<String, String> ComSizeMap = new HashMap<>();



    public static LocalDate getDatePicker(String opening) {
        // 문자열을 LocalDate 타입으로 변환하여 반환합니다.
        // 회원 가입일자, 게시물 작성 일자, 상품 등록 일자 등에서 사용할 수 있습니다.
        System.out.println(opening);

        // (inputDate가) 20240724  (이거라면)
        int year = Integer.valueOf(opening.substring(0,4)); // 2024 ( substring메소드에서 (0,4) : 0, 1, 2, 3 (4 이전까지)
        // inputDate만
        // 받아감)
        int month = Integer.valueOf(opening.substring(5,7)); // 07
        int day = Integer.valueOf(opening.substring(8)); // 24 (beginIndex만 설정하면 8번째부터 마지막까지 다 담어감)

        return LocalDate.of(year,month,day);
    }


    public static void showAlert(Alert.AlertType alertType, String[] message) {

    }


}