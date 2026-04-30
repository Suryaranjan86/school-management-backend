package com.srs.school.repository;

import com.srs.school.entity.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends JpaRepository<FeePayment, String> {

    @Query(value = "SELECT nextval('receipt_seq')", nativeQuery = true)
    Long getNextReceipt();
}