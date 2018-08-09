package ru.dz.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dz.model.User;
import ru.dz.model.Visit;
import ru.dz.repositories.UserRepositories;
import ru.dz.repositories.VisitReporties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Transactional
@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    UserRepositories userRepositories;

    @Autowired
    VisitReporties visitReporties;

    //проверка существования пользователя
    @Override
    public Optional<User> isExsist(String userIp) {
        Optional<User> optionalUser = userRepositories.findOneByUserIp(userIp);
        if(optionalUser.isPresent()) return optionalUser;
        else return Optional.empty();
    }

    //получение общего количества посещений за текущие сутки
    @Override
    public Integer getTotalCount() {
        LocalDate date  = LocalDate.now();
        //получение общего кол-ва посещений от начала наступления суток до момента получения информации
        Optional<List<Visit>> visits = visitReporties.findVisitsByTimeIsBetween(
                LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0)),
                LocalDateTime.now()
        );
        if(visits.isPresent()) return visits.get().size();
        else return new Integer(0);
    }

    //получение количества уникальных пользователей за текущие сутки
    @Override
    public Integer getUniqueCount() {
        LocalDate date  = LocalDate.now();
        //получение общего кол-ва посещений от начала наступления суток до момента получения информации
        Optional<List<Visit>> visits = visitReporties.findVisitsByTimeIsBetween(
                LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0)),
                LocalDateTime.now()
        );
        if(visits.isPresent()) {
            //отсортировка уникальных пользователей через HashSet
            Set<User> setUser = new HashSet<>();
            for(Visit visit : visits.get()){
                setUser.add(visit.getUser());
            }
            return setUser.size();
        }
        else return new Integer(0);
    }

    //получение общего количества посещений за указанный период
    @Override
    public Integer getTotalCountForPeriod(LocalDateTime begin, LocalDateTime end) {
        Optional<List<Visit>> visits = visitReporties.findVisitsByTimeIsBetween(begin, end);
        if(visits.isPresent()) return visits.get().size();
        else return new Integer(0);
    }

    //получение количества уникальных пользователей за указанный период
    @Override
    public Integer getUniqueCountForPeriod(LocalDateTime begin, LocalDateTime end) {
        //получение общего количества посещений за указанный период
        Optional<List<Visit>> visits = visitReporties.findVisitsByTimeIsBetween(begin, end);
        if(visits.isPresent()) {
            //отсортировка уникальных пользователей через HashSet
            Set<User> setUser = new HashSet<>();
            for(Visit visit : visits.get()){
                setUser.add(visit.getUser());
            }
            return setUser.size();
        }
        else return new Integer(0);
    }

    //получение количества постоянных пользователей за указанный период
    @Override
    public Integer getRegularCountForPeriod(LocalDateTime begin, LocalDateTime end) {
        //получение общего количества посещений за указанный период
        Optional<List<Visit>> visits = visitReporties.findVisitsByTimeIsBetween(begin, end);
        if(visits.isPresent()) {
            //отсортировка уникальных пользователей через HashSet
            Set<User> setUser = new HashSet<>();
            for(Visit visit : visits.get()){
                setUser.add(visit.getUser());
            }
            int count = 0;
            Set<String> setPageId = new HashSet<>();
            //отсортировка посещений уникальных страниц
            for (User user : setUser){
                for(Visit visitChek : user.getVisits()){
                    setPageId.add(visitChek.getPageId());
                }
                if(setPageId.size() > 10 ) count++;
            }
            return new Integer(count);
        }
        else return new Integer(0);
    }
}
