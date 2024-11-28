package com.example.campus.repository;

import com.example.campus.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository  extends JpaRepository<Course, Long> {
    List<Course> findByIsActiveTrue();

    @Query("SELECT c FROM Course c WHERE " +
            "(c.dates.startDate < :now AND (c.dates.endDate > :now OR c.dates.endDate IS NULL)) OR " +
            "(c.dates.startDate IS NULL AND c.dates.endDate > :now)")
    List<Course> findOpenCourses(@Param("now") LocalDateTime now);
}
