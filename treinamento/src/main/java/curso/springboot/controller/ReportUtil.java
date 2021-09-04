package curso.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public byte[] gerarRelatorio(List dados, String nomeRelatorio, ServletContext servletContext) throws Exception{
		
		/* Gera uma coleção de dados com da nossa lista de dados (Essa coleção é entendida pelo relatório) */
		JRBeanCollectionDataSource colecaoDados = new JRBeanCollectionDataSource(dados);
		
		/* Carrega o caminho do DataReport que deve ser compilado */
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
		
		/* Carrega e Alimenta o arquivo Jasper (DataReport) com nossa coleção de dados */
		JasperPrint relatorio = JasperFillManager.fillReport(caminhoJasper, new HashedMap(), colecaoDados);
		
		/* Retorna o relatório exportado para PDF (byte), para que seja possível baixar */
		return JasperExportManager.exportReportToPdf(relatorio);
	}

}
