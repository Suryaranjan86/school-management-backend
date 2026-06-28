package com.srs.school.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class SequenceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(SequenceRepository.class);

    /**
     * Get the next value from the receipt sequence
     */
    public Long getNextReceipt() {
        log.info("Entering getNextReceipt");
        String sql = "SELECT nextval('receipt_seq')";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    /**
     * Generate a unique ID for StudentAcademicRecord using StuAcaRecIdGenerator logic
     */
    public String generateStudentAcademicRecordId() {
        log.info("Entering generateStudentAcademicRecordId");
        Long nextVal = (Long) jdbcTemplate.queryForObject("select nextval('stu_aca_rec_seq')", Long.class);
        return "stu-aca-rec-" + nextVal;
    }

    /**
     * Generate a unique ID for Student using StuIdGenerator logic
     */
    public String generateStudentId() {
        log.info("Entering generateStudentId");
        Long nextVal = (Long) jdbcTemplate.queryForObject("select nextval('stu_seq')", Long.class);
        return "stu-" + nextVal;
    }

    /**
     * Generate a unique ID for Fee using FeeIdGenerator logic
     */
    public String generateFeeId() {
        log.info("Entering generateFeeId");
        Long nextVal = (Long) jdbcTemplate.queryForObject("select nextval('fee_seq')", Long.class);
        return "fee-" + nextVal;
    }
}
