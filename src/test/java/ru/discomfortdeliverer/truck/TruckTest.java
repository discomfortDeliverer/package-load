package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.view.ConsoleTruckView;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckTest {
    private Truck truck;
    private final String ls = System.lineSeparator();

    @BeforeEach
    void setUp() {
        truck = new Truck();
    }
    @Test
    void constructorTest() {
        char[][] expectedTruckBody = {
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '}
        };

        assertThat(truck.getTruckBody()).isEqualTo(expectedTruckBody);
    }

    @Test
    void canFitParcelAtCoordinates_GivenCoordinatesThatParcelCanBePlaced_ShouldReturnTrue() {
        Parcel parcel = new Parcel("999" + ls +
                                             "999" + ls +
                                             "999");
        int row = 0;
        int col = 0;
        assertThat(truck.canFitParcelAtCoordinates(parcel, row, col)).isTrue();
    }

    @Test
    void canFitParcelAtCoordinates_GivenCoordinatesThatParcelCanNotBePlaced_ShouldReturnFalse() {
        Parcel parcel = new Parcel("999" + ls +
                                             "999" + ls +
                                             "999");
        int row = 4;
        int col = 0;
        assertThat(truck.canFitParcelAtCoordinates(parcel, row, col)).isFalse();
    }

    @Test
    void canFitParcelAtCoordinates_GivenBottomSupportOnWhichParcelCanBePlaced_ShouldReturnTrue() {
        Parcel bottomParcel = new Parcel("22");
        truck.placeParcelByCoordinates(bottomParcel, 0, 0);
        Parcel topParcel = new Parcel("999" + ls +
                "999" + ls +
                "999");
        int row = 1;
        int col = 0;
        assertThat(truck.canFitParcelAtCoordinates(topParcel, row, col)).isTrue();
    }

    @Test
    void canFitParcelAtCoordinates_GivenBottomSupportOnWhichParcelCanNotBePlaced_ShouldReturnFalse() {
        Parcel bottomParcel = new Parcel("2");
        truck.placeParcelByCoordinates(bottomParcel, 0, 0);
        Parcel topParcel = new Parcel("999" + ls +
                "999" + ls +
                "999");
        int row = 1;
        int col = 0;
        assertThat(truck.canFitParcelAtCoordinates(topParcel, row, col)).isFalse();
    }

    @Test
    void placeParcelByCoordinates_GivenCoordinatesThatCanBePlaced_ShouldPlaceParcel() {
        Parcel parcel = new Parcel("999" + ls +
                "999" + ls +
                "999");

        int row = 0;
        int col = 0;
        char[][] expectedTruckBody = {
                {'9', '9', '9', ' ', ' ', ' '},
                {'9', '9', '9', ' ', ' ', ' '},
                {'9', '9', '9', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '}
        };
        truck.placeParcelByCoordinates(parcel, 0, 0);

        assertThat(truck.getTruckBody()).isEqualTo(expectedTruckBody);
    }

    @Test
    void canFitParcelAtCoordinates_TrackIsFull_ShouldReturnFalse() {
        Parcel firstParcel = new Parcel("999" + ls +
                "999" + ls +
                "999");
        truck.placeParcelByCoordinates(firstParcel, 0, 0);

        Parcel secondParcel = new Parcel("8888" + ls +
                                                   "8888");
        truck.placeParcelByCoordinates(secondParcel, 3, 0);
        Parcel thirdParcel = new Parcel("777" + ls +
                "7777");
        char[][] expectedTruckBody = {
                {'9', '9', '9', ' ', ' ', ' '},
                {'9', '9', '9', ' ', ' ', ' '},
                {'9', '9', '9', ' ', ' ', ' '},
                {'8', '8', '8', '8', ' ', ' '},
                {'8', '8', '8', '8', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '}
        };
        ConsoleTruckView.printTruckBody(truck);
        assertThat(truck.getTruckBody()).isEqualTo(expectedTruckBody);
        assertThat(truck.canFitParcelAtCoordinates(thirdParcel, 5, 0)).isFalse();
    }
}
