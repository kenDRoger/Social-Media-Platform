/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.exception;

/**
 *
 * @author ken
 */
public class BadRequestException extends Exception {

    /**
     * Param should be
     *
     * @param nonnull
     * @throws com.mycompany.webapplicationdb.exception.BadRequestException
     */
    public static void checkIfValidRequests(Object... nonnull) throws BadRequestException {
        if (nonnull == null) {
            throw new BadRequestException("Request has Empty Parameters");
        }

        for (Object obj : nonnull) {
            if (obj == null || obj.toString().isEmpty()) {
                throw new BadRequestException("Request invalid");
            }
        }
    }

    public BadRequestException(String message) {
        super(message);
    }
}
