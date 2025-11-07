

public class calender {

    int ThisYear;
    int room_id;
    int calendar1[][][];     //thisyear
    int calendar2[][][];     //nextyear



    public calender(int year, int room_num){
        ThisYear = year;
        room_id = room_num;
        calendar1 = new int[12][31][1];
        calendar2 = new int[12][31][1];

        //initialize calendars
        initialize(calendar1, ThisYear);
        initialize(calendar2, ThisYear + 1);

    }

    private void initialize(int cal[][][], int year){
        int daysInMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};
        if(isLeapYear(year)){//accounts for leap year
            daysInMonth[1] = 29;
        }


        for(int month = 0; month < 12; month++){
            for(int day = 0; day < daysInMonth[month]; day++){
                cal[month][day][0] = 1;   //1 means available
            }
        }


        AvailabilityUpdate(calendar1, room_id);
        AvailabilityUpdate(calendar2, room_id);
    }



    private void AvailabilityUpdate(int cal[][][], int room_id) {




    }




    private boolean isLeapYear(int year){
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            return true;
        }
        return false;
    }






}
