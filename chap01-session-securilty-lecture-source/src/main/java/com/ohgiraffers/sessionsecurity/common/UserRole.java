package com.ohgiraffers.sessionsecurity.common;

import lombok.Getter;

@Getter
public enum UserRole {
    /*필기
    * enum 이란?
    * 열거형 상수들의 집합을 의미한다!!!!!!!.
    * 주로 사용되는 예시로는
    * 고정되어있는 값들을 처리하기 위해 사용된다.
    * EX) 시스템의 권한이 단2개 - 일반사용자,관리자
    *  */

    USER("USER"), ADMIN("ADMIN");

    private String role;

    UserRole(String role){this.role = role;}

}
