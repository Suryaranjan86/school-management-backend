package com.srs.school.entity.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class UsrIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = (Long) session.createNativeQuery("select nextval('user_seq')").getSingleResult();
        return "usr-" + nextVal;
    }
}
