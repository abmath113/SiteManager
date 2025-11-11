package com.eps.sitemanager.services;

import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.SiteHistoryDTO;

public interface SiteHistoryService {

	SiteHistoryDTO getSiteHistoryBySiteCode(String siteCode);
	
	
}
