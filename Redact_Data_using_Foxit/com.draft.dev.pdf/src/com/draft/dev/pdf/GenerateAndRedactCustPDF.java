package com.draft.dev.pdf;

import com.foxit.sdk.common.*;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.RectFArray;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;
import com.foxit.sdk.pdf.TextSearch;
import com.foxit.sdk.pdf.annots.DefaultAppearance;
import com.foxit.sdk.pdf.annots.Redact;
import com.foxit.sdk.pdf.interform.Control;
import com.foxit.sdk.pdf.interform.Field;
import com.foxit.sdk.pdf.interform.Form;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.addon.Redaction;

public class GenerateAndRedactCustPDF {

	// Load the library.
	// You can also use System.load("filename") instead. The filename argument must
	// be an absolute path name.
	// -Djava.library.path="E:/Draft_Dev/Redact_Data_In_PDF/foxitpdfsdk_7_6_win_java/lib/"
	/**
	 * To load the Foxit SDK libaries based on your windows.
	 */
	public static void loadlib() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			if (System.getProperty("sun.arch.data.model").equals("64")) {
				System.loadLibrary("fsdk_java_win64");
			} else {
				System.loadLibrary("fsdk_java_win32");
			}
		} else {
			if (System.getProperty("sun.arch.data.model").equals("64")) {
				System.loadLibrary("fsdk_java_linux64");
			} else {
				System.loadLibrary("fsdk_java_linux32");
			}
		}
	}

	/**
	 * @return
	 */
	public static boolean loadLicense() {
		boolean licenseState = false;
		// Initialize the library.
		String sn = "T7j/Rad064vQ3pMnFs6NhyPNMyKtZ+cuITAt28sGmn12jEtdNe4zbw==";
		String key = "8f0YFUGMvRkN+ddwhrBxubRv+38GzmILMFDAylEsKf8LJ6zGRC2G5OVn8FLVO7vEe3d8XxoPzOjFbXLya4rrL+46U94JomiPpWBxxU9Mo4wL3DHmO3EN9L44A8Yk8+J2HA26XRskvNIDPoeK8TKDYsNAZ4prIDTvjhGKH5iU2IVaVHO1uceS0NdBaCBftMDg5j2CgCk6p3gFpjEIq4Pvr1mJhEgR2ZLzia0IbrZxRf1Mjv8VCJr33vrJkLa16wlM+6N4NsDB1mIuex3xhnTAkMLfH90iZ+oHgAGzXGFEiPuKcbbkWVQT9SrGFKhZ1njO5TRFfGQBkueHLoeT12cRvKMFXVF5NbKAT37yhq64r1NOKeAHoEL0e8BGzdKeLzN469JJCiFo2ooX5I8L4uGCLXkAHKGYYNnXwtw01o4Iqh0lbCgLV1XFPX2s+O8K/gqsk/2DLlCc5hTYPyg+EPV/6j+kIi+rg6RNYXzNCcMwTKX2dmb6PszcFAA0unTrlySX8UlvZuAQJdhVyyT4HhkxBPSj9qDiEyMRbkjw/N9eM0YqL9XyLt/xzkDbDa5KNv6dker+AYD/ue0eLFO3/1qKJyk+zDS/9VTy5+qXt780SY6KSPXmmU7RWFOvamleMP29os9mnn8uAljhXw0VQl3JKYJNBycM6U2EzyEZ/xMZdJxvupjqE9Qtw1TPDGGIUixfC3pD0c8WpH4c4sflQlMtp/jKo7M55nJgKWEj0xnAaKyF1YCvZS8/CqVo8hSUubEt/moj6cZ0lDZUZj2t5YLyrQr7/83mGV5lWtlHvPBM1FGD+0QUCSNNnGF4azWYPOOZQ7sDNYYl6xQEJTiMxx1gXERH3EOOE/QrnrAjN/y0hVjsDHzPXelGN/vExF5n/qDT19+4cPK9MUz7gvw7rvPc8hpTNbfV5c1hrhUck25bYFP8+i3TIFLAY0VWY0br17yAKKMmgsZIKRq93jKFWMysrxY2F9NlUGIu/Hy0CIL4VYfTLkGw3mTHO84i+sd0sZ6os0qv2hETr0eFktXjzwLfVAnljfr/OlIi/rr4qupMfQvY6564tQGti+5xHiKRtM2Ro45EDwygHqVjR4AllWpTRWPfWp42r889lLMK8xlbuG9Sv11ykovaGW1uet55030cv6pXFMmrvk6VCGmWJ/e91dXqwxdn1ERhARpc1X18SUb+PP2Fx+qkaGLfIalP/4jUzDKUzUHnlJowjOw8nGvyGTxmIhbSiijcU0l5zDCIUbk04KwuDECg2YPntsdAelEmF3nE14xUnCDhBlcE1zzBjMGQn6yPZ34gTpHqOT6i+SEW8RM/6+I=";
		int error_code = Library.initialize(sn, key);
		if (error_code != e_ErrSuccess)
			licenseState = true;

		return licenseState;
	}

	/**
	 * This method creates a PDF file and appends the field data that is passed as a
	 * map.
	 * 
	 * @return
	 */
	public static boolean createPDF(String fileName, Map<String, String[]> fieldData) {
		try {

			// Creating a new PDF DOC. The empty constructor PDFDOC() creates a new file
			// rather opening the existing file.
			PDFDoc doc = new PDFDoc();

			// Insert a page in the newly created PDF file with the page size a letter
			PDFPage page = doc.insertPage(0, PDFPage.e_SizeLetter);

			// Create a new form to create new fields inside the doc.
			Form form = new Form(doc);

			// Iterator variable to increment after creating each field.
			int iteration = 0;

			// Creating the Fields based on the Field data
			for (Entry<String, String[]> entry : fieldData.entrySet()) {

				// To position the field, this offset is used.
				int offset = iteration * 60;

				// Add a control to the form using addcontrol() and the Key as entry.getKey().
				// This key contains name of the each field passed from the HTML Form. For
				// E.g. FirstName. It'll also place the control in the reactangles generated
				// using the offset.
				Control control = form.addControl(page, entry.getKey(), Field.e_TypeTextField,
						new RectF(50f, 600f - offset, 400f, 640f - offset));

				// Getting the created field from the control to set its value.
				Field field = control.getField();

				// Set the value of the Field. For Eg. FirstName : Vikram
				field.setValue(entry.getKey() + ": " + entry.getValue()[0]);

				// Increment the iteration. So the next field is created in the next line.
				iteration++;

			}

			page.flatten(true, PDFPage.e_FlattenAll);

			// Save the file locally and return the file's location to the caller.
			doc.saveAs(fileName, e_SaveFlagNoOriginal);

		} catch (PDFException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method is used to redact the text in the PDF File
	 * 
	 * @param fileName
	 * @param textToRedact
	 */
	public static void redactText(String fileName, String textToRedact) {
		try {
			// Open the PDF file(filename) object thats passed.
			PDFDoc doc = new PDFDoc(fileName);

			// Load the document
			int error_code = doc.load(null);

			// Get the first PDF Page in the Page2 object
			PDFPage page = doc.getPage(0);

			// Load the doc into the Redaction Object
			Redaction redaction = new Redaction(doc);

			// Start Parsing the PDF page.
			page.startParse(PDFPage.e_ParsePageNormal, null, false);

			// Creating a Text page Object using the first Page. This is required to create
			// a Text Search.
			TextPage text_page = new TextPage(page, TextPage.e_ParseTextNormal);

			// Create a Text Search option using the Text page you have created for the
			// first PDF Page.
			TextSearch text_search = new TextSearch(text_page);

			// Create an Array which will store the rectangles of the Matched Text.
			RectFArray matched_rect_array = new RectFArray();

			// Set the Text pattern using the Text to Redact.
			text_search.setPattern(textToRedact);

			// Iterate through the texts in the page and if the text is found add the
			// rectangles of the relevant text to the matched_rect_array array.
			while (text_search.findNext()) {
				RectFArray temp_rect_array = text_search.getMatchRects();
				for (int z = 0; z < temp_rect_array.getSize(); z++)
					matched_rect_array.add(temp_rect_array.getAt(z));
			}

			// If the matched rectangle array size is more than 0 means, there is a matching
			// text found during search. proceed to Redact.
			if (matched_rect_array.getSize() > 0) {

				// Mark and Redact annotate the identified rectangles.
				Redact redact = redaction.markRedactAnnot(page, matched_rect_array);

				// Reset the apperance stream() and save the document. This will just have a red
				// mark around the text
				redact.resetAppearanceStream();
				doc.saveAs("c:/temp/" + "AboutFoxit_redected_default.pdf", PDFDoc.e_SaveFlagNormal);

				// Set the border colours and fill colours and reset appearance stream. So the
				// text is fully hidden.

				// set border color to Green
				redact.setBorderColor((long) 0x00FF00);
				// set fill color to Blue
				redact.setFillColor((long) 0x0000FF);
				// set rollover fill color to Red
				redact.setApplyFillColor((long) 0xFF0000);

				// Reset the apperance stream() and save the document. This will hide the text
				// using the fill colour red.
				redact.resetAppearanceStream();
				doc.saveAs("c:/temp/" + "AboutFoxit_redected_setColor.pdf", PDFDoc.e_SaveFlagNormal);

				// Change the opacity of the fill colour to half. So the Redacted text is still
				// visible.

				redact.setOpacity((float) 0.5);

				// save the document. This will hide the text using the fill colour red with
				// opacity 0.5.Text will be visible.
				redact.resetAppearanceStream();
				doc.saveAs("c:/temp/" + "AboutFoxit_redected_setOpacity.pdf", PDFDoc.e_SaveFlagNormal);

				// Readaction.apply.
				redaction.apply();
			}
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		loadlib();

		loadLicense();

		Map<String, String[]> myMap = new HashMap<String, String[]>();

		String[] val = { "Vikram" };
		myMap.put("First Name", val);

		String[] val2 = { "Aruchamy" };
		myMap.put("Last Name", val2);

		String[] val3 = { "123456789" };
		myMap.put("SSN", val3);

		String[] val4 = { "123456789" };
		myMap.put("CCN", val4);

		createPDF("c:/temp/new_Sample18.pdf", myMap);

		redactText("c:/temp/new_Sample18.pdf", "ABC");

		System.gc();
	}
}
