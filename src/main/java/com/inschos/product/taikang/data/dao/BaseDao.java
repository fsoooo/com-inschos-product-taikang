package com.inschos.product.taikang.data.dao;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

public class BaseDao {
    public void rollBack() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}

