package com.polot.gym.repository;

import com.polot.gym.entity.TrainingReport;
import com.polot.gym.repository.projection.CustomTrainingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingReportRepository extends JpaRepository<TrainingReport, Long> {

    @Query(value = """
            select t.trainerUsername as trainerUsername,
                   t.trainerFirstname as trainerFirstname,
                   t.trainerLastname as trainerLastname,
                   t.isActive as isActive,
                   t.reportYear as year,
                   t.reportMonth as month,
                   sum(t.trainingDuration) as durationSummary
            from TrainingReport t
            group by t.trainerUsername,
                     t.trainerFirstname,
                     t.trainerLastname,
                     t.isActive,
                     t.reportYear,
                     t.reportMonth
            """)
    List<CustomTrainingReport> findAllCustomTrainingReportsSummary();
}
