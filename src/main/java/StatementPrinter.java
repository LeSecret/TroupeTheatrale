import java.text.NumberFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatementPrinter {

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
              + "<td>" + FormatDevise(computeAmount(perf, plays)) + "</td>\n"
              + "<td>" + perf.audience + "</td>\n"
              + "</tr>\n");
    }

    result.replace("{$Invoice_Items}", invoiceItems.toString());
    result.replace("{@Invoice_Amount}", Integer.toString(totalAmount(invoice, plays)));
    result.replace("{$Total_Credits}", Integer.toString(volumeCredits(invoice, plays))); 

    return result;
  }

  public String print(Invoice invoice, Map<String, Play> plays) {
    StringBuffer result = new StringBuffer("Statement for " + invoice.customer + "\n");
    for (Performance perf : invoice.performances) {

      result.append("  " + perfplay(perf, plays).name + ": " + FormatDevise(computeAmount(perf, plays)) + " (" + perf.audience + " seats)\n");
    }

    // Ajout du montant total et des crédits de volume à la chaîne résultante
    result.append("Amount owed is " + FormatDevise(totalAmount(invoice, plays)) + "\n");
    result.append("You earned " + volumeCredits(invoice, plays) + " credits\n");
    
    // Conversion du StringBuffer en chaîne de caractères et retour
    return result.toString();
  }

  private int computeAmount(Performance perf, Map<String, Play> plays)
  {
    int result = 0;

      switch (perfplay(perf, plays).type) {
        case "tragedy":
          // Calcul du montant pour une pièce de type "tragedy"
          result = 40000;
          if (perf.audience > 30) {
            result += 1000 * (perf.audience - 30);
          }
          break;
        case "comedy":
          // Calcul du montant pour une pièce de type "comedy"
          result = 30000;
          if (perf.audience > 20) {
            result += 10000 + 500 * (perf.audience - 20);
          }
          result += 300 * perf.audience;
          break;
        default:
          throw new Error("unknown type: ${perfplay(perf, plays)}");
      }
    return result;
  }

  private int totalAmount(Invoice invoice, Map<String, Play> plays){
    int result = 0;

    for(Performance perf: invoice.performances){
      result += computeAmount(perf, plays);
    }
    return result;
  }


  private Play perfplay(Performance perf, Map<String, Play> plays) {
    return plays.get(perf.playID);
  }

  private int computeCredits(Performance perf, Map<String, Play> plays){
    int result = 0;
    result += Math.max(perf.audience - 30, 0);

    // add extra credit for every ten comedy attendees
    if ("comedy".equals(perfplay(perf, plays).type)) 
      result += Math.floor(perf.audience / 5);
    return result;
  }

  private int volumeCredits(Invoice invoice, Map<String, Play> plays){
    int result = 0;

    for(Performance perf: invoice.performances){
      result += computeCredits(perf, plays);
    }
    return result;
  }

  private String FormatDevise(int totalAmount) {
    return NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount / 100);
  }

}
