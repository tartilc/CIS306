package javaprog2;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Parser
{
  private static final String EPSILON = "e";
  private static final String STOP = "$";
  private static Map<String, String[]> LOOKUPTABLE = null;
  private static Map<String, Map<String, String>> PARSETABLE = null;
  
  public static void main(String[] args)
    throws Exception
  {
    initializeData();
    
    Scanner in = new Scanner(System.in);
    System.out.println("Input String: ");
    String inputString = in.nextLine();
    
    List<String> input = Parser.LexicalAnalyzer.extractTokens(inputString + "$");
    List<String> matched = new ArrayList();
    
    Stack<String> stack = new Stack();
    
    stack.push("$");
    stack.push("E");
    String output = String.format("%20s%20s%25s%20s\n", new Object[] { "MATCHED", "STACK", "INPUT", "ACTION" });
    output = output + String.format("%20s%20s%25s%20s\n", new Object[] { " ", stackToString(stack), listToString(input), " " });
    while (!stack.isEmpty())
    {
      String token = (String)stack.pop();
      String action = "";
      if (LOOKUPTABLE.containsKey(token))
      {
        Map<String, String> tokenPARSETABLE = (Map)PARSETABLE.get(token);
        
        String nextInput = (String)input.get(0);
        if (!tokenPARSETABLE.containsKey(nextInput))
        {
          output = output + "Error.";
          return;
        }
        String[] productionTokens = ((String)tokenPARSETABLE.get(nextInput)).split(" ");
        if ((productionTokens.length == 1) && (productionTokens[0].equals("e")))
        {
          action = "output " + token + " -> " + productionTokens[0];
        }
        else
        {
          for (int i = productionTokens.length - 1; i >= 0; i--)
          {
            action = productionTokens[i] + action;
            stack.push(productionTokens[i]);
          }
          action = "output " + token + " -> " + action;
        }
      }
      else if (((String)input.get(0)).equals(token))
      {
        input.remove(0);
        if (!token.equals("$"))
        {
          matched.add(token);
          action = "match " + token;
        }
      }
      else
      {
        output = output + "Error.";
        return;
      }
      if (!stack.isEmpty()) {
        output = output + String.format("%20s%20s%25s%25s\n", new Object[] { listToString(matched), stackToString(stack), listToString(input), action });
      }
    }
    if ((!stack.isEmpty()) || (!input.isEmpty())) {
      output = output + "Error.";
    }
    System.out.println(output);
    
    PrintWriter outFile = new PrintWriter(new FileWriter("output.txt"));
    outFile.println(output);
    outFile.close();
  }
  
  private static String stackToString(Stack<String> stack)
  {
    Stack<String> tempStack = new Stack();
    
    String string = "";
    while (!stack.isEmpty())
    {
      string = string + (String)stack.peek();
      tempStack.push(stack.pop());
    }
    while (!tempStack.isEmpty()) {
      stack.push(tempStack.pop());
    }
    return string;
  }
  
  private static String listToString(List<String> list)
  {
    String string = "";
    for (int i = 0; i < list.size(); i++) {
      string = string + (String)list.get(i);
    }
    return string;
  }
  
  private static void initializeData()
  {
    LOOKUPTABLE = new HashMap();
    LOOKUPTABLE.put("E", new String[] { "T E'" });
    LOOKUPTABLE.put("E'", new String[] { "+ T E'", "e" });
    LOOKUPTABLE.put("T", new String[] { "F T'" });
    LOOKUPTABLE.put("T'", new String[] { "* F T'", "e" });
    LOOKUPTABLE.put("F", new String[] { "( E )", "id" });
    
    PARSETABLE = new HashMap();
    
    PARSETABLE.put("E", new HashMap(1000));
    ((Map)PARSETABLE.get("E")).put("id", "T E'");
    ((Map)PARSETABLE.get("E")).put("(", "T E'");
    
    PARSETABLE.put("E'", new HashMap(1000));
    ((Map)PARSETABLE.get("E'")).put("+", "+ T E'");
    ((Map)PARSETABLE.get("E'")).put(")", "e");
    ((Map)PARSETABLE.get("E'")).put("$", "e");
    
    PARSETABLE.put("T", new HashMap(1000));
    ((Map)PARSETABLE.get("T")).put("id", "F T'");
    ((Map)PARSETABLE.get("T")).put("(", "F T'");
    
    PARSETABLE.put("T'", new HashMap(1000));
    ((Map)PARSETABLE.get("T'")).put("+", "e");
    ((Map)PARSETABLE.get("T'")).put("*", "* F T'");
    ((Map)PARSETABLE.get("T'")).put(")", "e");
    ((Map)PARSETABLE.get("T'")).put("$", "e");
    
    PARSETABLE.put("F", new HashMap(1000));
    ((Map)PARSETABLE.get("F")).put("id", "id");
    ((Map)PARSETABLE.get("F")).put("(", "( E )");
  }
  
  private static class LexicalAnalyzer
  {
    private static String[] TERMINAL_SYMBOLS = { "(", "i", "+", "*", ")" };
    
    private static List<String> extractTokens(String input)
    {
      for (String terminalSymbol : TERMINAL_SYMBOLS) {
        input = input.replace(terminalSymbol, " " + terminalSymbol + " ");
      }
      input = input.trim();
      
      Object symbolsList = new ArrayList();
      Scanner scanner = new Scanner(input);
      while (scanner.hasNext())
      {
        String value = scanner.next();
        if (value.equals("i")) {
          value = "id";
        }
        ((List)symbolsList).add(value);
      }
      return (List<String>)symbolsList;
    }
  }
}
