package com.tejas.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.tejas.entity.CitizenPlan;
import com.tejas.repo.CitizenPlanRepository;
import com.tejas.request.SearchRequest;
import com.tejas.util.EmailUtils;
import com.tejas.util.ExcelGenerator;
import com.tejas.util.PdfGenerator;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private CitizenPlanRepository planRepo;

	@Autowired
	private ExcelGenerator excelGenerator;

	@Autowired
	private PdfGenerator pdfGenerator;

	@Override
	public List<String> getPlanNames() {

		List<String> planNames = planRepo.getPlanNames();

		return planNames;
	}

	@Override
	public List<String> getPlanStatuses() {

		return planRepo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> search(SearchRequest request) {

		CitizenPlan entity = new CitizenPlan();

		if (null != request.getPlanName() && !"".equals(request.getPlanName())) {
			entity.setPlanName(request.getPlanName());
		}

		if (null != request.getPlanStatus() && !"".equals(request.getPlanStatus())) {
			entity.setPlanStatus(request.getPlanStatus());
		}

		if (null != request.getGender() && !"".equals(request.getGender())) {
			entity.setGender(request.getGender());
		}

		if (null != request.getPlanStartDate() && !"".equals(request.getPlanStartDate())) {
			String planStartDate = request.getPlanStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// convert String to LocalDate
			LocalDate localDate = LocalDate.parse(planStartDate, formatter);
			entity.setPlanStartDate(localDate);
		}

		if (null != request.getPlanEndDate() && !"".equals(request.getPlanEndDate())) {
			String planEndDate = request.getPlanEndDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// convert String to LocalDate
			LocalDate localDate = LocalDate.parse(planEndDate, formatter);
			entity.setPlanEndDate(localDate);
		}

		return planRepo.findAll(Example.of(entity));
	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception {

		File f = new File("Plans.xls");

		List<CitizenPlan> plans = planRepo.findAll();

		excelGenerator.generate(response, plans, f);

		String subject = "Test Mail Subject";
		String body = "<h1> Test Mail Body </h1>";
		String to = "yyy@gmail.com";

		emailUtils.sendEmail(subject, body, to, f);

		// After attachment & downloading in browser delete the file form server
		// (Workspace folder)
		f.delete();

		return true;
	}

	@Override
	public boolean exportPdf(HttpServletResponse response) throws Exception {

		File f = new File("Plans.pdf");

		List<CitizenPlan> plans = planRepo.findAll();

		pdfGenerator.generate(response, plans, f);

		String subject = "Test Mail Subject";
		String body = "<h1> Test Mail Body </h1>";
		String to = "yyy@gmail.com";

		emailUtils.sendEmail(subject, body, to, f);

		// After attachment & downloading in browser delete the file form server
		// (Workspace folder)
		f.delete();

		return true;
	}

}
