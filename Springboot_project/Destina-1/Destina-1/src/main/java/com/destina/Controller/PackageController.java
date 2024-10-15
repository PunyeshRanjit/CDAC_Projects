package com.destina.Controller;



import com.destina.Dto.PackageDto;
import com.destina.Service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public ResponseEntity<?> getAllPackages() {
        List<PackageDto> packages = packageService.getAllPackages();
        return packages.isEmpty() ? ResponseEntity.ok("Nothing") : ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDto> getPackageById(@PathVariable("id") Long id) {
        PackageDto pkg = packageService.getPackageById(id);
        return pkg == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(pkg);
    }

    @GetMapping("/search/{location}")
    public ResponseEntity<?> searchPackages(@PathVariable("location") String location) {
        List<PackageDto> packages = packageService.searchPackages(location);
        return packages.isEmpty() ? ResponseEntity.ok("No packages for this location!!!") : ResponseEntity.ok(packages);
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<PackageDto>> getPackagesByAgent(@PathVariable("agentId") Long agentId) {
        List<PackageDto> packages = packageService.getPackagesByAgent(agentId);
        return packages.isEmpty() ? ResponseEntity.ok().body(null) : ResponseEntity.ok(packages);
    }

//    @PostMapping
//    public ResponseEntity<String> createPackage(@RequestParam String title,
//                                                @RequestParam String description,
//                                                @RequestParam String location,
//                                                @RequestParam LocalDateTime startDate,
//                                                @RequestParam LocalDateTime endDate,
//                                                @RequestParam BigDecimal pricePerPerson,
//                                                @RequestParam int numberOfSeatsAvailable,
//                                                @RequestParam Long agentId,
//                                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
//        byte[] imageData = imageFile != null ? imageFile.getBytes() : null;
//
//        packageService.createPackage(title, description, location, startDate, endDate,
//                pricePerPerson, numberOfSeatsAvailable, agentId, imageData);
//        return ResponseEntity.ok("Package created successfully");
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updatePackage(@PathVariable Long id,
//                                                @RequestParam String title,
//                                                @RequestParam String description,
//                                                @RequestParam String location,
//                                                @RequestParam LocalDateTime startDate,
//                                                @RequestParam LocalDateTime endDate,
//                                                @RequestParam BigDecimal pricePerPerson,
//                                                @RequestParam int numberOfSeatsAvailable,
//                                                @RequestParam Long agentId,
//                                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
//        byte[] imageData = imageFile != null ? imageFile.getBytes() : null;
//
//        packageService.updatePackage(id, title, description, location, startDate, endDate,
//                pricePerPerson, numberOfSeatsAvailable, agentId, imageData);
//        return ResponseEntity.ok("Package updated successfully");
//    }
    
    
    
    
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> createPackage(@RequestPart("package") PackageDto packageDto,
                                                @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        System.out.println("Hello ");
    	byte[] imageData = imageFile != null ? imageFile.getBytes() : null;
        packageDto.setImage(imageData);

        packageService.createPackage(packageDto);
        return ResponseEntity.ok("Package created successfully");
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> updatePackage(@PathVariable("id") Long id,
                                                @RequestPart("package") PackageDto packageDto,
                                                @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        byte[] imageData = imageFile != null ? imageFile.getBytes() : null;
        packageDto.setImage(imageData);

        packageService.updatePackage(id, packageDto);
        return ResponseEntity.ok("Package updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePackage(@PathVariable("id") Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.ok("Package deleted successfully");
    }
}
