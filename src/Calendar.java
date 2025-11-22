import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Calendar class handles the availability calendar for room reservations.
 * It initializes a 2-year calendar, marks unavailable dates based on past dates and existing reservations
 * for a specific room, and provides methods to retrieve available end dates for reservations.
 * @author Andy Hernandez
 * @version 1.0
 * Date: 11/15/2025
 */
public class Calendar {
/**
 * Holds the current year, month, and day obtained from the database
 * Used for calendar calculations
 */
    private int thisYear;
    /**
     * Holds the current month (1-12) obtained from the database
     */
    private int thisMonth;
    /**
     * Holds the current day (1-31) obtained from the database
     */
    private int thisDay;
    /**
     * The ID of the room this calendar is associated with
     */
    private int room_id;
    /**
     * 4D array representing the calendar:
     * Dimensions: [2 years][12 months][31 days][1 availability status]
     */
    private int calendar[][][][];
    /**
     * Array to hold available end dates for reservations
     */
    private String endDates[] = new String[30];
    /**
     * Array holding the number of days in each month
     * Adjusted for leap years when necessary
     */
    private         int daysInMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};
/**
 *  Returns the 4D calendar array representing availability for 2 years, 12 months, 31 days, and 1 availability status
 *  The availability status is represented as follows:
 *  1 - Available
 *  0 - Unavailable
 */
    public int[][][][] getCalendar() {
        return calendar;
    }

    /**
     * Returns the year value stored in thisYear
     * This is typically the current year obtained from the database
     * and is used for calendar calculations
     * @return the current year as an integer
     */
    public int getThisYear() {
        return thisYear;
    }
    /**
     * Constructor for Calendar class
     * Initializes the internal calendar structure and sets
     * the current date by querying the database
     * @param room_num the ID number of the room this calendar belongs to
     */
    public Calendar(int room_num){
        room_id = room_num;
        calendar = new int[2][12][31][1];

        setToday();
        //initialize calendars
        initialize(calendar);


    }
    /**
     * Initializes the calendar by setting default availability, invalidating past dates,
     * and invalidating already reserved dates for the specified room.
     * @param cal the 4D calendar array to initialize
     */
    private void initialize(int cal[][][][]){//sets up the default availability according to year type
        setDefault(cal);
        invalidatePastDates(cal);
        invalidateAlreadyReservedDates(cal, room_id);
    }
    /**
     * The default avaliability is set to 1 (avaliable) for all dates in the 2-year calandar
     * @param cal the 4D calendar array to modify
     */
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
    /**
     * Invalidates past dates in the calendar by setting their availability to 0 (unavailable)
     * in the 2-year calendar.
     * @param cal the 4D calendar array to update
     */
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

    /**
     * Invalidates already reserved dates for the specified room by querying the database
     * and setting their availability to 0 (unavailable) in the calendar.
     * @param cal the 4D calander array to update
     * @param room_id the room whose reservation are being checked
     */
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
    /**
     * Checks if a given year is a leap year.
     * @param year the year to evaluate
     * @return true if the year is a leap year, false otherwise
     */
    private boolean isLeapYear(int year){
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            return true;
        }
        return false;
    }
    /**
     * Displays the entire calendar with availability status for each date.
     * Prints the year, month, day, and availability status in a formatted manner.
     */
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

    /**
     * Sets the current date (year, month, day) by querying the database for the current date.
     * This method initializes the thisYear, thisMonth, and thisDay variables.
     */
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

    /**
     * Retrieves available end dates for reservations starting from the specified year, month, and day indices.
     * @param yIndex the year index (0 for current year, 1 for next year)
     * @param mIndex the month index (0-11)
     * @param dIndex the day index (0-30)
     * @return an array of available end date strings in the format "YYYY-MM-DD"
     */
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

    /**
     * Prints the available end dates stored in the endDates array.
     */
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




