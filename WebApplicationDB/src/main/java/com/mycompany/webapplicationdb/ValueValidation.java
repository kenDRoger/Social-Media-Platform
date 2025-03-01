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

    public static void validatePassword(String password) throws InvalidPasswordLengthException, EmptyPasswordException{
        if(password==null){
            throw new EmptyPasswordException();
        }
        if(password.length()>30){
            throw new InvalidPasswordLengthException();
        }
    }

    public static void validateUserRole(String userRole) throws InvalidUserRoleException, EmptyUserRoleException{
        if(userRole==null){
            throw new EmptyUserRoleException();
        }
        if(!userRole.equals("admin") && !userRole.equals("super_admin") && !userRole.equals("user")){
            throw new InvalidUserRoleException();
        }
    }

    public static void validateFollow(String follow) throws InvalidFollowLengthException, EmptyFollowException{
        if(follow==null){
            throw new EmptyFollowException();
        }
        if(follow.length()>30){
            throw new InvalidFollowLengthException();
        }
    }

    public static void validateTitle(String title) throws InvalidTitleLengthException, EmptyTitleException{
        if(title==null){
            throw new EmptyTitleException();
        }
        if(title.length()>30){
            throw new InvalidTitleLengthException();
        }
    }
    public static void validateContent(String content) throws InvalidContentLengthException, EmptyContentException{
        if(content==null){
            throw new EmptyContentException();
        }
        if(content.length()>200){
            throw new InvalidContentLengthException();
        }
    }
    
    public static void validateUserName(String username) throws InvalidUserNameLengthException, EmptyUserNameException, InvalidUserNameException{
        if(username==null){
            throw new EmptyUserNameException();
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new InvalidUserNameException();
        }
        if(username.length()>30){
            throw new InvalidUserNameLengthException();
        }
    }

    public static class InvalidTitleLengthException extends Exception {

        public InvalidTitleLengthException() {
        }
    }

    public static class InvalidUserNameLengthException extends Exception {

        public InvalidUserNameLengthException() {
        }
    }

    public static class InvalidContentLengthException extends Exception {

        public InvalidContentLengthException() {
        }
    }

    public static class InvalidFollowLengthException extends Exception {

        public InvalidFollowLengthException() {
        }
    }

    public static class InvalidPasswordLengthException extends Exception {

        public InvalidPasswordLengthException() {
        }
    }

    public static class InvalidUserRoleException extends Exception {

        public InvalidUserRoleException() {
        }
    }

    public static class EmptyPasswordException extends Exception {

        public EmptyPasswordException() {
        }
    }

    public static class EmptyUserRoleException extends Exception {

        public EmptyUserRoleException() {
        }
    }

    public static class EmptyFollowException extends Exception {

        public EmptyFollowException() {
        }
    }

    public static class EmptyTitleException extends Exception {

        public EmptyTitleException() {
        }
    }

    public static class EmptyContentException extends Exception {

        public EmptyContentException() {
        }
    }

    public static class EmptyUserNameException extends Exception {

        public EmptyUserNameException() {
        }
    }

    public static class InvalidUserNameException extends Exception {

        public InvalidUserNameException() {
        }
    }
    
}
