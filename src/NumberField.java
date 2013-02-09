import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

	/** The current validity. */
	private boolean contentsValid;

	/** Whether we're deliberately overriding validation. */
	private boolean pleaseDontWorryAboutValidatingThis;
	
	/** Cached copy of the last string in the money field. We revert to this when an invalid character is entered. */
	private String oldEntry;
	
	/** Number field verifier */
	private NumberFieldVerifier verifier;

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
		this.oldEntry = "";
		this.verifier = new NumberFieldVerifier();
		getDocument().addDocumentListener(verifier);
		contentsValid=false; //there *is* no int
		pleaseDontWorryAboutValidatingThis=false;
		setBackground(Color.PINK);
	}

	/**
	 * Checks whether the current contents reflect valid input.
	 * @return whether they are valid
	 */
	public boolean areContentsValid()
	{
		this.verifier.verify(false);
		return contentsValid;
	}

	/**
	 * Clears the field gracefully.
	 * Please use this instead of <tt>setText("")</tt>, which will print a warning to the status bar!
	 */
	public void clearNumberEntered()
	{
		pleaseDontWorryAboutValidatingThis=true;
		setText("");
		pleaseDontWorryAboutValidatingThis=false;
	}

	/**
	 * Retrieves the number entered by the user.
	 * @return dat numbah
	 */
	public int getNumber()
	{
		return Integer.parseInt(getText());
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
	private class NumberFieldVerifier implements DocumentListener
	{
		/** @inheritDoc */
		public boolean verify(boolean changeStatusBar)
		{
			int choice;
			
			if(pleaseDontWorryAboutValidatingThis)
			{
				contentsValid = !getText().equals("");
			}
			else
			{
				try
				{
					choice=Integer.parseInt(getText());
					
					if(formatDescriptor.validate(choice))
					{
						if(changeStatusBar)
							bar.clearStatus(StatusBar.PRIORITY_INVALID_INPUT);
						contentsValid=true;
					}
					else
					{
						if(changeStatusBar)
							bar.setColor(bar.STATUS_BAD_COLOR);
						//the format descriptor will have set the status appropriately
						contentsValid=false;
					}
				}
				catch(NumberFormatException nai)
				{
					if(changeStatusBar)
						bar.setStatus("Please enter only an integral number", StatusBar.STATUS_BAD_COLOR);
					contentsValid=false;
				}
			}
			
			if(contentsValid) {
				setBackground(Color.WHITE);
			} else {
				setBackground(Color.PINK);
			}
			
			return contentsValid;
		}

		/** @inheritDoc */
		public void insertUpdate(DocumentEvent ignored)
		{
			generalUpdate();
		}

		/** @inheritDoc */
		public void removeUpdate(DocumentEvent ignored)
		{
			generalUpdate();
		}
		
		private void generalUpdate()
		{
			if(verify(true) || getText().length() == 0)
			{
				oldEntry = getText();
			}
			else if(getText().length() >= 1)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("fff");
						pleaseDontWorryAboutValidatingThis=true;
						setText(oldEntry);
						pleaseDontWorryAboutValidatingThis=false;
					}
				});
			}
		}

		/** @inheritDoc */
		public void changedUpdate(DocumentEvent ignored) {}
	}
}
