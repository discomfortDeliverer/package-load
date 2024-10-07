package ru.discomfortdeliverer.controller.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ParcelRestController {
    private final ParcelService parcelService;

    @Autowired
    public ParcelRestController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/parcels")
    public List<Parcel> getAllParcels() {
        return parcelService.getAll();
    }

    @GetMapping("/parcels/{name}")
    public Parcel getParcelByName(@PathVariable String name) {
        return parcelService.getByName(name);
    }

    @DeleteMapping("/parcels/{name}")
    public Parcel deleteParcelByName(@PathVariable String name) {
        return parcelService.deleteByName(name);
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
    public Parcel changeParcelSymbolByName(@RequestBody ParcelEntity parcelEntity) {
        return parcelService.changeParcelFormFromRest(parcelEntity.getName(),
                parcelEntity.getForm(), parcelEntity.getSymbol());
    }
}
