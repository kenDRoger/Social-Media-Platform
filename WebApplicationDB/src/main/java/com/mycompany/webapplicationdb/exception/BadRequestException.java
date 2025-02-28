/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webapplicationdb.exception;


/**
 *
 * @author ken
 */
public class BadRequestException extends Exception {
    //TODO: Implement in everything for handling exceptions with invalid requests

    /**
     * Param should be
     *
     * @param nonnull
     * @throws com.mycompany.webapplicationdb.exception.BadRequestException
     */
    public static void checkIfValidRequests(Object... nonnull) throws BadRequestException {
        for (Object obj : nonnull) {
            if (obj == null) {
                throw new BadRequestException();
            }
        }
    }
}
