package com.prokarma.app.provider;

public interface AppTransaction {

	void begin();
    void commit();
    void rollback();
    void setRollbackOnly();
    boolean getRollbackOnly();
    boolean isActive();
}
