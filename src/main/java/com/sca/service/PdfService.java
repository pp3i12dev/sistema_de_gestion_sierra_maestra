package com.sca.service;

import java.util.List;
import java.util.Map;

public interface PdfService {
    byte[] generatePdf(List<Map<String, Object>> data) throws Exception;
}
