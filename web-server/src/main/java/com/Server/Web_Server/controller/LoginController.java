package com.Server.Web_Server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    @GetMapping("/")
    public String loginPage(){

        System.out.println("EXECUTED!!!");

        return "loginTemplate/login";
    }

    @PostMapping("/login/post")
    public String loginPage_post(){     //User unidentified_user
        //Id, password가 존재하지 않는다면 예외처리
//        User identified_user = authenticateUser(unidentified_user.getUsername(), unidentified_user.getPassword());
//        if(identified_user == null){
//            //alert 메세지 출력
//            //Get 부분에서 해야할듯
//            return "redirect:/";
//        }

        return "";
    }

    @GetMapping("/login/create")
    public String createAccountPage(){
        System.out.println("EXECUTED!!!");

        return "/loginTemplate/createAccount";
    }



    @PostMapping("login/create/post")
    public String createAccountPage_post(){     //User newUser

//        if( userRepository.findByID(newUser.getId()) == null){
//            //예외처리
//            //return "redirect:/login/create"/
//        }
//
//        addNewUser(newUser.getUserName(), newUser.getPassword(), newUser.getEmail());
//        //같은 이름을 가지는 User, ID가 있다면 예외처리
//
//        //return "redirect:/login/create";

        return "";
    }

}
