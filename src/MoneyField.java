import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
	 * Cached copy of the last string in the money field. We revert to this when an invalid character is entered.
	 **/
	private String oldEntry;
	
	/** Whether we're deliberately overriding validation. */
	private boolean pleaseDontWorryAboutValidatingThis;

	/**
	 * Create a new MoneyField using the given status bar for error reporting.
	 * @param statusBar The status bar to use for error reporting.
	 **/
	public MoneyField(StatusBar statusBar)
	{
		super(20);
		this.statusBar = statusBar;
		this.oldEntry = "";
		monetaryAmount = -1;
		MoneyInputVerifier verifier = new MoneyInputVerifier();
		setInputVerifier(verifier);
		getDocument().addDocumentListener(verifier);
		pleaseDontWorryAboutValidatingThis=false;
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
		pleaseDontWorryAboutValidatingThis=true;
		monetaryAmount = -1;
		setText("");
		pleaseDontWorryAboutValidatingThis=false;
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
			
			if(pleaseDontWorryAboutValidatingThis) {
				return true;
			}
			
			if (monetaryAmount == U.BAD_MONEY)
			{
				//if (!getText().equals(""))
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
				statusBar.clearStatus(StatusBar.PRIORITY_INVALID_INPUT);
				return true;
			}
		}

		/** @inheritDoc */
		public void changedUpdate(DocumentEvent ignored) {}

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
			if(verify(null) || getText().length() == 0)
			{
				oldEntry = getText();
			}
			else if(getText().length() >= 1)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						pleaseDontWorryAboutValidatingThis = true;
						setText(oldEntry);
						pleaseDontWorryAboutValidatingThis = false;
					}
				});
			}
		}
	}
}
