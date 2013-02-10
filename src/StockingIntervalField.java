import java.awt.Color;
import java.util.ArrayList;

/**
 * A validating field accepting positive integers that aren't invalid stocking intervals.
 * The user is prevented from entering non-integral characters into the box.
 * In case the chosen stocking interval is invalid, the box turns pink for visual ID.
 * Validation errors are explained to the <tt>StatusBar</tt>.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class StockingIntervalField extends NumberField
{
	/** Our controller backend. */
	private ManagerMachineManagementScreen controller;

	/** The system status bar. */
	private StatusBar bar;

	/**
	 * Constructor.
	 * Supplies the controller that will be handling input validation.
	 * @param bar the status bar to which I should complain
	 * @param controller the controller that will report on intervals' validities
	 */
	public StockingIntervalField(ManagerMachineManagementScreen controller, StatusBar bar)
	{
		super(POSITIVE_Z); //allow entry of any positive integer
		this.controller=controller;
		this.bar=bar;
		wrapValidationFeedbackLoop();
	}

	/**
	 * Adds our additional validation condition if there's already a machine/layout.
	 */
	private void wrapValidationFeedbackLoop()
	{
		//try to get our hands on a vending machine
		ArrayList<VendingMachine> machs=controller.listMachinessAll();
		if(machs.size()!=0) //there are already existing machines
		{ //there's an established product layout, so we need to validate intervals
			final VendingMachine mach=machs.get(0); //grab one
			
			substituteFeedbackLoop(new ConditionButtonCondition()
			{
				@Override
				public boolean checkCondition()
				{
					try
					{
						int validity=controller.stockingIntervalValidity(mach, Integer.parseInt(getText()));
						
						if(validity>0) //not a great choice
						{
							setBackground(Color.PINK);
							bar.setStatusConditionally("Due to expiration concerns, this should be at most "+validity+" days", bar.STATUS_BAD_COLOR, bar.PRIORITY_SUPPLEMENTAL_VALIDATION);
						}
						
						return validity==0;
					}
					catch(NumberFormatException initialValidationMustHaveFailed)
					{
						return false; //closed world assumption
					}
				}
			});
		}
	}
}
