package com.example.gymreportserver.repository;

import com.example.gymreportserver.entity.TrainingReport;
import com.example.gymreportserver.repository.projection.CustomTrainingReport;
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
                   t.year as year,
                   t.month as month,
                   sum(t.trainingDuration) as durationSummary
            from TrainingReport t
            group by t.trainerUsername,
                     t.trainerFirstname,
                     t.trainerLastname,
                     t.isActive,
                     t.year,
                     t.month
            """)
    List<CustomTrainingReport> findAllByGroupBy();
}
