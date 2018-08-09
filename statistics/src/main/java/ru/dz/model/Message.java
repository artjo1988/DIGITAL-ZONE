package ru.dz.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Message {
    private Integer totalCountUsers;
    private Integer uniqueCountUsers;
    private Integer totalCountUsersForPeriod;
    private Integer uniqueCountUsersForPeriod;
    private Integer regularCountUsersForPeriod;
}
