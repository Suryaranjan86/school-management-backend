package com.srs.school.entity.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class StuAcaRecIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = (Long) session.createNativeQuery("select nextval('stu_aca_rec_seq')").getSingleResult();
        return "stu-aca-rec-" + nextVal;
    }
}

