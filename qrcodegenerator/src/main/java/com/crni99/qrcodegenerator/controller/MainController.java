package com.crni99.qrcodegenerator.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crni99.qrcodegenerator.service.QRCodeService;

@Controller
public class MainController {

	private QRCodeService qrCodeService;

	public MainController(QRCodeService qrCodeService) {
		this.qrCodeService = qrCodeService;
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@PostMapping("/generate")
	public String generateQRCode(@RequestParam("text") String text, Model model,
			RedirectAttributes redirectAttributes) {
		if (text == null || text.isBlank() || text.isEmpty()) {
			return "redirect:/";
		}
		String qrCode = qrCodeService.getQRCode(text);
		model.addAttribute("text", text);
		model.addAttribute("qrcode", qrCode);
		return "index";
	}

	@GetMapping("/decode")
	public String decodeQRCode() {
		return "decode";
	}

	@PostMapping("/uploadQrCode")
	public String uploadQrCode(@RequestParam("qrCodeFile") MultipartFile qrCodeFile,
			RedirectAttributes redirectAttributes) {
		if (qrCodeFile.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please choose file to upload.");
			return "redirect:/decode";
		}
		try {
			String qrContent = qrCodeService.decodeQR(qrCodeFile.getBytes());
			redirectAttributes.addFlashAttribute("qrContent", qrContent);
			return "redirect:/decode";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/decode";
	}

}
