package com.srs.school.entity.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class FeeIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = (Long) session.createNativeQuery("select nextval('fee_seq')").getSingleResult();
        return "fee-" + nextVal;
    }
}
