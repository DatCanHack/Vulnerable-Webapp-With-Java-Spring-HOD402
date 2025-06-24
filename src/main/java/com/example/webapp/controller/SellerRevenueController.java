package com.example.webapp.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import io.netty.handler.codec.http.HttpHeaders;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Path;

@Controller
@RequestMapping("/seller/revenue")
public class SellerRevenueController {

    @GetMapping("/upload")
    public String showUploadForm() {
        return "seller/revenue/upload";
    }

   @PostMapping("/upload")
public String handleUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    try {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file.getInputStream());

        String month = doc.getElementsByTagName("month").item(0).getTextContent();
        String data = doc.getElementsByTagName("data").item(0).getTextContent();

        redirectAttributes.addFlashAttribute("message", "✅ Upload thành công!");
        redirectAttributes.addFlashAttribute("month", month);
        redirectAttributes.addFlashAttribute("data", data);
        // Save file 
        //String originalFilename = file.getOriginalFilename();
        //File destinationFile = new File(uploadDir, originalFilename);
        //file.transferTo(destinationFile);
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "❌ Lỗi xử lý XML: " + e.getMessage());
        return "redirect:/templates/error";
    }

    return "redirect:/seller/revenue/upload";
}

//Direct to /uploads/file


/*@GetMapping("/download")
    public ResponseEntity<Resource> downloadRevenueReport(@RequestParam("file") String filename) {
        try {
            // LỖ HỔNG 1: PATH TRAVERSAL
            Path filePath = Paths.get(REPORT_DIRECTORY + filename);
            System.out.println("[SERVER] Đọc file tại: " + filePath.toAbsolutePath());

            // LỖ HỔNG 2: JAVA DESERIALIZATION
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath));
            Object reportObject = ois.readObject(); // <--- RCE ĐƯỢC KÍCH HOẠT TẠI ĐÂY
            ois.close();

            // Phục vụ việc tải file về
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            System.err.println("[SERVER] Lỗi nghiêm trọng khi xử lý file: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    } */
}
