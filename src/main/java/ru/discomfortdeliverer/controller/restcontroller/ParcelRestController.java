package ru.discomfortdeliverer.controller.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParcelRestController {

    private final ParcelService parcelService;

    @GetMapping("/parcels")
    public List<Parcel> getAll() {
        List<Parcel> allParcels = parcelService.getAll();
        for (Parcel parcel : allParcels) {
            parcel.reverseParcelForm();
        }
        log.info("Список всех посылок - {}", allParcels);
        return allParcels;
    }

    @GetMapping("/parcels/{name}")
    public Parcel getByName(@PathVariable String name) {
        log.info("Входной параметр name={}", name);
        Parcel parcel = parcelService.getByName(name);
        parcel.reverseParcelForm();
        log.info("Полученная посылка - {}", parcel);
        return parcel;
    }

    @DeleteMapping("/parcels/{name}")
    public Parcel deleteByName(@PathVariable String name) {
        log.info("Входной параметр name={}", name);
        Parcel parcel = parcelService.deleteByName(name);
        parcel.reverseParcelForm();
        log.info("Удаленная посылка - {}", parcel);
        return parcel;
    }

    @PatchMapping("/parcels/name")
    public Parcel changeNameByName(@RequestParam(name = "old-name") String oldName,
                                   @RequestParam(name = "new-name") String newName) {
        log.info("Входные параметры oldName={}, newName={}", oldName, newName);
        Parcel parcel = parcelService.updateName(oldName, newName);
        log.info("Посылка с обновленным именем - {}", parcel);
        return parcel;
    }

    @PatchMapping("/parcels/symbol")
    public Parcel changeSymbolByName(@RequestParam(name = "parcel-name") String parcelName,
                                     @RequestParam(name = "new-symbol") String newSymbol) {
        log.info("Входные параметры parcelName={}, newSymbol={}", parcelName, newSymbol);
        Parcel parcel = parcelService.updateSymbol(parcelName, newSymbol);
        log.info("Посылка с обновленным символом и формой - {}", parcel);
        return parcel;
    }

    @PatchMapping("/parcels/form")
    public Parcel changeFormByName(@RequestBody ParcelEntity parcelEntity) {
        log.info("Тело запроса parcelEntity={}", parcelEntity);
        Parcel parcel = parcelService.changeParcelFormFromRest(parcelEntity.getName(),
                parcelEntity.getForm(), parcelEntity.getSymbol());
        parcel.reverseParcelForm();
        log.info("Посылка с обновленным символом и формой - {}", parcel);
        return parcel;
    }
}
