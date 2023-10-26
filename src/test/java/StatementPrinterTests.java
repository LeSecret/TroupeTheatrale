import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.approvaltests.Approvals.verify;

public class StatementPrinterTests {

    @Test
    void exampleStatement() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new Tragedy("Hamlet"));
        plays.put("as-like",  new Comedy("As You Like It"));
        plays.put("othello",  new Tragedy("Othello"));

        Invoice invoice = new Invoice(new Customer("BigCo", "1", 103), List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        var result = statementPrinter.toText(invoice, plays);

        verify(result);
    }

    @Test
    void exampleStatementCustomer() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet",  new Tragedy("Hamlet"));
        plays.put("as-like",  new Comedy("As You Like It"));
        plays.put("othello",  new Tragedy("Othello"));

        Invoice invoice = new Invoice(new Customer("BigCo", "1", 150), List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        var result = statementPrinter.toText(invoice, plays);

        verify(result);
    }

    @Test
    void exampleStatementHTML() {
        Map<String, Play> plays = Map.of(
                "hamlet",  new Tragedy("Hamlet"),
                "as-like", new Comedy("As You Like It"),
                "othello", new Tragedy("Othello"));

        Invoice invoice = new Invoice(new Customer("BigCo", "1", 0), List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        var result = statementPrinter.toHTML(invoice, plays);

        verify(result);
    }

    /*@Test
    void statementWithNewPlayTypes() {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("henry-v",  new Play("Henry V", "history"));
        plays.put("as-like",  new Play("As You Like It", "pastoral"));

        Invoice invoice = new Invoice("BigCo", List.of(
              verify(result);
          new Performance("henry-v", 53),
                new Performance("as-like", 55)));

        StatementPrinter statementPrinter = new StatementPrinter();
        Assertions.assertThrows(Error.class, () -> {
            statementPrinter.print(invoice, plays);
        });
    }*/
}
