import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Calendar {

    private int thisYear;
    private int thisMonth;
    private int thisDay;
    private int room_id;
    private int calendar[][][][];
    private String endDates[] = new String[30];
    private         int daysInMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};

    public int[][][][] getCalendar() {
        return calendar;
    }

    public int getThisYear() {
        return thisYear;
    }


    public Calendar(int room_num){
        room_id = room_num;
        calendar = new int[2][12][31][1];

        setToday();
        //initialize calendars
        initialize(calendar);


    }

    private void initialize(int cal[][][][]){//sets up the default availability according to year type
        setDefault(cal);
        invalidatePastDates(cal);
        invalidateAlreadyReservedDates(cal, room_id);
    }

    private void setDefault(int cal[][][][]){

        for(int i = 0; i <= 1; i++) { //for each year
            if(isLeapYear(thisYear + i)){//accounts for leap year
                daysInMonth[1] = 29;
            }
            else{
                daysInMonth[1] = 28;
            }


            for (int month = 0; month < 12; month++) {
                for (int day = 0; day < daysInMonth[month]; day++) {
                    cal[i][month][day][0] = 1;   //1 means available
                }
            }
        }
    }

    private void  invalidatePastDates(int cal[][][][]){
        for(int i = 0; i <= 1; i++) { //for each year
            for (int month = 0; month < 12; month++) {
                for (int day = 0; day < 31; day++) {
                    if ( (thisYear + i == thisYear) && (month + 1 < thisMonth) ) {
                        cal[i][month][day][0] = 0; //invalidate past months
                    }
                    else if ( (thisYear + i == thisYear) && (month + 1 == thisMonth) && (day + 1 < thisDay) ) {
                        cal[i][month][day][0] = 0; //invalidate past days
                    }
                }
            }
        }
    }

    private void invalidateAlreadyReservedDates(int cal[][][][], int room_id) {

        MySQLConnection MyDB = new MySQLConnection();
        PreparedStatement getReservedDates = null;
        Connection con = null;
        try {
            con = MyDB.getConnection();
            String reservationQuery = "SELECT SCHEDULED_DATE, SCHEDULED_END_DATE FROM reservations WHERE ROOM_ID = ? AND (SCHEDULED_DATE >= CURRENT_DATE() OR SCHEDULED_END_DATE >= CURRENT_DATE())";
            getReservedDates = con.prepareStatement(reservationQuery);
            getReservedDates.setInt(1, room_id);
            ResultSet resvResult = getReservedDates.executeQuery();
            while (resvResult.next()) {
                String startDate = resvResult.getString("SCHEDULED_DATE");
                String endDate = resvResult.getString("SCHEDULED_END_DATE");
                int startYear = Integer.parseInt(startDate.substring(0, 4)) - this.thisYear;
                int startMonth = Integer.parseInt(startDate.substring(5, 7)) - 1;
                int startDay = Integer.parseInt(startDate.substring(8, 10)) - 1;
                int endYear = Integer.parseInt(endDate.substring(0, 4)) - this.thisYear;
                int endMonth = Integer.parseInt(endDate.substring(5, 7)) - 1;
                int endDay = Integer.parseInt(endDate.substring(8, 10)) - 1;


                if(startYear < 0){
                    startYear = 0;
                    startMonth = thisMonth - 1;
                    startDay = thisDay - 1;

                }
                else if(endYear > 1){
                    endYear = 1;
                    endMonth = 11;
                    endDay = 30;
                }
                boolean datesEqual = false;
                System.out.println("Invalidating reservation from: " + startDate + " to " + endDate);
                while(!datesEqual){
                    System.out.printf("Invalidating date: %d-%d-%d\n", startYear + this.thisYear, startMonth + 1, startDay + 1);
                    cal[startYear][startMonth][startDay][0] = 0; //0 means unavailable
                    if(startYear == endYear && startMonth == endMonth && startDay == endDay){
                        datesEqual = true;
                    }
                    else{
                        startDay++;
                        if(startDay > 30){
                            startDay = 0;
                            startMonth++;
                            if(startMonth > 11){
                                startMonth = 0;
                                startYear++;
                            }
                        }
                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        finally{
            try {
                if (getReservedDates != null) getReservedDates.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MyDB = null;
        }

    }




    private boolean isLeapYear(int year){
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            return true;
        }
        return false;
    }



    public void showCalendar() {
        int counter = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                for (int k = 0; k < 31; k++) {
                    System.out.printf("%d-%d-%d-%d  | ", thisYear + i, j + 1, k + 1, calendar[i][j][k][0]);
                    counter++;
                    if (counter % 7 == 0) {
                        System.out.println();
                    }
                }


            }


        }
    }
    private void setToday() {
            MySQLConnection MyDB = new MySQLConnection();
            PreparedStatement getToday = null;
            Connection con = null;
            try {
                con = MyDB.getConnection();
                String currentDateQuery = "SELECT CURRENT_DATE()";
                getToday = con.prepareStatement(currentDateQuery);
                ResultSet todayResult = getToday.executeQuery();
                String currentDate = "";
                    if (todayResult.next()) {
                        currentDate = todayResult.getString(1);
                    }
                thisYear = Integer.parseInt(currentDate.substring(0, 4));
                thisDay = Integer.parseInt(currentDate.substring(8, 10));
                thisMonth = Integer.parseInt(currentDate.substring(5, 7));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (getToday != null) getToday.close();
                    if (con != null) con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyDB = null;
            }
    }


    public String[] getEndDates(int yIndex, int mIndex, int dIndex) {
        for(int i = 0; i < 30; i++){
            endDates[i] = null;
        }
        int year = yIndex;
        int month = mIndex;
        int day = dIndex;

        for(int i = 0; i < 30; i++){
            if(calendar[year][month][day][0] == 1){
                String endDate = String.format("%04d-%02d-%02d", thisYear + year, month + 1, day + 1);
                endDates[i] = endDate;
            }
            else{
                break;
            }

            day++;
            int maxDays = daysInMonth[month];
            if(isLeapYear(thisYear + year) && month == 1){
                maxDays = 29;
            }
            if(day >= maxDays){
                day = 0;
                month++;
                if(month >= 12){
                    month = 0;
                    year++;
                    if(year > 1){
                        break;
                    }
                }
            }




        }

        return endDates;
    }

    public void printEndDates(){
        System.out.println();
        int counter = 0;
        for(int i = 0; i < endDates.length; i++){
            if(endDates[i] != null){
                System.out.print(endDates[i] + "  ");
                counter++;
                if(counter % 5 == 0) {
                    System.out.println();
                }
            }
        }
    }


}




