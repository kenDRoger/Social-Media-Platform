/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webapplicationdb;


/**
 *
 * @author ken
 */
public class ValueValidation {

    public static void validatePassword(String password) throws InvalidPasswordLengthException, EmptyPasswordException {
        if (password == null) {
            throw new EmptyPasswordException("Password cannot be null");
        }
        if (password.length() > 30) {
            throw new InvalidPasswordLengthException("Password length exceeds limit: " + password.length());
        }
    }

    public static void validateUserRole(String userRole) throws InvalidUserRoleException, EmptyUserRoleException {
        if (userRole == null) {
            throw new EmptyUserRoleException("User role cannot be null");
        }
        if (!userRole.equals("admin") && !userRole.equals("super_admin") && !userRole.equals("user")) {
            throw new InvalidUserRoleException("Invalid user role: " + userRole);
        }
    }

    public static void validateFollow(String follow) throws InvalidFollowLengthException, EmptyFollowException {
        if (follow == null) {
            throw new EmptyFollowException("Follow cannot be null");
        }
        if (follow.length() > 30) {
            throw new InvalidFollowLengthException("Follow length exceeds limit: " + follow.length());
        }
    }

    public static void validateTitle(String title) throws InvalidTitleLengthException, EmptyTitleException {
        if (title == null) {
            throw new EmptyTitleException("Title cannot be null");
        }
        if (title.length() > 30) {
            throw new InvalidTitleLengthException("Title length exceeds limit: " + title.length());
        }
    }

    public static void validateContent(String content) throws InvalidContentLengthException, EmptyContentException {
        if (content == null) {
            throw new EmptyContentException("Content cannot be null");
        }
        if (content.length() > 200) {
            throw new InvalidContentLengthException("Content length exceeds limit: " + content.length());
        }
    }

    public static void validateUserName(String username) throws InvalidUserNameLengthException, EmptyUserNameException, InvalidUserNameException {
        if (username == null) {
            throw new EmptyUserNameException("Username cannot be null");
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new InvalidUserNameException("Invalid username: " + username);
        }
        if (username.length() > 30) {
            throw new InvalidUserNameLengthException("Username length exceeds limit: " + username.length());
        }
    }

    public static class InvalidTitleLengthException extends Exception {
        public InvalidTitleLengthException(String error) {
            super(error);
        }
    }

    public static class InvalidUserNameLengthException extends Exception {
        public InvalidUserNameLengthException(String error) {
            super(error);
        }
    }

    public static class InvalidContentLengthException extends Exception {
        public InvalidContentLengthException(String error) {
            super(error);
        }
    }

    public static class InvalidFollowLengthException extends Exception {
        public InvalidFollowLengthException(String error) {
            super(error);
        }
    }

    public static class InvalidPasswordLengthException extends Exception {
        public InvalidPasswordLengthException(String error) {
            super(error);
        }
    }

    public static class InvalidUserRoleException extends Exception {
        public InvalidUserRoleException(String error) {
            super(error);
        }
    }

    public static class EmptyPasswordException extends Exception {
        public EmptyPasswordException(String error) {
            super(error);
        }
    }

    public static class EmptyUserRoleException extends Exception {
        public EmptyUserRoleException(String error) {
            super(error);
        }
    }

    public static class EmptyFollowException extends Exception {
        public EmptyFollowException(String error) {
            super(error);
        }
    }

    public static class EmptyTitleException extends Exception {
        public EmptyTitleException(String error) {
            super(error);
        }
    }

    public static class EmptyContentException extends Exception {
        public EmptyContentException(String error) {
            super(error);
        }
    }

    public static class EmptyUserNameException extends Exception {
        public EmptyUserNameException(String error) {
            super(error);
        }
    }

    public static class InvalidUserNameException extends Exception {
        public InvalidUserNameException(String error) {
            super(error);
        }
    }
    
}
