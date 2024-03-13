package com.pantrychef.backend.images;

import com.pantrychef.backend.recipes.Recipe;
import com.pantrychef.backend.recipes.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/images")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @PostMapping
    public Image addImage(@RequestBody Image image) throws Exception {
        image.setId(null);
        return imageRepository.save(image);
    }

    @GetMapping
    public Page<Image> getPagesOfImages(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        return imageRepository.findAll(pageRequest);
    }

    @GetMapping(path="/{id}")
    public Image getImage(@PathVariable Integer id) throws Exception {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isEmpty()) throw new Exception();
        return image.get();
    }

    @PutMapping(path = "/{id}")
    public Image updateImage(@RequestBody Image image) throws Exception {
        if (image.getId() == null || !imageRepository.existsById(image.getId())) {
            throw new Exception();
        }
        return imageRepository.save(image);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteImage(@PathVariable Integer id) throws Exception {
        if (!imageRepository.existsById(id)) {
            throw new Exception();
        }
        imageRepository.deleteById(id);
    }
}
