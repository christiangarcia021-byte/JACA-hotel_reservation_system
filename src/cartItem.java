//this holds the rooms a user selected and what hotel it is from
// used by cartcontroller and hopefully the reservation controller

public class cartItem {
    public final room selectedRoom;
    public final hotel selectedHotel;

    public cartItem(room selectedRoom, hotel selectedHotel) {
        this.selectedRoom = selectedRoom;
        this.selectedHotel = selectedHotel;

    }

    public String displayName() {
        return selectedRoom.getName() + " " + selectedHotel.getName();
    }

    public double price() {
        return selectedRoom.getPrice();
    }
}
