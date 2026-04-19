package com.srs.school.context;

import com.srs.school.filter.SchoolIdFilter;

/**
 * Utility class to manage school context across the application
 */
public class SchoolContext {
    
    public static String getSchoolId() {
        return SchoolIdFilter.getSchoolId();
    }
    
    public static void setSchoolId(String schoolId) {
        SchoolIdFilter.setSchoolId(schoolId);
    }
    
    public static boolean hasSchoolId() {
        return getSchoolId() != null && !getSchoolId().isEmpty();
    }
}

