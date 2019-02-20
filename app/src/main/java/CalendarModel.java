import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Date;

public class CalendarModel {
    Calendar cal = new Calendar() {
        @Override
        protected void computeTime() {

        }

        @Override
        protected void computeFields() {

        }

        @Override
        public void add(int field, int amount) {

        }

        @Override
        public void roll(int field, boolean up) {

        }

        @Override
        public int getMinimum(int field) {
            return 0;
        }

        @Override
        public int getMaximum(int field) {
            return 0;
        }

        @Override
        public int getGreatestMinimum(int field) {
            return 0;
        }

        @Override
        public int getLeastMaximum(int field) {
            return 0;
        }
    }
    // User account

    // Events from calendar

    // Events from UsageStats

    // Calendar accounts

    // private Methods
    //

    // Methods
    public MTBEvent[] getPlannedEvents (Date startDay = , Date endDay = ) {
        if ( startDay == null ) {

        }
    };

    // }
    // default to today

    // getUsageEvents (startDay, endDay)
    // default  to today
}
