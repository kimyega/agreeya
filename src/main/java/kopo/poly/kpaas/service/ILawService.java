package kopo.poly.kpaas.service;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.LawDTO;

import java.io.File;
import java.util.List;

public interface ILawService {

    int bulkInsertLaws(List<LawDTO> pList, HttpServletRequest request) throws Exception;

    int parseXmlAndInsert(File xmlFile, int countryId, HttpServletRequest request) throws Exception;

}
