package org.tpri.sc.dao.exception;

/**
 * @description 数据库操作异常类
 * @author 易文俊
 * @since 2015-04-02
 */
public class DaoException extends RuntimeException{
	
	private static final long serialVersionUID = 8844602256614491188L;

	public DaoException() {
		super();
		
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DaoException(String message) {
		super(message);
		
	}

	public DaoException(Throwable cause) {
		super(cause);
	}
	
	
}