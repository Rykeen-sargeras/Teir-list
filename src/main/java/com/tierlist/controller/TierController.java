package com.tierlist.controller;

import com.tierlist.model.TierImage;
import com.tierlist.repository.TierImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TierController {

    private final TierImageRepository repo;

    public TierController(TierImageRepository repo) {
        this.repo = repo;
    }

    // GET all images
    @GetMapping("/images")
    public List<Map<String, Object>> getAllImages() {
        List<TierImage> images = repo.findAllByOrderBySortOrderAsc();
        List<Map<String, Object>> result = new ArrayList<>();
        for (TierImage img : images) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", img.getId());
            m.put("tierSlot", img.getTierSlot());
            m.put("imageData", img.getImageData());
            m.put("locked", img.isLocked());
            result.add(m);
        }
        return result;
    }

    // POST upload new image
    @PostMapping("/images")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tier", defaultValue = "__pool__") String tier
    ) throws IOException {
        String base64 = "data:" + file.getContentType() + ";base64," +
                Base64.getEncoder().encodeToString(file.getBytes());

        TierImage img = new TierImage(tier, base64);
        img.setSortOrder((int) repo.count());
        repo.save(img);

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", img.getId());
        resp.put("tierSlot", img.getTierSlot());
        resp.put("imageData", img.getImageData());
        resp.put("locked", img.isLocked());
        return ResponseEntity.ok(resp);
    }

    // PATCH move image to a tier
    @PatchMapping("/images/{id}/move")
    public ResponseEntity<Map<String, Object>> moveImage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        Optional<TierImage> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        TierImage img = opt.get();
        if (img.isLocked()) return ResponseEntity.badRequest().body(Map.of("error", "Image is locked"));
        img.setTierSlot(body.get("tier"));
        repo.save(img);
        return ResponseEntity.ok(Map.of("id", img.getId(), "tierSlot", img.getTierSlot(), "locked", img.isLocked()));
    }

    // PATCH lock image
    @PatchMapping("/images/{id}/lock")
    public ResponseEntity<Map<String, Object>> lockImage(@PathVariable Long id) {
        Optional<TierImage> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        TierImage img = opt.get();
        img.setLocked(true);
        repo.save(img);
        return ResponseEntity.ok(Map.of("id", img.getId(), "locked", true));
    }

    // DELETE image
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE all — reset board
    @DeleteMapping("/images")
    public ResponseEntity<Void> resetAll() {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
