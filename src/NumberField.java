import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * A validating field accepting integers.
 * The user will be unable to leave the field until he or she enters a legal value.
 * Validation errors are reported to the <tt>StatusBar</tt>.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class NumberField extends JTextField
{
	/** A <tt>NumberFormat</tt> accepting positive integers. */
	public static final NumberFormat POSITIVE_Z=MinValueNumberFormat.ONE;

	/** A <tt>NumberFormat</tt> accepting nonnegative integers. */
	public static final NumberFormat NONNEGATIVE_Z=MinValueNumberFormat.ZERO;

	/** The status bar to which validation problems are reported. */
	private static StatusBar bar=null;

	/** The descriptor of the proper integer format. */
	private NumberFormat formatDescriptor;

	/**
	 * This should be called once to point the validation framework at the <tt>StatusBar</tt>.
	 * @param bar that status bar
	 * @return whether the operation was permitted (replaced a <tt>null</tt> with a non-<tt>null</tt>
	 */
	public static boolean setBar(StatusBar bar)
	{
		if(NumberField.bar==null && bar!=null)
		{
			NumberField.bar=bar;
			NumberFormat.setCommunicator(new StatusBarValidationComplainer());
			return true;
		}
		else return false;
	}

	/**
	 * Constructor.
	 * Supplies the type of integer expected by this field.
	 * @param formatDescriptor the bounds on accepted values
	 */
	public NumberField(NumberFormat formatDescriptor)
	{
		this.formatDescriptor=formatDescriptor;
		setInputVerifier(new NumberFieldVerifier());
	}

	/**
	 * Provides a wrapper for reporting validation errors to the status bar.
	 */
	private static class StatusBarValidationComplainer implements ValidationComplainer
	{
		/** @inheritDoc */
		public void alert(String message)
		{
			bar.setStatus(message);
		}
	}

	/**
	 * Verifies input and prints any errors to the status bar as focus is about to be lost.
	 * In case of any such errors, the focus is retained until they are fixed.
	 */
	private class NumberFieldVerifier extends InputVerifier
	{
		/** @inheritDoc */
		public boolean verify(JComponent ignored)
		{
			int choice;
			
			try
			{
				choice=Integer.parseInt(getText());
			}
			catch(NumberFormatException nai)
			{
				bar.setStatus("Please enter only an integral number");
				bar.setColor(Color.RED);
				return false;
			}
			
			if(formatDescriptor.validate(choice))
			{
				bar.setColor(Color.GREEN);
				return true;
			}
			else
			{
				bar.setColor(Color.RED);
				return false;
			}
		}
	}
}
