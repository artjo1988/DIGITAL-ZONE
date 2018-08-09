package ru.dz.service;


import ru.dz.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StatisticService {
    Optional<User> isExsist(String userIp);
    Integer getTotalCount();
    Integer getUniqueCount();
    Integer getTotalCountForPeriod(LocalDateTime begin, LocalDateTime end);
    Integer getUniqueCountForPeriod(LocalDateTime begin, LocalDateTime end);
    Integer getRegularCountForPeriod(LocalDateTime begin, LocalDateTime end);
}
