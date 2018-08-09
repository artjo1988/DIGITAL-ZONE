package ru.dz.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dz.model.Message;
import ru.dz.model.User;
import ru.dz.model.Visit;
import ru.dz.repositories.UserRepositories;
import ru.dz.repositories.VisitReporties;
import ru.dz.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StatisticController {

    @Autowired
    UserRepositories userRepositories;

    @Autowired
    VisitReporties visitReporties;

    @Autowired
    StatisticService statisticService;

    //фиксация посещения
    @GetMapping("/fix")
    public ResponseEntity<Object> getHello(HttpServletRequest request, String url){
        //получение IP-адреса клиента
        String userIp = request.getRemoteAddr();

        //если будемполучать некорректный ip,
        //то можно воспользоваться закомментированным блоком

        /*
        String userIp = null;
        if (request.getHeader("X-Forwarded-For") == null) {
            userIp = request.getRemoteAddr();
        }
        else {
            userIp = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
        */

        //время запроса
        LocalDateTime time= LocalDateTime.now();
        Optional<User> optionalUser = statisticService.isExsist(userIp);
        //если пользователь с таким ip существует, то визит сохраняем по этим пользователем
        if(optionalUser.isPresent()) visitReporties.save(Visit.builder()
                .user(optionalUser.get())
                .pageId(url)
                .time(time)
                .build());
        //если пользователь с таким ip не существует, то сначала создаём и сохраняем пользователя
        //сразу же вытаскиваем его из БД, чтобы удостоверится, что пользователь сохранился
        //и сохраняем визит с этим пользователем
        else {
            List<Visit> visits = new ArrayList<>();
            User user = User.builder()
                    .userIp(userIp)
                    .visits(visits)
                    .build();
            userRepositories.save(user);
            User userChek  = statisticService.isExsist(userIp).orElseThrow(IllegalArgumentException::new);
            visitReporties.save(Visit.builder()
                    .user(userChek)
                    .pageId(url)
                    .time(time)
                    .build());
        }
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/results")
    public ResponseEntity<Object> postResult(){
        Integer totalCount = statisticService.getTotalCount();
        Integer uiqueCount = statisticService.getUniqueCount();

        return  ResponseEntity.ok(Message.builder()
                .totalCountUsers(totalCount)
                .uniqueCountUsers(uiqueCount)
                .build());
    }

    @GetMapping("/resultsForPeriod")
    public ResponseEntity<Object> postResultForPeriod(String dateBegin, String timeBegin,String dateEnd, String timeEnd){
        LocalDateTime begin = LocalDateTime.of(LocalDate.parse(dateBegin), LocalTime.parse(timeBegin));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(dateEnd), LocalTime.parse(timeEnd));
        Integer totalCountForPeriod = statisticService.getTotalCountForPeriod(begin, end);
        Integer uniqueCountForPeriod = statisticService.getUniqueCountForPeriod(begin, end);
        Integer regularCountForPeriod = statisticService.getRegularCountForPeriod(begin, end);

        return ResponseEntity.ok(Message.builder()
                .totalCountUsersForPeriod(totalCountForPeriod)
                .uniqueCountUsersForPeriod(uniqueCountForPeriod)
                .regularCountUsersForPeriod(regularCountForPeriod)
                .build());
    }




}
