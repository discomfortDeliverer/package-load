package ru.discomfortdeliverer.controller.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParcelRestController {

    private final ParcelService parcelService;

    @GetMapping("/parcels")
    public List<Parcel> getAllParcels() {
        List<Parcel> allParcels = parcelService.getAll();
        for (Parcel parcel : allParcels) {
            parcel.reverseParcelForm();
        }
        return allParcels;
    }

    @GetMapping("/parcels/{name}")
    public Parcel getParcelByName(@PathVariable String name) {
        Parcel parcel = parcelService.getByName(name);
        parcel.reverseParcelForm();
        return parcel;
    }

    @DeleteMapping("/parcels/{name}")
    public Parcel deleteParcelByName(@PathVariable String name) {
        Parcel parcel = parcelService.deleteByName(name);
        parcel.reverseParcelForm();
        return parcel;
    }

    @PatchMapping("/parcels/name")
    public Parcel changeParcelNameByName(@RequestParam(name = "old-name") String oldName,
                                         @RequestParam(name = "new-name") String newName) {
        return parcelService.updateName(oldName, newName);
    }

    @PatchMapping("/parcels/symbol")
    public Parcel changeParcelSymbolByName(@RequestParam(name = "parcel-name") String parcelName,
                                           @RequestParam(name = "new-symbol") String newSymbol) {
        return parcelService.updateSymbol(parcelName, newSymbol);
    }

    @PatchMapping("/parcels/form")
    public Parcel changeParcelFormByName(@RequestBody ParcelEntity parcelEntity) {
        Parcel parcel = parcelService.changeParcelFormFromRest(parcelEntity.getName(),
                parcelEntity.getForm(), parcelEntity.getSymbol());
        parcel.reverseParcelForm();
        return parcel;
    }
}
