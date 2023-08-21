package com.surendra.oauth.server.exception;

/**
 * Exception which is thrown when there is an error occurred while encrypting or
 * de-crypting the data.
 * <p>
 * The error can occur while because of padding required for the data
 * corresponding to the algorithm, invalid key for encryption, invalid data to
 * encrypt/decrypt etc.
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 * @see Exception
 */
public class CryptographyException extends Exception {

	/**
	 * Serialized Version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new CryptographyException with <code>null</code> as its
	 * detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public CryptographyException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is
	 * <i>not</i> automatically incorporated in this exception's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public CryptographyException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public CryptographyException(final String message) {
		super(message);
	}

	/**
	 * Constructs a new Cryptography Exception with the specified cause and a
	 * detail message of <tt>(cause==null ? null : cause.toString())</tt> (which
	 * typically contains the class and detail message of <tt>cause</tt>). This
	 * constructor is useful for exceptions that are little more than wrappers
	 * for other throwables (for example,
	 * {@link java.security.PrivilegedActionException}).
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public CryptographyException(final Throwable throwable) {
		super(throwable);
	}
}