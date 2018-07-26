package top.lrshuai.i18n.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping(path={"/","/index"})
	public Object index(){
		return "index";
	}
}
