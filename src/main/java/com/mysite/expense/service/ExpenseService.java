package com.mysite.expense.service;

import com.mysite.expense.dto.ExpenseDTO;
import com.mysite.expense.dto.ExpenseFilterDTO;
import com.mysite.expense.entity.Expense;
import com.mysite.expense.entity.User;
import com.mysite.expense.exception.ExpenseNotFoundException;
import com.mysite.expense.repository.ExpenseRepository;
import com.mysite.expense.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    @Autowired
    private final ExpenseRepository expRepo;

    private final ModelMapper modelMapper;

    private final UserService uService;

    //모든 비용 리스트를 찾는 서비스
    public List<Expense> findAllExpenses() {
        return expRepo.findAll();
    }

    // Entity => DTO 변환 (다른 메서드에서만 사용 변환용)
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        expenseDTO.setDateString(DateTimeUtil.convertDateToString(expenseDTO.getDate()));
        return expenseDTO;
    }

    // DTO => Entity 변환
    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        if (expenseDTO.getId() == null) {
            // expenseId 유니크 문자열 입력 (자바 유틸 UUID 사용)
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        // 문자열날짜 => 날짜 변환
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        return expense;
    }

    //모든 비용 DTO 리스트를 리턴
    public List<ExpenseDTO> getAllExpenses() {
        User user = uService.getLoggedInUser();
        List<Expense> list = expRepo.findByDateBetweenAndUserId(
                Date.valueOf(LocalDate.now().withDayOfMonth(1)),
                Date.valueOf(LocalDate.now()),
                user.getId()); //엔티티 리스트
        List<ExpenseDTO> listDTO = list.stream()
                .map((expense) -> mapToDTO(expense))
                .collect(Collectors.toList());
        return listDTO;
    }

    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = mapToEntity(expenseDTO);
        expense.setUser(uService.getLoggedInUser());
        expense = expRepo.save(expense);
        return mapToDTO(expense);
    }

    public void deleteExpense(String id) {
        Expense expense= getExpense(id);
        expRepo.delete(expense);
    }

    private Expense getExpense(String id) {
        return expRepo.findByExpenseId(id).orElseThrow(() -> new ExpenseNotFoundException("해당 ID의 아이템을 찾을 수 없습니다 : " + uService.getLoggedInUser().getId()));
    }

    public ExpenseDTO getExpenseById(String id) {
        Expense expense = getExpense(id);
        ExpenseDTO expenseDTO = mapToDTO(expense);
        expenseDTO.setDateString(DateTimeUtil.convertDateToInput(expense.getDate()));
        return expenseDTO;
    }

    public List<ExpenseDTO> getFilterExpenses(ExpenseFilterDTO filterDTO) throws ParseException {
        String keyword = filterDTO.getKeyword();
        String sortBy = filterDTO.getSortBy();
        String startDate = filterDTO.getStartDate();
        String endDate = filterDTO.getEndDate();

        // 입력 안할시에는 0일 부터 현재 날짜까지 출력
        Date startDay = !startDate.isEmpty() ? DateTimeUtil.convertStringToDate(startDate) : new Date(0);
        Date endDay = !endDate.isEmpty() ? DateTimeUtil.convertStringToDate(endDate) : new Date(System.currentTimeMillis());

        List<Expense> list = expRepo.findByNameContainingAndDateBetweenAndUserId(keyword, startDay, endDay, uService.getLoggedInUser().getId());
        List<ExpenseDTO> filterlist = list.stream().map(this::mapToDTO).collect(Collectors.toList());

        // date 또는 amount로 검색할 때 내림차순으로 정렬
        if (sortBy.equals("date")) {
            filterlist.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        } else if (sortBy.equals("amount")) {
            filterlist.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
        }

        return filterlist;
    }

    public Long totalExpenses(List<ExpenseDTO> expenses) {
        Long sum = expenses.stream().map(x -> x.getAmount()).reduce(0L, Long::sum);
        return sum;
    }

}
