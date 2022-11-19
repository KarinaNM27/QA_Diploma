package data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {


    final private static String database = "mysql";
    // либо final private static String database = "postgresql";
    final private static String approvedCardNumber = "4444444444444441";
    final private static String declinedCardNumber = "4444444444444442";


    @Value
    public static class CardInfo {
        private String number;
        private String month;
        private String year;
        private String owner;
        private String cvv;
    }

    public static CardInfo setCard(String setNumber, String setMonth, String setYear, String setOwner, String setCVV) {
        return new CardInfo(setNumber, setMonth, setYear, setOwner, setCVV);
    }

    public static CardInfo setValidCard(String month, String year, String owner, String cvv) {
        return setCard(approvedCardNumber, month, year, owner, cvv);
    }

    public static CardInfo setInvalidCard(String month, String year, String owner, String cvv) {
        return setCard(declinedCardNumber, month, year, owner, cvv);
    }

    public static String[] generateYear(int shift) {
        String date = LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
        return new String[]{date};
    }


    public static String generateOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().firstName() + " " + faker.name().lastName();
    }



    @SneakyThrows
    public static String getPaymentStatus() {
        val sql = "SELECT status FROM payment_entity;";
        val runner = new QueryRunner();
        String paymentStatus;

        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
                //либо val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass")
        ) {
            paymentStatus = runner.query(conn, sql, new ScalarHandler<>());
        }
        return paymentStatus;
    }

    @SneakyThrows
    public static String getCreditStatus() {
        val status = "SELECT status FROM credit_request_entity;";
        val runner = new QueryRunner();
        String creditStatus;

        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
                //либо val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass")
        ) {
            creditStatus = runner.query(conn, status, new ScalarHandler<>());
        }

        return creditStatus;
    }

    @SneakyThrows
    public static long getPaymentId() {
        val sql = "SELECT COUNT(id) as count FROM payment_entity;";
        val runner = new QueryRunner();
        long paymentId;

        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
                //либо val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass")
        ) {
            paymentId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return paymentId;
    }

    @SneakyThrows
    public static long getCreditId() {
        val sql = "SELECT COUNT(id) as count FROM credit_request_entity;";
        val runner = new QueryRunner();
        long creditId;

        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
                //либо val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass")
        ) {
            creditId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return creditId;
    }

    @SneakyThrows
    public static long getOrderId() {
        val sql = "SELECT COUNT(id) as count FROM order_entity;";
        val runner = new QueryRunner();
        long orderId;

        try (
                val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
                //либо val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass")

        ) {
            orderId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return orderId;
    }


    @SneakyThrows
    private static String requestSQL(String requestSQL) {

        Thread.sleep(500);
        var runner = new QueryRunner();


        if (database.equals("mysql")) {
            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app", "app", "pass"

                    )
            ) {
                return runner.query(conn, requestSQL, new ScalarHandler<>());
            }
        } else if (database.equals("postgresql")) {
            {
                try (
                        var conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/app", "app", "pass"

                        )
                ) {
                    return runner.query(conn, requestSQL, new ScalarHandler<>());
                }
            }
        }

        return requestSQL;
    }


    @SneakyThrows
    public static void clearSUTData() {
        var runner = new QueryRunner();
        var sqlDeleteAllOrders = "DELETE FROM order_entity;";
        var sqlDeleteAllCreditRequest = "DELETE FROM credit_request_entity;";
        var sqlDeleteAllPaymentRequest = "DELETE FROM payment_entity;";

        if (database.equals("mysql")) {
            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app", "app", "pass"

                    )
            ) {
                runner.update(conn, sqlDeleteAllOrders);
                runner.update(conn, sqlDeleteAllCreditRequest);
                runner.update(conn, sqlDeleteAllPaymentRequest);
            }
        } else if (database.equals("postgresql")) {
            try (
                    val conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/app", "app", "pass"
                    )
            ) {
                runner.update(conn, sqlDeleteAllOrders);
                runner.update(conn, sqlDeleteAllCreditRequest);
                runner.update(conn, sqlDeleteAllPaymentRequest);
            }
        }

    }

}
