import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utility class for various thing at the view level
 * @author Matthew Koontz
 **/
public class U
{
	/**
	 * Value returned by parseMoney() when the string given is not a valid
	 * representation of a monetary amount.
	 **/
	public static final int BAD_MONEY = -1;

	/**
	 * Value returned by parseMoney() when the string given is a valid
	 * representation of a monetary amount, but is too large to be stored in
	 * a 32-bit signed integer (Java's int primitive).
	 **/
	public static final int TOO_MUCH_MONEY = -2;

	/**
	 * Pattern to use for matching money.
	 **/
	private static final Pattern moneyPattern = Pattern.compile("(\\d*)(\\.(\\d(\\d?)))?");

	/**
	 * Private constructor to prevent creation.
	 **/
	private U() {}

	/**
	 * Parses monetary amount as string and returns the amount as an integer
	 * in cents.
	 * @param input The string to parse.
	 * @return The monetary amount in cents. If the amount was not valid,
	 * returns BAD_MONEY. If the amount was valid, but was too large, returns
	 * TOO_MUCH_MONEY.
	 **/
	public static int parseMoney(String input)
	{
		if (input.equals(""))
			return BAD_MONEY;
		Matcher matcher = moneyPattern.matcher(input);
		if (matcher.matches())
		{
			String moneyStr = matcher.group(1);
			String centStr = matcher.group(3);
			if (centStr != null)
			{
				moneyStr += centStr;
				if (centStr.length() == 1)
					moneyStr += "0";
			}
			else
			{
				moneyStr += "00";
			}
			try
			{
				return Integer.parseInt(moneyStr);
			}
			catch (NumberFormatException formatException)
			{
				return TOO_MUCH_MONEY;
			}
		}
		else
			return BAD_MONEY;
	}
}
