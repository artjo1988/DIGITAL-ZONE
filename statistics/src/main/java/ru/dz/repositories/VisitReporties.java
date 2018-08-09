package ru.dz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpInc;
import ru.dz.model.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitReporties extends JpaRepository<Visit, Long> {
    Optional<List<Visit>> findVisitsByTimeIsBetween(LocalDateTime timeOne, LocalDateTime timeTwo);
}
