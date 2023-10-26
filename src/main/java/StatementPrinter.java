import java.text.NumberFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatementPrinter {

  final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

  public String printText(Invoice invoice, Map<String, Play> plays) {
    StringBuffer result = new StringBuffer("Statement for " + invoice.customer.name + "\n");

    for (Performance perf : invoice.performances) {
      // print every play
      result.append("  " + perfplay(perf, plays).name + ": " + frmt.format(perfplay(perf, plays).getPrix(perf.audience)) + " (" + perf.audience + " seats)\n");
    }
    if(invoice.customer.soldCredits >= 150){
      int totalAmount = totalAmount(invoice, plays) - 15;
      int volumeCredits = invoice.customer.soldCredits - 150;

      result.append("You have been deducted of 150 credits and 15$\n");
      result.append("Amount owed is " + frmt.format(totalAmount) + "\n");
      result.append("You earned " + volumeCredits + " credits\n");

      return result.toString();
    }
    result.append("Amount owed is " + frmt.format(totalAmount(invoice, plays)) + "\n");
    result.append("You earned " + volumeCredits(invoice, plays) + " credits\n");

    return result.toString();
  }


  public String printHTML(Invoice invoice, Map<String, Play> plays) {
    String result = "";
    try {
      result = Files.readString(Paths.get(getClass().getResource("templates\\HTMLTemplate.txt").toURI()));
    } catch (Exception e) {
      throw new Error("Cannot read template");
    }

    StringBuffer invoiceItems = new StringBuffer();
    for (Performance perf : invoice.performances) {
      invoiceItems.append(
              "<tr>\n"
              + "<td>" + perfplay(perf, plays).name  + "</td>\n"
              + "<td>" + frmt.format(perfplay(perf, plays).getPrix(perf.audience)) + "</td>\n"
              + "<td>" + perf.audience + "</td>\n"
              + "</tr>\n");
    }

    result.replace("{$Invoice_Items}", invoiceItems.toString());
    result.replace("{@Invoice_Amount}", Integer.toString(totalAmount(invoice, plays)));
    result.replace("{$Total_Credits}", Integer.toString(volumeCredits(invoice, plays))); 

    return result;
  }

  private int totalAmount(Invoice invoice, Map<String, Play> plays){
    int result = 0;

    for(Performance perf: invoice.performances){
      result += perfplay(perf, plays).getPrix(perf.audience);
    }
    return result;
  }


  private Play perfplay(Performance perf, Map<String, Play> plays) {
    return plays.get(perf.playID);
  }

  private int volumeCredits(Invoice invoice, Map<String, Play> plays){
    int result = 0;

    for(Performance perf: invoice.performances){
      result += perfplay(perf, plays).getCredits(perf.audience);
    }
    return result;
  }

}
