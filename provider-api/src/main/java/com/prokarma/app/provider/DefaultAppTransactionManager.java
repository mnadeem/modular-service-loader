package com.prokarma.app.provider;

import java.util.LinkedList;
import java.util.List;

public class DefaultAppTransactionManager implements AppTransactionManager {

	private List<AppTransaction> transactions = new LinkedList<AppTransaction>();
	private List<AppTransaction> afterCompletion = new LinkedList<AppTransaction>();
	private boolean active;
	private boolean rollback;

	public void begin() {
		if (active) {
			throw new IllegalStateException("Transaction already active");
		}

		for (AppTransaction tx : transactions) {
			tx.begin();
		}

		active = true;
	}

	public void commit() {
		RuntimeException exception = null;
		for (AppTransaction tx : transactions) {
			try {
				tx.commit();
			} catch (RuntimeException e) {
				exception = exception == null ? e : exception;
			}
		}
		for (AppTransaction tx : afterCompletion) {
			try {
				tx.commit();
			} catch (RuntimeException e) {
				exception = exception == null ? e : exception;
			}
		}
		active = false;
		if (exception != null) {
			throw exception;
		}

	}

	public void rollback() {
		RuntimeException exception = null;
		for (AppTransaction tx : transactions) {
			try {
				tx.rollback();
			} catch (RuntimeException e) {
				exception = exception != null ? e : exception;
			}
		}
		for (AppTransaction tx : afterCompletion) {
			try {
				tx.rollback();
			} catch (RuntimeException e) {
				exception = exception != null ? e : exception;
			}
		}
		active = false;
		if (exception != null) {
			throw exception;
		}

	}

	public void setRollbackOnly() {
		rollback = true;
	}

	public boolean getRollbackOnly() {
		if (rollback) {
			return true;
		}

		for (AppTransaction tx : transactions) {
			if (tx.getRollbackOnly()) {
				return true;
			}
		}

		return false;
	}

	public boolean isActive() {
		return active;
	}

	public void enlist(AppTransaction transaction) {
		if (active && !transaction.isActive()) {
			transaction.begin();
		}

		transactions.add(transaction);

	}

	public void enlistAfterCompletion(AppTransaction transaction) {
		if (active && !transaction.isActive()) {
			transaction.begin();
		}

		afterCompletion.add(transaction);

	}

}
