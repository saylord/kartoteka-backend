package kz.logitex.kartoteka.report;

import kz.logitex.kartoteka.model.Building;
import kz.logitex.kartoteka.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ReportRequestDTO {
    // Входящие - true / Исходящие - false
    private boolean ingoing = true;
    // Основные данные
    private Long start;
    private Long end;
    // Для фильтрации
    private String description;
    private List<Building> building;
    private List<User> executor;
}
