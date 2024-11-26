package com.ohgiraffers.sessionsecurity.user.model.dto;

import com.ohgiraffers.sessionsecurity.common.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginUserDTO {

    /*필기
    * 로그인시 사용할 DTO 클래스는
    * 화면 상에서 입력 받은 ID, PASSWORD 뿐만 아니라
    * 로그인 성공시 필요한 정보들을 추가적으로 담는다.*/



    private  int userCode;
    private String userId;
    private String userName;
    private String password;
    private UserRole userRole;

    public List<String> getRole() {
        if(this.userRole.getRole().length()>0){
            //회원의 권한이 여러 개 ex) 일반회원 , 관리자회원
            //두 권한을 담기위한 리스트
            return Arrays.asList(this.userRole.getRole().split(","));
        }
        return new ArrayList<>();
    }

}
