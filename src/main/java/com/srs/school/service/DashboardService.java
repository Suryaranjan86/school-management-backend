package com.srs.school.service;

import com.srs.school.context.SchoolContext;
import com.srs.school.dto.ClassStudentCountDto;
import com.srs.school.dto.SchoolStudentCountDto;
import com.srs.school.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    public SchoolStudentCountDto getSchoolWiseStudentCount() {
        log.info("Entering getSchoolWiseStudentCount");
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return new SchoolStudentCountDto(0L, 0L, 0L);
        }
        return dashboardRepository.getSchoolWiseStudentCount(schoolId);
    }

    public List<ClassStudentCountDto> getClassWiseCurrentAcademicSessionStudentCount() {
        log.info("Entering getClassWiseCurrentAcademicSessionStudentCount");
        String schoolId = SchoolContext.getSchoolId();
        if (schoolId == null || schoolId.isEmpty()) {
            return List.of();
        }
        return dashboardRepository.getClassWiseCurrentAcademicSessionStudentCount(schoolId);
    }
}
