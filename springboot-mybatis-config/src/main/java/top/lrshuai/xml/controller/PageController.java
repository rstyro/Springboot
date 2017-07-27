package top.lrshuai.xml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping(value={"/index","/"})
	public String index(Model model){
		model.addAttribute("title", "测试");
		model.addAttribute("atext", "这个冬天不太Cool");
		return "index";
	}
	
}
