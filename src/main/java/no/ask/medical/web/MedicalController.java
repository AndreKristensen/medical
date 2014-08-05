package no.ask.medical.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MedicalController {

    
    @RequestMapping("/")
    public String getIndex(){
        System.out.println("yo");
        return "index";
    }
    
}
