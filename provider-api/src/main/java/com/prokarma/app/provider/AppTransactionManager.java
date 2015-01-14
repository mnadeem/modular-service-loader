package com.prokarma.app.provider;

public interface AppTransactionManager extends AppTransaction {
	
	void enlist(AppTransaction transaction);
    void enlistAfterCompletion(AppTransaction transaction);
}
