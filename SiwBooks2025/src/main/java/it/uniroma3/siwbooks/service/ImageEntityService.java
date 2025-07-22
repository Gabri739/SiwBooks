package it.uniroma3.siwbooks.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import it.uniroma3.siwbooks.model.ImageEntity;
import it.uniroma3.siwbooks.repository.ImageEntityrRepository;
import jakarta.transaction.Transactional;

import static it.uniroma3.siwbooks.model.ImageEntity.PATH;

@Service
public class ImageEntityService {
    
	@Autowired
    private ImageEntityrRepository imageEntityRepository;
	
    @Value("${upload.dir}")
    private String uploadDir;

    public ImageEntity getImage(Long id) {
        return imageEntityRepository.findById(id).orElse(null);
    }

    @Transactional
	public void savePhysicalImage(byte[] data, String name) throws IOException {
		
        Path uploadPath = Paths.get(uploadDir + PATH);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(name);
        Files.write(filePath, data);
	}

	public void deletePhysicalImage(ImageEntity photo) {
		File file = new File(uploadDir + photo.getName());
		file.delete();
	}
	
	public boolean existsImage(Long imgId) {
		return this.imageEntityRepository.existsById(imgId);
	}
}