package it.uniroma3.siwbooks.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwbooks.model.Book;
import it.uniroma3.siwbooks.model.ImageEntity;
import it.uniroma3.siwbooks.service.BookService;
import it.uniroma3.siwbooks.service.ImageEntityService;

@Controller
public class ImageEntityController {

	@Autowired
	private BookService bookService;

	@Autowired
	private ImageEntityService imageEntityService;

	@GetMapping("/admin/books/{bookId}/images/{imgId}/delete")
	public String deletePhotoFromBook(@PathVariable("bookId") Long bookId, @PathVariable("imgId") Long imgId) {
		if (!this.bookService.existsBook(bookId) || !this.imageEntityService.existsImage(imgId))
			return "error.html";

		if (!this.bookService.existsBookWithImage(bookId, imgId))
			return "redirect:/admin/books/" + bookId;
		ImageEntity img = imageEntityService.getImage(imgId);
		Book book = this.bookService.getBook(bookId);
		book.getImages().remove(img);
		this.imageEntityService.deletePhysicalImage(img);
		this.bookService.saveBook(book);
		return "redirect:/admin/books/" + bookId;
	}

	@PostMapping("/admin/books/{bookId}/images/add")
	public String addPhototoBook(@PathVariable("bookId") Long bookId, @RequestParam("imageFile") MultipartFile file, Model model) {
		
			if(!this.bookService.existsBook(bookId))
				return "error.html";
			Book book = this.bookService.getBook(bookId);
		try {
			String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			ImageEntity img = new ImageEntity(name);
			this.imageEntityService.savePhysicalImage(file.getBytes(), name);
			book.getImages().add(img);
			this.bookService.saveBook(book);
			return "redirect:/admin/books/" + bookId;
		} catch (IOException e) {
			model.addAttribute("book", book);
			model.addAttribute("showModalAddPhoto", true);
			model.addAttribute("msgPhotoError", "Inserisci una foto");
			return "admin/book.html";
			
		}
	}
}