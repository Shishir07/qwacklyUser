package com.qwackly.user.controller;

import com.qwackly.user.handler.MyResourceHttpRequestHandler;
import com.qwackly.user.model.DBFileEntity;
import com.qwackly.user.properties.FileStorageProperties;
import com.qwackly.user.response.UploadFileResponse;
import com.qwackly.user.service.DBFileStorageService;
import com.qwackly.user.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequestMapping("/v1")
@RestController
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    @Autowired
    private MyResourceHttpRequestHandler handler;

    @Autowired
    private FileStorageProperties fileStorageProperties;


    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType( resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +  resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file-> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/downloadDirectory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> downloads(final HttpServletResponse response, @RequestParam String dir) {
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=yourFile.zip");
        StreamingResponseBody stream = out -> {
            final String home = System.getProperty("user.home");
            final File directory = new File(home + File.separator + dir);
            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            if(directory.exists() && directory.isDirectory()) {
                try {
                    for (final File file : directory.listFiles()) {
                        final InputStream inputStream=new FileInputStream(file);
                        final ZipEntry zipEntry=new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);
                        byte[] bytes=new byte[1024];
                        int length;
                        while ((length=inputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        inputStream.close();
                    }
                    zipOut.close();
                } catch (final IOException e) {
                    logger.error("Exception while reading and streaming data {} ", e);
                }
            }
        };
        logger.info("steaming response {} ", stream);
        return new ResponseEntity(stream, HttpStatus.OK);
    }



    @PostMapping("/uploadFileinDB")
    public UploadFileResponse uploadFileinDB(@RequestParam("file") MultipartFile file) {
        DBFileEntity dbFile = dbFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFileinDB/")
                .path(dbFile.getId())
                .toUriString();

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFilesinDB")
    public List<UploadFileResponse> uploadMultipleFilesinDB(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFileinDB(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFileinDB")
    public ResponseEntity<Resource> downloadFileinDB(@RequestParam String fileId) {
        // Load file from database
        DBFileEntity dbFile = dbFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/videoStream")
    public void byterange(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName)
            throws ServletException, IOException {
        String fileDir = fileStorageProperties.getUploadDir();
        File videoFile = fileStorageService.returnFile(fileDir,fileName);
        request.setAttribute(MyResourceHttpRequestHandler.ATTR_FILE, videoFile);
        handler.handleRequest(request, response);
    }


}
