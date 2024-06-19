package com.example.Perpustakaan.service.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public class CustomPDFTextStripper extends PDFTextStripper {

    public CustomPDFTextStripper() throws IOException {
    }

    @Override
    protected void writeString(String string) throws IOException {
        // Overriding this method to handle custom text extraction logic if needed
        super.writeString(string);
    }

    public String getText(PDDocument document) throws IOException {
        return super.getText(document);
    }
}
