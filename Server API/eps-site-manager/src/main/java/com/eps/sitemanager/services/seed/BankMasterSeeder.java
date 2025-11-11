package com.eps.sitemanager.services.seed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.repository.master.BankMasterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankMasterSeeder implements CommandLineRunner {

    private final BankMasterRepository bankMasterRepository;

    @Override
    @Transactional
    public void run(String... args) {
        initializeBanks();
    }

    private void initializeBanks() {
        List<BankMaster> defaultBanks = List.of(
            createBank("BOI", "Bank of India"),
            createBank("HDFC", "HDFC"),
            createBank("CBI", "Central Bank of India"),
            createBank("UBI", "Union Bank of India"),
            createBank("SBI", "State Bank of India"),
            createBank("BOB", "Bank of Baroda"),
            createBank("BOM", "Bank of Maharashtra"),
            createBank("OBC", "Oriental Bank of Commerce"),
            createBank("PNB", "Punjab National Bank"),
            createBank("Canara", "Canara Bank"),
            createBank("Dena", "Dena Bank"),
            createBank("KGB", "Kerala Gramin Bank"),
            createBank("COB", "Corporation Bank"),
            createBank("IDBI", "Industrial Development Bank of India"),
            createBank("EPSB", "EPSBANCS"),
            createBank("IDFC", "Infrastructure Development Finance Company"),
            createBank("IOB", "Indian Overseas Bank")
        );

        for (BankMaster bank : defaultBanks) {
            List<BankMaster> existing = bankMasterRepository.findByBankCode(bank.getBankCode());
            if (existing == null || existing.isEmpty()) {
                bankMasterRepository.save(bank);
                log.info("Created bank: {} - {}", bank.getBankCode(), bank.getBankName());
            } else {
                log.debug("Bank already exists: {}", bank.getBankCode());
            }
        }
    }

    private BankMaster createBank(String code, String name) {
        BankMaster bank = new BankMaster();
        bank.setBankCode(code);
        bank.setBankName(name);
        return bank;
    }
}
