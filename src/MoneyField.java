import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MoneyField extends JTextField
{
	/**
	 * The monetary amount the user has specified in this field.
	 **/
	private int monetaryAmount;

	/**
	 * The status bar to use for error reporting.
	 **/
	private StatusBar statusBar;

	/**
	 * Create a new MoneyField using the given status bar for error reporting.
	 * @param statusBar The status bar to use for error reporting.
	 **/
	public MoneyField(StatusBar statusBar)
	{
		super(20);
		this.statusBar = statusBar;
		monetaryAmount = -1;
		MoneyInputVerifier verifier = new MoneyInputVerifier();
		setInputVerifier(verifier);
		getDocument().addDocumentListener(verifier);
	}

	/**
	 * Checks whether the current contents reflect valid input.
	 * @return whether they are valid
	 */
	public boolean areContentsValid()
	{
		return getInputVerifier().verify(null);
	}

	/**
	 * @return The monetary amount specified by the user.
	 **/
	public int getMonetaryAmount()
	{
		return monetaryAmount;
	}

	/**
	 * Clears the field.
	 **/
	public void clearMoneyEntered()
	{
		monetaryAmount = -1;
		setText("");
	}

	/**
	 * Verifies monetary input.
	 **/
	private class MoneyInputVerifier extends InputVerifier implements DocumentListener
	{
		/**
		 * @inheritDoc
		 **/
		public boolean verify(JComponent _)
		{
			monetaryAmount = U.parseMoney(getText());
			if (monetaryAmount == U.BAD_MONEY)
			{
				if (!getText().equals(""))
					statusBar.setStatus("Invalid monetary amount", StatusBar.STATUS_BAD_COLOR);
				return false;
			}
			else if (monetaryAmount == U.TOO_MUCH_MONEY)
			{
				statusBar.setStatus("Monetary amount too large", StatusBar.STATUS_BAD_COLOR);
				return false;
			}
			else
			{
				statusBar.clearStatus();
				return true;
			}
		}

		/** @inheritDoc */
		public void changedUpdate(DocumentEvent ignored) {}

		/** @inheritDoc */
		public void insertUpdate(DocumentEvent ignored)
		{
			verify(null);
		}

		/** @inheritDoc */
		public void removeUpdate(DocumentEvent ignored)
		{
			verify(null);
		}
	}
}
