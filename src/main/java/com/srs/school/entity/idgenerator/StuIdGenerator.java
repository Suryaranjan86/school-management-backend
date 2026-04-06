package com.srs.school.entity.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class StuIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = (Long) session.createNativeQuery("select nextval('stu_seq')").getSingleResult();
        return "stu-" + nextVal;
    }
}
