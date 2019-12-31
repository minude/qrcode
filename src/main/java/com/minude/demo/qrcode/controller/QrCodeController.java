package com.minude.demo.qrcode.controller;

import com.minude.demo.qrcode.util.FileDownloadUtil;
import com.minude.demo.qrcode.util.QrCodeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author minude
 * @version 1.0
 * @date 2019-12-31 13:48
 */
@Controller
@RequestMapping("qr")
public class QrCodeController {

    @PostMapping("decode")
    @ResponseBody
    public String decode(@RequestParam("qrimg") MultipartFile qrimg) throws Exception {

        return QrCodeUtil.decode(qrimg.getInputStream());
    }

    @GetMapping("encode")
    public void encode(String msg, HttpServletResponse response) {

        OutputStream out = null;
        InputStream in = null;
        try {
            File file = QrCodeUtil.encode(msg);
            out = response.getOutputStream();
            in = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            while (in.read(bytes) != -1) {
                out.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("encode/download")
    public ResponseEntity encodeDownload(String msg) throws Exception {

        File file = QrCodeUtil.encode(msg);
        return FileDownloadUtil.prepare(file);
    }
}
