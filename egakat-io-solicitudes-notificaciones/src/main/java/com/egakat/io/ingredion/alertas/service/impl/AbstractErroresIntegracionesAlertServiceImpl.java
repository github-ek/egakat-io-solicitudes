package com.egakat.io.ingredion.alertas.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.web.util.HtmlUtils;

import com.egakat.core.alertas.service.impl.AlertServiceImpl;
import com.egakat.core.mail.dto.MailMessageDto;
import com.egakat.integration.dto.ErrorIntegracionDto;
import com.egakat.io.ingredion.alertas.dto.ErrorActaDto;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractErroresIntegracionesAlertServiceImpl
		extends AlertServiceImpl<List<ErrorActaDto>, MailMessageDto> {

	protected static final String PARAM_TITLE = "title";
	protected static final String PARAM_ERRORES = "errores";
	protected static final String PARAM_SUBTOTALES = "subtotales";
	protected static final String PARAM_XLS = "xls";

	@Override
	protected String getSubject() {
		return getCode();
	}

	@Override
	protected Map<String, Object> getData(List<ErrorActaDto> list) {
		val result = new HashMap<String, Object>();

		val subtotal = new StringBuilder();
		val detail = new StringBuilder();
		val xls = new ArrayList<List<String>>();
		xls.add(getExcelHeaders());

		val estados = getGruposByEstadoAndSubEstado(list);

		estados.forEach((estado, subestados) -> {
			subestados.forEach((subestado, whids) -> {
				whids.forEach((whid, bodegas) -> {
					bodegas.forEach((bodega, ciudades) -> {
						ciudades.forEach((ciudad, actas) -> {
							appendSubtotal(subtotal, estado, subestado, whid, bodega, ciudad, actas.size());
							actas.stream().forEach(acta -> {
								appendDetail(detail, acta);
								appendxls(xls, acta);
							});
						});
					});
				});
			});
		});

		result.put(PARAM_SUBTOTALES, subtotal.toString());
		result.put(PARAM_ERRORES, detail.toString());
		result.put(PARAM_XLS, xls);

		return result;
	}

	protected Map<String, Map<String, Map<String, Map<String, Map<String, List<ErrorActaDto>>>>>> getGruposByEstadoAndSubEstado(
			List<ErrorActaDto> actas) {

		val comparator = Comparator.comparing(ErrorActaDto::getEstadoIntegracion)
				.thenComparing(ErrorActaDto::getSubEstadoIntegracion).thenComparing(ErrorActaDto::getWhid)
				.thenComparing(ErrorActaDto::getBodegaCodigoAlterno).thenComparing(ErrorActaDto::getCiudadNombreAlterno)
				.thenComparing(ErrorActaDto::getNumeroSolicitud);

		val groupingBy = Collectors.groupingBy(ErrorActaDto::getEstadoIntegracion,
				Collectors.groupingBy(ErrorActaDto::getSubEstadoIntegracion,
						Collectors.groupingBy(ErrorActaDto::getWhid,
								Collectors.groupingBy(ErrorActaDto::getBodegaCodigoAlterno,
										Collectors.groupingBy(ErrorActaDto::getCiudadNombreAlterno)))));

		val stream = actas.stream();
		val result = stream.sorted(comparator).collect(groupingBy);
		return result;
	}

	protected void appendSubtotal(StringBuilder sb, String estado, String subestado, String whid,
			String bodegaCodigoAlterno, String ciudadNombreAlterno, int n) {
		sb.append("<tr>");
		sb.append("<td>").append(estado).append("</td>");
		sb.append("<td>").append(subestado).append("</td>");
		sb.append("<td>").append(whid).append("</td>");
		sb.append("<td>").append(bodegaCodigoAlterno).append("</td>");
		sb.append("<td>").append(ciudadNombreAlterno).append("</td>");
		sb.append("<td style=\"text-align: right\">").append(n).append("</td>");
		sb.append("</tr>\n");
	}

	protected void appendDetail(StringBuilder sb, ErrorActaDto acta) {
		val formatter = getFormatterDateTime();
		val comparator = Comparator.comparing(ErrorIntegracionDto::getFechaCreacion);

		val list = acta.getErrores().stream().sorted(comparator);

		list.forEach(error -> {
			sb.append("<tr>");
			sb.append("<td>").append(acta.getEstadoIntegracion()).append("</td>");
			sb.append("<td>").append(acta.getSubEstadoIntegracion()).append("</td>");
			sb.append("<td>").append(acta.getWhid()).append("</td>");
			sb.append("<td>").append(acta.getBodegaCodigoAlterno()).append("</td>");
			sb.append("<td>").append(acta.getCiudadNombreAlterno()).append("</td>");
			sb.append("<td>").append(acta.getId()).append("</td>");
			sb.append("<td>").append(acta.getNumeroSolicitud()).append("</td>");
			sb.append("<td>").append(acta.getDestinatarioNombre()).append("</td>");
			sb.append("<td>").append(formatter.format(error.getFechaCreacion())).append("</td>");
			sb.append("<td>").append(error.getCodigo()).append("</td>");
			sb.append("<td>").append(HtmlUtils.htmlEscape(error.getMensaje())).append("</td>");
			sb.append("</tr>\n");
		});
	}

	private List<String> getExcelHeaders() {
		return Arrays.asList("ESTADO", "SUBESTADO", "BODEGA WMS", "BODEGA INGREDION", "CIUDAD", "ID",
				"NÚMERO DE SOLICITUD", "DESTINATARIO", "FECHA", "CÓDIGO", "MENSAJE");
	}

	private void appendxls(List<List<String>> xls, ErrorActaDto acta) {
		val formatter = getFormatterDateTime();

		val group = Arrays.asList(acta.getEstadoIntegracion(), acta.getSubEstadoIntegracion(), acta.getWhid(),
				acta.getBodegaCodigoAlterno(), acta.getCiudadNombreAlterno(), String.valueOf(acta.getId()),
				acta.getNumeroSolicitud(), acta.getDestinatarioNombre());

		acta.getErrores().forEach(error -> {
			val row = new ArrayList<String>(group);
			row.addAll(Arrays.asList(formatter.format(error.getFechaCreacion()), error.getCodigo(),
					error.getMensaje()));

			xls.add(row);
		});
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// --
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected File[] getAttachments(String reportCode, Map<String, Object> data) {
		
		@SuppressWarnings("unchecked")
		val xls = (List<List<String>>) data.get(PARAM_XLS);
		
		XSSFWorkbook workbook = createWorkbook(xls);

		val filename = getFileName();
		val file = writeWorkbook(filename, workbook);

		val result = new File[] { file };
		return result;
	}

	protected String getFileName() {
		val now = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
		val result = getCode() + "-" + now + "-";
		return result;
	}

	protected XSSFWorkbook createWorkbook(List<List<String>> data) {
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet();
		populateSheet(sheet, data);
		formatSheet(sheet, data);

		return workbook;
	}

	protected void populateSheet(XSSFSheet sheet, List<List<String>> data) {
		int rowNum = 0;
		for (val record : data) {
			XSSFRow row = sheet.createRow(rowNum++);

			int colNum = 0;
			for (val field : record) {
				XSSFCell cell = row.createCell(colNum++);
				cell.setCellValue(field);
			}
		}
	}

	protected void formatSheet(XSSFSheet sheet, List<List<String>> data) {
		@SuppressWarnings("deprecation")
		XSSFTable my_table = sheet.createTable();
		CTTable cttable = my_table.getCTTable();
		CTTableStyleInfo table_style = cttable.addNewTableStyleInfo();
		table_style.setName("TableStyleMedium2");

		/* Define the data range including headers */
		val row = data.size();
		if (row > 0) {
			val col = data.get(0).size();
			val ref1 = new CellReference(0, 0);
			val ref2 = new CellReference(row - 1, col - 1);
			AreaReference range = new AreaReference(ref1, ref2, SpreadsheetVersion.EXCEL2007);
			/* Set Range to the Table */
			cttable.setRef(range.formatAsString());
			/* this is the display name of the table */
			cttable.setDisplayName("ERRORES");
			/* This maps to "displayName" attribute in &lt;table&gt;, OOXML */
			cttable.setName("ERRORES");
			// id attribute against table as long value
			cttable.setId(1L);

			CTTableColumns columns = cttable.addNewTableColumns();
			columns.setCount(col);
			/* Define Header Information for the Table */
			for (int i = 0; i < col; i++) {
				CTTableColumn column = columns.addNewTableColumn();
				column.setName("Column" + i);
				column.setId(i + 1);
				sheet.autoSizeColumn(i);
			}
		}
	}

	protected File writeWorkbook(String filename, XSSFWorkbook workbook) {
		File file;
		try {
			file = File.createTempFile(filename, ".xlsx");
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
				workbook.write(outputStream);
				workbook.close();
			}
			return file;
		} catch (IOException e) {
			log.error("Ocurrio un error al intentar crear el archivo adjunto " + filename, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected MailMessageDto createMessage(String code, String subject, String content, File[] attachments) {
		return new MailMessageDto(code, subject, content, attachments);
	}
}
