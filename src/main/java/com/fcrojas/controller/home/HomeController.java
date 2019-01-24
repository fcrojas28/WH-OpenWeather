package com.fcrojas.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author frojas
 *
 */
@Controller
public class HomeController {

	@RequestMapping("/")
	public String home() {
		return "home";
	}
}
