package work.process;

public class Calendar {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

    private int D;
    private int M;
    private int Y;

    public static final String[] months = {
            "",
            "January", "February", "March",
            "April", "May", "June",
            "July", "August", "September",
            "October", "November", "December"
    };

    public Calendar(int D, int M, int Y) {
        this.D = D;
        this.M = M;
        this.Y = Y;
    }

    public int getD() { return D; }
    public void setD(int d) { D = d; }
    public int getM() { return M; }
    public void setM(int m) { M = m; }
    public int getY() { return Y; }
    public void setY(int y) { Y = y; }

    public static int day(int M, int D, int Y) {
        int y = Y - (14 - M) / 12;
        int x = y + y/4 - y/100 + y/400;
        int m = M + 12 * ((14 - M) / 12) - 2;
        int d = (D + x + (31*m)/12) % 7;
        return d;
    }

    public static boolean isLeapYear(int year) {
        if  ((year % 4 == 0) && (year % 100 != 0)) return true;
        if  (year % 400 == 0) return true;
        return false;
    }

    public void printCalendar() {
        int[] days = {
                0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };

        if (M == 2 && isLeapYear(Y)) days[M] = 29;


        System.out.println("   " + months[M] + " " + Y);
        System.out.println("S  M Tu  W Th  F  S");

        int d = day(M, 1, Y);

        for (int i = 0; i < d; i++)
            System.out.print("   ");
        for (int i = 1; i <= days[M]; i++) {
            System.out.printf("%2s ", (i == D) ? RED + i + RESET : i);
            if (((i + d) % 7 == 0) || (i == days[M])) System.out.println();
        }

    }
}
