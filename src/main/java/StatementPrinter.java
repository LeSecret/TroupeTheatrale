import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {

  public String print(Invoice invoice, HashMap<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;
    StringBuffer result = new StringBuffer("Statement for " + invoice.customer + "\n");

    // Format de devise
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);

      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type))
        volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result.append("  " + play.name + ": " + frmt.format(totalAmount(perf, play) / 100) + " (" + perf.audience + " seats)\n");
      totalAmount += totalAmount(perf, play);
    }

    // Ajout du montant total et des crédits de volume à la chaîne résultante
    result.append("Amount owed is " + frmt.format(totalAmount / 100) + "\n");
    result.append("You earned " + volumeCredits + " credits\n");
    
    // Conversion du StringBuffer en chaîne de caractères et retour
    return result.toString();
  }

  private int totalAmount(Performance perf, Play play)
  {
    int result = 0;

      switch (play.type) {
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
          throw new Error("unknown type: ${play.type}");
      }
    return result;
  }

}
