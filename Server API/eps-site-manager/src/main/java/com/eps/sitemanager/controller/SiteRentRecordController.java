package com.eps.sitemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.data.validator.SiteRentRecordExcelProcessorService;

@RestController
@RequestMapping("/api/rent-records")
public class SiteRentRecordController {

	@Autowired
	private SiteRentRecordExcelProcessorService siteRentRecordExcelProcessorService;

	@PostMapping("/upload/{rentForMonth}")
	public ResponseEntity<?> processExcel(@RequestParam("file") MultipartFile file, @PathVariable String rentForMonth) {

		return siteRentRecordExcelProcessorService.processExcelFile(file, rentForMonth);

	}
	
//	@GetMapping("/getrentrecord/{siteId}")
//	public ResponseEntity<Object> getRentRecord(@PathVariable int siteId) {

   }