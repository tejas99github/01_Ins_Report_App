package com.tejas.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tejas.entity.CitizenPlan;
import com.tejas.request.SearchRequest;
import com.tejas.service.ReportService;

@Controller
public class ReportController {

	@Autowired
	private ReportService service;

	@GetMapping("/pdf")
	public void pdfExport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment;filename=plans.pdf");
		service.exportPdf(response);

	}

	@GetMapping("/excel")
	public void excelExport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "attachment;filename=plans.xls");
		service.exportExcel(response);
	}

	@PostMapping("/search")
	public String handleSearch(@ModelAttribute("Search") SearchRequest Search, Model model) {
		List<CitizenPlan> plans = service.search(Search);
		model.addAttribute("plans", plans);
		init(model);
		return "index";
	}

	// This Method is used to load index page

	@GetMapping("/")
	public String indexPage(Model model) {
		model.addAttribute("Search", new SearchRequest());
		init(model);
		return "index";
	}

	private void init(Model model) {
		model.addAttribute("names", service.getPlanNames());
		model.addAttribute("status", service.getPlanStatuses());
	}
}
