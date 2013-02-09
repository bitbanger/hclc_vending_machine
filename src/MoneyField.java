import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;

/**
 * A validating field accepting monetary amounts.
 * The user is prevented from entering any invalid characters into the box.
 * In case the entered input is invalid, the box turns pink for visual identification.
 * Validation errors are explained to the <tt>StatusBar</tt>.
 * @author Matthew Koontz <mjk3979@rit.edu>
 */
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
	
	/** The current validity. */
	private boolean contentsValid;
	
	/** Money input verifier */
	private MoneyInputVerifier verifier;
	
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
		verifier = new MoneyInputVerifier();
		getDocument().addDocumentListener(verifier);
		pleaseDontWorryAboutValidatingThis=false;
		setBackground(Color.PINK);
	}

	/**
	 * Checks whether the current contents reflect valid input.
	 * @return whether they are valid
	 */
	public boolean areContentsValid()
	{
		return verifier.verify(false);
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
	private class MoneyInputVerifier implements DocumentListener
	{	
		/**
		 * @inheritDoc
		 **/
		public boolean verify(boolean changeStatusBar)
		{
			monetaryAmount = U.parseMoney(getText());
			
			boolean retVal = false;
			
			if(pleaseDontWorryAboutValidatingThis) {
				contentsValid = !getText().equals("");
			}
			else if (monetaryAmount == U.BAD_MONEY)
			{
				if(changeStatusBar)
					statusBar.setStatus("Invalid monetary amount", StatusBar.STATUS_BAD_COLOR);
				contentsValid = false;
			}
			else if (monetaryAmount == U.TOO_MUCH_MONEY)
			{
				if(changeStatusBar)
					statusBar.setStatus("Monetary amount too large", StatusBar.STATUS_BAD_COLOR);
				contentsValid = false;
			}
			else
			{
				if(changeStatusBar)
					statusBar.clearStatus(StatusBar.PRIORITY_INVALID_INPUT);
				contentsValid = true;
			}
			
			if(contentsValid) {
				setBackground(Color.WHITE);
			} else {
				setBackground(Color.PINK);
			}
			
			return contentsValid;
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
			if(verify(true) || getText().length() == 0)
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
