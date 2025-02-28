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

    public static void validatePassword(String password) throws InvalidPasswordException{
        if(password.length()>30){
            throw new InvalidPasswordException();
        }
    }

    public static void validateUserRole(String userRole) throws InvalidUserRoleException{   
        if(!userRole.equals("admin") && !userRole.equals("super_admin") && !userRole.equals("guest")){
            throw new InvalidUserRoleException();
        }
    }

    public static void validateFollow(String follow) throws InvalidFollowException{
        if(follow.length()>30){
            throw new InvalidFollowException();
        }
    }

    public static void validateTitle(String title) throws InvalidTitleException{
        if(title.length()>30 || title==null){
            throw new InvalidTitleException();
        }
    }
    public static void validateContent(String content) throws InvalidContentException{
        if(content.length()>200){
            throw new InvalidContentException();
        }
    }
    
    public static void validateUserName(String username) throws InvalidUserNameException{
        if(username.length()>30){
            throw new InvalidUserNameException();
        }
    }

    public static class InvalidTitleException extends Exception {

        public InvalidTitleException() {
        }
    }

    public static class InvalidUserNameException extends Exception {

        public InvalidUserNameException() {
        }
    }

    public static class InvalidContentException extends Exception {

        public InvalidContentException() {
        }
    }

    public static class InvalidFollowException extends Exception {

        public InvalidFollowException() {
        }
    }

    public static class InvalidPasswordException extends Exception {

        public InvalidPasswordException() {
        }
    }

    public static class InvalidUserRoleException extends Exception {

        public InvalidUserRoleException() {
        }
    }
    
}
