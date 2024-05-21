package com.mysite.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 검색을 위한 필터 클래스
@Data   // getter setter 만들어줌
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성
public class ExpenseFilterDTO {

    public ExpenseFilterDTO(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private String keyword;

    private String sortBy;

    private String startDate;

    private String endDate;

}