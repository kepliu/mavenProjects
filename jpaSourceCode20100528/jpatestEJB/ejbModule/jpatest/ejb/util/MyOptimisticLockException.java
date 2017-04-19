package jpatest.ejb.util;

@javax.ejb.ApplicationException(rollback = true)
public class MyOptimisticLockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MyOptimisticLockException() {
		super();
	}

	public MyOptimisticLockException(String message) {
		super(message);
	}

	public MyOptimisticLockException(Throwable cause) {
		super(cause);
	}

	public MyOptimisticLockException(String message, Throwable cause) {
		super(message, cause);
	}

}
